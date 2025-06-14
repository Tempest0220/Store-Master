// src/main/java/com/store/stationery/StationeryStore.java
package com.store.real.store.stationary;

import com.store.framework.*;

public class StationeryStore extends Store {
    public StationeryStore(String name, SalesRule rule) {
        super(name, rule);
        initStore();
    }

    @Override
    protected void initStore() {
        ProductCategory pens = new ProductCategory("Pens");
        ProductCategory papers = new ProductCategory("Papers");
        productCategories.add(pens);
        productCategories.add(papers);

        ProductComponent ballpoint = new ProductItem("Ballpoint Pen", 1.5, 0);
        ProductComponent gelPen = new ProductItem("Gel Pen", 2.0, 0);
        ProductComponent notebook = new ProductItem("Notebook", 3.0, 0);
        ProductComponent sketchpad = new ProductItem("Sketchpad", 5.0, 0);

        pens.add(ballpoint);
        pens.add(gelPen);
        papers.add(notebook);
        papers.add(sketchpad);

        productRegistry.addProduct(ballpoint);
        productRegistry.addProduct(gelPen);
        productRegistry.addProduct(notebook);
        productRegistry.addProduct(sketchpad);

        RegistrySetProductStock("Ballpoint Pen", 100);
        RegistrySetProductStock("Gel Pen", 80);
        RegistrySetProductStock("Notebook", 50);
        RegistrySetProductStock("Sketchpad", 30);
    }

    @Override
    public void stockShelf(String productName, int quantity) {
        RegistrySetProductStock(productName, quantity);
    }

    @Override
    public void sell(String productName, int quantity, Customer customer) {
        ProductComponent p = productRegistry.getProduct(productName);
        if (p == null || p.getQuantity() < quantity)
            throw new IllegalArgumentException("庫存不足：" + productName);

        double total = (customer == null)
                ? p.getPrice() * quantity
                : salesRule.applySale(p, quantity, customer);

        p.setQuantity(p.getQuantity() - quantity);

        System.out.printf("Sold %d x %s for total: %.2f%n",
                quantity, productName, total);
    }
}