package org.example;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.example.requests.CreateRequest;
import org.example.requests.TransactionRequest;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.example.requests.Request;
import org.xml.sax.InputSource;


public class Controller {
  public Element parseXML(String xml) {
    try {
//      File inputFile = new File(filename);
//      System.out.println(inputFile);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
      doc.getDocumentElement().normalize();

      Element request = doc.getDocumentElement();
      String type = request.getNodeName();
//      System.out.println(type);

      Request r = requestBuild(type, request);

      //有一个input
//      ByteArrayInputStream in = new ByteArrayInputStream(inputFile.toString().getBytes());
//      // parse in
//      Request r = requestBuild(type, in);


//      System.out.println(request.getAttribute("account"));


//      NodeList nList = request.getChildNodes();

//      for (int i = 0; i < nList.getLength(); i++) {
//        Node nNode = nList.item(i);
//        System.out.println("\nCurrent Element :" + nNode.getNodeName());
//        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//          Element eElement = (Element) nNode;
//          System.out.println(eElement.getAttribute("account"));
//        }
//      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public Request requestBuild(String type, Element element) {
    switch (type) {
      case "create":
        return new CreateRequest(element);
      case "transaction":
        return new TransactionRequest(element);
      default:
        return null;
    }
  }

  public static void executeRequest(List<Request> request) {
    for (Request r : request) {
      r.execute();
    }
  }

//  public static void main(String[] args) {
//    System.out.println(true);
//    Element root = parseXML("src/main/java/org/example/input.txt");
////    NodeList nList = root.getElementsByTagName("student");
//  }
}

