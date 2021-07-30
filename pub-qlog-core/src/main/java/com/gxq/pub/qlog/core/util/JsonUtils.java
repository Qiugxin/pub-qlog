package com.gxq.pub.qlog.core.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/***
 * json工具类
 *
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    /**
     * convert object to json value, whether exclude null value as "null".
     * @param obj
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "";
        }

        return GSON.toJson(obj);
    }

    /**
     * convert list object from json value
     * @param jsonString
     */
    public static Map<String, Object> toMap(String jsonString) {
        if (jsonString == null || jsonString.trim().length() == 0) {
            return null;
        }

        return GSON.fromJson(jsonString, new TypeToken<Map<String, Object>>(){}.getType());
    }
}
