package org.example.requests.sub_create_requests;

import org.example.RequestException;
import org.example.Service;
import org.example.results.subResults.Created;
import org.example.results.subResults.ErrorResult;
import org.example.results.subResults.SubResult;

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

  @Override
  public SubResult execute() {
    try {
      Service.createSymbol(this);
      SubResult created = new Created();
      created.addAttribute("sym", this.getName());
      created.addAttribute("id", this.getAccountId());
      return created;
    } catch (RequestException e) {
      SubResult error = new ErrorResult(e.getMessage());
      error.addAttribute("sym", this.getName());
      error.addAttribute("id", this.getAccountId());
      return error;
    }
  }

  @Override
  public String toString() {
    return "CreateSymbol{" +
           "name='" + name + '\'' +
           ", accountId='" + accountId + '\'' +
           ", amount=" + amount +
           '}';
  }
}
