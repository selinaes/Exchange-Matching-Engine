package org.example.requests.sub_transaction_requests;

public class Query implements SubTransactionRequest{
  private String transactionId;

  public Query(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
}
