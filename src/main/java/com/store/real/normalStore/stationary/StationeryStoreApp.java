package com.store.real.normalStore.stationary;

import com.store.framework.Store;
import com.store.framework.MembershipSalesRule;
import com.store.gui.StoreGUI;
import com.store.gui.concreteFactory.DefaultStorePanelFactory;

import javax.swing.*;

public class StationeryStoreApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Store store = new StationeryStore(
                    "My Stationery Shop",
                    new MembershipSalesRule(0.05));

            StoreGUI gui = new StoreGUI(store, new DefaultStorePanelFactory());

            // 用 JFrame 顯示 StoreGUI（現在是 JPanel）
            JFrame frame = new JFrame(store.getName() + " 管理介面");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);
            frame.setJMenuBar(gui.getMenuBar()); // 重要：加上內建的選單
            frame.setContentPane(gui);           // 加入面板內容
            frame.setVisible(true);
        });
    }
}
