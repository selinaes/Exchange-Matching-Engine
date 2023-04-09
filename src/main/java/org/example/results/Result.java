package org.example.results;

import org.example.results.subResults.SubResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Result {
  List<SubResult> subResults;

//  addSubResult

  public Result() {
    this.subResults = new ArrayList<>();
  }

  public void addSubResult(SubResult subResult) {
    this.subResults.add(subResult);
  }

  public Document toXML() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.newDocument();
      Element root = document.createElement("results");
      document.appendChild(root);

      for (SubResult subResult : subResults) {
        System.out.println(subResult);
        root.appendChild(subResult.toXML(document));
      }

      document.getDocumentElement().normalize();

      return document;
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }


  @Override
  public String toString() {
    return "Result{" +
           "subResults=" + subResults +
           '}';
  }

  public String toXMLString() {

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer;
    try {

      transformer = tf.newTransformer();

      // Uncomment if you do not require XML declaration
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//      OutputKeys.In
//      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      //A character stream that collects its output in a string buffer,
      //which can then be used to construct a string.
      StringWriter writer = new StringWriter();

      //transform document to string
      transformer.transform(new DOMSource(this.toXML()), new StreamResult(writer));
//      transformer.transform(new DOMSource(this.toXML()), new StreamResult(System.out));
//      System.out.println("ttttttttttttt");
//      System.out.println(this.toXML());
//      System.out.println(writer.getBuffer().toString());
      String xmlString = writer.getBuffer().toString();
//      System.out.println(xmlString);
      return xmlString;
    } catch (TransformerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }


}
