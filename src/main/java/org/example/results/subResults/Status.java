package org.example.results.subResults;

import org.example.results.subResults.AbstractSubResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Status extends AbstractSubResult {


  private final List<SubResult> subResults;

  public Status() {
    super();
    this.subResults = new ArrayList<>();
  }

  public Status(List<SubResult> subResults) {
    super();
    this.subResults = subResults;
  }

  @Override
  public String toString() {
    return "Status{" + super.toString() + ",subResults=" + this.subResults + "}";
  }

  public void addSubResult(SubResult subResult) {
    this.subResults.add(subResult);
  }

  public Element toXML(Document document) {

    Element root = document.createElement("status");
//    document.appendChild(root);
    for (SubResult subResult : subResults) {
      root.appendChild(subResult.toXML(document));
    }

    return root;
  }
}
