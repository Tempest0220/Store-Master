package com.store.framework;

// Abstract Store skeleton
public abstract class Store {
    protected String name;
    protected SalesRule salesRule;

    public Store(String name, SalesRule salesRule) {
        this.name = name;
        this.salesRule = salesRule;
        initStore();
    }

    public String getName(){
        return name;
    }

    protected abstract void initStore();

    public abstract void stockShelf(String productName, int quantity);
    public abstract void restockWarehouse(String productName, int quantity);
    public abstract void sell(String productName, int quantity, Customer customer);
    public abstract void showShelf();
    public abstract void showWarehouse();
}
