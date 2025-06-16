package com.store.real.restaurant.tableState;

import com.store.framework.Customer;
import com.store.real.restaurant.RestaurantStore;
import com.store.real.restaurant.tableState.*;

import java.util.Map;

/** 桌次物件，運用 State pattern 控制狀態轉移與行為 */
public class TableContext {
    private final int tableNo;
    private final RestaurantStore store;
    private TableState state = new EmptyState();
    private Customer member;  // 若會員入座則保持參考

    public TableContext(int no, RestaurantStore store){ this.tableNo = no; this.store = store; }

    // 對外操作
    public void reserve(){ state.reserve(this); }
    public void seat(Customer mem){ this.member = mem; state.seat(this); }
    public void cancel(){ state.cancel(this); }
    public void order(Map<String,Integer> items){ state.order(this, items); }
    public double checkout(){ return state.checkout(this); }

    // getter, setter 給 State 用
    public void setState(TableState s){ this.state = s; }
    public String getStateName(){ return state.name(); }
    public int getTableNo(){ return tableNo; }
    public RestaurantStore getStore(){ return store; }
    public Customer getMember(){ return member; }
    public void setMember(Customer m){ this.member = m; }
}
