package org.framework.common.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright @ 2016QIANLONG.
 * All right reserved.
 * Class Name : org.framework.common.util
 * Description :
 * Author : snowxuyu
 * Date : 2016/11/29
 */

public abstract class HttpsClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private HttpsClientUtils() {
    }

    public static String sendGetRequest(String reqURL) throws Exception {
        String respContent = "通信失败";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(20000));
        HttpGet httpGet = new HttpGet(reqURL);

        try {
            HttpResponse e = httpClient.execute(httpGet);
            HttpEntity entity = e.getEntity();
            if(null != entity) {
                Charset respHeaderDatas = ContentType.getOrDefault(entity).getCharset();
                respContent = EntityUtils.toString(entity, respHeaderDatas);
                EntityUtils.consume(entity);
            }

            System.out.println("-------------------------------------------------------------------------------------------");
            StringBuilder var26 = new StringBuilder();
            Header[] respStatusLine = e.getAllHeaders();
            int respHeaderMsg = respStatusLine.length;

            for(int respBodyMsg = 0; respBodyMsg < respHeaderMsg; ++respBodyMsg) {
                Header header = respStatusLine[respBodyMsg];
                var26.append(header.toString()).append("\r\n");
            }

            String var27 = e.getStatusLine().toString();
            String var28 = var26.toString().trim();
            System.out.println("HTTP应答完整报文=[" + var27 + "\r\n" + var28 + "\r\n\r\n" + respContent + "]");
            System.out.println("-------------------------------------------------------------------------------------------");
            return respContent;
        } catch (ConnectTimeoutException var19) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", var19);
            throw var19;
        } catch (SocketTimeoutException var20) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", var20);
            throw var20;
        } catch (ClientProtocolException var21) {
            logger.error("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下", var21);
            throw var21;
        } catch (ParseException var22) {
            logger.error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下", var22);
            throw var22;
        } catch (IOException var23) {
            logger.error("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下", var23);
            throw var23;
        } catch (Exception var24) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", var24);
            throw var24;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public static String sendPostRequest(String reqURL, String reqData, String contentType, String encodeCharset) throws Exception {
        String reseContent = "通信失败";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(20000));
        HttpPost httpPost = new HttpPost(reqURL);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + encodeCharset);

        try {
            httpPost.setEntity(new StringEntity(reqData == null?"":reqData, encodeCharset));
            HttpResponse e = httpClient.execute(httpPost);
            HttpEntity entity = e.getEntity();
            if(null != entity) {
                reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException var14) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", var14);
            throw var14;
        } catch (SocketTimeoutException var15) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", var15);
            throw var15;
        } catch (Exception var16) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", var16);
            throw var16;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return reseContent;
    }

    public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String contentType, String encodeCharset) throws Exception {
        String responseContent = "通信失败";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(20000));
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };

        try {
            SSLContext e = SSLContext.getInstance("TLS");
            e.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(e, hostnameVerifier);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpPost httpPost = new HttpPost(reqURL);
            httpPost.setHeader("Content-Type", contentType);
            if(null != params) {
                ArrayList response = new ArrayList();
                Iterator entity = params.entrySet().iterator();

                while(entity.hasNext()) {
                    Map.Entry entry = (Map.Entry)entity.next();
                    response.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
                }

                httpPost.setEntity(new UrlEncodedFormEntity(response, encodeCharset));
            }

            HttpResponse response1 = httpClient.execute(httpPost);
            HttpEntity entity1 = response1.getEntity();
            if(null != entity1) {
                responseContent = EntityUtils.toString(entity1, ContentType.getOrDefault(entity1).getCharset());
                EntityUtils.consume(entity1);
            }
        } catch (ConnectTimeoutException var19) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", var19);
            throw var19;
        } catch (SocketTimeoutException var20) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", var20);
            throw var20;
        } catch (Exception var21) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", var21);
            throw var21;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseContent;
    }

    public static String sendGetSSLRequest(String reqURL, Map<String, String> params, String contentType, String encodeCharset) throws Exception {
        String responseContent = "通信失败";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(20000));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(20000));
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };

        try {
            SSLContext e = SSLContext.getInstance("TLS");
            e.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(e, hostnameVerifier);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            String paramStr = "";
            if(null != params) {
                ArrayList httpGet = new ArrayList();
                int response = 0;

                for(Iterator entity = params.entrySet().iterator(); entity.hasNext(); ++response) {
                    Map.Entry entry = (Map.Entry)entity.next();
                    httpGet.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
                    if(response != 0) {
                        paramStr = paramStr + "&" + (String)entry.getKey() + "=" + (String)entry.getValue();
                    } else {
                        paramStr = paramStr + "?" + (String)entry.getKey() + "=" + (String)entry.getValue();
                    }
                }
            }

            HttpGet var24 = new HttpGet(reqURL + paramStr);
            var24.setHeader("Content-Type", contentType);
            HttpResponse var25 = httpClient.execute(var24);
            HttpEntity var26 = var25.getEntity();
            if(null != var26) {
                responseContent = EntityUtils.toString(var26, ContentType.getOrDefault(var26).getCharset());
                EntityUtils.consume(var26);
            }
        } catch (ConnectTimeoutException var20) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", var20);
            throw var20;
        } catch (SocketTimeoutException var21) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", var21);
            throw var21;
        } catch (Exception var22) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", var22);
            throw var22;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseContent;
    }

    public static String sendGetSSLRequest(String reqURL, Map<String, String> params, String contentType, String encodeCharset, Integer socket_time) throws Exception {
        String responseContent = "通信失败";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", socket_time);
        httpClient.getParams().setParameter("http.socket.timeout", socket_time);
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };

        try {
            SSLContext e = SSLContext.getInstance("TLS");
            e.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(e, hostnameVerifier);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            String paramStr = "";
            if(null != params) {
                ArrayList httpGet = new ArrayList();
                int response = 0;

                for(Iterator entity = params.entrySet().iterator(); entity.hasNext(); ++response) {
                    Map.Entry entry = (Map.Entry)entity.next();
                    httpGet.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
                    if(response != 0) {
                        paramStr = paramStr + "&" + (String)entry.getKey() + "=" + (String)entry.getValue();
                    } else {
                        paramStr = paramStr + "?" + (String)entry.getKey() + "=" + (String)entry.getValue();
                    }
                }
            }

            HttpGet var25 = new HttpGet(reqURL + paramStr);
            var25.setHeader("Content-Type", contentType);
            HttpResponse var26 = httpClient.execute(var25);
            HttpEntity var27 = var26.getEntity();
            if(null != var27) {
                responseContent = EntityUtils.toString(var27, ContentType.getOrDefault(var27).getCharset());
                EntityUtils.consume(var27);
            }
        } catch (ConnectTimeoutException var21) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", var21);
            throw var21;
        } catch (SocketTimeoutException var22) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", var22);
            throw var22;
        } catch (Exception var23) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", var23);
            throw var23;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseContent;
    }

    public static String sendPostSSLRequest(String reqURL, Integer timout, String contentType, String encodeCharset, String message) throws Exception {
        String responseContent = "通信失败";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", timout);
        httpClient.getParams().setParameter("http.socket.timeout", timout);
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };

        try {
            SSLContext e = SSLContext.getInstance("TLS");
            e.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(e, hostnameVerifier);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpPost httpPost = new HttpPost(reqURL);
            httpPost.setHeader("Content-Type", contentType);
            StringEntity reqEntiey = new StringEntity(message, encodeCharset);
            httpPost.setEntity(reqEntiey);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(null != entity) {
                responseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException var20) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", var20);
            throw var20;
        } catch (SocketTimeoutException var21) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", var21);
            throw var21;
        } catch (Exception var22) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", var22);
            throw var22;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseContent;
    }

    public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String encodeCharset) throws Exception {
        String responseContent = "通信失败";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(30000));
        httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(30000));
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };

        try {
            SSLContext e = SSLContext.getInstance("TLS");
            e.init((KeyManager[])null, new TrustManager[]{trustManager}, (SecureRandom)null);
            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(e, hostnameVerifier);
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            HttpPost httpPost = new HttpPost(reqURL);
            if(null != params) {
                ArrayList response = new ArrayList();
                Iterator entity = params.entrySet().iterator();

                while(entity.hasNext()) {
                    Map.Entry entry = (Map.Entry)entity.next();
                    response.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
                }

                httpPost.setEntity(new UrlEncodedFormEntity(response, encodeCharset));
            }

            HttpResponse response1 = httpClient.execute(httpPost);
            HttpEntity entity1 = response1.getEntity();
            if(null != entity1) {
                responseContent = EntityUtils.toString(entity1, ContentType.getOrDefault(entity1).getCharset());
                EntityUtils.consume(entity1);
            }
        } catch (ConnectTimeoutException var18) {
            logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", var18);
            throw var18;
        } catch (SocketTimeoutException var19) {
            logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", var19);
            throw var19;
        } catch (Exception var20) {
            logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", var20);
            throw var20;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseContent;
    }

    public static void main(String[] args) {
        try {
            String reponse = sendPostSSLRequest("http://1.202.150.2345:9080/dv/authentication.do?_t=json", (Map)null, "application/json;charset=utf-8", "text/xml;charset=UTF-8");
            System.out.println(reponse);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

}
