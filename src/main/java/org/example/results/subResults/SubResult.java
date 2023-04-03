package org.example.results.subResults;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Map;

public interface SubResult {
  Map<String, String> getAttributes();

  void addAttribute(String key, String value);

  Node toXML(Document document);
}
