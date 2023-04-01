package org.example.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Table;
// import org.example.models.Position;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
//    @GeneratedValue(generator = "increment")
//    @GenericGenerator(name = "increment", strategy = "increment")
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

    @Override
    public String toString() {
        return "Account [balance=" + balance + ", id=" + id + ", positions=" + positions + "]";
    }

}