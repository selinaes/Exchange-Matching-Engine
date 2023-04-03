package org.example.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


import javax.persistence.Table;

//import org.hibernate.
// import org.example.models.Position;
//import javax.validation.constraints.Length;
//import org.hibernate.validator.constraints.Length;
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "accounts")
public class Account {
  @Id
  @Size(min = 10, max = 10, message = "Id must be 10 characters")
  private String id;

  private double balance;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
  private List<Position> positions;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
  private List<Order> orders;

  public Account() {
    this.positions = new ArrayList<>();
    this.orders = new ArrayList<>();
  }

  public Account(double balance) {
    this.balance = balance;
    this.positions = new ArrayList<>();
    this.orders = new ArrayList<>();
  }

  public Account(double balance, String accountId) {
    this.balance = balance;
    this.id = accountId;
    this.positions = new ArrayList<>();
    this.orders = new ArrayList<>();

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

  public List<Position> getPositions() {
    return positions;
  }

  public void setPositions(List<Position> positions) {
    this.positions = positions;
  }

  public Position getPositionBySym(String symbol) {
    for (Position position : positions) {
      if (position.getSymbol().equals(symbol)) {
        return position;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "Account [balance=" + balance + ", id=" + id + ", positions=" + positions + "]";
  }

}