package org.example.requests.sub_transaction_requests;

import org.example.RequestException;
import org.example.Service;
import org.example.results.subResults.ErrorResult;
import org.example.results.subResults.Status;
import org.example.results.subResults.SubResult;

import java.util.List;

public class QueryRequest implements SubTransactionRequest {
  private String transactionId;

  private String accountId;

  public QueryRequest(String accountId, String transactionId) {

    this.accountId = accountId;
    this.transactionId = transactionId;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
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
      List<SubResult> subResults = Service.queryOrder(this);
      SubResult statusResult = new Status(subResults);
      statusResult.addAttribute("id", this.getTransactionId());
      return statusResult;
    } catch (RequestException e) {
      SubResult error = new ErrorResult(e.getMessage());
      error.addAttribute("id", this.getTransactionId());
      return error;
    }
  }
}
