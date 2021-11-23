package com.mars.counter.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mars.counter.bean.res.Account;
import com.mars.counter.bean.res.PosiInfo;

import java.util.Arrays;
import java.util.List;

public class JsonUtil {
    private static Gson gson = new Gson();
    private static JsonParser jsonParser = new JsonParser();
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 处理json格式的数组
     * @param json
     * @param classOfListT
     * @return
     */
    public static <T> List<T> fromJsonArr(String json, Class<T> classOfListT) {
        List<T> beans = Lists.newArrayList();
        for(JsonElement jsonBean : jsonParser.parse(json).getAsJsonArray()){
            T bean = gson.fromJson(jsonBean , classOfListT);
            beans.add(bean);
        }
        return beans;
    }
}
