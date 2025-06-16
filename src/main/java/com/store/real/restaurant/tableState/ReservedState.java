package com.store.real.restaurant.tableState;


import java.util.Map;

public class ReservedState implements TableState {
    @Override public String name(){return "預約中";}
    @Override public void reserve(TableContext ctx){ /* already */ }
    @Override public void seat(TableContext ctx){ ctx.setState(new OccupiedState()); }
    @Override public void cancel(TableContext ctx){ ctx.setState(new EmptyState()); }
    @Override public void order(TableContext ctx, Map<String,Integer> items){ throw new UnsupportedOperationException(); }
    @Override public double checkout(TableContext ctx){ return 0; }
}
