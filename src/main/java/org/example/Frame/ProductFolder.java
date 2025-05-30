package org.example.Frame;

import java.util.ArrayList;

public class ProductFolder extends Product{
    private final ArrayList<Product> components;
    ProductFolder(int id, String name) {
        super(id, name, 0);
        components = new ArrayList<>();
    }
    public int getPrice(){
        throw new UnsupportedOperationException("folder don't have price");
    }
    public void addNum(int num){
        throw new UnsupportedOperationException("folder cannot add amount");
    }
    public void substractNum(int num){
        throw new UnsupportedOperationException("folder cannot substract amount");
    }
    public int getAmount(){
        throw new UnsupportedOperationException("folder don't have amount");
    }

    public void add(Product productToAdd){
        components.add(productToAdd);
    }
    boolean hasComposite(String name){
        for (Product p : components){
            if (p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public Product getComposite(String name){
        for (Product p : components){
            if (p.getName().equals(name)){
                return p;
            }
        }
        throw new ProductExistenceException("The product '" + name + "' dots not exists");
    }

    public String toString(){
        return  "ProductFolder(id: " + getId() +
                ", name: " + getName() +
                ")";
    }
    public void display(int hierarchy){
        System.out.println(repeat(hierarchy, "  ") + this);
        for (Product p : components){
            p.display(hierarchy + 1);
        }
    }

    // 使用測試
    public static void main(String[] args){
        ProductFolder root = new ProductFolder(10, "文具店庫存");
        ProductFolder stationary = new ProductFolder(11, "文具類");
        stationary.add(new Product(1000, "鉛筆", 20));
        stationary.add(new Product(1001, "橡皮擦", 10));
        root.add(stationary);
        ProductFolder books = new ProductFolder(11, "書類");
        books.add(new Product(1002, "字典", 100));
        books.add(new Product(1003, "筆記本", 30));
        root.add(books);
        root.display();
    }
}
