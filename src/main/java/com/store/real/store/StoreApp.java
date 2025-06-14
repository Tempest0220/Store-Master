package com.store.real.store;

import com.store.framework.Store;
import com.store.real.store.stationary.StationeryStore;
import com.store.real.store.vegetable.VegetableStore; // 在此先不關注此類別

import javax.swing.SwingUtilities;

public class StoreApp {
    public static void main(String[] args) {
        // 建立文具店實例
        Store stationeryStore = new StationeryStore("My Stationery Shop",
                new com.store.framework.MembershipSalesRule(0.05));

//        Store vegetableStore = new VegetableStore("My Vegetable Shop",
//                new com.store.framework.MembershipSalesRule(0.10));


        // 在 Swing UI Thread 啟動
        SwingUtilities.invokeLater(() -> {
            StoreGUI gui = new StoreGUI(stationeryStore);
            gui.run();
        });
    }
}
