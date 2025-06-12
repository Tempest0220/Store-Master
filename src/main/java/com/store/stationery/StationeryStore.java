package com.store.stationery;

import com.store.framework.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 文具店（單一 InventoryManager 版本）
 */
public class StationeryStore extends Store {

    private final InventoryManager inventoryManager = new InventoryManager();
    private final List<ProductCategory> categories = new ArrayList<>();

    public StationeryStore(String name, SalesRule rule) {
        super(name, rule);
        /* ★ 新增：此時欄位已就緒，再呼叫初始化 ★ */
        initStore();
    }

    /* ------------------------------ 初始化 ------------------------------ */
    @Override
    protected void initStore() {

        /* 1. 建立分類節點 */
        ProductCategory pens   = new ProductCategory("Pens");
        ProductCategory papers = new ProductCategory("Papers");
        categories.add(pens);
        categories.add(papers);

        /* 2. 建立商品（初始庫存為 0） */
        ProductComponent ballpoint = new ProductItem("Ballpoint Pen", 1.5, 0);
        ProductComponent gelPen    = new ProductItem("Gel Pen",      2.0, 0);
        ProductComponent notebook  = new ProductItem("Notebook",     3.0, 0);
        ProductComponent sketchpad = new ProductItem("Sketchpad",    5.0, 0);

        /* 3. 放入分類 */
        pens.add(ballpoint);
        pens.add(gelPen);
        papers.add(notebook);
        papers.add(sketchpad);

        /* 4. 註冊到唯一的 InventoryManager */
        inventoryManager.addProduct(ballpoint);
        inventoryManager.addProduct(gelPen);
        inventoryManager.addProduct(notebook);
        inventoryManager.addProduct(sketchpad);

        /* 5. 進貨（一次就足夠，不再分倉庫／貨架） */
        restockWarehouse("Ballpoint Pen", 100);
        restockWarehouse("Gel Pen",        80);
        restockWarehouse("Notebook",       50);
        restockWarehouse("Sketchpad",      30);
    }

    /* ------------------------------ 供 GUI 取用的分類操作 ------------------------------ */
    public List<ProductCategory> getCategories() { return categories; }

    public void addCategory(String name) { categories.add(new ProductCategory(name)); }

    public void moveProductToCategory(String prodName, String catName) {
        ProductComponent target = inventoryManager.getProduct(prodName);
        if (target == null) return;

        categories.forEach(c -> c.remove(target));                 // 先拔除
        categories.stream()                                        // 再加入目標
                .filter(c -> c.getName().equals(catName))
                .findFirst()
                .ifPresent(c -> c.add(target));
    }

    public void deleteProduct(String prodName) {
        categories.forEach(c ->                                   // 從分類樹拿掉
                c.getChildren().removeIf(pc -> pc.getName().equals(prodName)));
        inventoryManager.removeProduct(prodName);                  // 從存貨刪除
    }

    public void removeCategory(String catName) {
        categories.removeIf(c -> c.getName().equals(catName));
    }

    public void moveCategory(String fromCat, String toCat) {
        ProductCategory src = null, dst = null;
        for (ProductCategory c : categories) {
            if (c.getName().equals(fromCat)) src = c;
            if (c.getName().equals(toCat))   dst = c;
        }
        if (src == null || dst == null) return;
        categories.remove(src);
        dst.add(src);
    }

    public void addNewProductType(String name, double price, int initQty) {
        ProductComponent item = new ProductItem(name, price, initQty);
        inventoryManager.addProduct(item);
    }

    /**
     * 代理 InventoryManager 的查詢方法，判斷商品是否存在
     */
    public boolean isProductNameExist(String name) {
        return inventoryManager.isProductNameExist(name);
    }

    /* ------------------------------ Store 規格實作 ------------------------------ */
    @Override
    public void restockWarehouse(String productName, int quantity) {
        inventoryManager.restock(productName, quantity);
    }

    /**
     * 既然只有一份存貨資料，stockShelf 不再需要移貨，
     * 這裡改為簡單增加存貨（或可直接留空做 no-op）。
     */
    @Override
    public void stockShelf(String productName, int quantity) {
        inventoryManager.restock(productName, quantity);
    }

    @Override
    public void sell(String productName, int quantity, Customer customer) {
        ProductComponent p = inventoryManager.getProduct(productName);
        if (p == null || p.getQuantity() < quantity)
            throw new IllegalArgumentException("庫存不足：" + productName);

        double total = (customer == null)
                ? p.getPrice() * quantity
                : salesRule.applySale(p, quantity, customer);

        p.setQuantity(p.getQuantity() - quantity);

        System.out.printf("Sold %d x %s for total: %.2f%n",
                quantity, productName, total);
    }

    @Override
    public void showWarehouse() {
        System.out.println("Inventory for " + name + ":");
        inventoryManager.printInventory();
    }

    /** 保留方法相容性，直接呼叫 showWarehouse() */
    @Override
    public void showShelf() { showWarehouse(); }

}
