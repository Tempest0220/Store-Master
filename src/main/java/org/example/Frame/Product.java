package org.example.Frame;

public class Product{
    private final int id;
    private final String name;
    private final int price;
    int getId(){
        return id;
    }
    String getName(){
        return name;
    }
    int getPrice(){
        return price;
    }
    private int amount;
    void addNum(int num){
        if (num < 0) throw new IllegalArgumentException("數量不可為負");
        this.amount += num;
    }
    void substractNum(int num){
        if (num < 0) throw new IllegalArgumentException("數量不可為負");
        if (this.amount < num) throw new ProductNotEnoughException("庫存不足");
        this.amount -= num;
    }
    int getAmount(){
        return amount;
    }
    Product(int id, String name, int price){
        this.id = id;
        this.name = name;
        this.amount = 0;
        this.price = price;
    }
    // copying constructor
    Product(Product product){
        id = product.id;
        name = product.name;
        amount = product.amount;
        price = product.price;
    }
}

