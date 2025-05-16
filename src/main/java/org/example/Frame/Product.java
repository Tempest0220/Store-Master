package org.example.Frame;

public abstract class Product {
    private String name;
    private double price;
    private int ShelvesAmount; // 商品庫存數量

    public Product(String name, double price, int ShelvesAmount) {
        this.name = name;
        this.price = price;
        this.ShelvesAmount = ShelvesAmount;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getShelvesAmount() { return ShelvesAmount; }

   
}

