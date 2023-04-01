package org.example.requests.sub_transaction_requests;

public class Order implements SubTransactionRequest{
  private String accountId;
  private String symbol;
  private double amount;
  private double limit;

  public Order(String accountId, String symbol, double amount, double limit) {
    this.accountId = accountId;
    this.symbol = symbol;
    this.amount = amount;
    this.limit = limit;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getLimit() {
    return limit;
  }

  public void setLimit(double limit) {
    this.limit = limit;
  }
}
