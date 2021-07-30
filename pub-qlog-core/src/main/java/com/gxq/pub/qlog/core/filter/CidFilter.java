package com.gxq.pub.qlog.core.filter;


import ch.qos.logback.classic.LoggerContext;
import com.gxq.pub.qlog.core.constant.BaseConstants;
import com.gxq.pub.qlog.core.util.*;
import com.gxq.pub.qlog.http.constant.HttpMediaConstants;
import com.gxq.pub.qlog.http.request.ResettableStreamRequestWrapper;
import com.gxq.pub.qlog.http.response.ResettableStreamResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/***
 * 日志filter
 *
 * author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j
public class CidFilter implements Filter {

    /**
     * 静态资源列表
     */
    private List<String> staticResources = Arrays.asList("/webjars/**", "/**/*.html", "/**/*.htm", "/**/*.jpeg",
            "/**/*.jpg", "/**/*.png", "/**/*.ico", "/**/*.js", "/**/*.css", "/**/*.gif", "/**/*.woff");

    /**
     * 忽略资源列表
     */
    private List<String> ignoreResources = Collections.emptyList();

    /**
     * web上下文
     */
    private WebApplicationContext webApplicationContext;

    private String applicationName;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 1、记录请求开始时间&初始华
        long startTime = Instant.now().toEpochMilli();
        // 清理当前线程中之前使用的缓存
        MdcUtils.removeAllMDC();
        TransactionUtils.removeTracer();

        // 2、请求与响应真实实例转换
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        try {
            // 3、初始日志上下文transactionId
            String transactionId = TransactionUtils.getTransactionId(request);
            MDC.put(BaseConstants.Tracer.TRANSACTION_ID, transactionId);

            // 4、获取当前请求路径
            String uri = RequestUtils.getRequestPath(request);
            log.debug("current request path: " + uri);

            // 5、忽略静态资源或者自定义路径
            if (isFilterUri(uri)) {
                log.debug("ignore this request url: " + uri);
                chain.doFilter(req, res);
                return;
            }

            // 6、初始当前MDC参数
            String serviceId = TransactionUtils.getServiceId(uri, applicationName);
            preHandleLog(transactionId, serviceId);
            Map<String, String> cachMap = MDC.getCopyOfContextMap();

            // 7、打印请求日志
            ResettableStreamRequestWrapper requestWrapper = new ResettableStreamRequestWrapper(request);
            StringBuilder responseBody = new StringBuilder();
            ResettableStreamResponseWrapper responseWrapper =
                    new ResettableStreamResponseWrapper(response, responseBody);

            long re1 = 0, re2;
            if (log.isDebugEnabled()) {
                re1 = Instant.now().toEpochMilli();
            }
            recordRequestLog(request, serviceId, requestWrapper);
            if (log.isDebugEnabled()) {
                re2 = Instant.now().toEpochMilli();
                log.debug("print request log spent: " + (re2 - re1));
            }

            // 8、执行请求
            if (log.isDebugEnabled()) {
                re1 = Instant.now().toEpochMilli();
            }
            chain.doFilter(requestWrapper, responseWrapper);
            if (log.isDebugEnabled()) {
                re2 = Instant.now().toEpochMilli();
                log.debug("print execute business spent: " + (re2 - re1));
            }

            // 当心被业务系统删除MDC相关的值,重新设置回去
            if (StringUtils.isEmpty(MDC.get(BaseConstants.Tracer.TRANSACTION_ID))) {
                MdcUtils.setCurrentLocalMDC(cachMap, null);
            }

            // 9、打印响应日志
            if (log.isDebugEnabled()) {
                re1 = Instant.now().toEpochMilli();
            }
            recordResponseLog(request, serviceId, responseBody, startTime);
            if (log.isDebugEnabled()) {
                re2 = Instant.now().toEpochMilli();
                log.debug("print response log spent: " + (re2 - re1));
            }
        } catch (Exception e) {
            log.error("CidFilter error", e);
            throw e;
        }
    }

    @Override
    public void destroy() {
        // 清理日志上下文
        TransactionUtils.removeTracer();
    }

    @Override
    public void init(FilterConfig fc) {
        webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(fc.getServletContext());

        // 1、获取静态资源列表（可自定义格式，需符合ant）
        String resources = fc.getInitParameter(BaseConstants.STATIC_RESOURCE_URI_ENV);
        if (StringUtils.isNotBlank(resources)) {
            staticResources = Arrays.asList(resources.split(BaseConstants.Separator.COMMA));
        }

        // 2、获取忽略请求列表
        String ignores = fc.getInitParameter(BaseConstants.IGNORE_RESOURCE_URI_ENV);
        if (StringUtils.isNotBlank(ignores)) {
            ignoreResources = Arrays.asList(ignores.split(BaseConstants.Separator.COMMA));
        }

        // 3、获取当前应用名称
        applicationName = fc.getInitParameter(BaseConstants.LOG_NAME_KEY);

        // 4、设置rocketmq日志目录
        if (LoggerFactory.getILoggerFactory() instanceof LoggerContext) {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            String logBase = context.getProperty("log.base");
            if (StringUtils.isNotEmpty(logBase)) {
                RocketMQUtil.setLogRoot(logBase);
            }
        }
    }

    /**
     * 处理日志相关的参数
     *
     * @param transactionId
     * @param serviceId
     */
    private void preHandleLog(String transactionId, String serviceId) {
        String protocol = BaseConstants.ProtocolEnum.HTTP.name();
        String spanId = TransactionUtils.getSpanId(BaseConstants.Tracer.SPAN_ID);

        // 从全链路跟踪中获取父节点，如果为空，表示根节点
        String parentId = TransactionUtils.getSpanId(BaseConstants.Tracer.PARENT_ID);

        MDC.put(BaseConstants.Tracer.SERVICE_ID, serviceId);
        MDC.put(BaseConstants.Tracer.SPAN_ID, spanId);
        MDC.put(BaseConstants.Tracer.PARENT_ID, parentId);
        MDC.put(BaseConstants.ProtocolEnum.getName(), protocol);

        log.debug("transationId:{}, serviceId:{}, spanId:{}, parentId:{}", transactionId, serviceId, spanId, parentId);
        TransactionUtils.transientLog(transactionId, serviceId, spanId, parentId, protocol);
    }

    /**
     * 记录请求日志
     *
     * @param request
     * @param serviceId
     * @param requestWrapper
     */
    private void recordRequestLog(HttpServletRequest request, String serviceId,
                                  ResettableStreamRequestWrapper requestWrapper) {
        Object requestBody = requestWrapper.getRequestBody();
        if (RequestMethod.GET.name().equals(request.getMethod())
                || HttpMediaConstants.FORM_URLENCODED.equals(request.getContentType())) {
            requestBody = RequestUtils.parseRequestParams(request);
        }
        InterfaceLogUtils.logRequest(serviceId, BaseConstants.ProtocolEnum.HTTP.name(), requestBody);
    }

    /**
     * 记录响应&access日志
     *
     * @param request
     * @param serviceId
     * @param responseBody
     * @param startTime
     */
    private void recordResponseLog(HttpServletRequest request, String serviceId, StringBuilder responseBody,
                                   long startTime) {
        String module = StringUtils.defaultIfEmptyTrim(applicationName, webApplicationContext.getApplicationName());
        String body = isNotEmpty(responseBody) ? responseBody.toString() : HttpMediaConstants.EMPTY_STR;
        InterfaceLogUtils.logResponse(serviceId, BaseConstants.ProtocolEnum.HTTP.name(), body, startTime);
        AccessLogUtils.logAccess(request, module, serviceId, startTime, body);
    }

    private boolean isNotEmpty(StringBuilder responseBody) {
        return responseBody != null && StringUtils.isNotEmpty(responseBody.toString());
    }

    /**
     * 是否过滤该请求
     *
     * @param uri
     * @return
     */
    private boolean isFilterUri(String uri) {
        return RequestUtils.isIgnore(uri, staticResources) || RequestUtils.isIgnore(uri, ignoreResources);
    }
}