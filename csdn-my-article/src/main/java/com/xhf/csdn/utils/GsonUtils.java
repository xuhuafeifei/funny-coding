package com.xhf.csdn.utils;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author feigebuge
 * @email 2508020102@qq.com
 */
public class GsonUtils {
    private static final Gson gson = new Gson();


    public static String toJsonStr(JsonObject jsonObject) {
        return gson.toJson(jsonObject);
    }

    public static String toJsonStr(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJson(JsonElement jsonEle, Class<T> clazz) {
        try {
            return gson.fromJson(jsonEle, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> fromJsonArray(JsonArray jsonArray, Class<T> clazz) {
        Iterator<JsonElement> it = jsonArray.iterator();
        List<T> ans = new ArrayList<>();
        while (it.hasNext()) {
            T res = GsonUtils.fromJson(it.next(), clazz);
            ans.add(res);
        }
        return ans;
    }
}
