package org.example.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private String id;

    private double amount;
    private double limitPrice;
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    private Account account;

    public enum Status {
        OPEN, EXECUTED, CANCELLED
    }

    public Order(double amount, double limitPrice, String symbol, Account account) {
        this.amount = amount;
        this.limitPrice = limitPrice;
        this.symbol = symbol;
        this.account = account;
        this.status = Status.OPEN;
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

    public String toString() {
        return "Order [id=" + id + ", amount=" + amount + ", limitPrice=" + limitPrice + ", symbol=" + symbol + ", status=" + status + ", account=" + account + "]";
    }

    

}
