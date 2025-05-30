package org.example.Frame;

public class Product{
    private final int id;
    private final String name;
    private final int price;
    int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    private int amount;
    public void addNum(int num){
        if (num < 0) throw new IllegalArgumentException("數量不可為負");
        this.amount += num;
    }
    public void substractNum(int num){
        if (num < 0) throw new IllegalArgumentException("數量不可為負");
        if (this.amount < num) throw new ProductNotEnoughException("庫存不足");
        this.amount -= num;
    }
    public int getAmount(){
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


    protected String repeat(int count, String with){
        return new String(new char[count]).replace("\0", with);
    }
    public String toString(){
        return "Product(id: " + id +
                ", name: " + name +
                ", amount: " + amount +
                ", price: " + price +
                ")";
    }
    public void display(int hierarchy){
        System.out.println(repeat(hierarchy, "  ") + this);
    }
    public void display(){
        display(0);
    }
}

