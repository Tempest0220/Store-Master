package com.store.stationery;
import com.store.framework.*;

import com.store.framework.*;

import java.util.ArrayList;
import java.util.List;

public class StationeryStore extends Store {
    // 文具店專屬的兩個管理器
    private InventoryManager warehouseManager;
    private InventoryManager shelfManager;
    /* ★ 新增：根級分類清單（最外層） */
    private List<ProductCategory> categories = new ArrayList<>();

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

    /* ★ 新增：對外取用分類 */
    public List<ProductCategory> getCategories() { return categories; }

    /* ★ 新增：新增分類 */
    public void addCategory(String name) { categories.add(new ProductCategory(name)); }

    /* ★ 新增：將商品移到指定分類 */
    public void moveProductToCategory(String prodName, String catName) {
        ProductComponent target = warehouseManager.getProduct(prodName);
        if (target == null) return;

        // 從現有分類拿掉
        categories.forEach(c -> c.remove(target));
        // 加到新分類
        categories.stream()
                .filter(c -> c.getName().equals(catName))
                .findFirst()
                .ifPresent(c -> c.add(target));
    }

    /* ★ 新增：刪除商品（分類 + 兩管理器同步） */
    public void deleteProduct(String prodName) {
        categories.forEach(c -> c.getChildren()
                .stream()
                .filter(pc -> pc.getName().equals(prodName))
                .findFirst()
                .ifPresent(c::remove));
        warehouseManager.removeProduct(prodName);
        shelfManager.removeProduct(prodName);
    }

    public void addNewProductType(String name, double price, int initQty) {
        ProductComponent item = new ProductItem(name, price, initQty);
        warehouseManager.addProduct(item);
        shelfManager.addProduct(item);           // 同一實體→兩邊同步
    }

    public void removeCategory(String catName) {
        // 刪掉根級分類
        categories.removeIf(c -> c.getName().equals(catName));
        // 同時也可以額外做：把該分類下所有商品從兩個管理器移除（視需求而定）
    }

    public void moveCategory(String fromCat, String toCat) {
        ProductCategory source = null, target = null;
        // 在根級找
        for (ProductCategory c : categories) {
            if (c.getName().equals(fromCat)) source = c;
            if (c.getName().equals(toCat))   target = c;
        }
        if (source == null || target == null) return;
        // 從根級移除 source
        categories.remove(source);
        // 加到目標分類的子節點裡
        target.add(source);
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
