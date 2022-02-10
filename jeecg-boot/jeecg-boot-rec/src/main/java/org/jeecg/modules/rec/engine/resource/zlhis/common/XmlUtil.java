package org.jeecg.modules.rec.engine.resource.zlhis.common;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xml工具类
 *
 * @author yanbiao
 * @since 2019/11/15 15:54
 */
public class XmlUtil {
    /**
     * json转xml
     *
     * @param json
     * @return xml字符串
     * @throws DocumentException
     */
    public static String json2Xml(String json) throws DocumentException {
        JSONObject object = JSONObject.fromObject(json);
        String sXml = "";
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setTypeHintsEnabled(false);
        //设置根目录
        xmlSerializer.setRootName("body");
        String sContent = xmlSerializer.write(object, StandardCharsets.UTF_8.toString());
        Document docCon = DocumentHelper.parseText(sContent);
        sXml = docCon.getRootElement().asXML();
        return sXml;
    }

    public static String json2Xml2(String json) throws DocumentException {
        JSONObject object = JSONObject.fromObject(json);
        String sXml = "";
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setTypeHintsEnabled(false);
        //设置根目录
        xmlSerializer.setRootName("body19802123");
        String sContent = xmlSerializer.write(object, StandardCharsets.UTF_8.toString());
        Document docCon = DocumentHelper.parseText(sContent);
        sXml = docCon.getRootElement().asXML();
        sXml = sXml.replaceAll("<body19802123>", "");
        sXml = sXml.replaceAll("</body19802123>", "");
        sXml = sXml.replaceAll("<e>", "");
        sXml = sXml.replaceAll("</e>", "");
        return sXml;
    }

    /**
     * xml字符串转json对象
     *
     * @param xml
     * @return json对象
     */
    public static JSONObject xml2Json(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        //将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）
        String json = xmlSerializer.read(xml).toString();
        return JSONObject.fromObject(json);
    }

    /**
     * 将String类型的xml转换成对象
     *
     * @param clazz
     * @param xmlStr
     * @return
     */
    public static <T> T xmlStrToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return (T) xmlObject;
    }

    /**
     * xml字符串转json对象
     *
     * @param xml
     * @return json对象
     */
    public static com.alibaba.fastjson.JSONObject xml2Json2(String xml) {
        xml = replaceIrregular(xml);
        XMLSerializer xmlSerializer = new XMLSerializer();
        //将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）
        String json = xmlSerializer.read(xml).toString();
        return com.alibaba.fastjson.JSONObject.parseObject(json);
    }

    /**
     * xml字符串转json对象
     *
     * @param xml
     * @return json对象
     */
    public static com.alibaba.fastjson.JSONObject xml2JsonFilter(String xml) {
        xml = replaceIrregular(xml);
        XMLSerializer xmlSerializer = new XMLSerializer();
        //将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）
        String json = xmlSerializer.read(xml).toString();
        return com.alibaba.fastjson.JSONObject.parseObject(json);
    }

    public static String replaceIrregular(String xml) {
        //除去不规范符号
        Pattern pa = Pattern.compile(">(\\s*|\n|\t|\r)<");
        Matcher m = pa.matcher(xml);
        xml = m.replaceAll("><");

        //除去xml头
        pa = Pattern.compile("<\\?xml.*(?=\\?>)\\?>", Pattern.CASE_INSENSITIVE);
        m = pa.matcher(xml);
        xml = m.replaceAll("");

        xml = xml.replaceAll("<([A-Z|a-z]+)\\s/>", "");
        xml = xml.replaceAll("<([A-Z|a-z]+)/>", "");
        xml = xml.replaceAll("<([A-Z|a-z]+)></([A-Z|a-z]+)>", "");
        xml = xml.replaceAll("\uFEFF", "");
        return xml;
    }

    /**
     * xml字符串转json对象
     *
     * @param xml
     * @return json对象
     */
    public static com.alibaba.fastjson.JSONObject xml2Json2(String xml, String arrayName) {
        xml = replaceIrregular(xml);
        XMLSerializer xmlSerializer = new XMLSerializer();
        //将xml转为json（注：如果是元素的属性，会在json里的key前加一个@标识）
        String json = xmlSerializer.read(xml).toString();
        Object obj = com.alibaba.fastjson.JSONObject.parse(json);
        if (obj instanceof com.alibaba.fastjson.JSONObject)
            return (com.alibaba.fastjson.JSONObject) obj;
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put(arrayName, obj);
        return jsonObject;
    }


    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, Object> data, String rootName) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.newDocument();
            org.w3c.dom.Element root = document.createElement(rootName);
            document.appendChild(root);
            for (String key : data.keySet()) {
                String value = data.get(key) + "";
                if (value == null) {
                    value = "";
                }
                value = value.trim();
                org.w3c.dom.Element filed = document.createElement(key);
                filed.appendChild(document.createTextNode(value));
                root.appendChild(filed);
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
            writer.close();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除多余空行和空格
     */
    public static String dealRedundantSpaceAndBlankLine(String content)
    {
        if (content == null || content.length() == 0)
        {
            return "";
        }
        StringBuilder strAfterRemoveCRSB = new StringBuilder();
        for (int i = 0; i < content.length(); i++)
        {
            if (content.charAt(i) != '\r')
                strAfterRemoveCRSB.append(content.charAt(i));
        }
        String strAfterRemoveCR = strAfterRemoveCRSB.toString();
        if (strAfterRemoveCR == null || strAfterRemoveCR.length() == 0)
        {
            return "";
        }
        StringBuilder resultSB = new StringBuilder();
        String[] lines = strAfterRemoveCR.split("\n");
        int blankCount = 0;
        for (String line : lines)
        {
            if (line == null)
            {
                continue;
            }
            String lineTrim = line.trim();
            if ("".equals(lineTrim))
            {
                blankCount++;
                if (blankCount <= 2) {
                    resultSB.append("\n");
                }
            } else {
                blankCount = 0;
                resultSB.append(dealSpace4OneLine(line)).append("\n");
            }
        }
        resultSB.deleteCharAt(resultSB.length() - 1);
        return resultSB.toString();
    }

    /**
     * 移除1行中的多余空格
     */
    public static String dealSpace4OneLine(String line)
    {
        if (line == null || "".equals(line)) {
            return "";
        }
        int spaceCount = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char curChar = line.charAt(i);
            if (curChar == ' ')
            {
                spaceCount++;
                if (spaceCount <= 5) {
                    sb.append(' ');
                }
            } else {
                spaceCount = 0;
                sb.append(curChar);
            }
        }
        return sb.toString();
    }


    /**
     * (多层)map转换为xml格式字符串
     *
     * @param map     需要转换为xml的map
     * @param isCDATA 是否加入CDATA标识符 true:加入 false:不加入
     * @return xml字符串
     */
    public static String multilayerMapToXml(Map<String, Object> map, String rootName, boolean isCDATA) {
        String parentName = rootName;
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = recursionMapToXml(doc.getRootElement(), parentName, map, isCDATA);
        return formatXML(xml);
    }

    /**
     * (多层)map转换为xml格式字符串
     *
     * @param map     需要转换为xml的map
     * @param isCDATA 是否加入CDATA标识符 true:加入 false:不加入
     * @return xml字符串
     */
    public static String multilayerMapToXml(Map<String, Object> map, boolean isCDATA) {
        String root = "body19802123";
        Document doc = DocumentHelper.createDocument();
        doc.addElement(root);
        String xml = recursionMapToXml(doc.getRootElement(), root, map, isCDATA);
        xml = xml.replaceAll("<body19802123>", "");
        xml = xml.replaceAll("</body19802123>", "");
        return xml;
    }

    /**
     * multilayerMapToXml核心方法，递归调用
     *
     * @param element    节点元素
     * @param parentName 根元素属性名
     * @param map        需要转换为xml的map
     * @param isCDATA    是否加入CDATA标识符 true:加入 false:不加入
     * @return xml字符串
     */
    @SuppressWarnings("unchecked")
    private static String recursionMapToXml(Element element, String parentName, Map<String, Object> map, boolean isCDATA) {
        Element xmlElement = element.addElement(parentName);
        map.keySet().forEach(key -> {
            Object obj = map.get(key);
            if (obj instanceof Map) {
                recursionMapToXml(xmlElement, key, (Map<String, Object>) obj, isCDATA);
            } else {
                String value = obj == null ? "" : obj.toString();
                if (isCDATA) {
                    xmlElement.addElement(key).addCDATA(value);
                } else {
                    xmlElement.addElement(key).addText(value);
                }
            }
        });
        return xmlElement.asXML();
    }


    /**
     * 格式化xml,显示为容易看的XML格式
     *
     * @param xml 需要格式化的xml字符串
     * @return
     */
    public static String formatXML(String xml) {
        String requestXML = null;
        try {
            // 拿取解析器
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml));
            if (null != document) {
                StringWriter stringWriter = new StringWriter();
                // 格式化,每一级前的空格
                OutputFormat format = new OutputFormat("	", true);
                // xml声明与内容是否添加空行
                format.setNewLineAfterDeclaration(false);
                // 是否设置xml声明头部
                format.setSuppressDeclaration(false);
                // 是否分行
                format.setNewlines(true);
                XMLWriter writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                writer.close();
                requestXML = stringWriter.getBuffer().toString();
            }
            return requestXML;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
