package org.example.requests.sub_create_requests;

public class CreateSymbol implements SubCreateRequest {
  private String name;
  private String accountId;
  private double amount;

  public CreateSymbol(String name, String accountId, double amount) {
    this.name = name;
    this.accountId = accountId;
    this.amount = amount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }


}
