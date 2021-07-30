package com.gxq.pub.qlog.core.util;

import lombok.extern.slf4j.Slf4j;

/***
 * 远程接口日志打印处理
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j(topic = "remote")
public final class RemoteLogUtils {

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
     */
    public static void logResponse(String serviceId, String protocol, Object response) {
        LogInfoUtils.logResponse(log, serviceId, protocol, response);
        MdcUtils.removeLogType();
    }

}
