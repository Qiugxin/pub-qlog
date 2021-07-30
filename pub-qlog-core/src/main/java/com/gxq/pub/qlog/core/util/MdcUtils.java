package com.gxq.pub.qlog.core.util;

import com.gxq.pub.qlog.core.constant.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/***
 * slf4j MDC helper
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j
public class MdcUtils {

    /**
     * 清除缓存设置
     */
    public static void removeAllMDC() {
        MDC.remove(BaseConstants.Tracer.SERVICE_ID);
        MDC.remove(BaseConstants.ProtocolEnum.getName());
        MDC.remove(BaseConstants.LogTypeEnum.getName());
    }

    public static void removeLogType() {
        MDC.remove(BaseConstants.LogTypeEnum.getName());
    }

    public static void removeTimeDiff() {
        MDC.remove(BaseConstants.TIME_DIFF);
    }

    /**
     * 重新设置MDC参数
     */
    public static void setCurrentLocalMDC(Map<String, String> attMap, String serviceId) {
        if (attMap == null ) {
            attMap = new HashMap<>(1 << 4);
        }

        //优先从上游请求参数中获取
        String transactionId = attMap.get(BaseConstants.Tracer.TRANSACTION_ID);
        String spandId = attMap.get(BaseConstants.Tracer.SPAN_ID);
        String parentId = attMap.get(BaseConstants.Tracer.PARENT_ID);
        String attMapServiceId = attMap.get(BaseConstants.Tracer.SERVICE_ID);

        //上游请求参数中&&父线程中没有，则从全链路中获取
        if (StringUtils.isEmpty(transactionId)) {
            transactionId = MDC.get(BaseConstants.Tracer.TRACE_ID);
            spandId = MDC.get(BaseConstants.Tracer.SPAN_ID);
            parentId = MDC.get(BaseConstants.Tracer.PARENT_ID);
            log.debug("transactionid:{}, spandId:{}, parentId:{} from trace. ", transactionId, spandId, parentId);
        }

        //本地生成
        transactionId = StringUtils.isNotEmpty(transactionId) ? transactionId : Long.toHexString(ThreadLocalRandom.current().nextLong());
        spandId = StringUtils.isNotEmpty(spandId) ? spandId : transactionId;
        parentId = StringUtils.isNotEmpty(parentId) ? parentId : transactionId;

        MDC.put(BaseConstants.Tracer.TRANSACTION_ID, transactionId);
        MDC.put(BaseConstants.Tracer.SPAN_ID, spandId);
        MDC.put(BaseConstants.Tracer.PARENT_ID, parentId);
        //优先设置上游的serviceId
        MDC.put(BaseConstants.Tracer.SERVICE_ID, attMapServiceId);
        if ( StringUtils.isNotBlank(serviceId)) {
            MDC.put(BaseConstants.Tracer.SERVICE_ID, serviceId);
        }
    }
}
