package org.example.results.subResults;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Canceled extends AbstractSubResult {
  private final List<SubResult> subResults;

  public Canceled() {
    super();
    this.subResults = new ArrayList<>();
  }

  public Canceled(List<SubResult> subResults) {
    super();
    this.subResults = subResults;
  }

  public void addSubResult(SubResult subResult) {
    this.subResults.add(subResult);
  }

  @Override
  public Node toXML(Document document) {
    Element element = document.createElement("canceled");
    for (Map.Entry<String, String> entry : this.getAttributes().entrySet()) {
      element.setAttribute(entry.getKey(), entry.getValue());
    }
    return element;
  }

  @Override
  public String toString() {
    return "Canceled{" +
           "subResults=" + subResults + ", " + super.toString() +
           '}';
  }
}
