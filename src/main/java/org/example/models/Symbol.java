package org.example.models;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Symbol symbol1 = (Symbol) o;
    return symbol.equals(symbol1.symbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol);
  }
}
