package org.example.results.subResults;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Map;

public class Created extends AbstractSubResult {


  @Override
  public Node toXML(Document document) {
    Element element = document.createElement("created");
    for (Map.Entry<String, String> entry : this.getAttributes().entrySet()) {
      element.setAttribute(entry.getKey(), entry.getValue());
    }
    return element;
  }

  @Override
  public String toString() {
    return "Created{" + super.toString() + "}";
  }
}
