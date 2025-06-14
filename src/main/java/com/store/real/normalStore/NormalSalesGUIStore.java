package com.store.real.normalStore;

import com.store.framework.Customer;
import com.store.framework.ProductComponent;
import com.store.framework.SalesRule;
import com.store.framework.Store;

/**
 * 給GUI使用的委託方法
 */
public abstract class NormalSalesGUIStore extends Store {

    public NormalSalesGUIStore(String name, SalesRule salesRule) {
        super(name, salesRule);
    }
    public ProductComponent getProduct(String key){
        return productRegistry.getProduct(key);
    }
    public double applySale(ProductComponent p, int qty, Customer customer){
        return salesRule.applySale(p, qty, customer);
    }
}
