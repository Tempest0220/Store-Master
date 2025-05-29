package org.example.Frame;

import java.util.HashMap;

// Singleton
public class ProductManager {
    private ProductManager(){}
    static ProductManager instance;
    public static ProductManager getInstance(){
        if (instance == null)
            instance = new ProductManager();
        return instance;
    }

    HashMap<Integer, Product> products = new HashMap<>();
    int serial = 1000;
    Product fromId(int id){
        return new Product(products.get(id));
    }
    Product addProduct(String name, int price){
        Product productToAdd = new Product(serial, name, price);
        products.put(serial, productToAdd);
        serial++;
        return productToAdd;
    }

    // 使用方法
    public static void main(String[] args){
        ProductManager manager = ProductManager.getInstance(); // 取得singleton實例
        Product pencil = manager.addProduct("pencil", 10); // 新增一項貨品
        manager.addProduct("eraser", 20).addNum(30); // 新增另一項貨品，順便設定數量
        pencil.addNum(20); // 假設進貨20支鉛筆
        pencil.substractNum(5); // 假設有人買5枝鉛筆
        System.out.println("鉛筆剩餘數量:" + pencil.getAmount()); // product.getAmount顯示此商品的數量

        int inputId = 1001; // 假設結帳時，店員輸入ID(假設可能是刷條碼)
        Product gottenProduct = manager.fromId(inputId); // 取得此ID的產品
        System.out.println("id " + inputId + " 的產品名稱是 " + gottenProduct.getName() + "，賣" + gottenProduct.getPrice() + "塊錢");
    }
}