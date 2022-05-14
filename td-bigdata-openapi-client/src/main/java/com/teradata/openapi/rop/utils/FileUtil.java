package com.teradata.openapi.rop.utils;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件处理工具
 * @author jdkleo
 *
 */
public class FileUtil {
    private final static Log logger = StaticLog.get();
    /**
     * 断点续传支持
     * @param file
     * @param request
     * @param response
     * @return 跳过多少字节
     */
    public static long headerSetting(File file,HttpServletRequest request, HttpServletResponse response) {
        long len = file.length();//文件长度
        if ( null == request.getHeader("Range") ){
            setResponse(new RangeSettings(len),file.getName(),response);
            return 0;
        }
        String range = request.getHeader("Range").replaceAll("bytes=", "");
        RangeSettings settings = getSettings(len,range);
        setResponse(settings,file.getName(),response);
        return settings.getStart();
    }

    private static void setResponse(RangeSettings settings,String fileName, HttpServletResponse response) {

        response.addHeader("Content-Disposition", "attachment; filename=\"" + IoUtil.toUtf8String(fileName) + "\"");
        response.setContentType( IoUtil.setContentType(fileName));// set the MIME type.

        if (!settings.isRange())
        {
            response.addHeader("Content-Length", String.valueOf(settings.getTotalLength()));
        }
        else
        {
            long start = settings.getStart();
            long end = settings.getEnd();
            long contentLength = settings.getContentLength();

            response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);

            response.addHeader("Content-Length", String.valueOf(contentLength));

            String contentRange = new StringBuffer("bytes ").append(start).append("-").append(end).append("/").append(settings.getTotalLength()).toString();
            response.setHeader("Content-Range", contentRange);
        }
    }

    private static RangeSettings getSettings(long len, String range) {
        long contentLength = 0;
        long start = 0;
        long end = 0;
        if (range.startsWith("-"))// -500，最后500个
        {
            contentLength = Long.parseLong(range.substring(1));//要下载的量
            end = len-1;
            start = len - contentLength;
        }
        else if (range.endsWith("-"))//从哪个开始
        {
            start = Long.parseLong(range.replace("-", ""));
            end = len -1;
            contentLength = len - start;
        }
        else//从a到b
        {
            String[] se = range.split("-");
            start = Long.parseLong(se[0]);
            end = Long.parseLong(se[1]);
            contentLength = end-start+1;
        }

        logger.debug("RANGE VALUE start:" + start + " end:" + end + " contentLength:" + contentLength + " len:" + len);

        return new RangeSettings(start,end,contentLength,len);
    }



}