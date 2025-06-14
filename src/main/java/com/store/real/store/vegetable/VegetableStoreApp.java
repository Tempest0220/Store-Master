package com.store.real.store.vegetable;

import com.store.framework.Store;
import com.store.real.store.StoreGUI;
import com.store.real.store.stationary.StationeryStore;

import javax.swing.*;

public class VegetableStoreApp {
    public static void main(String[] args) {

        Store vegetableStore = new VegetableStore("My Vegetable Shop",
                new com.store.framework.MembershipSalesRule(0.10));


        // 在 Swing UI Thread 啟動
        SwingUtilities.invokeLater(() -> {
            StoreGUI gui = new StoreGUI(vegetableStore);
            gui.run();
        });
    }
}
