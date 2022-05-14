package com.teradata.openapi.rop.impl;

import akka.actor.ActorRef;
import akka.util.Timeout;
import com.teradata.openapi.access.biz.bean.ApiInfo;
import com.teradata.openapi.access.biz.bean.DownLoadAttr;
import com.teradata.openapi.access.biz.bean.StructApiArg;
import com.teradata.openapi.access.biz.bean.UserInfo;
import com.teradata.openapi.access.core.ApiAppSecretManager;
import com.teradata.openapi.access.core.ApiServiceAccessController;
import com.teradata.openapi.access.core.ext.PromiseIdReal;
import com.teradata.openapi.framework.deploy.SyncExecuteException;
import com.teradata.openapi.framework.deploy.SyncExecuteResult;
import com.teradata.openapi.framework.expression.ExpressionParser;
import com.teradata.openapi.framework.message.request.*;
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK;
import com.teradata.openapi.framework.util.FastJSONUtil;
import com.teradata.openapi.framework.util.LoadProperties;
import com.teradata.openapi.framework.util.RedisUtil;
import com.teradata.openapi.framework.util.SpringContextUtil;
import com.teradata.openapi.rop.*;
import com.teradata.openapi.rop.config.SystemParameterNames;
import com.teradata.openapi.rop.event.*;
import com.teradata.openapi.rop.marshaller.JacksonJsonRopMarshaller;
import com.teradata.openapi.rop.marshaller.JaxbXmlRopMarshaller;
import com.teradata.openapi.rop.marshaller.MessageMarshallerUtils;
import com.teradata.openapi.rop.request.RopRequestMessageConverter;
import com.teradata.openapi.rop.request.UploadFileConverter;
import com.teradata.openapi.rop.response.ErrorResponse;
import com.teradata.openapi.rop.response.RejectedServiceResponse;
import com.teradata.openapi.rop.response.ServiceUnavailableErrorResponse;
import com.teradata.openapi.rop.response.TimeoutErrorResponse;
import com.teradata.openapi.rop.security.*;
import com.teradata.openapi.rop.security.SecurityManager;
import com.teradata.openapi.rop.session.DefaultSessionManager;
import com.teradata.openapi.rop.session.SessionBindInterceptor;
import com.teradata.openapi.rop.session.SessionManager;
import com.teradata.openapi.rop.utils.ZipCompressFromHDFS;
import com.xiaoleilu.hutool.util.SecureUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.util.Assert;
import scala.concurrent.Await;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;
import scala.concurrent.impl.Promise.DefaultPromise;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class AnnotationServletServiceRouter implements ServiceRouter {

    private final static Logger logger = LoggerFactory.getLogger(AnnotationServletServiceRouter.class);

    public static final String APPLICATION_XML = "application/xml";

    public static final String APPLICATION_JSON = "application/json";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    public static final String DEFAULT_EXT_ERROR_BASE_NAME = "i18n/rop/ropError";

    private static final String I18N_ROP_ERROR = "i18n/rop/error";

    private RopMarshaller xmlMarshallerRop = new JaxbXmlRopMarshaller();

    private RopMarshaller jsonMarshallerRop = new JacksonJsonRopMarshaller();

    private RequestContextBuilder requestContextBuilder;

    private SecurityManager securityManager;

    private FormattingConversionService formattingConversionService;

    private ThreadPoolExecutor threadPoolExecutor;

    private RopContext ropContext;

    private RopEventMulticaster ropEventMulticaster;

    private List<Interceptor> interceptors = new ArrayList<Interceptor>();

    private List<RopEventListener> listeners = new ArrayList<RopEventListener>();

    private boolean signEnable = true;

    private ApplicationContext applicationContext;

    // 所有服务方法的最大过期时间，单位为秒(0或负数代表不限制)
    private int serviceTimeoutSeconds = Integer.MAX_VALUE;

    // 会话管理器
    private SessionManager sessionManager = new DefaultSessionManager();

    // 服务调用频率管理器
    private InvokeTimesController invokeTimesController = new DefaultInvokeTimesController();

    private Class<? extends ThreadFerry> threadFerryClass;

    private String extErrorBasename;

    private String[] extErrorBasenames;

    @Override
    public void service(Object request, Object response) {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        // 获取服务方法最大过期时间
        String method = servletRequest.getParameter(SystemParameterNames.getMethod());
        String version = servletRequest.getParameter(SystemParameterNames.getVersion());
        if (logger.isInfoEnabled()) {
            logger.info("服务方法：" + method + "(" + version + ") 调用开始");
        }
        int serviceMethodTimeout = getServiceMethodTimeout(method, version);
        long beginTime = System.currentTimeMillis();
        String codeType = servletRequest.getParameter(SystemParameterNames.getCodeType());
        String jsonpCallback = getJsonpcallback(servletRequest);

        // 使用异常方式调用服务方法
        try {

            // 执行线程摆渡
            ThreadFerry threadFerry = buildThreadFerryInstance();
            if (threadFerry != null) {
                threadFerry.doInSrcThread();
            }

            ServiceRunnable runnable = new ServiceRunnable(servletRequest, servletResponse, jsonpCallback, threadFerry);
            Future<?> future = this.threadPoolExecutor.submit(runnable);
            while (!future.isDone()) {
                future.get(serviceMethodTimeout, TimeUnit.SECONDS);
            }
        } catch (RejectedExecutionException ree) {// 超过最大的服务平台的最大资源限制，无法提供服务
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:" + method + "(" + version + ")，超过最大资源限制，无法提供服务。");
            }
            RopRequestContext ropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            RejectedServiceResponse ropResponse = new RejectedServiceResponse(ropRequestContext);
            writeResponse(ropResponse, servletRequest, servletResponse, ServletRequestContextBuilder.getResponseFormat(servletRequest),
                    codeType, jsonpCallback);
            fireAfterDoServiceEvent(ropRequestContext);
        } catch (TimeoutException e) {// 服务时间超限
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:" + method + "(" + version + ")，服务调用超时。");
            }
            RopRequestContext ropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            TimeoutErrorResponse ropResponse = new TimeoutErrorResponse(ropRequestContext.getMethod(), ropRequestContext.getLocale(),
                    serviceMethodTimeout);
            writeResponse(ropResponse, servletRequest, servletResponse, ServletRequestContextBuilder.getResponseFormat(servletRequest),
                    codeType, jsonpCallback);
            fireAfterDoServiceEvent(ropRequestContext);
        } catch (Throwable throwable) {// 产生未知的错误
            if (logger.isInfoEnabled()) {
                logger.info("调用服务方法:" + method + "(" + version + ")，产生异常", throwable);
            }
            ServiceUnavailableErrorResponse ropResponse = new ServiceUnavailableErrorResponse(method,
                    ServletRequestContextBuilder.getLocale(servletRequest), throwable);
            writeResponse(ropResponse, servletRequest, servletResponse, ServletRequestContextBuilder.getResponseFormat(servletRequest),
                    codeType, jsonpCallback);
            RopRequestContext ropRequestContext = buildRequestContextWhenException(servletRequest, beginTime);
            fireAfterDoServiceEvent(ropRequestContext);
        } finally {
            logger.info("服务方法：" + method + "(" + version + ") 调用结束");
            try {
                servletResponse.getOutputStream().flush();
                servletResponse.getOutputStream().close();
            } catch (IOException e) {
                logger.error("关闭响应出错", e);
            }
        }
    }

    /**
     * 获取JSONP的参数名，如果没有返回
     *
     * @param servletRequest
     * @return
     */
    private String getJsonpcallback(HttpServletRequest servletRequest) {
        if (servletRequest.getParameterMap().containsKey(SystemParameterNames.getJsonp())) {
            String callback = servletRequest.getParameter(SystemParameterNames.getJsonp());
            if (StringUtils.isEmpty(callback)) {
                callback = "callback";
            }
            return callback;
        } else {
            return null;
        }
    }

    @Override
    public void startup() {
        if (logger.isInfoEnabled()) {
            logger.info("开始启动Rop框架...");
        }
        Assert.notNull(this.applicationContext, "Spring上下文不能为空");

        // 初始化类型转换器
        if (this.formattingConversionService == null) {
            this.formattingConversionService = getDefaultConversionService();
        }
        registerConverters(formattingConversionService);

        // 实例化ServletRequestContextBuilder
        this.requestContextBuilder = new ServletRequestContextBuilder(this.formattingConversionService);

        // 设置校验器
        if (this.securityManager == null) {
            this.securityManager = new DefaultSecurityManager();
        }

        // 设置异步执行器
        if (this.threadPoolExecutor == null) {
            this.threadPoolExecutor = new ThreadPoolExecutor(200, Integer.MAX_VALUE, 5 * 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }

        // 创建Rop上下文
        this.ropContext = buildRopContext();

        // 初始化事件发布器
        this.ropEventMulticaster = buildRopEventMulticaster();

        // 注册会话绑定拦截器
        this.addInterceptor(new SessionBindInterceptor());

        // 初始化信息源
        initMessageSource();

        // 产生Rop框架初始化事件
        fireAfterStartedRopEvent();

        if (logger.isInfoEnabled()) {
            logger.info("Rop框架启动成功！");
        }
    }

    private void registerConverters(FormattingConversionService conversionService) {
        conversionService.addConverter(new RopRequestMessageConverter());
        conversionService.addConverter(new UploadFileConverter());
    }

    private ThreadFerry buildThreadFerryInstance() {
        if (threadFerryClass != null) {
            return BeanUtils.instantiate(threadFerryClass);
        } else {
            return null;
        }
    }

    @Override
    public void shutdown() {
        fireBeforeCloseRopEvent();
        threadPoolExecutor.shutdown();
    }

    @Override
    public void setSignEnable(boolean signEnable) {
        if (!signEnable && logger.isWarnEnabled()) {
            logger.warn("rop close request message sign");
        }
        this.signEnable = signEnable;
    }

    @Override
    public void setThreadFerryClass(Class<? extends ThreadFerry> threadFerryClass) {
        if (logger.isDebugEnabled()) {
            logger.debug("ThreadFerry set to {}", threadFerryClass.getName());
        }
        this.threadFerryClass = threadFerryClass;
    }

    @Override
    public void setInvokeTimesController(InvokeTimesController invokeTimesController) {
        if (logger.isDebugEnabled()) {
            logger.debug("InvokeTimesController set to {}", invokeTimesController.getClass().getName());
        }
        this.invokeTimesController = invokeTimesController;
    }

    @Override
    public void setServiceTimeoutSeconds(int serviceTimeoutSeconds) {
        if (logger.isDebugEnabled()) {
            logger.debug("serviceTimeoutSeconds set to {}", serviceTimeoutSeconds);
        }
        this.serviceTimeoutSeconds = serviceTimeoutSeconds;
    }

    @Override
    public void setSecurityManager(SecurityManager securityManager) {
        if (logger.isDebugEnabled()) {
            logger.debug("securityManager set to {}", securityManager.getClass().getName());
        }
        this.securityManager = securityManager;
    }

    @Override
    public void setFormattingConversionService(FormattingConversionService formatConversionService) {
        if (logger.isDebugEnabled()) {
            logger.debug("formatConversionService set to {}", formatConversionService.getClass().getName());
        }
        this.formattingConversionService = formatConversionService;
    }

    @Override
    public void setSessionManager(SessionManager sessionManager) {
        if (logger.isDebugEnabled()) {
            logger.debug("sessionManager set to {}", sessionManager.getClass().getName());
        }
        this.sessionManager = sessionManager;
    }

    /**
     * 获取默认的格式化转换器
     *
     * @return
     */
    private FormattingConversionService getDefaultConversionService() {
        FormattingConversionServiceFactoryBean serviceFactoryBean = new FormattingConversionServiceFactoryBean();
        serviceFactoryBean.afterPropertiesSet();
        return serviceFactoryBean.getObject();
    }

    @Override
    public void setExtErrorBasename(String extErrorBasename) {
        if (logger.isDebugEnabled()) {
            logger.debug("extErrorBasename set to {}", extErrorBasename);
        }
        this.extErrorBasename = extErrorBasename;
    }

    @Override
    public void setExtErrorBasenames(String[] extErrorBasenames) {
        if (extErrorBasenames != null) {
            List<String> list = new ArrayList<String>();
            for (String errorBasename : extErrorBasenames) {
                if (StringUtils.isNotBlank(errorBasename)) {
                    list.add(errorBasename);
                }
            }
            this.extErrorBasenames = list.toArray(new String[0]);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("extErrorBasenames set to {}", extErrorBasenames);
        }
    }

    @Override
    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
        if (logger.isDebugEnabled()) {
            logger.debug("threadPoolExecutor set to {}", threadPoolExecutor.getClass().getName());
            logger.debug("corePoolSize:{}", threadPoolExecutor.getCorePoolSize());
            logger.debug("maxPoolSize:{}", threadPoolExecutor.getMaximumPoolSize());
            logger.debug("keepAliveSeconds:{} seconds", threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
            logger.debug("queueCapacity:{}", threadPoolExecutor.getQueue().remainingCapacity());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public RopContext getRopContext() {
        return this.ropContext;
    }

    @Override
    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
        if (logger.isDebugEnabled()) {
            logger.debug("add  interceptor {}", interceptor.getClass().getName());
        }
    }

    @Override
    public void addListener(RopEventListener listener) {
        this.listeners.add(listener);
        if (logger.isDebugEnabled()) {
            logger.debug("add  listener {}", listener.getClass().getName());
        }
    }

    public int getServiceTimeoutSeconds() {
        return serviceTimeoutSeconds > 0 ? serviceTimeoutSeconds : Integer.MAX_VALUE;
    }

    /**
     * 取最小的过期时间
     *
     * @param method
     * @param version
     * @return
     */
    private int getServiceMethodTimeout(String method, String version) {
        ServiceMethodHandler serviceMethodHandler = ropContext.getServiceMethodHandler(method, version);
        if (serviceMethodHandler == null) {
            return getServiceTimeoutSeconds();
        } else {
            int methodTimeout = serviceMethodHandler.getServiceMethodDefinition().getTimeout();
            if (methodTimeout <= 0) {
                return getServiceTimeoutSeconds();
            } else {
                return methodTimeout;
            }
        }
    }

    private class ServiceRunnable implements Runnable {

        private HttpServletRequest servletRequest;

        private HttpServletResponse servletResponse;

        private ThreadFerry threadFerry;

        private String jsonpCallback;

        private ServiceRunnable(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String jsonpCallback,
                                ThreadFerry threadFerry) {
            this.servletRequest = servletRequest;
            this.servletResponse = servletResponse;
            this.jsonpCallback = jsonpCallback;
            this.threadFerry = threadFerry;
        }

        @Override
        public void run() {
            if (threadFerry != null) {
                threadFerry.doInDestThread();
            }

            RopRequestContext ropRequestContext = null;
            RopRequest ropRequest = null;
            try {
                // 用系统级参数构造一个RequestContext实例（第一阶段绑定）
                ropRequestContext = requestContextBuilder.buildBySysParams(ropContext, servletRequest, servletResponse);

                // 验证系统级参数的合法性
                logger.info("{}|调用服务方法：{}参数校验开始", ropRequestContext.getRequestId(), ropRequestContext.getMethod());
                MainError mainError = securityManager.validateSystemParameters(ropRequestContext);
                if (mainError != null) {
                    logger.info("{}|调用服务方法：{}参数校验异常: {}", ropRequestContext.getRequestId(), ropRequestContext.getMethod(),
                            MessageMarshallerUtils.getMessage(new ErrorResponse(mainError), ropRequestContext.getMessageFormat()));
                    ropRequestContext.setRopResponse(new ErrorResponse(mainError));
                } else {

                    // 绑定业务数据（第二阶段绑定）
                    ropRequest = requestContextBuilder.buildRopRequest(ropRequestContext);

                    // 进行其它检查业务数据合法性，业务安全等
                    mainError = securityManager.validateOther(ropRequestContext);
                    if (mainError != null) {
                        logger.info("{}|调用服务方法：{}参数校验异常: {}", ropRequestContext.getRequestId(), ropRequestContext.getMethod(),
                                MessageMarshallerUtils.getMessage(new ErrorResponse(mainError), ropRequestContext.getMessageFormat()));
                        ropRequestContext.setRopResponse(new ErrorResponse(mainError));
                    } else {
                        firePreDoServiceEvent(ropRequestContext);

                        // 服务处理前拦截
                        invokeBeforceServiceOfInterceptors(ropRequestContext);

                        if (ropRequestContext.getRopResponse() == null) { // 拦截器未生成response
                            // 如果拦截器没有产生ropResponse时才调用服务方法
                            ropRequestContext.setRopResponse(doService(ropRequest, servletRequest, servletResponse));

                            // 输出响应前拦截
                            invokeBeforceResponseOfInterceptors(ropRequest);
                        }
                    }
                }
                logger.info("{}|调用服务方法:{}参数校验结束", ropRequestContext.getRequestId(), ropRequestContext.getMethod());
                // 输出响应
                writeResponse(ropRequestContext.getRopResponse(), servletRequest, servletResponse, ropRequestContext.getMessageFormat(),
                        ropRequestContext.getCodeType(), jsonpCallback);
            } catch (Throwable e) {
                if (ropRequestContext != null) {
                    String method = ropRequestContext.getMethod();
                    Locale locale = ropRequestContext.getLocale();
                    if (logger.isDebugEnabled()) {
                        String message = java.text.MessageFormat.format("service {0} call error", method);
                        logger.debug(message, e);
                    }
                    ServiceUnavailableErrorResponse ropResponse = new ServiceUnavailableErrorResponse(method, locale, e);

                    // 输出响应前拦截
                    invokeBeforceResponseOfInterceptors(ropRequest);
                    writeResponse(ropResponse, servletRequest, servletResponse, ropRequestContext.getMessageFormat(),
                            ropRequestContext.getCodeType(), jsonpCallback);
                } else {
                    throw new RopException("RopRequestContext is null.", e);
                }
            } finally {
                if (ropRequestContext != null) {

                    // 发布服务完成事件
                    ropRequestContext.setServiceEndTime(System.currentTimeMillis());

                    // 完成一次服务请求，计算次数
                    invokeTimesController.caculateInvokeTimes(ropRequestContext.getAppKey(), ropRequestContext.getSession());
                    fireAfterDoServiceEvent(ropRequestContext);
                }
            }
        }
    }

    /**
     * 当发生异常时，创建一个请求上下文对象
     *
     * @param request
     * @param beginTime
     * @return
     */
    private RopRequestContext buildRequestContextWhenException(HttpServletRequest request, long beginTime) {
        RopRequestContext ropRequestContext = requestContextBuilder.buildBySysParams(ropContext, request, null);
        ropRequestContext.setServiceBeginTime(beginTime);
        ropRequestContext.setServiceEndTime(System.currentTimeMillis());
        return ropRequestContext;
    }

    private RopContext buildRopContext() {
        DefaultRopContext defaultRopContext = new DefaultRopContext(this.applicationContext);
        defaultRopContext.setSignEnable(this.signEnable);
        defaultRopContext.setSessionManager(sessionManager);
        return defaultRopContext;
    }

    private RopEventMulticaster buildRopEventMulticaster() {

        SimpleRopEventMulticaster simpleRopEventMulticaster = new SimpleRopEventMulticaster();

        // 设置异步执行器
        if (this.threadPoolExecutor != null) {
            simpleRopEventMulticaster.setExecutor(this.threadPoolExecutor);
        }

        // 添加事件监听器
        if (this.listeners != null && this.listeners.size() > 0) {
            for (RopEventListener ropEventListener : this.listeners) {
                simpleRopEventMulticaster.addRopListener(ropEventListener);
            }
        }

        return simpleRopEventMulticaster;
    }

    /**
     * 发布Rop启动后事件
     */
    private void fireAfterStartedRopEvent() {
        AfterStartedRopEvent ropEvent = new AfterStartedRopEvent(this, this.ropContext);
        this.ropEventMulticaster.multicastEvent(ropEvent);
    }

    /**
     * 发布Rop启动前事件
     */
    private void fireBeforeCloseRopEvent() {
        PreCloseRopEvent ropEvent = new PreCloseRopEvent(this, this.ropContext);
        this.ropEventMulticaster.multicastEvent(ropEvent);
    }

    private void fireAfterDoServiceEvent(RopRequestContext ropRequestContext) {
        this.ropEventMulticaster.multicastEvent(new AfterDoServiceEvent(this, ropRequestContext));
    }

    private void firePreDoServiceEvent(RopRequestContext ropRequestContext) {
        this.ropEventMulticaster.multicastEvent(new PreDoServiceEvent(this, ropRequestContext));
    }

    /**
     * 在服务调用之前拦截
     *
     * @param ropRequestContext
     */
    private void invokeBeforceServiceOfInterceptors(RopRequestContext ropRequestContext) {
        Interceptor tempInterceptor = null;
        try {
            if (interceptors != null && interceptors.size() > 0) {
                for (Interceptor interceptor : interceptors) {

                    interceptor.beforeService(ropRequestContext);

                    // 如果有一个产生了响应，则阻止后续的调用
                    if (ropRequestContext.getRopResponse() != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("拦截器[" + interceptor.getClass().getName() + "]产生了一个RopResponse," + " 阻止本次服务请求继续，服务将直接返回。");
                        }
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            ropRequestContext
                    .setRopResponse(new ServiceUnavailableErrorResponse(ropRequestContext.getMethod(), ropRequestContext.getLocale(), e));
            logger.error("在执行拦截器[" + tempInterceptor.getClass().getName() + "]时发生异常.", e);
        }
    }

    /**
     * 在服务调用之后，返回响应之前拦截
     *
     * @param ropRequest
     */
    private void invokeBeforceResponseOfInterceptors(RopRequest ropRequest) {
        RopRequestContext ropRequestContext = ropRequest.getRopRequestContext();
        Interceptor tempInterceptor = null;
        try {
            if (interceptors != null && interceptors.size() > 0) {
                for (Interceptor interceptor : interceptors) {
                    interceptor.beforeResponse(ropRequestContext);
                }
            }
        } catch (Throwable e) {
            ropRequestContext
                    .setRopResponse(new ServiceUnavailableErrorResponse(ropRequestContext.getMethod(), ropRequestContext.getLocale(), e));
            logger.error("在执行拦截器[" + tempInterceptor.getClass().getName() + "]时发生异常.", e);
        }
    }

    private void writeResponse(Object ropResponse, HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                               MessageFormat messageFormat, String codeType, String jsonpCallback) {
        try {
            if (!(ropResponse instanceof ErrorResponse) && messageFormat == MessageFormat.stream) {
                if (logger.isDebugEnabled()) {
                    logger.debug("使用{}输出方式，由服务自身负责响应输出工作.", MessageFormat.stream);
                }
                return;
            }
            // if (logger.isDebugEnabled()) {
            // logger.debug("输出响应：" + MessageMarshallerUtils.getMessage(ropResponse, messageFormat));
            // }
            RopMarshaller ropMarshaller = jsonMarshallerRop;
            String contentType = APPLICATION_JSON;
            if (messageFormat == MessageFormat.xml) {
                ropMarshaller = xmlMarshallerRop;
                contentType = APPLICATION_XML;
            }
            servletResponse.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            servletResponse.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "*");
            servletResponse.setCharacterEncoding(codeType);
            servletResponse.setContentType(contentType);

            if (jsonpCallback != null) {
                servletResponse.getOutputStream().write(jsonpCallback.getBytes());
                servletResponse.getOutputStream().write('(');
            }
            // 如果取出的数据是带参数的字符串对象则直接返回结果
            if (ropResponse instanceof String) {
                servletResponse.getOutputStream().write(ropResponse.toString().getBytes());
            } else if (ropResponse instanceof DownLoadAttr) {
                //ZipCompressFromFile zc = new ZipCompressFromFile();
                ZipCompressFromHDFS zc = new ZipCompressFromHDFS();
                zc.download(servletRequest, servletResponse, (DownLoadAttr) ropResponse);
            } else {
                // 是封装好的ropResponse对象则对其进行格式化返回
                ropMarshaller.marshaller(ropResponse, servletResponse.getOutputStream());
            }

            if (jsonpCallback != null) {
                servletResponse.getOutputStream().write(')');
                servletResponse.getOutputStream().write(';');
            }
        } catch (IOException e) {
            throw new RopException(e);
        }
    }

    private Object doService(RopRequest ropRequest, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Object ropResponse = null;
        RopRequestContext context = ropRequest.getRopRequestContext();
        if (context.getMethod() == null) {
            ropResponse = new ErrorResponse(MainErrors.getError(MainErrorType.MISSING_METHOD, context.getLocale(),
                    SystemParameterNames.getMethod()));
        } else if (!ropContext.isValidMethod(context.getMethod())) {
            MainError invalidMethodError = MainErrors.getError(MainErrorType.INVALID_METHOD, context.getLocale(), context.getMethod());
            ropResponse = new ErrorResponse(invalidMethodError);
        } else {
            // 取请求类型
            RequestTypeFormat reqTypeFormat = context.getRequestTypeFormat();
            try {
                ReqToFindBody tofindBody = new ReqToFindBody();
                // 组装数据给finder
                ReqToFind tofind = reqToFindBean(context);
                // 带格式的数据指纹(md5加密)
                String retFormatFinger = finger(tofind, true);
                // 不带格式的数据指纹(md5加密)
                String retDataFinger = finger(tofind, false);
                tofind.setRetFormatFinger(retFormatFinger);
                tofind.setRetDataFinger(retDataFinger);

                tofindBody.setClientTreadId(Thread.currentThread().getId());
                String bodyJson = FastJSONUtil.serialize(tofind);
                //logger.warn("messageBody json :{}", bodyJson);
                tofindBody.setMessageBody(bodyJson);
                if (reqTypeFormat == RequestTypeFormat.syn) {
                    // 同步请求 先从redis查取带格式的数据
                    try {
                        ropResponse = RedisUtil.getInstance().getString(retFormatFinger);
                        if (ropResponse == null) {
                            ropResponse = synCallActor(tofindBody, tofind.getReqID(), context, servletRequest, servletResponse);
                        } else {
                            logger.info("指纹命中 retFormatFinger：{}", retFormatFinger);
                        }
                    } catch (Exception e) {
                        if (e.getMessage().equals("Redis execute exception")) {
                            logger.error("Redis error:{}", e.getMessage());
                            ropResponse = synCallActor(tofindBody, tofind.getReqID(), context, servletRequest, servletResponse);
                        }
                    }

                } else {
                    // 异步，调用finder
                    Object tempObj = callActor(tofindBody, tofind.getReqID());
                    if (tempObj instanceof FindToReqAsynYIK) {
                        if ("DownLoad".equals(((FindToReqAsynYIK) tempObj).getRetCode())) {
                            String jsonDownLoadAttrArgs = ((FindToReqAsynYIK) tempObj).getReqStat();
                            DownLoadAttr dla = FastJSONUtil.deserialize(jsonDownLoadAttrArgs, DownLoadAttr.class);
                            ropResponse = dla;
                        } else {
                            ropResponse = tempObj;
                        }
                    }
                }

            } catch (Exception e) { // 出错则抛服务不可用的异常
                if (logger.isInfoEnabled()) {
                    logger.info("调用" + context.getMethod() + "时发生异常，异常信息为：" + e.getMessage());
                    e.printStackTrace();
                }
                // 出现异常则把Promise与reqId之间的关系清除
                PromiseIdReal.getInstance().remove(context.getRequestId());
                ropResponse = new ServiceUnavailableErrorResponse(context.getMethod(), context.getLocale(), e);
            }
            logger.info("{}|服务方法：{} {}调用Finder结束", context.getRequestId(), context.getMethod(), reqTypeFormat.name());

        }
        return ropResponse;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object callActor(ReqToFindBody sendObject, String reqId) throws Exception {
        logger.info("{}|调用Finder开始", reqId);
        Object ropResponse = null;
        ActorRef clientRef = (ActorRef) SpringContextUtil.getBean("clientActor");
        clientRef.tell(sendObject, ActorRef.noSender());
        Timeout timeout = new Timeout(Duration.create(Constants.AKKA_TIME_OUT, TimeUnit.SECONDS));
        Promise pro = new DefaultPromise();
        Map<String, Promise> proMap = PromiseIdReal.getInstance();
        proMap.put(reqId, pro);
        ropResponse = Await.result(pro.future(), timeout.duration());
        return ropResponse;
    }

    /**
     * 同步调用actor数据处理
     *
     * @param sendObject
     * @param reqId
     * @param context
     * @return
     * @throws Exception
     * @author houbl
     */
    private Object synCallActor(ReqToFindBody sendObject, String reqId, RopRequestContext context, HttpServletRequest servletRequest,
                                HttpServletResponse servletResponse) throws Exception {
        Object ropResponse = null;
        Object tempObj = null;
        tempObj = callActor(sendObject, reqId);
        // 同步如果没有数据 返回FindToReqAsynYIK
        if (tempObj instanceof FindToReqAsynYIK) {
            if ("DownLoad".equals(((FindToReqAsynYIK) tempObj).getRetCode())) {
                String jsonDownLoadAttrArgs = ((FindToReqAsynYIK) tempObj).getReqStat();
                DownLoadAttr dla = FastJSONUtil.deserialize(jsonDownLoadAttrArgs, DownLoadAttr.class);
                ropResponse = dla;
            } else {
                ropResponse = tempObj;
            }
        } else if (tempObj instanceof SyncExecuteResult) {
            // 带格式的数据
            ropResponse = ((SyncExecuteResult) tempObj).result();
            // 同步数据到redis (在此做影响效率) 如果redis宕了 不影响后面流程
            try {
                String expireStr = LoadProperties.getProp("redis.expire", "configure.properties");
                int expire = Integer.parseInt(StringUtils.defaultIfBlank(expireStr, Constants.DEFAULT_REDIS_EXPIRE + "")) * 24 * 60 * 60;
                RedisUtil.getInstance().setString(((SyncExecuteResult) tempObj).formatFinger(), ropResponse.toString(), expire);
            } catch (Exception e) {
                logger.error("redis error:" + e.getMessage());
            }
        } else if (tempObj instanceof SyncExecuteException) {
            ropResponse = new ServiceUnavailableErrorResponse(context.getMethod(), context.getLocale(), ((SyncExecuteException) tempObj).e());
        }
        return ropResponse;
    }

    /**
     * 数据指纹生成（带格式与非格式）
     *
     * @param toFind
     * @param isFormat
     * @return
     * @author houbl
     */
    private String finger(ReqToFind toFind, boolean isFormat) {
        try {
            Map<String, Object> paramValues = new HashMap<String, Object>();
            // 设置参数
            paramValues.put("apiId", toFind.getApiId());
            paramValues.put(SystemParameterNames.getVersion(), toFind.getApi_version());

            if (toFind.getIsSyn() == Constants.REQ_TYPE_0) {
                if (toFind.getPageNum() != null && toFind.getPageSize() != null) {
                    paramValues.put(Constants.PAGE_NUM, toFind.getPageNum());
                    paramValues.put(Constants.PAGE_SIZE, toFind.getPageSize());
                }
            }

            List<ReqArg> reqArgs = toFind.getReqArgs();
            for (ReqArg reqArg : reqArgs) {
                paramValues.put(reqArg.getFieldName(),
                        reqArg.getCalcPrincId() + Constants.JOIN_SIGN + StringUtils.join(reqArg.getFieldValue(), Constants.SPLIT_SIGN));
            }

            List<RepArg> repArgs = toFind.getRepArgs();
            List<String> repParam = new ArrayList<String>();
            for (RepArg repArg : repArgs) {
                repParam.add(repArg.getFieldName());
            }
            // 对业务级响应参数进行排序
            Collections.sort(repParam);
            paramValues.put(SystemParameterNames.getFields(), StringUtils.join(repParam, Constants.SPLIT_SIGN));

            if (isFormat) {
                paramValues.put(SystemParameterNames.getFormat(), FastJSONUtil.serialize(toFind.getFormat()));
                paramValues.put(SystemParameterNames.getCodeType(), toFind.getEncode());
            }

            StringBuilder sb = new StringBuilder();
            List<String> paramNames = new ArrayList<String>(paramValues.size());
            paramNames.addAll(paramValues.keySet());
            Collections.sort(paramNames);

            for (String paramName : paramNames) {
                sb.append(paramName).append(paramValues.get(paramName));
            }

            return SecureUtil.md5(sb.toString(), Constants.UTF8);

        } catch (Exception e) {
            throw new RopException(e);
        }
    }

    /**
     * 请求数据转化finder 实体对象
     *
     * @param context
     * @return
     * @author houbl
     */
    private ReqToFind reqToFindBean(RopRequestContext context) {
        ReqToFind reqToFind = new ReqToFind();
        reqToFind.setReqID(context.getRequestId());
        reqToFind.setAppKey(context.getAppKey());

        Map<Object, Object> apiMap = ApiServiceAccessController.getApiMap();
        ApiInfo apiInfo = (ApiInfo) apiMap.get(context.getMethod() + Constants.JOIN_SIGN + context.getVersion());
        reqToFind.setApiId(apiInfo.getApiId());
        reqToFind.setApiSort(apiInfo.getApiSort());
        reqToFind.setApiClsCode(apiInfo.getApiClsCode());
        reqToFind.setApi_version(Integer.valueOf(context.getVersion()));
        reqToFind.setApiName(context.getMethod());
        reqToFind.setTimeStamp(System.currentTimeMillis());

        // 封装format
        Format format = new Format();
        format.setFormType(context.getFormat());
        if (context.getFormat().equals(MessageFormat.xls.name())) {
            Map<String, String> paramMap = context.getAllParams();
            String excelHeader = paramMap.get(Constants.EXCEL_HEADER);
            // 在此不做任何校验 前面已经做了校验操作
            FormDetail formDetail = FastJSONUtil.deserialize(excelHeader, FormDetail.class);
            format.setFormDetail(formDetail);
        }
        reqToFind.setFormat(format);

        reqToFind.setEncode(context.getCodeType());

        if (context.isSysAppKey()) {
            reqToFind.setPriority(0);
        } else {
            ApiAppSecretManager appManager = new ApiAppSecretManager();
            UserInfo user = appManager.getUserInfo(context.getAppKey());
            reqToFind.setPriority(user.getPriority());
        }

        Map<String, String> reqParamMap = context.getReqParams();
        //scan downloadflag by baolin
        for (Map.Entry<String,String> rpm : reqParamMap.entrySet()){
            logger.debug("scan ReqParamMap:" + rpm.getKey() + " -> " + rpm.getValue());
        }

        if (context.getReqType().equals(RequestTypeFormat.syn.name())) {
            reqToFind.setIsSyn(Constants.REQ_TYPE_0);
            String pageNum = reqParamMap.get(Constants.PAGE_NUM);
            String pageSize = reqParamMap.get(Constants.PAGE_SIZE);
            if (StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
                reqToFind.setPageNum(Integer.parseInt(pageNum));
                reqToFind.setPageSize(Integer.parseInt(pageSize));
            }
        } else if (context.getReqType().equals(RequestTypeFormat.asyn.name())) {
            reqToFind.setIsSyn(Constants.REQ_TYPE_1);
        } else {
            reqToFind.setIsSyn(Constants.REQ_TYPE_2);
        }

        List<RepArg> repArgs = new ArrayList<RepArg>();
        String[] fieldsArry = context.getFields().split(Constants.SPLIT_SIGN);

        List<ReqArg> reqArgs = new ArrayList<ReqArg>();

        List<StructApiArg> apiArgList = apiInfo.getApiArgList();
        Map<String, StructApiArg> apiArgMap = new HashMap<String, StructApiArg>();
        for (StructApiArg apiArg : apiArgList) {
            apiArgMap.put(apiArg.getFieldName(), apiArg);
        }

        for (String fields : fieldsArry) {
            RepArg repArg = new RepArg();
            StructApiArg apiArg = apiArgMap.get(fields);
            BeanUtils.copyProperties(apiArg, repArg);
            repArgs.add(repArg);
        }
        reqToFind.setRepArgs(repArgs);
        for (String paramKey : reqParamMap.keySet()) {
            ReqArg reqArg = new ReqArg();
            String paramValue = reqParamMap.get(paramKey);
            List<String> fieldValue = new ArrayList<String>();
            if ("_EXPRESSION_".equals(paramKey.toUpperCase())) {
                fieldValue.add(paramValue);
                List<String> columnsForExpress = ExpressionParser.getColumnsInJava(paramValue);
                HashMap<String,List<SorcType>> expressionAtomSorcTypeMap = new HashMap<String,List<SorcType>>();
                for (String column : columnsForExpress) {
                    StructApiArg apiArg = apiArgMap.get(column);
                    String sorcType = StringUtils.defaultIfEmpty(apiArg.getFieldSorcType(), Constants.DEFAULT_JSON_BLANK_VALUE);
                    List<SorcType> sorcTypeList = FastJSONUtil.deserializeList(sorcType, SorcType.class);
                    expressionAtomSorcTypeMap.put(column,sorcTypeList);
                }
                reqArg.setFieldName("_expression_");
                reqArg.setFieldTargtType("");
                reqArg.setCalcPrincId(0);
                reqArg.setMustType(2);
                reqArg.setFieldValue(fieldValue);
                reqArg.setField_sorc_type(new ArrayList<SorcType>());
                reqArg.setExpressionAtomSorcTypeMap(expressionAtomSorcTypeMap);
                reqArgs.add(reqArg);
            } else {
                StructApiArg apiArg = apiArgMap.get(paramKey);
                if (apiArg != null) {
                    BeanUtils.copyProperties(apiArg, reqArg);
                    if (context.getReqType().equals(RequestTypeFormat.syn.name())) {
                        reqArg.setMustType(apiArg.getSyncMustType());
                    } else if (context.getReqType().equals(RequestTypeFormat.asyn.name())) {
                        reqArg.setMustType(apiArg.getAsynMustType());
                    } else {
                        reqArg.setMustType(apiArg.getRssMustType());
                    }
                    //String paramValue = reqParamMap.get(paramKey);
                    //List<String> fieldValue = new ArrayList<String>();
                    fieldValue = Arrays.asList(paramValue.split(Constants.SPLIT_SIGN));
                    reqArg.setFieldValue(fieldValue);
                    String sorcType = StringUtils.defaultIfEmpty(apiArg.getFieldSorcType(), Constants.DEFAULT_JSON_BLANK_VALUE);
                    List<SorcType> sorcTypeList = FastJSONUtil.deserializeList(sorcType, SorcType.class);
                    reqArg.setField_sorc_type(sorcTypeList);
                    reqArgs.add(reqArg);
                }
            }
        }
        reqToFind.setReqArgs(reqArgs);
        return reqToFind;
    }

    /**
     * 设置国际化资源信息
     */
    private void initMessageSource() {
        HashSet<String> baseNamesSet = new HashSet<String>();
        baseNamesSet.add(I18N_ROP_ERROR);// ROP自动的资源

        if (extErrorBasename == null && extErrorBasenames == null) {
            // baseNamesSet.add(DEFAULT_EXT_ERROR_BASE_NAME);
        } else {
            if (extErrorBasename != null) {
                baseNamesSet.add(extErrorBasename);
            }
            if (extErrorBasenames != null) {
                baseNamesSet.addAll(Arrays.asList(extErrorBasenames));
            }
        }
        String[] totalBaseNames = baseNamesSet.toArray(new String[0]);

        if (logger.isInfoEnabled()) {
            logger.info("加载错误码国际化资源：{}", StringUtils.join(totalBaseNames, ","));
        }
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setBasenames(totalBaseNames);
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(bundleMessageSource);
        MainErrors.setErrorMessageSourceAccessor(messageSourceAccessor);
        SubErrors.setErrorMessageSourceAccessor(messageSourceAccessor);
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public FormattingConversionService getFormattingConversionService() {
        return formattingConversionService;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public RopEventMulticaster getRopEventMulticaster() {
        return ropEventMulticaster;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public List<RopEventListener> getListeners() {
        return listeners;
    }

    public boolean isSignEnable() {
        return signEnable;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getExtErrorBasename() {
        return extErrorBasename;
    }

}