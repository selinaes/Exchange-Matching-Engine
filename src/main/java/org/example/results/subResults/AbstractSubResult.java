package org.example.results.subResults;

//import org.w3c.dom.Node;

import org.example.results.subResults.SubResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class AbstractSubResult implements SubResult {
  private final Map<String, String> attributes;

  public AbstractSubResult() {
    this.attributes = new HashMap<>();
  }

  public AbstractSubResult(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  @Override
  public Map<String, String> getAttributes() {
    return this.attributes;
  }

  @Override
  public void addAttribute(String key, String value) {
    this.attributes.put(key, value);
  }


  @Override
  public Node toXML(Document document) {
    return null;
  }

  @Override
  public String toString() {
    return "{" +
           "attributes=" + attributes +
           '}';
  }
}
