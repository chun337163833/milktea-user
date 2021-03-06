package com.milktea.milkteauser.util;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

/**
 * 基于HttpClient封装的HTTP访问工具类
 */
public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    //private static CloseableHttpClient httpClient=HttpClients.createDefault(); // 创建httpClient实例
    //private static HttpGet httpGet=new HttpGet(); // 创建httpget实例



    public static  String get(String url) throws Exception{
        CloseableHttpClient httpClient=HttpClients.createDefault(); // 创建httpClient实例
        HttpGet httpGet=new HttpGet();
        httpGet.setURI(URI.create(url));
       // httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko"); // 设置请求头消息User-Agent
        CloseableHttpResponse response= null; // 执行http get请求
        response = httpClient.execute(httpGet);
        int statusCode=response.getStatusLine().getStatusCode();
        if (statusCode!=200){
           throw new Exception("无法连接该地址");
        }

        HttpEntity entity=response.getEntity(); // 获取返回实体
        String content=EntityUtils.toString(entity,"utf-8");
        response.close(); // response关闭
        httpClient.close(); // httpClient关闭httpClient关闭
        return content;
    }

    /**
     *
     * @param url    访问url
     * @param params 携带参数
     * @return    返回内容
     * @throws IOException
     */
    public static String get(String url, Map<String,String> params) throws Exception{
        StringBuilder href=new StringBuilder(url+"?");
        params.forEach((k,v)->{
              href.append(k).append("=").append(v).append("&");

        });
        CloseableHttpClient httpClient=HttpClients.createDefault();
        HttpGet httpGet=new HttpGet(href.toString());
        // httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko"); // 设置请求头消息User-Agent
        CloseableHttpResponse response=httpClient.execute(httpGet); // 执行http get请求
        int statusCode=response.getStatusLine().getStatusCode();
        if (statusCode!=200){
            throw new Exception("无法连接该地址");
        }
        HttpEntity entity=response.getEntity(); // 获取返回实体
        String content=EntityUtils.toString(entity,"utf-8");
        response.close();
        httpClient.close();
        return content;
    }

    /**
     *
     * @param url 请求url
     * @return
     * @throws IOException
     */
     public static String post(String url) throws Exception{
         CloseableHttpClient client = HttpClients.createDefault();
         HttpPost httpPost = new HttpPost(url);
         httpPost.setHeader("Accept", "application/json");
         httpPost.setHeader("Content-type", "application/json");

         CloseableHttpResponse response = client.execute(httpPost);
         int statusCode=response.getStatusLine().getStatusCode();
         if (statusCode!=200){
             throw new Exception("无法连接该地址");
         }
         HttpEntity resEntity=response.getEntity(); // 获取返回实体
         String content=EntityUtils.toString(resEntity,"utf-8");
         //  assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
         response.close();
         client.close();

         return content;

     }
    /**
     *
     * @param url     请求url
     * @param params  参数
     * @return
     * @throws IOException
     */
    public static String post(String url,Map<String,String> params) throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //将Map转为Json
        Gson gson = new Gson();
        String json = gson.toJson(params);
        StringEntity entity = new StringEntity(json,"UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        //表单形式
        httpPost.setHeader("Content-type", "application/json");
        

        CloseableHttpResponse response = client.execute(httpPost);
        int statusCode=response.getStatusLine().getStatusCode();
        if (statusCode!=200){
            throw new Exception("无法连接该地址"+statusCode);
         }
        HttpEntity resEntity=response.getEntity(); // 获取返回实体
        String content=EntityUtils.toString(resEntity,"utf-8");
      //  assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        response.close();
        client.close();

        return content;

    }

    /**
     * post 表单
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String postForm(String url,Map<String,String> params) throws Exception{
        //采用绕过验证的方式处理https请求
       /* SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);*/


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //将Map转为String
        StringBuilder param=new StringBuilder();
        params.forEach((k,v)->{
            param.append(k).append("=").append(v).append("&");
        });

        StringEntity entity = new StringEntity(param.toString(),"UTF-8");
        httpPost.setEntity(entity);
       // httpPost.setHeader("Accept", "application/json");
        //表单形式
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        

        CloseableHttpResponse response = client.execute(httpPost);
        int statusCode=response.getStatusLine().getStatusCode();
        if (statusCode!=200){
            throw new Exception("连接地址失败"+statusCode);
         }
        HttpEntity resEntity=response.getEntity(); // 获取返回实体
        String content=EntityUtils.toString(resEntity,"utf-8");
      //  assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        response.close();
        client.close();

        return content;

    }


    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }



}
