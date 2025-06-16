package com.store.real.restaurant.tableState;

import java.util.Map;

/** State 介面：定義座位可執行的動作 */
public interface TableState {
    String name();
    void reserve(TableContext ctx);
    void seat(TableContext ctx);
    void cancel(TableContext ctx);
    /** 下單：name→qty */
    void order(TableContext ctx, Map<String,Integer> items);
    /** 結帳並回到空桌，回傳本次金額 */
    double checkout(TableContext ctx);
}
