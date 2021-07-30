package com.gxq.pub.qlog.core.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.gxq.pub.qlog.core.bo.TransactionBo;
import com.gxq.pub.qlog.core.constant.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ThreadLocalRandom;

/***
 * 日志事务工具类
 *
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j
public final class TransactionUtils {

    public static final TransmittableThreadLocal<TransactionBo> TRACE_THREAD_LOCAL = new TransmittableThreadLocal<TransactionBo>();

    /**
     * 获取当前请求中的transactionId
     *
     * @param request
     * @return
     */
    public static String getTransactionId(HttpServletRequest request) {
        String transactionId = request.getHeader(BaseConstants.Tracer.TRANSACTION_ID);
        log.debug("transaction id {} from http header. ", transactionId);
        if (StringUtils.isEmpty(transactionId)) {
            transactionId = MDC.get(BaseConstants.Tracer.TRACE_ID);
            log.debug("transaction id {} from trace. ", transactionId);
        }

        transactionId = StringUtils.isNotEmpty(transactionId) ? transactionId : Long.toHexString(ThreadLocalRandom.current().nextLong());
        log.debug("return transaction id {}.", transactionId);
        return transactionId;
    }

    /**
     * 获取当前请求的serviceId
     *
     * @param path
     * @param contextPath
     * @return
     */
    public static String getServiceId(String path, String contextPath) {
        String serviceId = RequestUtils.getRequestPathKey(path);
        if (StringUtils.isNotEmpty(contextPath)) {
            serviceId = contextPath + BaseConstants.Separator.SLASHES + serviceId;
        }

        return serviceId;
    }

    /**
     * 在当前线程中缓存相关的参数
     *
     * @param transactionId
     * @param serviceId
     * @param spandId
     * @param parentId
     * @param potocal
     */
    public static void transientLog(String transactionId, String serviceId, String spandId, String parentId, String potocal) {
        TransactionBo bo = TransactionBo.builder().transactionId(transactionId).serviceId(serviceId).spanId(spandId).parentId(parentId).protocol(potocal).build();
        TRACE_THREAD_LOCAL.set(bo);
    }

    /**
     * 从缓存中获取当前MDC相关参数
     *
     * @return
     */
    public static TransactionBo getTracer() {
        TransactionBo transactionBo = TRACE_THREAD_LOCAL.get();
        if (null != transactionBo) {
            log.debug("get tracer(transactionId={}, serviceId={}, spanId={}) from cache", transactionBo.getTransactionId(), transactionBo.getServiceId(), transactionBo.getSpanId());
        } else {
            log.debug("the cache of tracer is null");
        }

        return transactionBo;
    }

    /**
     * 获取spanId
     * @return
     */
    public static String getSpanId(String key) {
        String spandId = MDC.get(key);
        log.debug("spand id {} from MDC. ", spandId);

        spandId = StringUtils.isNotEmpty(spandId) ? spandId : Long.toHexString(ThreadLocalRandom.current().nextLong());
        log.debug("return {} id {}.", key, spandId);
        return spandId;
    }

    /**
     * 从缓存中清除MDC相关参数
     */
    public static void removeTracer() {
        TRACE_THREAD_LOCAL.remove();
    }


    /**
     * 跨线程调用，子线程初始参数
     */
    public static void generateFromTracer() {
        TransactionBo bo = TransactionUtils.getTracer();

        if  ( bo != null ) {
            MDC.put(BaseConstants.Tracer.TRANSACTION_ID, bo.getTransactionId());
            MDC.put(BaseConstants.Tracer.SERVICE_ID, bo.getServiceId());
            MDC.put(BaseConstants.Tracer.SPAN_ID, bo.getSpanId());
            MDC.put(BaseConstants.Tracer.PARENT_ID, bo.getParentId());
            MDC.put(BaseConstants.ProtocolEnum.getName(), bo.getProtocol());
        } else {
            log.debug("can not found transaction.");
        }
    }
}
