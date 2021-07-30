package com.gxq.pub.qlog.core.util;

import com.gxq.pub.qlog.core.constant.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * request & session helper
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j
public class RequestUtils {

    private RequestUtils() throws Exception {
        throw new IllegalAccessException("Can not instance.");
    }

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 获取请求Path
     *
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        String requestPath = "";
        String requestURI = request.getRequestURI();
        if (StringUtils.isNotBlank(requestURI)) {
            String contextPath = request.getContextPath();
            requestPath = !"/".equals(contextPath) && StringUtils.isNotBlank(contextPath) && requestURI.startsWith(contextPath) ? requestURI.substring(contextPath.length()) : requestURI;
        }

        if (StringUtils.isBlank(requestPath)) {
            requestPath = request.getServletPath();
        }

        return requestPath;
    }

    /**
     * 解析http请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, Object> parseRequestParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>(1 << 4);
        Enumeration<String> parameterNames = request.getParameterNames();

        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String[] values = request.getParameterValues(name);
                if (values == null || values.length <= 0) {
                    continue;
                }
                StringBuilder value = new StringBuilder(values[0]);
                for (int i = 1; i < values.length; i++) {
                    value.append(",").append(values[i]);
                }
                params.put(name, value.toString());
            }
        }
        return params;
    }

    /**
     * 获取http请求路径，替换"/"为"_"
     *
     * @param url 请求path
     * @return
     */
    public static String getRequestPathKey(String url) {
        String path = url;
        if (url.startsWith(BaseConstants.Separator.SLASHES)) {
            path = url.replaceFirst(BaseConstants.Separator.SLASHES, StringUtils.EMPTY);
        }

        return path;
    }

    /**
     * 是否忽略该请求路径
     *
     * @param uri
     * @param ignoreUriList
     * @return
     */
    public static boolean isIgnore(String uri, List<String> ignoreUriList) {
        if (null == ignoreUriList || ignoreUriList.isEmpty()) {
            log.debug(" ignore uri list is empty.");
            return false;
        }

        for (String pattern : ignoreUriList) {
            if (PATH_MATCHER.match(pattern, uri)) {
                log.debug("ignore this uri" + uri);
                return true;
            }
        }

        return false;
    }

}
