package org.example.models;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

class Position {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
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

    public String getSymbol() {
        return this.symbol;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return "Position: " + this.symbol + " " + this.quantity;
    }
}