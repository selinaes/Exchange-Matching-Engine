package org.example.requests;

import org.example.requests.sub_create_requests.SubCreateRequest;
import org.example.requests.sub_transaction_requests.CancelRequest;
import org.example.requests.sub_transaction_requests.OrderRequest;
import org.example.requests.sub_transaction_requests.QueryRequest;
import org.example.requests.sub_transaction_requests.SubTransactionRequest;

import org.example.results.Result;
import org.example.results.subResults.SubResult;
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
    String orderId;
    switch (element.getNodeName()) {
      case "order":
        String symbol = element.getAttribute("sym");
        double amount = Double.parseDouble(element.getAttribute("amount"));
        double limit = Double.parseDouble(element.getAttribute("limit"));
        res.add(new OrderRequest(accountId, symbol, amount, limit));
        break;
      case "query":
        orderId = element.getAttribute("id");
        res.add(new QueryRequest(accountId, orderId));
        break;
      case "cancel":
        orderId = element.getAttribute("id");
        res.add(new CancelRequest(accountId, orderId));
        break;
      default:
        throw new IllegalArgumentException("Invalid create request");
    }
    return res;
  }

  @Override
  public Result execute() {
    Result result = new Result();
    for (SubTransactionRequest subTransactionRequest : subTransactionRequests) {
      SubResult subResult = subTransactionRequest.execute();
      result.addSubResult(subResult);
    }
    return result;
  }

  @Override
  public String toString() {
    return "TransactionRequest{" +
           "subTransactionRequests=" + subTransactionRequests +
           '}';
  }
}
