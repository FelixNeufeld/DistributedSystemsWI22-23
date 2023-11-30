package de.hrw.dsalab.distsys.chat.XML;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLParser {
    public static void parseXML(String path1, String path2) {
        StringBuilder sb = new StringBuilder();
        try {
            CustomHandler handler = new CustomHandler();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            System.out.println("Parsing " + path1 + ":");
            saxParser.parse(path1, handler);

            System.out.println("\nParsing " + path2 + ":");
            saxParser.parse(path2, handler);

            List<String> tags = handler.getTagList();
            Map<String, Map<String, String>> attributes = handler.getAttribute();

            for (String t : tags) {
                if (attributes.get(t) != null) {
                    sb.append("<!ELEMENT " + t + " ANY>\n");

                    Map<String, String> innerMap = attributes.get(t);

                    sb.append("<!ATTLIST " + t + "\n\t");
                    for (Map.Entry<String, String> entry : innerMap.entrySet()) {
                        sb.append(entry.getKey() + " CDATA #IMPLIED\n\t");
                    }
                    sb.append(">\n");
                } else {
                    sb.append("<!ELEMENT " + t + " ANY>\n");
                }
            }

            // Überprüfe auf Redundanzen
            List<String> uniqueTags = new ArrayList<>(tags);
            uniqueTags.removeIf(tag -> tags.indexOf(tag) != tags.lastIndexOf(tag));

            for (String uniqueTag : uniqueTags) {
                System.out.println("WARNUNG: Das Tag '" + uniqueTag + "' kommt mehr als einmal vor.");
            }

            Path dtdFilePath = Path.of("C:/Users/Neufe/Desktop/Felix/Projects/Uni/Distributed Systems/DistributedSystemsWI22-23/Chat/src/de/hrw/dsalab/distsys/chat/XML/both.dtd");
            Files.writeString(dtdFilePath, sb.toString(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            System.out.println("DTD-Datei erfolgreich erstellt.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class CustomHandler extends DefaultHandler {
        List<String> tagList = new ArrayList<>();
        Map<String, Map<String, String>> attribute = new HashMap<>();
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            tagList.add(qName);

            if (attributes.getLength() > 0) {
                Map<String, String> attributeMap = new HashMap<>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    String attributeName = attributes.getQName(i);
                    String attributeValue = attributes.getValue(i);
                    attributeMap.put(attributeName, attributeValue);
                }
                attribute.put(qName, attributeMap);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

        }

        public List<String> getTagList() {
            return tagList;
        }

        public Map<String, Map<String, String>> getAttribute() {
            return attribute;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            for(String s : tagList){
                sb.append(s);
            }
            return sb.toString();
        }
    }
}
