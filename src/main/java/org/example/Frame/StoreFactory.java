package org.example.Frame;

public interface StoreFactory {
    ProductManager getProductManager();
    SalesRule getSalesRule();
    MoneyManager getMoneyManager();
}
