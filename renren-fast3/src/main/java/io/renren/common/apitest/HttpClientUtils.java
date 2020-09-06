package io.renren.common.apitest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

public class HttpClientUtils {
    // 通过默认配置 创建一个httpClient实例
    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static CloseableHttpResponse httpResponse = null;
    private static String result = null;
    // 设置配置请求参数
    private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
            .setConnectionRequestTimeout(35000)// 请求超时时间
            .setSocketTimeout(60000)// 数据读取超时时间
            .build();
    /**
     * doGett()
     * @param url
     * headers：null
     * params：null 是传入的是以 & 为分隔符的 String
     * @return
     */
    public static  String doGet(String url){
        return doGet(url, Collections.emptyMap() ,Collections.emptyMap());
    }

    /**
     * doGet()
     * @param url
     * @param headers
     * @param paramMap
     * @return
     */
    public static String doGet(String url, Map<String, Object> headers, Map<String, Object> paramMap) {

        // 为httpGet实例设置配置
        URIBuilder uriBuilder= null;
        // 记录起始时间
        Long start = System.currentTimeMillis();
        try {
            uriBuilder = new URIBuilder(url);

            if (paramMap != null && !paramMap.isEmpty()) {
                Set<String> keySet = paramMap.keySet();
                for (String key : keySet) {
                    //设置参数
                    uriBuilder.setParameter(key,paramMap.get(key).toString());
                }
            }


            // 创建HttpGet对象，设置URL地址
            HttpGet httpGet =new HttpGet(uriBuilder.build());

            // 设置请求头信息，鉴权
            if (headers != null && !headers.isEmpty()) {
                Set<String> keySet = headers.keySet();
                for (String key : keySet) {
                    httpGet.setHeader(key, headers.get(key).toString());
                }
            }

            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象

            httpResponse = httpClient.execute(httpGet);
            // 返回状态
            StatusLine line = httpResponse.getStatusLine();
            if(line.getStatusCode() == 200){
                // 通过返回对象获取返回数据, HttpEntity
                HttpEntity entity = httpResponse.getEntity();
                // 通过EntityUtils中的toString方法将结果转换为字符串
                result = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (URISyntaxException e){

        }
        finally {
            try {
                if (httpResponse != null){
                    httpResponse.close();
                }
                // 记录结束时间
                Long end = System.currentTimeMillis();
                if (((end-start) > 10*1000) && httpClient != null){
                    httpClient.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }
    /**
     * doPost()
     * @param url
     * @param headers
     * @param params 传入的是以 & 为分隔符的 String
     * @return
     */
    public static  String doPost(String url, Map<String, Object> headers, String params){
        Map<String, Object> paramMap = new HashMap();
        String[] pStrings = params.split("&");
        for (int i = 0; i < pStrings.length; i++) {
            String param = pStrings[i];
            String[] param_array = param.split("=");
            paramMap.put(param_array[0], param_array[1]);
        }
        return doPost(url,headers,paramMap);
    }
    /**
     * doPost()
     * @param url
     * @param headers
     * @param paramMap
     * @return
     */
    public static String doPost(String url, Map<String, Object> headers, Map<String, Object> paramMap){
        // 创建httpGet远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 起始时间
        Long start = System.currentTimeMillis();
        // 设置请求头信息，鉴权
        if (headers != null && !headers.isEmpty()) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                httpPost.setHeader(key, headers.get(key).toString());
            }
        }
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);

        // 设置 POST 请求参数
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        // 封装post请求参数
        if (null != paramMap && !paramMap.isEmpty()) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Set<String> keySet = paramMap.keySet();
            for (String key : keySet){
                list.add(new BasicNameValuePair(key, paramMap.get(key).toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 执行get请求得到返回对象
        try {
            httpResponse = httpClient.execute(httpPost);
            // 返回状态
            StatusLine line = httpResponse.getStatusLine();
            if(line.getStatusCode() == 200){
                // 通过返回对象获取返回数据, HttpEntity
                HttpEntity entity = httpResponse.getEntity();
                // 通过EntityUtils中的toString方法将结果转换为字符串
                result = EntityUtils.toString(entity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (httpResponse != null){
                    httpResponse.close();
                }
                // 记录结束时间
                Long end = System.currentTimeMillis();
                if (((end-start) > 10*1000) && httpClient != null){
                    httpClient.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }

    /***
     * doPostJson() 方法
     * @param url
     * @param headers
     * @param param, json串
     * @return
     */
    public static String doPostJson(String url, Map<String, Object> headers, String param){
        // 创建httpGet远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 记录起始时间
        Long start = System.currentTimeMillis();
        // 设置请求头信息，鉴权
        if (headers != null && !headers.isEmpty()) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                httpPost.setHeader(key, headers.get(key).toString());
            }
        }
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);

        // 设置 POST 请求参数
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json");
        // 封装post请求参数
        if (null != param ) {
            httpPost.setEntity(new StringEntity(param,"utf-8"));
        }

        // 执行get请求得到返回对象
        try {
            httpResponse = httpClient.execute(httpPost);
            // 返回状态
            StatusLine line = httpResponse.getStatusLine();
            if(line.getStatusCode() == 200){
                // 通过返回对象获取返回数据, HttpEntity
                HttpEntity entity = httpResponse.getEntity();
                // 通过EntityUtils中的toString方法将结果转换为字符串
                result = EntityUtils.toString(entity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (httpResponse != null){
                    httpResponse.close();
                }
                // 记录结束时间
                Long end = System.currentTimeMillis();
                if (((end-start) > 10*1000) && httpClient != null){
                    httpClient.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }


    public static void main(String[] args) {
        String url = "https://aicampus.ainirobot.com/api/hw/v1/c/p/sso_auth";
        String student_uid = "0901d8b3-4cd4-4f71-84d1-798e7cda26d9";
        String parent_uid = "ffec6e02-0756-4843-a0b0-e0c9364ba507";
        Map global_data = new HashMap();
        global_data.put("parent_uid",parent_uid);
        global_data.put("student_uid",student_uid);
        //String params = "parent_uid=ffec6e02-0756-4843-a0b0-e0c9364ba507&student_uid=0901d8b3-4cd4-4f71-84d1-798e7cda26d9";
        String params = "parent_uid=${parent_uid}&student_uid=${student_uid}";
        String result = doPost(url,null, params);
        System.out.println(result);
        // fail
        // {"ret":"100400","msg":"parent_uid invalid","stime":"1597840682","data":{},"reqid":"46772bd3ad3d7dc35f6936466bbb1211","strace":"46772bd3ad3d7dc35f6936466bbb1211"}
        // success
        //{"code":0,"msg":"ok","data":{"total":1436,"primary":{"total":925,"boys":529,"girls":396},"middle":{"total":511,"boys":325,"girls":186}}}


    }
}
