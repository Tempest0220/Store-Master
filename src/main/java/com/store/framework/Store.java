package com.store.framework;

/** 商店抽象骨架 */
public abstract class Store {
    protected String name;
    protected SalesRule salesRule;

    public Store(String name, SalesRule salesRule) {
        this.name      = name;
        this.salesRule = salesRule;
        /* ★ 刪除：這裡不再呼叫 initStore() ★ */
    }

    /* ---- 共用介面 ---- */
    public String getName() { return name; }

    /** 留下給子類實作的初始化邏輯 */
    protected abstract void initStore();

    public abstract void stockShelf      (String productName, int quantity);
    public abstract void restockWarehouse(String productName, int quantity);
    public abstract void sell            (String productName, int quantity, Customer customer);
    public abstract void showShelf();
    public abstract void showWarehouse();
}
