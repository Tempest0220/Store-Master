// src/main/java/com/store/framework/Store.java
package com.store.framework;

import java.util.ArrayList;
import java.util.List;

public abstract class Store {
    protected String name;
    protected SalesRule salesRule;
    protected final List<ProductCategory> productCategories = new ArrayList<>();
    protected final InventoryManager productRegistry = new InventoryManager();

    public Store(String name, SalesRule salesRule) {
        this.name = name;
        this.salesRule = salesRule;
    }

    /** 子類只需實作初始化邏輯 */
    protected abstract void initStore();

    public void receive(String name, int qty) {
        ProductComponent product = productRegistry.getProduct(name);
        if (product != null) {
            product.setQuantity(product.getQuantity() + qty);
        } else {
            throw new IllegalArgumentException("商品不存在: " + name);
        }
    }

    // ---- category 增刪改查 ----
    protected void _CategoryAddProduct(String prodName, String catName) {
        ProductComponent target = productRegistry.getProduct(prodName);
        if (target == null) return;
        _CategoryRemoveItemRecursive(target, productCategories);
        ProductCategory dest = _findCategory(catName);
        if (dest != null && !dest.getChildren().contains(target)) dest.add(target);
    }

    protected void _CategoryAddNewCategory(String name) {
        productCategories.add(new ProductCategory(name));
    }

    protected void _CategoryRemoveCategoryAndProducts(String catName) {
        ProductCategory target = _findCategory(catName);
        if (target == null) return;
        ProductCategory parent = _findParent(target);
        if (parent == null) productCategories.remove(target);
        else parent.remove(target);
    }

    protected void _CategoryMoveCategoryAndProducts(String fromCat, String toCat) {
        ProductCategory src = _findCategory(fromCat);
        if (src == null) return;
        ProductCategory parent = _findParent(src);
        if (parent == null) productCategories.remove(src);
        else parent.remove(src);
        if (toCat == null || toCat.isBlank()) {
            productCategories.add(src);
        } else {
            ProductCategory dst = _findCategory(toCat);
            if (dst != null && !_isCategoryDescendant(src, dst)) dst.add(src);
            else productCategories.add(src);
        }
    }

    // ---- registry 增刪改查 ----
    protected void _RegistryAddNewProduct(String name, double price, int initQty) {
        ProductComponent item = new ProductItem(name, price, initQty);
        productRegistry.addProduct(item);
    }

    protected void _RegistryDeleteProduct(String prodName) {
        ProductComponent p = productRegistry.getProduct(prodName);
        if (p == null) return;
        _CategoryRemoveItemRecursive(p, productCategories);
        productRegistry.removeProduct(prodName);
    }

    protected boolean _RegistryIsProductNameExist(String name) {
        return productRegistry.isProductNameExist(name);
    }

    protected void _RegistrySetProductStock(String productName, int quantity) {
        productRegistry.restock(productName, quantity);
    }

    protected void _RegistryShowAllProducts() {
        System.out.println("Inventory for " + name + ":");
        productRegistry.printInventory();
    }

    // ---- 工具函數 ----
    protected boolean _CategoryRemoveItemRecursive(ProductComponent p, List<ProductCategory> pool) {
        for (ProductCategory c : pool) {
            if (c.getChildren().contains(p)) {
                c.remove(p);
                return true;
            }
            if (_CategoryRemoveItemRecursive(p, _extractSubs(c))) return true;
        }
        return false;
    }

    protected void _collectCategories(ProductCategory cat, List<ProductCategory> out) {
        out.add(cat);
        for (ProductComponent ch : cat.getChildren())
            if (ch instanceof ProductCategory sub) _collectCategories(sub, out);
    }

    protected ProductCategory _findCategory(String name) {
        for (ProductCategory c : productCategories) {
            ProductCategory r = _findCategoryRec(c, name);
            if (r != null) return r;
        }
        return null;
    }

    protected ProductCategory _findCategoryRec(ProductCategory root, String name) {
        if (root.getName().equals(name)) return root;
        for (ProductComponent ch : root.getChildren())
            if (ch instanceof ProductCategory sub) {
                ProductCategory r = _findCategoryRec(sub, name);
                if (r != null) return r;
            }
        return null;
    }

    protected ProductCategory _findParent(ProductCategory target) {
        return _findParentRec(null, productCategories, target);
    }

    protected ProductCategory _findParentRec(ProductCategory parent, List<ProductCategory> pool, ProductCategory target) {
        for (ProductCategory c : pool) {
            if (c == target) return parent;
            ProductCategory r = _findParentRec(c, _extractSubs(c), target);
            if (r != null) return r;
        }
        return null;
    }

    protected List<ProductCategory> _extractSubs(ProductCategory cat) {
        List<ProductCategory> subs = new ArrayList<>();
        for (ProductComponent ch : cat.getChildren())
            if (ch instanceof ProductCategory sub) subs.add(sub);
        return subs;
    }

    protected boolean _isCategoryDescendant(ProductCategory ancestor, ProductCategory descendant) {
        if (ancestor == descendant) return true;
        for (ProductComponent ch : ancestor.getChildren())
            if (ch instanceof ProductCategory sub &&
                    _isCategoryDescendant(sub, descendant)) return true;
        return false;
    }

    // ---- public 介面（給 GUI 用）----
    public List<ProductCategory> CategoryGetCategories() {
        return productCategories;
    }

    public void CategoryAddProduct(String prodName, String catName) {
        _CategoryAddProduct(prodName, catName);
    }

    public void CategoryAddNewCategory(String name) {
        _CategoryAddNewCategory(name);
    }

    public void CategoryRemoveCategoryAndProduct(String catName) {
        _CategoryRemoveCategoryAndProducts(catName);
    }

    public void CategoryMoveCategoryAndProducts(String fromCat, String toCat) {
        _CategoryMoveCategoryAndProducts(fromCat, toCat);
    }

    public void RegistryAddNewProduct(String name, double price, int initQty) {
        _RegistryAddNewProduct(name, price, initQty);
    }

    public void RegistryDeleteProduct(String prodName) {
        _RegistryDeleteProduct(prodName);
    }

    public boolean RegistryIsProductNameExist(String name) {
        return _RegistryIsProductNameExist(name);
    }

    public void RegistrySetProductStock(String productName, int quantity) {
        _RegistrySetProductStock(productName, quantity);
    }

    public void RegistryShowAllProducts() {
        _RegistryShowAllProducts();
    }

    public void showShelf() {
        RegistryShowAllProducts();
    }

    public List<ProductCategory> CategoryGetAllCategories() {
        List<ProductCategory> list = new ArrayList<>();
        for (ProductCategory c : productCategories) _collectCategories(c, list);
        return list;
    }

    public boolean isCategoryDescendant(String ancName, String descName) {
        ProductCategory a = _findCategory(ancName);
        ProductCategory d = _findCategory(descName);
        return (a != null && d != null) && _isCategoryDescendant(a, d);
    }

    // 需由子類實作
    public abstract void stockShelf(String productName, int quantity);
    public abstract void sell(String productName, int quantity, Customer customer);

    public String getName() {
        return name;
    }
}