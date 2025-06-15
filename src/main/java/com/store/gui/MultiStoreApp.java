package com.store.gui;

import com.store.framework.*;
import com.store.gui.concreteFactory.DefaultStorePanelFactory;
import com.store.gui.concreteFactory.RestaurantPanelFactory;
import com.store.real.normalStore.stationary.StationeryStore;
import com.store.real.normalStore.vegetable.VegetableStore;
import com.store.real.restaurant.RestaurantSalesRule;
import com.store.real.restaurant.RestaurantStore;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 為方便demo開發出來的統一所有商店的GUI
 */
public class MultiStoreApp extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final Map<String, StoreGUI> storeGUIs = new HashMap<>();
    private final JComboBox<String> storeSelector = new JComboBox<>();

    public MultiStoreApp() {
        setTitle("多商店切換系統");
        setSize(1024, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 建立所有商店
        createAndAddStore("蔬菜店", new VegetableStore(
                "My Vegetable Shop",
                new SimpleSalesRule()));

        createAndAddStore("文具店", new StationeryStore(
                "My Stationery Shop",
                new MembershipSalesRule(0.05)));

        RestaurantSalesRule restaurantSalesRule = new RestaurantSalesRule(null);
        RestaurantStore restaurantStore = new RestaurantStore("My Restaurant", restaurantSalesRule);
        createAndAddStore("餐廳", restaurantStore);
        restaurantSalesRule.setStore(restaurantStore);

        // 設定選單
        storeSelector.addActionListener(e -> {
            String selected = (String) storeSelector.getSelectedItem();
            if (selected != null) {
                cardLayout.show(mainPanel, selected);
                StoreGUI selectedGUI = storeGUIs.get(selected);
                setJMenuBar(selectedGUI.getMenuBar());
                setTitle(selectedGUI.getStore().getName() + " - 管理介面");
                revalidate();
                repaint();
            }
        });

        add(storeSelector, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // 預設顯示第一個商店
        if (storeGUIs.size() > 0) {
            storeSelector.setSelectedIndex(0);
            storeSelector.getActionListeners()[0].actionPerformed(null);
        }
    }

    private void createAndAddStore(String key, Store store) {
        StoreGUI gui;
        if (store instanceof RestaurantStore)
            gui = new StoreGUI(store, new RestaurantPanelFactory());
        else
            gui = new StoreGUI(store, new DefaultStorePanelFactory());
        storeGUIs.put(key, gui);
        mainPanel.add(gui, key);
        storeSelector.addItem(key);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MultiStoreApp().setVisible(true));
    }
}
