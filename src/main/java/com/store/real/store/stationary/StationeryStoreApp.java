package com.store.real.store.stationary;

import com.store.framework.Store;
import com.store.real.store.StoreGUI;

import javax.swing.SwingUtilities;

public class StationeryStoreApp {
    public static void main(String[] args) {

        Store stationeryStore = new StationeryStore("My Stationery Shop",
                new com.store.framework.MembershipSalesRule(0.05));


        // 在 Swing UI Thread 啟動
        SwingUtilities.invokeLater(() -> {
            StoreGUI gui = new StoreGUI(stationeryStore);
            gui.run();
        });
    }
}
