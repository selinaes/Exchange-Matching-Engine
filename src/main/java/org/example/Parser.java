package org.example;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.example.requests.CreateRequest;
import org.example.requests.TransactionRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.example.requests.Request;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class Parser {
  public static Request parseXML(String xml) throws ParserConfigurationException, IOException, SAXException {

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
    doc.getDocumentElement().normalize();

    Element request = doc.getDocumentElement();
    String type = request.getNodeName();

    return requestBuilder(type, request);

  }

  public static Request requestBuilder(String type, Element element) {
    switch (type) {
      case "create":
        return new CreateRequest(element);
      case "transactions":
        return new TransactionRequest(element);
      default:
        throw new IllegalArgumentException("Invalid request");
    }
  }

  public static void print(Document document) {
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(document);
      StreamResult console = new StreamResult(System.out);
      transformer.transform(source, console);
    } catch (TransformerException e) {
      e.printStackTrace();
    }

  }
}

