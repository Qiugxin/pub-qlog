package com.gxq.pub.qlog.http.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
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
}
