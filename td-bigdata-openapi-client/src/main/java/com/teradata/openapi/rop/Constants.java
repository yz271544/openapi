package com.teradata.openapi.rop;

public class Constants {

	public static final String SYS_DEFAULT_APP_KEY = "system";

	public static final String SYS_DEFAULT_APP_SECRET = "system";

	public static final String UTF8 = "UTF-8";

	public static final String GBK = "GBK";

	public static final String OPENAPI_APIS_KEY = "APIS_KEY";

	// 必选状态
	public static final Integer PROP_NECESSARY_0 = 0;

	// 必选其一
	public static final Integer PROP_NECESSARY_1 = 1;

	// 可选
	public static final Integer PROP_NECESSARY_2 = 2;

	// 周期字段
	public static final Integer PROP_NECESSARY_3 = 3;

	// 计算法则单目
	public static final String SUBHD_TYPE_1 = "1";

	// 计算法则双目
	public static final String SUBHD_TYPE_2 = "2";

	// 计算法则多目
	public static final String SUBHD_TYPE_N = "N";

	// 分割符
	public static final String SPLIT_SIGN = ",";

	// 连接符
	public static final String JOIN_SIGN = "#";

	// 调用akka超时时间 单位 s
	public static final Integer AKKA_TIME_OUT = 120;

	// 同步
	public static final Integer REQ_TYPE_0 = 0;

	// 异步
	public static final Integer REQ_TYPE_1 = 1;

	// 订阅
	public static final Integer REQ_TYPE_2 = 2;

	// reqtoFind
	public static final String MESSAGE_TYPE_1 = "1";

	// cycleRcd
	public static final String MESSAGE_TYPE_2 = "2";

	// 控制开关 0关 1开
	public static final String ON_FLAG = "1";

	public static final String OFF_FLAG = "0";

	// 是否 0否1是
	public static final String YES_FLAG = "1";

	public static final String NO_FLAG = "0";

	// 请求方式
	public static final String REQ_WAY_POST = "POST";

	public static final String REQ_WAY_GET = "GET";

	public static final String DEFAULT_JSON_BLANK_VALUE = "[]";

	// 分页参数
	public static final String PAGE_SIZE = "pageSize";

	public static final String PAGE_NUM = "pageNum";

	public static final Integer DEFAULT_PAGE_SIZE = 200;

	// api 类型
	public static final Integer API_SORT_CODE_4 = 4; // 工具类

	public static final String EXCEL_HEADER = "excelHeader";

	// redis key 生存时间默认 40天
	public static final Integer DEFAULT_REDIS_EXPIRE = 40;

}
