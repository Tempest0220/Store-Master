package org.example.Frame;

public interface SalesRule {
    int getTotalPrice(Product[] products, boolean isMember);
    int getSingleProductPrice(Product product, boolean isMember);
}
