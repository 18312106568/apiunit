package com.mrb.apiunit.factory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.gson.*;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务工厂（单例）
 *
 * @author MRB
 */
public class CAServiceFactory {

    //请求服务地址
    private String url;

    //http请求创建者
    private OkHttpClient.Builder builder;

    //CA服务单例
    private static CAServiceFactory instance;

    private Gson gson;

    public static Map<Class, Object> serviceMap = new HashMap<>();

    private CAServiceFactory(String url) {
        this.url = url;
        this.builder = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS).
                readTimeout(10, TimeUnit.SECONDS).
                writeTimeout(10, TimeUnit.SECONDS);
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                
                return new JsonPrimitive(src.format( DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                
            }
        }).registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }).create();

    }

    /**
     * 获取实例
     *
     * @param url
     * @return
     */

    public static CAServiceFactory
    getInstance(String url) {
        if (instance == null) {
            instance = new CAServiceFactory(url);
        }
        return instance;
    }

    /**
     * 获取请求cilent
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public <T> T getService(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .build();
        return (T) retrofit.create(clazz);
    }
}
