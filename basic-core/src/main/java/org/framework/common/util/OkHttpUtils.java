package org.framework.common.util;

import okhttp3.*;
import org.framework.common.crypto.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: snowxuyu
 * Date: 2017/3/24
 * Time: 16:36
 */
public abstract class OkHttpUtils {

    private final static Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    private static final String CHARSET = "UTF-8";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static ThreadLocal<Map<String, String>> httpHeader = new ThreadLocal<>();


    private static OkHttpClient buildOkHttpClinent() {
        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
    }


    public static String doGet(String url, Map<String, String> params) throws IOException {
        OkHttpClient okHttpClient;
        Request request;
        Response response = null;
        okHttpClient = buildOkHttpClinent();
        try {
            if ( params != null && !params.isEmpty() ) {
                url += "?" + Tools.createLinkString(params, true);
            }
            Request.Builder builder = new Request.Builder().url(url);
            handlerHeader(builder);
            request = builder.build();
            response = okHttpClient.newCall(request).execute();
            if ( !response.isSuccessful() ) {
                logger.error("Unexpected code ", response);
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } finally {
            if ( response != null ) {
                response.close();
            }
        }
    }


    public static String doPost(String url, Map<String, String> params) throws IOException {
        OkHttpClient okHttpClient;
        Request request;
        Response response = null;
        okHttpClient = buildOkHttpClinent();
        try {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            Request.Builder builder = new Request.Builder().url(url);
            handlerHeader(builder);
            request = builder.post(formBuilder.build()).build();
            response = okHttpClient.newCall(request).execute();
            if ( !response.isSuccessful() ) {
                logger.error("Unexpected code ", response);
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } finally {
            if ( response != null ) {
                response.close();
            }
        }
    }


    public static String doPost(String url, String jsonParam) throws IOException {
        OkHttpClient okHttpClient;
        Request request;
        Response response = null;
        okHttpClient = buildOkHttpClinent();
        try {
            RequestBody requestBody = RequestBody.create(JSON, jsonParam);
            Request.Builder builder = new Request.Builder().url(url);
            handlerHeader(builder);
            request = builder.post(requestBody).build();
            response = okHttpClient.newCall(request).execute();
            if ( !response.isSuccessful() ) {
                logger.error("Unexpected code ", response);
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } finally {
            if ( response != null ) {
                response.close();
            }
        }
    }

    private static void handlerHeader(Request.Builder builder) {
        if ( httpHeader != null && httpHeader.get() != null ) {
            Map<String, String> map = httpHeader.get();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }


    public static void setHeader(Map<String, String> header) {
        if ( header != null ) {
            httpHeader.set(header);
        }
    }


}
