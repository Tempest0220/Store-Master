package com.store.real.restaurant.tableState;

import com.store.real.restaurant.TableContext;

import java.util.Map;

public class EmptyState implements TableState {
    @Override public String name(){return "空桌";}
    @Override public void reserve(TableContext ctx){ ctx.setState(new ReservedState()); }
    @Override public void seat(TableContext ctx){ ctx.setState(new OccupiedState()); }
    @Override public void cancel(TableContext ctx){ /* nothing */ }
    @Override public void order(TableContext ctx, Map<String,Integer> items){ /* not allowed */ }
    @Override public double checkout(TableContext ctx){ return 0; }
}
