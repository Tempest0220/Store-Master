package com.store.stationery;
import com.store.framework.*;

import com.store.framework.*;

public class StationeryStore extends Store {
    // 文具店專屬的兩個管理器
    private InventoryManager warehouseManager;
    private InventoryManager shelfManager;

    public StationeryStore(String name, SalesRule rule) {
        super(name, rule);
    }

    @Override
    protected void initStore() {
        // 先初始化兩個管理器
        warehouseManager = new InventoryManager();
        shelfManager = new InventoryManager();

        // 建立分類與商品
        ProductCategory pens = new ProductCategory("Pens");
        ProductCategory papers = new ProductCategory("Papers");

        ProductComponent ballpoint = new ProductItem("Ballpoint Pen", 1.5, 0);
        ProductComponent gelPen    = new ProductItem("Gel Pen",      2.0, 0);
        ProductComponent notebook  = new ProductItem("Notebook",     3.0, 0);
        ProductComponent sketchpad = new ProductItem("Sketchpad",    5.0, 0);

        // 分類（可選，用於複雜需求）
        pens.add(ballpoint);
        pens.add(gelPen);
        papers.add(notebook);
        papers.add(sketchpad);

        // 加入管理器
        warehouseManager.addProduct(ballpoint);
        warehouseManager.addProduct(gelPen);
        warehouseManager.addProduct(notebook);
        warehouseManager.addProduct(sketchpad);

        shelfManager.addProduct(ballpoint);
        shelfManager.addProduct(gelPen);
        shelfManager.addProduct(notebook);
        shelfManager.addProduct(sketchpad);

        // 初始進貨與上架
        restockWarehouse("Ballpoint Pen", 100);
        restockWarehouse("Gel Pen",       80);
        restockWarehouse("Notebook",      50);
        restockWarehouse("Sketchpad",     30);

        stockShelf("Ballpoint Pen", 50);
        stockShelf("Gel Pen",       40);
        stockShelf("Notebook",      20);
        stockShelf("Sketchpad",     10);
    }

    public void addNewProductType(String name, double price){
        ProductComponent item = new ProductItem(name, price, 0);
        warehouseManager.addProduct(item);
        shelfManager.addProduct(item);
    }

    @Override
    public void restockWarehouse(String productName, int quantity) {
        warehouseManager.restock(productName, quantity);
    }

    @Override
    public void stockShelf(String productName, int quantity) {
        ProductComponent wh = warehouseManager.getProduct(productName);
        ProductComponent sh = shelfManager.getProduct(productName);
        if (wh == null || wh.getQuantity() < quantity) {
            System.out.println("Warehouse insufficient for " + productName);
        } else {
            wh.setQuantity(wh.getQuantity() - quantity);
            sh.setQuantity(sh.getQuantity() + quantity);
        }
    }

    @Override
    public void sell(String productName, int quantity, Customer customer) {
        ProductComponent p = shelfManager.getProduct(productName);
        // 1) 庫存不足 → 拋例外
        if (p == null || p.getQuantity() < quantity) {
            throw new IllegalArgumentException("庫存不足：" + productName);
        }
        // 2) 計算價格：有會員打折且累點，無會員原價
        double total;
        if (customer == null) {
            total = p.getPrice() * quantity;
        } else {
            total = salesRule.applySale(p, quantity, customer);
        }
        // 3) 扣貨
        p.setQuantity(p.getQuantity() - quantity);
        System.out.printf("Sold %d x %s for total: %.2f\n", quantity, productName, total);
    }

    @Override
    public void showWarehouse() {
        System.out.println("Warehouse Inventory for " + name + ":");
        warehouseManager.printInventory();
    }

    @Override
    public void showShelf() {
        System.out.println("Shelf Inventory for " + name + ":");
        shelfManager.printInventory();
    }
}
