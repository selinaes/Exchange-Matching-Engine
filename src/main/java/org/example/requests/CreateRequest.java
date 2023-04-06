package org.example.requests;

import org.example.requests.sub_create_requests.CreateAccount;
import org.example.requests.sub_create_requests.CreateSymbol;
import org.example.requests.sub_create_requests.SubCreateRequest;
import org.example.results.Result;
import org.example.results.subResults.SubResult;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;


public class CreateRequest implements Request {
  private List<SubCreateRequest> subCreateRequests;


  public CreateRequest(Element element) {
    this.subCreateRequests = new ArrayList<>();
    NodeList nList = element.getChildNodes();
    for (int i = 0; i < nList.getLength(); i++) {
      if (nList.item(i).getNodeType() == Element.ELEMENT_NODE) {
        Element subElement = (Element) nList.item(i);

        this.subCreateRequests.addAll(subCreateRequestsBuilder(subElement));
      }
    }
  }


  private List<SubCreateRequest> subCreateRequestsBuilder(Element element) {
    List<SubCreateRequest> res = new ArrayList<>();
    String accountId;
    switch (element.getNodeName()) {
      case "account":
        accountId = element.getAttribute("id");
        String balance = element.getAttribute("balance");
        res.add(new CreateAccount(accountId, Double.parseDouble(balance)));
        break;
      case "symbol":
        String symbol = element.getAttribute("sym");
        NodeList symbolChildren = element.getChildNodes();
        for (int j = 0; j < symbolChildren.getLength(); j++) {
          if (symbolChildren.item(j).getNodeType() == Element.ELEMENT_NODE) {
            element = (Element) symbolChildren.item(j);
            accountId = element.getAttribute("id");
            double amount = element.getTextContent().equals("") ? 0 : Double.parseDouble(element.getTextContent());
            res.add(new CreateSymbol(symbol, accountId, amount));
          }
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid create request");
    }
    return res;
  }

  public List<SubCreateRequest> getSubCreateRequests() {
    return subCreateRequests;
  }

  @Override
  public Result execute() {
    Result result = new Result();
    for (SubCreateRequest subCreateRequest : subCreateRequests) {
      SubResult subResult = subCreateRequest.execute();
      result.addSubResult(subResult);
    }
    return result;
  }

  @Override
  public String toString() {
    return "CreateRequest{" +
           "subCreateRequests=" + subCreateRequests +
           '}';
  }
}
