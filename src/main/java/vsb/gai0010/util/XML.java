package vsb.gai0010.orm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML {
    public static Map<String, String> toMap(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        HashMap<String, String> map = new HashMap<>();

        NodeList configuration = document.getElementsByTagName("Configuration").item(0).getChildNodes();

        for (int i = 0; i < configuration.getLength(); i++) {
            Node current = configuration.item(i);
            if (current.getNodeType() == Node.ELEMENT_NODE) {
                map.put(current.getNodeName().toLowerCase(), current.getTextContent());
            }
        }

        return map;
    }

}
