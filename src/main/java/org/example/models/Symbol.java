package org.example.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "symbols")
public class Symbol {

  @Id
  private String symbol;

  public Symbol() {
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String sym) {
    this.symbol = sym;
  }
}
