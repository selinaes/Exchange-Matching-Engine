package org.example.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
//  @Size(min = 10, max = 10, message = "Id must be 10 characters")
  private String id;

  private double balance;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
  private List<Position> positions;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
  private List<Order> orders;


  @Version
  private Long version;


  public Account() {
    this.positions = new ArrayList<>();
    this.orders = new ArrayList<>();
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Double.compare(account.balance, balance) == 0
           && Objects.equals(id, account.id)
           && Objects.equals(positions, account.positions)
           && Objects.equals(orders, account.orders)
           && Objects.equals(version, account.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, balance, positions, orders, version);
  }
}