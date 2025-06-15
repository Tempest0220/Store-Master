package com.store.real.normalStore.vegetable;

import com.store.framework.*;
import com.store.real.normalStore.NormalSalesGUIStore;

import java.time.LocalDate;

public class VegetableStore extends NormalSalesGUIStore {
    public VegetableStore(String name) {
        super(name, new MembershipSalesRule(0.1));
        initStore();
    }

    @Override
    protected void initStore() {
        ProductCategory leafy = new ProductCategory("葉菜類");
        ProductCategory root = new ProductCategory("根莖類");
        ProductCategory expired = new ProductCategory("即期產品");
        productCategories.add(leafy);
        productCategories.add(root);
        productCategories.add(expired);

        ProductComponent cabbage = new ProductItem("高麗菜", 30, 0);
        ProductComponent spinach = new ProductItem("菠菜", 25, 0);
        ProductComponent carrot = new ProductItem("紅蘿蔔", 20, 0);
        ProductComponent potato = new ProductItem("馬鈴薯", 15, 0, new RateDiscount(0.9));

        ProductComponent expiredCabbage = new ExpiringProductItem("即期高麗菜", 10, 0, LocalDate.now().minusDays(1), new RateDiscount(0.5));
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
        RegistrySetProductStock("即期高麗菜", 10);
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