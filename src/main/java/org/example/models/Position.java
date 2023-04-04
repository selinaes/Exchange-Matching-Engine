package org.example.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

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
  @NotNull
  private String symbol;
  private double quantity;

  @ManyToOne
  @NotNull
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return Double.compare(position.quantity, quantity) == 0 && id.equals(position.id)
           && symbol.equals(position.symbol) && account.equals(position.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, symbol, quantity, account);
  }
}