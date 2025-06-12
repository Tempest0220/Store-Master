package com.store.app;

import com.store.stationery.StationeryStore;

import javax.swing.SwingUtilities;

public class StationeryStoreApp {
    public static void main(String[] args) {
        // 建立文具店實例
        StationeryStore store = new StationeryStore("My Stationery Shop",
                new com.store.framework.MembershipSalesRule(0.05));
        // 在 Swing UI Thread 啟動
        SwingUtilities.invokeLater(() -> {
            StationeryStoreGUI gui = new StationeryStoreGUI(store);
            gui.run();
        });
    }
}
