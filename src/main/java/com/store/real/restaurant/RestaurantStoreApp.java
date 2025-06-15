package com.store.real.restaurant;

import com.store.gui.StoreGUI;
import com.store.gui.concreteFactory.RestaurantPanelFactory;

import javax.swing.*;

public class RestaurantStoreApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            RestaurantStore store = new RestaurantStore(
                    "My Restaurant"
            );

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
