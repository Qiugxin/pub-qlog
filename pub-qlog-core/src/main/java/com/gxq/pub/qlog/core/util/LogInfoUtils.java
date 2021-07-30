package com.gxq.pub.qlog.core.util;

import com.gxq.pub.qlog.core.constant.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.event.Level;

/***
 * 日志工具类
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j
public final class LogInfoUtils {

    /**
     * 打印接口请求日志
     *
     * @param logger
     * @param serverId
     * @param protocol
     * @param info
     */
    public static void logRequest(Logger logger, String serverId, String protocol, Object info) {
        logRequest(logger, serverId, protocol, BaseConstants.LogTypeEnum.request.name(), info);
    }

    /**
     * 打印接口请求日志
     *
     * @param logger
     * @param serverId
     * @param protocol
     * @param logType
     * @param info
     */
    public static void logRequest(Logger logger, String serverId, String protocol, String logType, Object info) {
        log(logger, serverId, protocol, logType, Level.INFO, info);
    }

    /**
     * 打印接口响应日志
     *
     * @param logger
     * @param serverId
     * @param protocol
     * @param info
     */
    public static void logResponse(Logger logger, String serverId, String protocol, Object info) {
        logResponse(logger, serverId, protocol, BaseConstants.LogTypeEnum.response.name(), info);
    }

    /**
     * 打印接口响应日志
     *
     * @param logger
     * @param serverId
     * @param protocol
     * @param logType
     * @param info
     */
    public static void logResponse(Logger logger, String serverId, String protocol, String logType, Object info) {
        log(logger, serverId, protocol, logType, Level.INFO, info);
    }

    /**
     * 打印日志
     *
     * @param logger 日志对象
     * @param info   日志信息对象
     */
    public static void log(Logger logger, Object info) {
        log(logger, "", info);
    }

    /**
     * 打印日志
     *
     * @param logger   日志对象
     * @param protocol 请求协议
     * @param info     日志信息对象
     */
    public static void log(Logger logger, String protocol, Object info) {
        log(logger, protocol, info, Level.INFO);
    }

    /**
     * 打印日志
     *
     * @param logger    日志对象
     * @param protocol  请求协议
     * @param info      日志信息对象
     * @param levelType 日志级别
     */
    public static void log(Logger logger, String protocol, Object info, Level levelType) {
        log(logger, "", protocol, "", levelType, info);
    }

    /**
     * 打印日志
     *
     * @param logger
     * @param serverId
     * @param protocol
     * @param logType   [request | response]
     * @param info
     * @param levelType
     */
    public static void log(Logger logger, String serverId, String protocol, String logType, Level levelType, Object info, Object... arguments) {
        if (null == logger) {
            log.warn("logger must be not null!");
            return;
        }

        //serviceId
        String oldServiceId = MDC.get(BaseConstants.Tracer.SERVICE_ID);
        if (StringUtils.isNotBlank(serverId)) {
            MDC.put(BaseConstants.Tracer.SERVICE_ID, serverId);
        }

        //日志类型处理
        if (StringUtils.isNotBlank(logType)) {
            MDC.put(BaseConstants.LogTypeEnum.getName(), logType);
        }

        //协议处理
        if (StringUtils.isNotBlank(protocol)) {
            MDC.put(BaseConstants.ProtocolEnum.getName(), protocol);
        }

        // 目标处理对象(日志脱敏处理)
        String logMessage;
        if (info == null) {
            logMessage = "null";
        } else if (info instanceof String) {
            logMessage = info.toString();
        } else {
            logMessage = JsonUtils.toJson(info);
        }

        if (null == levelType || Level.INFO.equals(levelType)) {
            logger.info(logMessage, arguments);
        } else if (Level.ERROR.equals(levelType)) {
            logger.error(logMessage, arguments);
        } else if (Level.DEBUG.equals(levelType)) {
            logger.debug(logMessage, arguments);
        } else if (Level.WARN.equals(levelType)) {
            logger.warn(logMessage, arguments);
        }

        MDC.put(BaseConstants.Tracer.SERVICE_ID, oldServiceId);
    }
}