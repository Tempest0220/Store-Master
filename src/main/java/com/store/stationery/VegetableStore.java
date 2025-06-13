package com.store.stationery;

import com.store.framework.*;

import java.time.LocalDate;

public class VegetableStore extends Store {
    public VegetableStore(String name, SalesRule rule) {
        super(name, rule);
        initStore();
    }

    @Override
    protected void initStore() {
        ProductCategory leafy = new ProductCategory("葉菜類");
        ProductCategory root = new ProductCategory("根莖類");
        ProductCategory expired = new ProductCategory("過期產品");
        productCategories.add(leafy);
        productCategories.add(root);
        productCategories.add(expired);

        ProductComponent cabbage = new ProductItem("高麗菜", 30, 0);
        ProductComponent spinach = new ProductItem("菠菜", 25, 0);
        ProductComponent carrot = new ProductItem("紅蘿蔔", 20, 0);
        ProductComponent potato = new ProductItem("馬鈴薯", 15, 0);
        ProductComponent expiredCabbage = new ExpiringProductItem("過期高麗菜", 10, 0, LocalDate.now().minusDays(1));

        leafy.add(cabbage);
        leafy.add(spinach);
        root.add(carrot);
        root.add(potato);
        expired.add(expiredCabbage);

        productRegistry.addProduct(cabbage);
        productRegistry.addProduct(spinach);
        productRegistry.addProduct(carrot);
        productRegistry.addProduct(potato);
        productRegistry.addProduct(expiredCabbage);

        RegistrySetProductStock("高麗菜", 50);
        RegistrySetProductStock("菠菜", 40);
        RegistrySetProductStock("紅蘿蔔", 60);
        RegistrySetProductStock("馬鈴薯", 80);
        RegistrySetProductStock("過期高麗菜", 10);
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

        System.out.printf("賣出 %d 個 %s，總價：%.2f%n",
                quantity, productName, total);
    }
}