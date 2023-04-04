package org.example.models;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  private String id;

  //@ManyToOne
  private String parentId;

  private double amount;
  private double limitPrice;
  private String symbol;

  private long time;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @ManyToOne
  private Account account;

  public enum Status {
    OPEN, EXECUTED, CANCELLED
  }

  public Order() {
    this.status = Status.OPEN;
    this.time = Instant.now().getEpochSecond();
  }

  public Order(double amount, double limitPrice, String symbol, Account account) {
    this.amount = amount;
    this.limitPrice = limitPrice;
    this.symbol = symbol;
    this.account = account;
    this.status = Status.OPEN;
    this.time = Instant.now().getEpochSecond();
  }

  public double getAmount() {
    return amount;
  }

  public double getLimitPrice() {
    return limitPrice;
  }

  public String getSymbol() {
    return symbol;
  }

  public Status getStatus() {
    return status;
  }

  public Account getAccount() {
    return account;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setLimitPrice(double limitPrice) {
    this.limitPrice = limitPrice;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public void setTimeToNow() {
    this.time = Instant.now().getEpochSecond();
  }

  public String toString() {
    return "Order [id=" + id + ", amount=" + amount + ", limitPrice=" + limitPrice + ", symbol="
           + symbol + ", status=" + status + ", account=" + account + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Double.compare(order.amount, amount) == 0
           && Double.compare(order.limitPrice, limitPrice) == 0 && time == order.time
           && id.equals(order.id) && parentId.equals(order.parentId) && symbol.equals(order.symbol)
           && status == order.status && account.equals(order.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, parentId, amount, limitPrice, symbol, time, status, account);
  }
}
