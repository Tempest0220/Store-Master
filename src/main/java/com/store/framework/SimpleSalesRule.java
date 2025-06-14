package com.store.framework;


public class SimpleSalesRule implements SalesRule {

    @Override
    public double applySale(ProductComponent product, int quantity,
                            Customer customer /* 可能為 null */) {
        return product.getPrice() * quantity;
    }
}
