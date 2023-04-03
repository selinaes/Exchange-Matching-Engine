package org.example.requests.sub_transaction_requests;

import org.example.results.subResults.SubResult;

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

  @Override
  public SubResult execute() {
    return null;
  }
}
