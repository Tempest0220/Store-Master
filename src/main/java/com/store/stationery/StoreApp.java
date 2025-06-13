package com.store.stationery;
import com.store.framework.Store;
import javax.swing.SwingUtilities;

public class StoreApp {
    public static void main(String[] args) {
        // 建立文具店實例
//        Store store = new StationeryStore("My Stationery Shop",
//                new com.store.framework.MembershipSalesRule(0.05));

        // 建立蔬菜店實例
        Store store = new VegetableStore("My Vegetable Shop",
                new com.store.framework.MembershipSalesRule(0.10));


        // 在 Swing UI Thread 啟動
        SwingUtilities.invokeLater(() -> {
            StoreGUI gui = new StoreGUI(store);
            gui.run();
        });
    }
}
