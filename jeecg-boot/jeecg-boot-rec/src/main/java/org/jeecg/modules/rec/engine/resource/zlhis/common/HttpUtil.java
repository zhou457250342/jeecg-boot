package org.jeecg.modules.rec.engine.resource.zlhis.common;

import com.alibaba.druid.util.StringUtils;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jeecg.modules.rec.engine.model.RecException;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * HTTP工具类，用于发送hiip请求
 *
 * @author : zhang
 * @date : 2019/7/18
 */
@Component
public class HttpUtil {
    private static final int HTTP_SUCCESS = 200;
    //@Autowired
    //private AsynLogService asynLogService;

    /**
     * 发送post请求
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return 返回值字符串
     */
    public String doPost(String url, String param) {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        int status = 0;
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new StringEntity(param, StandardCharsets.UTF_8));

            CloseableHttpResponse response = httpclient.execute(httppost);
            status = response.getStatusLine().getStatusCode();
            if (status != HTTP_SUCCESS) {
                throw new HttpException();
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            httppost.releaseConnection();
            return result;
        } catch (Exception e) {
            System.out.println("doPost:" + e.getStackTrace());
            return null;
        }
    }

    public String doPostJson(String url, String json) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {

            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("User-Agent", "Mozilla/5.0");
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

            StringEntity stringEntity = new StringEntity(json, StandardCharsets.UTF_8);
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            httpResponse = httpClient.execute(httpPost);

            reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));

            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

        } catch (Exception var) {
            var.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
            httpClient.close();
        }
        String responseText = response.toString();
        if (StringUtils.isEmpty(responseText)) {
            throw new Exception("请求未响应");
        }
        return responseText;
    }

    public static String doPostXml(String url, String xmlInfo) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {

            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("User-Agent", "Mozilla/5.0");
            httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");

            StringEntity stringEntity = new StringEntity(xmlInfo, StandardCharsets.UTF_8);
            stringEntity.setContentType("text/xml");
            stringEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(stringEntity);
            httpResponse = httpClient.execute(httpPost);

            reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));

            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

        } catch (Exception var) {
            var.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
            httpClient.close();
        }
        String responseText = response.toString();
        if (StringUtils.isEmpty(responseText)) {
            throw new Exception("请求未响应");
        }
        return responseText;
    }

    /**
     * httpGet请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String doGet(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int status = response.getStatusLine().getStatusCode();
        if (status != HTTP_SUCCESS) {
            throw new HttpException("HTTP获取异常");
        }
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        httpGet.releaseConnection();
        return result;
    }

    public String doPostMap(String url, List<NameValuePair> params) throws Exception {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        int timeOut = 1000 * 60 * 5;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
        int status = 0;
        try {
//            params = new ArrayList(10);
//            params.add(new BasicNameValuePair("ReData","<Root></Root>"));
            HttpPost httppost = new HttpPost(url);
            httppost.setConfig(requestConfig);
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));


            CloseableHttpResponse response = httpclient.execute(httppost);
            status = response.getStatusLine().getStatusCode();
            if (status != HTTP_SUCCESS) {
                throw new HttpException();
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            httppost.releaseConnection();
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 发送get请求https
     *
     * @param hsUrl 请求地址
     * @param param 请求参数
     * @return 返回值字符串
     */
    public String doGetHttps(String hsUrl, HashMap param) {
        try {
            if (param != null && param.size() > 0) {
                hsUrl += "?";
                Iterator iter = param.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    hsUrl += key + "=" + URLEncoder.encode(val, "UTF-8") + "&";
                }
            }

            URL url = new URL(hsUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }
            };

            TrustManager[] tm = {xtm};

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });


            InputStream inStream = con.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] b = outStream.toByteArray();//网页的二进制数据
            outStream.close();
            inStream.close();
            String rtn = new String(b, "utf-8");
            return rtn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取request内容
     *
     * @param request
     * @return
     */
    public static String getRequestBody(HttpServletRequest request) throws Exception {
        StringBuffer notifyResult = new StringBuffer();
        try (BufferedReader reader = request.getReader()) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                notifyResult.append(inputLine);
            }
        }
        return notifyResult.toString();
    }

    /**
     * 响应Json内容
     *
     * @param resp
     * @param content
     */
    public static void responseJson(HttpServletResponse resp, com.alibaba.fastjson.JSONObject content) throws Exception {
        PrintWriter out = null;
        try {
            resp.setContentType("application/json;charset=UTF-8");
            out = resp.getWriter();
            out.write(content.toJSONString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 下载内容
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String downLoadContent(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse httpResp = httpclient.execute(httpGet);
            HttpEntity httpEntity = httpResp.getEntity();
            return EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            httpclient.close();
        }
        return null;
    }

    /**
     * 将HIIP的soap请求结果转成json格式
     *
     * @param xml 通过http请求获取返回的String，判断这个String是XML还是JSON，然后做出对应的解析方式
     * @return
     */
    public JSON result2Json(String xml) {
        String context = "";
        if ("".equals(xml)) {
            return new JSONObject();
        }
        if (xml.startsWith("<soap:Envelope")) {
            context = getSoapResult(xml);
        } else if (xml.startsWith("<res>")) {
            context = xml;
        }
        JSON json = XmlUtil.xml2Json(context);
        return json;
    }

    /**
     * 截取soap结果中实际返回的值
     */
    private String getSoapResult(String soapXml) {
        if (soapXml.indexOf("<resultXml>") > 0) {
            return getSoapResultContext(soapXml, "resultXml");
        } else if (soapXml.indexOf("<ns1:resultXml>") > 0) {
            return getSoapResultContext(soapXml, "ns1:resultXml");
        } else {
            return "";
        }
    }

    /**
     * 将xml中转码符号还原
     */
    private String getSoapResultContext(String soapXml, String resultEle) {
        int begin = soapXml.indexOf("<" + resultEle + ">");
        int end = soapXml.indexOf("</" + resultEle + ">");
        String context = soapXml.substring(begin + ("<" + resultEle + ">").length(), end);
        if (!"".equals(context)) {
            context = StringEscapeUtils.unescapeHtml4(context);
        }
        return context;
    }
}
