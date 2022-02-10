package org.jeecg.modules.rec.engine.resource.zlhis.common;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecg.modules.rec.engine.config.RecConfig;
import org.jeecg.modules.rec.engine.model.RecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * zlhis 工具类
 */
@Component
public class ZlHisUtil {

    @Autowired
    private HttpUtil httpUtil;
    @Autowired
    private RecConfig recConfig;


    public JSONObject httpPostService(String moduleName, String serviceName, String data, String... arrayPath) throws Exception {
        List<NameValuePair> params = parseServiceParamIn(data, serviceName);
        String url = recConfig.zlHis_serviceUrl() + "/" + moduleName;
        System.out.println("request:" + data);
        String result = httpUtil.doPostMap(url, params);
        System.out.println("response:" + result);
        return parseResult2JsonObject(parseResultDecrpt(result), arrayPath);
    }

    public JSONObject httpPostService(String uri, String moduleName, String serviceName, String data, String... arrayPath) throws Exception {
        List<NameValuePair> params = parseServiceParamIn(data, serviceName);

        //String url = SystemConfig.instanceOf(SysConfigPlatForm.class).getServiceUrl() + "/" + moduleName;
        String url = uri + "/" + moduleName;
        System.out.println("request:" + data);
        String result = new HttpUtil().doPostMap(url, params);
        System.out.println("response:" + result);
        return parseResult2JsonObject(parseResultDecrpt(result), arrayPath);
    }

    public JSONObject parseServiceResult(String result, String[] arrayPath) throws Exception {
        return parseResult2JsonObject(parseResultDecrpt(result), arrayPath);
    }

    private List<NameValuePair> parseServiceParamIn(String data, String serviceName) {
        List<NameValuePair> params = new ArrayList(1);
        data = data + "<HZDW>" + recConfig.zlHis_channelName() + "</HZDW><JSKLB>" + recConfig.zlHis_channelName() + "</JSKLB>";
        String token = recConfig.zlHis_token();
        String key = recConfig.zlHis_key();
//        token = "ED50707FD6CAB9D950EB59075101D1A6";
//        key = "333D9DEA4C711DBD";
        String tokenDecrypt = encryptAES(token, key);
        String dataDecrypt = encryptAES(data, key);
        String xmlData = "<ROOT>" +
                "<SERVICE><![CDATA[" + serviceName + "]]></SERVICE>" +
                "<DATAPARAM><![CDATA[" + dataDecrypt + "]]></DATAPARAM>" +
                "<TOKEN><![CDATA[" + tokenDecrypt + "]]></TOKEN>" +
                "<INSIDEKEY>p92MhiG0Wz/h47aKaMW7pA==</INSIDEKEY>" +
                "</ROOT>";
        params.add(new BasicNameValuePair("ReData", xmlData));
        return params;
    }

    public String parseServiceResultOut(boolean state, String data, String errMsg) {
        String token = recConfig.zlHis_token();
        String key = recConfig.zlHis_key();
//        token = "ED50707FD6CAB9D950EB59075101D1A6";
//        key = "333D9DEA4C711DBD";
        String tokenDecrypt = encryptAES(token, key);
        String xmlData = "<ROOT>" +
                "<TOKEN><![CDATA[" + tokenDecrypt + "]]></TOKEN>" +
                "<STATE><![CDATA[" + (state ? "T" : "F") + "]]></STATE>";
        if (!state) {
            xmlData += "<ERROR><ERROR_MSG><![CDATA[" + errMsg + "]]></ERROR_MSG></ERROR>";
        }
        if (!StringUtils.isEmpty(data)) {
        }
        xmlData += "</ROOT>";
        return xmlData;
    }


    private String parseResultDecrpt(String data) throws Exception {
        try {
            StringReader sr = new StringReader(data);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            NodeList nodelist = document.getElementsByTagName("DATAPARAM");
            String key = recConfig.zlHis_key();

            //key = "11E246AAFFBFB0B3";
            if (nodelist != null && nodelist.getLength() > 0) {
                Node node = nodelist.item(0).getFirstChild();
                String temp = decryptAES(node.getNodeValue(), key);
                temp = temp.replaceAll("<SL>[.]", "<SL>0.");
                temp = temp.replaceAll("<DJ>[.]", "<DJ>0.");
                temp = temp.replaceAll("<JE>[.]", "<JE>0.");
                node.setNodeValue(temp);
            }
            String result = parseXml2Str(document);
            System.out.println("DecryptResult:" + result);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    public JSONObject parseResult2JsonObject(String paramData, String[] arrayPath) throws Exception {
        try {
            JSONObject jsonObj = new XmlUtil().xml2Json2(paramData);
            //构建新的结果对象
            JSONObject resultJsonObj = new JSONObject();
            resultJsonObj.put("STATE", jsonObj.getString("STATE"));
            resultJsonObj.put("SERVICE", jsonObj.getString("SERVICE"));
            resultJsonObj.put("ERROR", jsonObj.get("ERROR"));
            String dataXml = jsonObj.getString("DATAPARAM");
            JSONObject contentObj = DataTransUtil.xml2JsonPrefix("<ROOT><DOC>" + dataXml + "</DOC></ROOT>", "ROOT/DOC", arrayPath);
            if (contentObj != null)
                resultJsonObj.put("CONTENT", contentObj.get("DOC"));
            return resultJsonObj;
        } catch (Exception e) {
            throw e;
        }
    }

    public JSONObject parseXml2Json(String xml) throws Exception {
        net.sf.json.JSONObject jsonObject = XmlUtil.xml2Json(xml);
        String jsonStr = jsonObject.toString();
        return JSONObject.parseObject(jsonStr);
    }

    /**
     * xml array节点 处理
     *
     * @return
     */
    public String parseXmlArrayByXpath(String xml, String prefix, String... arrayXpath) throws Exception {

        if (arrayXpath == null || arrayXpath.length < 1) return xml;
        Document document = documentByXmlstr(xml);
        XPath xPath = XPathFactory.newInstance().newXPath();
        for (String path : arrayXpath) {
            NodeList nodeList = (NodeList) xPath.evaluate(prefix + path, document, XPathConstants.NODESET);
            if (nodeList != null) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        element.setAttribute("class", "array");//设置属性load="false"
                    }
                }
            }
        }
        return parseXml2Str(document);
    }

    public String decryptAES(String sSrc, String sKey) {
        try {
            // 判断Key是否为16位
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return "解密异常";
        }
    }

    public String encryptAES(String sSrc, String sKey) {
        try {
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

            return Base64.getEncoder().encodeToString(encrypted);
//            return new BASE64Encoder().encode(encrypted);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return "加密异常";
        }
    }

    /**
     * 获取document对象
     *
     * @param xmlStr
     * @return
     */
    public Document documentByXmlstr(String xmlStr) throws Exception {
        StringReader sr = new StringReader(xmlStr);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }

    private String parseXml2Str(Document document) {
        try {
            StringWriter sw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(document), new StreamResult(sw));
            String result = sw.toString();
            sw.flush();
            sw.close();
            return result;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
