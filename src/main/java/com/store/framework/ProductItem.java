package com.store.framework;

// Leaf node for individual products
public class ProductItem implements ProductComponent {
    private String name;
    private double price;
    private int quantity;

    public ProductItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    @Override
    public int getQuantity() { return quantity; }

    @Override
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /* ★ 新增：調整售價時用 */
    public void setPrice(double price) { this.price = price; }
}
