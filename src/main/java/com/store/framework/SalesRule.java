package com.store.framework;

import java.util.Map;

/**
 * 計價策略：支援「整張訂單」與「單品」兩種呼叫方式。<br/>
 * 既有程式若仍呼叫舊版 (product, qty) 也能運作。
 */
public interface SalesRule {

    /**
     * 全量計價（推薦新程式都走這個）。
     * @param items    Map<商品, 數量>
     * @param customer 會員，可為 null
     * @return 應付金額
     */
    default double applySale(Map<ProductComponent, Integer> items, Customer customer){
        double sum = 0;
        for(ProductComponent product: items.keySet()){
            sum += applySale(product, items.get(product), customer);
        }
        return sum;
    };

    /**
     * 單品計價──預設轉呼叫全量版本，保持舊程式碼相容。
     */
    double applySale(ProductComponent product, int quantity, Customer customer);
}
