package com.store.framework;

import java.util.*;

// Manages product inventory
public class InventoryManager {
    private Map<String, ProductComponent> products = new HashMap<>();

    public void addProduct(ProductComponent product) {
        products.put(product.getName(), product);
    }

    public ProductComponent getProduct(String name) {
        return products.get(name);
    }

    public void restock(String name, int quantity) {
        ProductComponent p = products.get(name);
        if (p != null) {
            p.setQuantity(p.getQuantity() + quantity);
        }
    }

    public void printInventory() {
        System.out.println("Inventory Status:");
        products.values().forEach(p -> {
            System.out.printf("- %s: qty=%d, price=%.2f\n", p.getName(), p.getQuantity(), p.getPrice());
        });
    }
}
