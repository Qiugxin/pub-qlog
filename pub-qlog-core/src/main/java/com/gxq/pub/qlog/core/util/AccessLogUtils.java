package com.gxq.pub.qlog.core.util;

import com.gxq.pub.qlog.core.bo.AccessLogBo;
import com.gxq.pub.qlog.core.constant.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/***
 *  access日志处理
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */

@Slf4j(topic = "access")
public class AccessLogUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogUtils.class);

    AccessLogUtils() throws Exception {
        throw new IllegalAccessException("Can not instance.");
    }

    /**
     * 打印access日志
     *
     * @param request
     * @param module
     * @param serviceId
     * @param startTime
     * @param returnValue
     */
    public static void logAccess(HttpServletRequest request, String module, String serviceId, Long startTime, Object returnValue) {
        logAccess(module, serviceId, IpUtils.getRemoteIp(request), IpUtils.getLocalIp(), startTime, BaseConstants.ProtocolEnum.HTTP.name(), returnValue, true, null);
    }

    /**
     * 打印access日志
     *
     * @param request
     * @param module
     * @param serviceId
     * @param startTime
     * @param returnValue
     * @param properties
     */
    public static void logAccess(HttpServletRequest request, String module, String serviceId, Long startTime, Object returnValue, Map<String, Object> properties) {
        logAccess(module, serviceId, IpUtils.getRemoteIp(request), IpUtils.getLocalIp(), startTime, BaseConstants.ProtocolEnum.HTTP.name(), returnValue, true, properties);
    }

    /**
     * 打印access日志
     *
     * @param module
     * @param serviceId
     * @param remoteIp
     * @param localIp
     * @param startTime
     * @param protocol
     * @param returnValue
     * @param isServerSide
     */
    public static void logAccess(String module, String serviceId, String remoteIp, String localIp, Long startTime, String protocol, Object returnValue, boolean isServerSide, Map<String, Object> properties) {
        if (StringUtils.isBlank(serviceId)) {
            log.warn("serviceId must be not null!");
            return;
        }

        long endTime = Instant.now().toEpochMilli();
        AccessLogBo accessLogBo = AccessLogBo.builder().transactionId(MDC.get(BaseConstants.Tracer.TRANSACTION_ID)).build();

        accessLogBo.setRemoteIp(remoteIp);
        accessLogBo.setLocalIp(localIp);
        accessLogBo.setModule(module);
        accessLogBo.setServiceId(serviceId);
        accessLogBo.setProperties(properties);

        // 开始时间
        if (null != startTime) {
            accessLogBo.setStartTime(TimeUtils.toStringFormat0(TimeUtils.from(startTime)));
            accessLogBo.setTimeDiff(String.valueOf(endTime - startTime));
        }
        // 结束时间
        accessLogBo.setEndTime(TimeUtils.toStringFormat0(TimeUtils.from(endTime)));
        // 服务类型
        String endPoint = isServerSide ? BaseConstants.SERVICE_TYPE : BaseConstants.CLIENT_TYPE;
        accessLogBo.setEndPoint(endPoint);
        // 节点信息
        accessLogBo.setSpanId(MDC.get(BaseConstants.Tracer.SPAN_ID));
        accessLogBo.setParentId(MDC.get(BaseConstants.Tracer.PARENT_ID));

        // 业务返回为空处理，只打印日志，不处理异常情况
        if (null == returnValue) {
            LOGGER.debug("当前serviceId: {},响应报文为空", serviceId);
            LogInfoUtils.log(log, protocol, accessLogBo);
            return;
        }

        Map<String, Object> returnMap = new HashMap<>(1 << 4);
        String jsonStr;
        //处理响应数据转换
        if (returnValue instanceof String) {
            jsonStr = returnValue.toString();
        } else {
            jsonStr = JsonUtils.toJson(returnValue);
        }

        try {
            //数据本地转换，用于日志打印
            if (StringUtils.isNotEmpty(jsonStr)) {
                returnMap = JsonUtils.toMap(jsonStr);
            }
        } catch (Exception e) {
            log.debug("响应报文json解析失败，响应报文：{}", jsonStr);
        }

        //获取状态码，用于access日志打印
        if (returnMap != null && !returnMap.isEmpty()) {
            String resultCode = StringUtils.objToStr(convertToInt(returnMap.get(BaseConstants.DEFAULT_RESULT_CODE_NAME)));
            accessLogBo.setResultCode(resultCode);
            //兼容HTTP响应接口规范处理
            if (returnMap.containsKey(BaseConstants.DEFAULT_CODE_NAME)) {
                resultCode = StringUtils.objToStr(convertToInt(returnMap.get(BaseConstants.DEFAULT_CODE_NAME)));
                accessLogBo.setCode(resultCode);
            }

            if (StringUtils.isEmpty(accessLogBo.getResultCode()) && StringUtils.isEmpty(accessLogBo.getCode())) {
                if (returnMap.containsKey(BaseConstants.OTHER_RESULT_CODE_NAME)) {
                    String result = StringUtils.objToStr(convertToInt(returnMap.get(BaseConstants.OTHER_RESULT_CODE_NAME)));
                    accessLogBo.setResultCode(result);
                }
            }

            String errorCode = StringUtils.objToStr(convertToInt(returnMap.get(BaseConstants.DEFAULT_ERROR_CODE_NAME)));
            String errorDesc = StringUtils.objToStr(convertToInt(returnMap.get(BaseConstants.DEFAULT_ERROR_DESC_NAME)));

            accessLogBo.setErrorCode(errorCode);
            accessLogBo.setErrorDesc(errorDesc);
        }

        //正常打印access日志
        LogInfoUtils.log(log, protocol, accessLogBo);
    }

    private static Object convertToInt(Object object) {
        if (object instanceof Double) {
            return ((Double) object).intValue();
        }

        return object;
    }
}
