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

        // 先從舊分類遞迴移除
        removeProductFromTree(target, categories);

        ProductCategory dest = findCategory(catName);
        if (dest != null && !dest.getChildren().contains(target)) dest.add(target);
    }
    private boolean removeProductFromTree(ProductComponent p, List<ProductCategory> pool) {
        for (ProductCategory c : pool) {
            /* 若目前分類直接包含此商品 → 用 c.remove() 來刪除即可  */
            if (c.getChildren().contains(p)) {
                c.remove(p);          // ProductCategory 自帶的 remove()，操作的是可修改的內部 List
                return true;
            }
            /* 否則深入子分類繼續找 */
            if (removeProductFromTree(p, extractSubs(c))) return true;
        }
        return false;
    }

    public void deleteProduct(String prodName) {
        ProductComponent p = inventoryManager.getProduct(prodName);
        if (p == null) return;
        removeProductFromTree(p, categories);
        inventoryManager.removeProduct(prodName);
    }

    public void removeCategory(String catName) {
        ProductCategory target = findCategory(catName);
        if (target == null) return;
        ProductCategory parent = findParent(target);
        if (parent == null) categories.remove(target);
        else                parent.remove(target);
    }

    public void moveCategory(String fromCat, String toCat) {
        ProductCategory src = findCategory(fromCat);
        if (src == null) return;

        // 先自父節點拔除
        ProductCategory parent = findParent(src);
        if (parent == null)  categories.remove(src);   // 原本就在根
        else                 parent.remove(src);

        // 若 toCat 為 null 代表移到根，否則掛到指定分類
        if (toCat == null || toCat.isBlank()) {
            categories.add(src);
        } else {
            ProductCategory dst = findCategory(toCat);
            if (dst != null && !isDescendant(src, dst)) dst.add(src); // 避免形成循環
            else categories.add(src);                                 // 找不到目標就放回根
        }
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

    /** 回傳包含所有（含巢狀）分類的清單 */
    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> list = new ArrayList<>();
        for (ProductCategory c : categories) collectCategories(c, list);
        return list;
    }
    private void collectCategories(ProductCategory cat, List<ProductCategory> out) {
        out.add(cat);
        for (ProductComponent ch : cat.getChildren())
            if (ch instanceof ProductCategory sub) collectCategories(sub, out);
    }

    /** 依名稱尋找分類（巢狀也找得到） */
    private ProductCategory findCategory(String name) {
        for (ProductCategory c : categories) {
            ProductCategory r = findCategoryRec(c, name);
            if (r != null) return r;
        }
        return null;
    }
    private ProductCategory findCategoryRec(ProductCategory root, String name) {
        if (root.getName().equals(name)) return root;
        for (ProductComponent ch : root.getChildren())
            if (ch instanceof ProductCategory sub) {
                ProductCategory r = findCategoryRec(sub, name);
                if (r != null) return r;
            }
        return null;
    }

    /** 找到目標分類的父節點；若為根節點則回傳 null */
    private ProductCategory findParent(ProductCategory target) {
        return findParentRec(null, categories, target);
    }
    private ProductCategory findParentRec(ProductCategory parent,
                                          List<ProductCategory> pool,
                                          ProductCategory target) {
        for (ProductCategory c : pool) {
            if (c == target) return parent;
            ProductCategory r = findParentRec(c, extractSubs(c), target);
            if (r != null) return r;
        }
        return null;
    }
    private List<ProductCategory> extractSubs(ProductCategory cat) {
        List<ProductCategory> subs = new ArrayList<>();
        for (ProductComponent ch : cat.getChildren())
            if (ch instanceof ProductCategory sub) subs.add(sub);
        return subs;
    }

    /** 判斷 descendant 是否為 ancestor 的子孫 */
    private boolean isDescendant(ProductCategory ancestor, ProductCategory descendant) {
        if (ancestor == descendant) return true;
        for (ProductComponent ch : ancestor.getChildren())
            if (ch instanceof ProductCategory sub &&
                    isDescendant(sub, descendant)) return true;
        return false;
    }
    /* 對外給 GUI 用的封裝 */
    public boolean isDescendant(String ancName, String descName) {
        ProductCategory a = findCategory(ancName);
        ProductCategory d = findCategory(descName);
        return (a != null && d != null) && isDescendant(a, d);
    }
}
