package org.example.requests.sub_transaction_requests;

public class Cancel implements SubTransactionRequest{
  private String transactionId;

  public Cancel(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
}
