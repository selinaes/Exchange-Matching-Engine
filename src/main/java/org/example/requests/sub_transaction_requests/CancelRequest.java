package org.example.requests.sub_transaction_requests;

import org.example.RequestException;
import org.example.ServiceNew;
import org.example.results.subResults.Canceled;
import org.example.results.subResults.ErrorResult;
import org.example.results.subResults.SubResult;

import java.util.List;

public class CancelRequest implements SubTransactionRequest {
  private String orderId;
  private String accountId;

  public CancelRequest(String accountId, String orderId) {
    this.accountId = accountId;
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  @Override
  public SubResult execute() {
    try {
      List<SubResult> subResults = ServiceNew.cancelOrder(this);
      SubResult canceledResult = new Canceled(subResults);
      canceledResult.addAttribute("id", this.getOrderId());
      return canceledResult;
    } catch (RequestException e) {
      SubResult error = new ErrorResult(e.getMessage());
      error.addAttribute("id", this.getOrderId());
      return error;
    }
  }

  @Override
  public String toString() {
    return "CancelRequest{" +
           "orderId='" + orderId + '\'' +
           ", accountId='" + accountId + '\'' +
           '}';
  }
}
