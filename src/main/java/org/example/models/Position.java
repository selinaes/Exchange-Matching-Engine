package org.example.models;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "positions")
public class Position {

  @Id
//  @GeneratedValue(generator = "increment")
//  @GenericGenerator(name = "increment", strategy = "increment")
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  private String id;

  @Column(nullable = false)
  private String symbol;
  private double quantity;

  @ManyToOne
  private Account account;

  public Position() {
  }

  public Position(String symbol, double quantity) {
    this.symbol = symbol;
    this.quantity = quantity;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public String toString() {
    return "Position: " + this.symbol + " " + this.quantity;
  }
}