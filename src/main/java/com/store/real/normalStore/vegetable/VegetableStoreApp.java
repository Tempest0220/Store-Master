package com.store.real.normalStore.vegetable;

import com.store.framework.MembershipSalesRule;
import com.store.framework.Store;
import com.store.framework.SimpleSalesRule;
import com.store.gui.StoreGUI;
import com.store.gui.concreteFactory.DefaultStorePanelFactory;

import javax.swing.*;

public class VegetableStoreApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Store store = new VegetableStore(
                    "My Vegetable Shop");

            StoreGUI gui = new StoreGUI(store, new DefaultStorePanelFactory());

            // 用 JFrame 包住 StoreGUI（現在是 JPanel）
            JFrame frame = new JFrame(store.getName() + " 管理介面");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);
            frame.setJMenuBar(gui.getMenuBar()); // 選單列
            frame.setContentPane(gui);           // 內容面板
            frame.setVisible(true);
        });
    }
}
