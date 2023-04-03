package org.example.results.subResults;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Map;

public class ErrorResult extends AbstractSubResult {
  private final String message;

  public ErrorResult(String message) {
    super();
    this.message = message;
  }

  @Override
  public Node toXML(Document document) {
    Element element = document.createElement("error");
    for (Map.Entry<String, String> entry : this.getAttributes().entrySet()) {
//      System.out.println(entry.getKey());
//      System.out.println(entry.getValue());
      element.setAttribute(entry.getKey(), entry.getValue());
    }
    element.appendChild(document.createTextNode(this.message));
//    System.out.println("in error result: " + document.createTextNode(this.message));
//    System.out.println(element);
    return element;
  }

  @Override
  public String toString() {
    return "ErrorRequest{" + super.toString() + ",message=" + this.message + "}";
  }
}
