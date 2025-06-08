package org.example.Frame;

public class SaleManager {
    ProductManager productManager;
    SalesRule salesRule;
    public SaleManager(StoreFactory factory){
        productManager = factory.getProductManager();
        salesRule = factory.getSalesRule();
    }
}
