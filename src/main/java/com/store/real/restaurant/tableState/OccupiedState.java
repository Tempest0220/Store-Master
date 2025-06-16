package com.store.real.restaurant.tableState;


import java.util.HashMap;
import java.util.Map;

public class OccupiedState implements TableState {
    private Map<String,Integer> orderMap = new HashMap<>();
    @Override public String name(){return "用餐中";}
    @Override public void reserve(TableContext ctx){ throw new UnsupportedOperationException(); }
    @Override public void seat(TableContext ctx){ /* already */ }
    @Override public void cancel(TableContext ctx){ throw new UnsupportedOperationException(); }

    @Override
    public void order(TableContext ctx, Map<String,Integer> newCart){
        orderMap = newCart;
    }

    @Override
    public double checkout(TableContext ctx){
        double bill = ctx.getStore().settleOrder(orderMap, ctx.getMember(), true);
        orderMap.clear();
        ctx.setState(new EmptyState());
        ctx.setMember(null);
        return bill;
    }
}
