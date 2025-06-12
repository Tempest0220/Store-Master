package com.store.framework;

// Customer with membership points
public class Customer {
    private String id;
    private int points;

    public Customer(String id) {
        this.id = id;
        this.points = 0;
    }

    public void addPoints(int pts) {
        this.points += pts;
    }

    public int getPoints() {
        return points;
    }
}