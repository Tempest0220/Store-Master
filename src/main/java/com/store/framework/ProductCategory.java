package com.store.framework;

import java.util.*;

// Composite category that can hold sub-products or items
public class ProductCategory implements ProductComponent {
    private String name;
    private List<ProductComponent> children = new ArrayList<>();

    public ProductCategory(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return children.stream()
                .mapToDouble(c -> c.getPrice() * c.getQuantity())
                .sum();
    }

    @Override
    public int getQuantity() {
        return children.stream()
                .mapToInt(ProductComponent::getQuantity)
                .sum();
    }

    @Override
    public void setQuantity(int quantity) {
        throw new UnsupportedOperationException("Cannot set quantity on category");
    }

    @Override
    public void add(ProductComponent component) {
        children.add(component);
    }

    @Override
    public void remove(ProductComponent component) {
        children.remove(component);
    }

    @Override
    public List<ProductComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }
}