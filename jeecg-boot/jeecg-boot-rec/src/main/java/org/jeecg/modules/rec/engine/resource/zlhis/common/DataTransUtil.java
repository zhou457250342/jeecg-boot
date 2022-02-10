package org.jeecg.modules.rec.engine.resource.zlhis.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据转换工具
 */
public class DataTransUtil {

    public static JSONObject xml2Json(String xml, String... arrayXpath) throws Exception {
        xml = parseXmlArrayByXpath(xml, null, arrayXpath);
        JSONObject jsonObject = XmlUtil.xml2Json2(xml);
        return jsonObject;
    }


    public static JSONObject xml2JsonPrefix(String xml, String prefix, String... arrayXpath) throws Exception {
        xml = parseXmlArrayByXpath(xml, prefix, arrayXpath);
        JSONObject jsonObject = XmlUtil.xml2Json2(xml);
        filterNullElement(jsonObject);
        return jsonObject;
    }

    public static void filterNullElement(JSONObject jsonObject) {
        if (jsonObject == null) return;
        jsonObject.entrySet().forEach(op -> {
            if (op.getValue() instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) op.getValue();
                jsonArray.removeIf(rm -> rm == null || (rm instanceof JSONArray && ((JSONArray) rm).isEmpty()));
                jsonArray.forEach(ni -> {
                    if (ni instanceof JSONObject) filterNullElement((JSONObject) ni);
                });
            } else {
                if (op.getValue() instanceof JSONObject)
                    filterNullElement((JSONObject) op.getValue());
            }
        });
    }

    public static JSONObject xml2Json(String xml, String rootArrayName, String... arrayXpath) throws Exception {
        xml = parseXmlArrayByXpath(xml, null, arrayXpath);
        JSONObject jsonObject = XmlUtil.xml2Json2(xml, rootArrayName);
        return jsonObject;
    }

    public static <T> List<T> json2JavaObject(JSONArray list, Class<T> tClass) {
        if (CollectionUtils.isEmpty(list)) return null;
        return list.stream().map(op -> JSONObject.toJavaObject((JSON) op, tClass)).collect(Collectors.toList());
    }

    public static JSONObject jsonFilterEmpty(JSONObject jsonObject) {
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String key, Object value) {
                if (value instanceof JSONArray) {
                    JSONArray temp = ((JSONArray) value);
                    for (int i = 0; i < temp.size(); i++) {
                        try {
                            if (temp.getJSONObject(i) == null || temp.getJSONObject(i).isEmpty()) {
                                temp.remove(i);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
                return true;
            }
        };
        String temp = JSONObject.toJSONString(jsonObject, propertyFilter);
        return JSONObject.parseObject(temp);
    }

    private static String parseXmlArrayByXpath(String xml, String prefix, String... arrayXpath) throws Exception {
        try {
            if (arrayXpath == null || arrayXpath.length < 1) return xml;
            Document document = createXmlDocument(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();
            for (String path : arrayXpath) {
                String temp = path;
                if (StringUtils.isNotBlank(prefix)) temp = "/" + prefix + path;
                NodeList nodeList = (NodeList) xPath.evaluate(temp, document, XPathConstants.NODESET);
                if (nodeList != null) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            //element.setAttribute("class", "array");
                            Element elementChildOne = document.createElement(element.getNodeName());
                            elementChildOne.setAttribute("class", "temp");
                            Node parentNode = element.getParentNode();
                            List<Node> nodes = parseNodeList(parentNode.getChildNodes());
                            if (nodes.stream().filter(op -> op.getNodeName().equals(element.getNodeName())).count() <= 1)
                                parentNode.appendChild(elementChildOne);
                        }
                    }
                }
            }
            return xmlDocument2Str(document);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * 创建xml document
     *
     * @param xmlStr
     * @return
     * @throws Exception
     */
    private static Document createXmlDocument(String xmlStr) throws ParserConfigurationException, SAXException, IOException {
        StringReader sr = new StringReader(xmlStr);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }

    /**
     * xml
     *
     * @param document
     * @return
     */
    private static String xmlDocument2Str(Document document) {
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

    private static List<Node> parseNodeList(NodeList nodeList) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++)
            nodes.add(nodeList.item(i));
        return nodes;
    }
}
