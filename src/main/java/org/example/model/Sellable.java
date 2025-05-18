package org.example.model;
// Component: SellableComponent
// Leaf: Sellable
// Composite: SellableGroup
public abstract class Sellable implements SellableComponent {
    // 可以賣的東西有的屬性
    private String id;          // 識別名
    private String displayName; // 顯示名
    private double price;       // 價格
    private String description; // 基本描述

    // Constructor
    public Sellable(String id, String displayName, double price, String description) {
        this.id = id;
        this.displayName = displayName;
        this.price = price;
        this.description = description;
    }
    public Sellable(String id, double price) {
        this(id, id, price, "");
    }


    @Override
    public double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getDescription() {
        return description;
    }
}
