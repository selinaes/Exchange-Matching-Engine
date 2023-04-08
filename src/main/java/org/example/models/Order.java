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
  private double executedPrice;
  private String symbol;

  private long time;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status;

  @ManyToOne(cascade = {CascadeType.ALL})
  private Account account;

  public enum Status {
    OPEN, EXECUTED, CANCELLED
  }

  @Version
  private Long version;

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  public void setExecutedPrice(double executedPrice) {
    this.executedPrice = executedPrice;
  }

  public double getExecutedPrice() {
    return executedPrice;
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
           && Double.compare(order.limitPrice, limitPrice) == 0
           && Double.compare(order.executedPrice, executedPrice) == 0 && time == order.time
           && Objects.equals(id, order.id)
           && Objects.equals(parentId, order.parentId)
           && Objects.equals(symbol, order.symbol) && status == order.status
           && Objects.equals(account, order.account)
           && Objects.equals(version, order.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, parentId, amount, limitPrice, executedPrice, symbol, time, status, account, version);
  }
}
