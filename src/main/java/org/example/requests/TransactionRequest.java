package org.example.requests;

import org.example.requests.sub_create_requests.SubCreateRequest;
import org.example.requests.sub_transaction_requests.Cancel;
import org.example.requests.sub_transaction_requests.Order;
import org.example.requests.sub_transaction_requests.Query;
import org.example.requests.sub_transaction_requests.SubTransactionRequest;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class TransactionRequest implements Request {


  private List<SubTransactionRequest> subTransactionRequests;


  public TransactionRequest(Element element) {
    this.subTransactionRequests = new ArrayList<>();
    String accountId = element.getAttribute("id");
    NodeList nList = element.getChildNodes();
    for (int i = 0; i < nList.getLength(); i++) {
      if (nList.item(i).getNodeType() == Element.ELEMENT_NODE) {
        Element subElement = (Element) nList.item(i);
        this.subTransactionRequests.addAll(subTransactionRequestsBuilder(subElement, accountId));
      }
    }
  }

  public List<SubTransactionRequest> getSubTransactionRequests() {
    return subTransactionRequests;
  }

  private List<SubTransactionRequest> subTransactionRequestsBuilder(Element element, String accountId) {

    List<SubTransactionRequest> res = new ArrayList<>();
    String transactionId;
    switch (element.getNodeName()) {
      case "order":
        String symbol = element.getAttribute("sym");
        double amount = Double.parseDouble(element.getAttribute("amount"));
        double limit = Double.parseDouble(element.getAttribute("limit"));
        res.add(new Order(accountId, symbol, amount, limit));
        break;
      case "query":
        transactionId = element.getAttribute("id");
        res.add(new Query(transactionId));
        break;
      case "cancel":
        transactionId = element.getAttribute("id");
        res.add(new Cancel(transactionId));
        break;
    }
    return res;
  }

  @Override
  public void execute() {

  }
}
