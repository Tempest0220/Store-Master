package com.store.real.restaurant;

import com.store.gui.StoreGUI;
import com.store.gui.concreteFactory.RestaurantPanelFactory;

import javax.swing.*;

/** 程式進入點 */
public class RestaurantStoreApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            RestaurantSalesRule restaurantSalesRule = new RestaurantSalesRule(null);
            RestaurantStore store = new RestaurantStore(
                    "My Restaurant",
                    restaurantSalesRule
            );
            restaurantSalesRule.setStore(store);

            StoreGUI gui = new StoreGUI(store, new RestaurantPanelFactory());
            JFrame f = new JFrame(store.getName()+" 管理介面");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1000,600); f.setLocationRelativeTo(null);
            f.setJMenuBar(gui.getMenuBar());
            f.setContentPane(gui);
            f.setVisible(true);
        });
    }
}
