package org.example.requests.sub_create_requests;

import org.example.RequestException;
import org.example.ServiceNew;
import org.example.results.subResults.Created;
import org.example.results.subResults.ErrorResult;
import org.example.results.subResults.SubResult;

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

  @Override
  public SubResult execute() {

    try {
      ServiceNew.createAccount(this);
      SubResult created = new Created();
      created.addAttribute("id", this.getId());
      return created;

    } catch (RequestException e) {
      SubResult error = new ErrorResult(e.getMessage());
      error.addAttribute("id", this.getId());
//      System.out.println(error);
      return error;
    }
  }

  @Override
  public String toString() {
    return "CreateAccount{" +
           "id='" + id + '\'' +
           ", balance=" + balance +
           '}';
  }
}
