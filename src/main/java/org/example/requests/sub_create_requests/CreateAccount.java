package org.example.requests.sub_create_requests;

public class CreateAccount implements SubCreateRequest {
  private String id;
  private double balance;

  public CreateAccount(String id, double balance) {
    this.id = id;
    this.balance = balance;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }
}
