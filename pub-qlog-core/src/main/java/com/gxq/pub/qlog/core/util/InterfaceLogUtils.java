package com.gxq.pub.qlog.core.util;

import com.gxq.pub.qlog.core.constant.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.Instant;

/***
 * 接口日志打印处理
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j(topic = "interface")
public final class InterfaceLogUtils {

    InterfaceLogUtils() throws Exception {
        throw new IllegalAccessException("Can not instance.");
    }

    /**
     * 接口请求日志打印处理
     * @param serviceId
     * @param protocol
     * @param request
     */
    public static void logRequest(String serviceId, String protocol, Object request) {
        LogInfoUtils.logRequest(log, serviceId, protocol, request);
        MdcUtils.removeLogType();
    }

    /**
     * 接口响应日志打印处理
     * @param serviceId
     * @param protocol
     * @param response
     * @param startTime
     */
    public static void logResponse(String serviceId, String protocol, Object response, long startTime) {
        // 开始时间
        if (0 != startTime) {
            MDC.put(BaseConstants.TIME_DIFF, (Instant.now().toEpochMilli() - startTime) + "ms");
        }

        LogInfoUtils.logResponse(log, serviceId, protocol, response);
        MdcUtils.removeLogType();
        MdcUtils.removeTimeDiff();
    }
}
