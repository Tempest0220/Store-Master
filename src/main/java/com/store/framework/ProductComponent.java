package com.store.framework;

import java.util.*;

// Composite Pattern for Products
public interface ProductComponent {
    String getName();
    double getPrice();
    int getQuantity();
    void setQuantity(int quantity);
    default void add(ProductComponent component) {
        throw new UnsupportedOperationException();
    }
    default void remove(ProductComponent component) {
        throw new UnsupportedOperationException();
    }
    default List<ProductComponent> getChildren() {
        return Collections.emptyList();
    }
}
