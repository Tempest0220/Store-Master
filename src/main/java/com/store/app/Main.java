package com.store.app;

import com.store.framework.*;
import com.store.stationery.*;

public class Main {
    public static void main(String[] args) {
        SalesRule rule = new MembershipSalesRule(0.05); // 5% 折扣
        StationeryStore store = new StationeryStore("My Stationery Shop", rule);
        Customer alice = new Customer("A1001");

        store.showWarehouse();
        store.showShelf();

        store.sell("Ballpoint Pen", 5, alice);
        store.sell("Notebook", 2, alice);

        store.showShelf();
        System.out.println("Alice points: " + alice.getPoints());
    }
}
