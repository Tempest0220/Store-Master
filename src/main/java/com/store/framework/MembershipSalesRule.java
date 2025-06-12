package com.store.framework;

// Applies a fixed discount rate and awards membership points
public class MembershipSalesRule implements SalesRule {
    private double discountRate; // e.g., 0.1 for 10% off

    public MembershipSalesRule(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double applySale(ProductComponent product, int quantity, Customer customer) {
        double base = product.getPrice() * quantity;
        double total = base * (1 - discountRate);
        customer.addPoints((int) total);
        return total;
    }
}