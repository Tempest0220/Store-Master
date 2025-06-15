package com.store.real.restaurant;

import com.store.framework.*;
import com.store.real.normalStore.NormalSalesGUIStore;

import java.util.HashMap;
import java.util.Map;

/** 餐廳專用 Store：內含菜色分類表，並提供整張訂單結帳 API。 */
public class RestaurantStore extends NormalSalesGUIStore {

    /** productName -> category，供計價規則判斷套餐用 */
    private final Map<String, String> productCategoryMap = new HashMap<>();


    public RestaurantStore(String name) {
        super(name, new RestaurantSalesRule(null));
        initStore();
        ((RestaurantSalesRule)this.salesRule).setStore(this);
    }

    @Override protected void initStore() {
        ProductCategory starters = new ProductCategory("前菜");
        ProductCategory mains    = new ProductCategory("主食");
        ProductCategory desserts = new ProductCategory("甜點");

        productCategories.add(starters);
        productCategories.add(mains);
        productCategories.add(desserts);

        // 建立菜單：品名、售價、初始庫存 0
        addDish("Salad",      120, starters);
        addDish("Soup",       100, starters);
        addDish("Steak",      320, mains);
        addDish("Pasta",      260, mains);
        addDish("Cake",       150, desserts);
        addDish("Ice Cream",  130, desserts);

        // 預存庫存
        RegistrySetProductStock("Salad",     50);
        RegistrySetProductStock("Soup",      50);
        RegistrySetProductStock("Steak",     30);
        RegistrySetProductStock("Pasta",     40);
        RegistrySetProductStock("Cake",      60);
        RegistrySetProductStock("Ice Cream", 60);
    }

    private void addDish(String name, double price, ProductCategory cat){
        ProductComponent item = new ProductItem(name, price, 0);
        cat.add(item);
        productRegistry.addProduct(item);
        productCategoryMap.put(name, cat.getName());
    }

    /** 供 SalesRule 查詢品名對應分類 */
    public String getCategoryOf(String productName){
        return productCategoryMap.get(productName);
    }

    /** 整張訂單結帳。order: 品名 -> 數量 */
    public double settleOrder(Map<String,Integer> order, Customer member, boolean checkout){
        Map<ProductComponent,Integer> map = new HashMap<>();
        for(var e: order.entrySet()){
            ProductComponent p = productRegistry.getProduct(e.getKey());
            if(p==null || p.getQuantity()<e.getValue())
                throw new IllegalArgumentException("庫存不足："+e.getKey());
            map.put(p, e.getValue());
        }
        double total = salesRule.applySale(map, member);
//        System.out.println(map);
        // 扣庫存
        if (checkout)
            map.forEach((p,qty)-> p.setQuantity(p.getQuantity()-qty));
        return total;
    }

    /** 單品販售仍保留舊介面（外部若有人用得到） */
    @Override
    public void sell(String productName, int quantity, Customer customer) {
        Map<String,Integer> tmp = new HashMap<>();
        tmp.put(productName, quantity);
        double t = settleOrder(tmp, customer, true);
        System.out.printf("Sold %d x %s. Total: %.2f%n", quantity, productName, t);
    }

    @Override public void stockShelf(String productName, int qty){
        RegistrySetProductStock(productName, qty);
    }
}
