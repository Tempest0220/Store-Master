package com.store.framework;

import java.time.LocalDate;

public class ExpiringProductItem extends ProductItem {
    private LocalDate expiryDate;

    public ExpiringProductItem(String name, double price, int quantity, LocalDate expiryDate) {
        super(name, price, quantity);
        this.expiryDate = expiryDate;
    }

    public ExpiringProductItem(String name, double price, int quantity, LocalDate expiryDate, Discount discount) {
        super(name, price, quantity, discount);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    @Override
    public String toString() {
        return getName() + " (qty=" + getQuantity() + ", price=" + getPrice() +
                ", expiry=" + expiryDate + ")";
    }
}