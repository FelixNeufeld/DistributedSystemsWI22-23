package de.hrw.dsalab.distsys.chat.XML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLReader {
    private Document doc;
    private String path;
    private String fontName;
    private int fontSize;
    private Map<String, String> shortcuts = new HashMap<>();
    private String theme;
    private String connection;
    private int port;
    public XMLReader(String path){
        this.path = path;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(path);

            Element root = doc.getDocumentElement();

            // "*" gibt alle Elemente zurück
            NodeList nodeList = doc.getElementsByTagName("*");

            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                switch (node.getNodeName()){
                    case "fontName":
                        this.fontName = node.getTextContent();
                        break;
                    case "size":
                        this.fontSize = Integer.parseInt(node.getTextContent());
                        break;
                    case "connection":
                        this.connection = node.getTextContent();
                        break;
                    case "port":
                        this.port = Integer.parseInt(node.getTextContent());
                        break;
                    case "shortcut":
                        Element shortCutElement = (Element) node;
                        this.shortcuts.put(shortCutElement.getAttribute("key"), shortCutElement.getAttribute("action"));
                        break;
                    case "theme":
                        this.theme = node.getTextContent();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Document getDoc() {
        return doc;
    }

    public String getPath() {
        return path;
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public Map<String, String> getShortcuts() {
        return shortcuts;
    }

    public String getTheme() {
        return theme;
    }

    public String getConnection() {
        return connection;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Path: " + path + "\nFont: " + fontName + "\nFontsize: " + fontSize + "\nShortcuts: " + shortcuts.toString() + "\nTheme: " + theme;
    }
}
