package org.example.requests.sub_transaction_requests;

import org.example.RequestException;
import org.example.models.Order;
import org.example.results.subResults.ErrorResult;
import org.example.results.subResults.Opened;
import org.example.results.subResults.SubResult;
import org.example.Service;


public class OrderRequest implements SubTransactionRequest{
  private String accountId;
  private String symbol;
  private double amount;
  private double limit;

  public OrderRequest(String accountId, String symbol, double amount, double limit) {
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

  @Override
  public SubResult execute(){
    Order newOrder = null;
    try {
      // create a new order, subtract corresponding shares or cash
      newOrder = Service.createOrder(this);
      // get bestMatch order & execute matching till no other match
      Service.executeMatching(newOrder);
    }
    catch (RequestException e){
      SubResult error = new ErrorResult(e.getMessage());
      error.addAttribute("sym", this.symbol);
      error.addAttribute("amount", String.valueOf(this.amount));
      error.addAttribute("limit", String.valueOf(this.limit));
      return error;
    }

      // return <opened> result
      SubResult result = new Opened();
      result.addAttribute("sym", this.symbol);
      result.addAttribute("amount", String.valueOf(this.amount));
      result.addAttribute("limit", String.valueOf(this.limit));
      result.addAttribute("id", newOrder.getId());
      return result;
  }

  @Override
  public String toString() {
    return "OrderRequest{" +
           "accountId='" + accountId + '\'' +
           ", symbol='" + symbol + '\'' +
           ", amount=" + amount +
           ", limit=" + limit +
           '}';
  }
}
