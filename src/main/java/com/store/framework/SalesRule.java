package com.store.framework;

// Defines a sale strategy (e.g., membership discounts)
public interface SalesRule {
    double applySale(ProductComponent product, int quantity, Customer customer);
}
