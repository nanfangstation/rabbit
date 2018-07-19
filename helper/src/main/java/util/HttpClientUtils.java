/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package util;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-07-19 20:21
 */
public class HttpClientUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final String CHAR_SET = "UTF-8";

    //默认超时时间
    private static final int DEFAULT_TIMEOUT = 5000;

    private static final int HTTPCODE_200 = 200;

    private static volatile CloseableHttpClient httpClient;

    static {
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_TIMEOUT)
            .setConnectTimeout(DEFAULT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_TIMEOUT)
            .setStaleConnectionCheckEnabled(true).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(80);// 连接池最大并发连接数
        cm.setDefaultMaxPerRoute(20);// 单路由最大并发数
        if (httpClient == null) {
            httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(defaultRequestConfig)
                .build();
        }
    }

    public static String doPost(String url, Map<String, String> params) {
        LOG.info("请求记录：{},参数：{},timeout:{}", url, JSONObject.toJSONString(params), DEFAULT_TIMEOUT);
        return doPost(url, params, CHAR_SET, DEFAULT_TIMEOUT);
    }

    public static String doPost(String url, Map<String, String> params, int timeout) {
        LOG.info("请求记录：{},参数：{},timeout:{}", url, JSONObject.toJSONString(params), timeout);
        return doPost(url, params, CHAR_SET, timeout);
    }

    public static String doPost(String url, Map<String, String> params, String charset, int timeout) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if (timeout <= 0) {
            timeout = DEFAULT_TIMEOUT;
        }
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nameValuePairs = createHttpParams(params);
            HttpPost post = new HttpPost(url);
            if (!isCollectionEmpty(nameValuePairs)) {
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
            }

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).build();
            post.setConfig(requestConfig);
            response = httpClient.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HTTPCODE_200) {
                post.abort();
                throw new RuntimeException("http request error,http error code :" + responseCode);
            }
            HttpEntity responseEntity = response.getEntity();
            String respString = null;
            if (responseEntity != null) {
                respString = EntityUtils.toString(responseEntity, charset);
            }
            //Closing the content stream: (keeps the underlying connection alive)
            EntityUtils.consume(responseEntity);
            return respString;
        } catch (Exception e) {
            LOG.error("HttpClientUtils http post error, url=" + url, e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error("httpclient response close error ", e);
            }
        }

    }

    public static byte[] doPostByteArray(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nameValuePairs = createHttpParams(params);
            HttpPost post = new HttpPost(url);
            if (!isCollectionEmpty(nameValuePairs)) {
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
            }
            response = httpClient.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HTTPCODE_200) {
                post.abort();
                throw new RuntimeException("http request error,http error code :" + responseCode);
            }
            HttpEntity responseEntity = response.getEntity();
            byte[] respByteArr = null;
            if (responseEntity != null) {
                respByteArr = EntityUtils.toByteArray(responseEntity);
            }
            //Closing the content stream: (keeps the underlying connection alive)
            EntityUtils.consume(responseEntity);
            return respByteArr;
        } catch (Exception e) {
            LOG.error("HttpClientUtils http post error, url=" + url, e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error("httpclient response close error ", e);
            }
        }

    }

    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, CHAR_SET, DEFAULT_TIMEOUT);
    }

    public static String doGet(String url, Map<String, String> params, String charset, int timeout) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if (timeout <= 0) {
            timeout = DEFAULT_TIMEOUT;
        }
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> nameValuePairs = createHttpParams(params);
            URI uri;
            if (!isCollectionEmpty(nameValuePairs)) {
                uri = new URI(url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, charset)));
            } else {
                uri = new URI(url);
            }
            HttpGet httpGet = new HttpGet(uri);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).build();
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HTTPCODE_200) {
                httpGet.abort();
                throw new RuntimeException("http request error,http error code :" + responseCode);
            }
            //得到响应体
            HttpEntity entity = response.getEntity();
            String respString = null;
            if (entity != null) {
                respString = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            return respString;

        } catch (Exception e) {
            LOG.error("HttpClientUtils http get error, url=" + url, e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error("httpclient response close error ", e);
            }
        }

    }

    private static List<NameValuePair> createHttpParams(Map<String, String> params) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (!isMapEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nameValuePairs;
    }

    private static boolean isMapEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isCollectionEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
