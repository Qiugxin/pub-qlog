package com.gxq.pub.qlog.core.constant;

/***
 * http request & response日志输出
 *
 * author guixinQiu
 * @date 2021/1/7 15:57
 */
public interface BaseConstants {

	/**
	 * 日志上下文属性
	 */
	interface Tracer {
		/**
		 * 事务ID
		 */
		String TRANSACTION_ID = "transactionId";

		/**
		 * 事务ID
		 */
		String SERVICE_ID = "serviceId";

		/**
		 * 全链路跟踪ID
		 */
		String TRACE_ID = "traceId";

		/**
		 * 节点id
		 */
		String SPAN_ID = "spanId";

		/**
		 * 父节点id
		 */
		String PARENT_ID = "parentId";

	}


	/**
	 * 接口请求协议
	 */

	enum ProtocolEnum {

		/**
		 * HTTP Protocol.
		 */
		HTTP,

		/**
		 * DUBBO Protocol.
		 */
		DUBBO,

		/**
		 * WEBSERVICE Protocol.
		 */
		WEBSERVICE;

		public static String getName() {
			return "protocol";
		}

	}

	enum LogTypeEnum {

		/**
		 * HTTP request.
		 */
		request,

		/**
		 * HTTP response.
		 */
		response;

		public static String getName() {
			return "logType";
		}

	}


	/**
	 * 客户端 - 调用其它服务的
	 */
	String CLIENT_TYPE = "C";

	/**
	 * 服务端 - 被其它服务调用的
	 */
	String SERVICE_TYPE = "S";

	/**
	 * 默认接口resultCode字段名称
	 */
	String DEFAULT_RESULT_CODE_NAME = "resultCode";

	/**
	 * 兼容其它resultCode字段
	 */
	String OTHER_RESULT_CODE_NAME = "result";

	/**
	 * HTTP响应规范，接口resultCode字段名称
	 */
	String DEFAULT_CODE_NAME = "code";

	/**
	 * 默认接口errorCode字段名称
	 */
	String DEFAULT_ERROR_CODE_NAME = "errorCode";

	/**
	 * 默认接口errorDesc字段名称
	 */
	String DEFAULT_ERROR_DESC_NAME = "errorDesc";

	/**
	 *  接口业务日志
	 */
	String LOGGER_INTERFACE = "interface";

	/**
	 *  访问日志ID
	 */
	String LOGGER_ACCESS = "access";

	/**
	 *  远程接口日志
	 */
	String LOGGER_REMOTE = "remote";


	/**
	 * 是否代理
	 */
	String PROXY_FLAG = "proxy_flag";

	String PROXY_FLAG_1 = "1";

	String PUB_XLOG_FILTER = "xlog";

	String STATIC_RESOURCE_URI_ENV =  PUB_XLOG_FILTER + Separator.POINT + "staticResourcePatterns";

	String IGNORE_RESOURCE_URI_ENV = PUB_XLOG_FILTER + Separator.POINT + "ignoredPatterns";

	String LOG_NAME_KEY = BaseConstants.PUB_XLOG_FILTER + Separator.POINT + "name" ;

	/**
	 * 分隔符
	 */
	interface Separator {

		String POINT = ".";
		String COMMA = ",";
		String SEMICOLON = ";";
		String SLASHES = "/";
		String BACKSLASH = "\\";
		String UNDERLINE = "_";
		String MIDDLELINE = "-";
		String POUND = "#";
		String ASTERISK = "*";
		String QUESTION = "?";
		String AND = "&";
		String EQUAL = "=";
		String COLON = ":";
		String LEFTBRACKET = "[";
		String RIGHTBRACKET = "]";
		String VERTICALLINE = "|";
	}

	/**
	 * 空值
	 */
	String STRING_UNDEFINE_VALUE = "undefined";

	/**
	 * 忽略swagger
	 */
	String SWAGGER_CLASS_PACKAGE = "springfox.documentation";

	/**
	 * interface日志耗时打印属性
	 */
	String TIME_DIFF = "timeDiff";

}