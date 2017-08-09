package org.jbb.lib.cache.hazelcast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

@Component
public class HazelcastXmlEditor {

    public Document getXmlConfig(String filename) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            return b.parse(new File(filename));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void updateGroupName(Document doc, String groupName) {
        setTextContent(doc, "/hazelcast/group/name", groupName);
    }

    public void updateGroupPassword(Document doc, String groupPassword) {
        setTextContent(doc, "/hazelcast/group/password", groupPassword);
    }

    public void updatePort(Document doc, int port) {
        setTextContent(doc, "/hazelcast/network/port", String.valueOf(port));
    }

    public void putMemberList(Document doc, List<String> members) {
        putChildList(doc, "/hazelcast/network/join/tcp-ip/member-list", "member", members);
    }

    private void setTextContent(Document doc, String xpathExpression, String value) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node startDateNode = (Node) xPath.compile(xpathExpression)
                .evaluate(doc, XPathConstants.NODE);
            startDateNode.setTextContent(value);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    private void putChildList(Document doc, String xpathExpression, String nodeName,
        List<String> values) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node rootNode = (Node) xPath.compile(xpathExpression)
                .evaluate(doc, XPathConstants.NODE);
            for (Node child; (child = rootNode.getFirstChild()) != null;
                rootNode.removeChild(child)) {
                ;
            }
            values.forEach(
                value -> {
                    Text a = doc.createTextNode(value);
                    Element p = doc.createElement(nodeName);
                    p.appendChild(a);
                    rootNode.appendChild(p);
                }
            );
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
    }

    public void save(Document doc, String filename) {
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty(OutputKeys.METHOD, "xml");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource domSource = new DOMSource(doc);
            StreamResult sr = new StreamResult(new File(filename));
            tf.transform(domSource, sr);
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
    }

}
