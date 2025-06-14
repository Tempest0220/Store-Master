package com.store.gui;

import com.store.framework.Store;
import com.store.framework.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 支援外框與畫面切換的內嵌式 GUI。
 * 具體畫面由 StorePanelFactory 生成
 */
public class StoreGUI extends JPanel {

    private final Store store;
    private final StorePanelFactory factory;
    private final Map<String, Customer> members = new HashMap<>();

    private final CardLayout cards = new CardLayout();
    private final JPanel center = new JPanel(cards);

    private final JMenuBar menuBar = new JMenuBar();

    public StoreGUI(Store store, StorePanelFactory factory) {
        this.store = store;
        this.factory = factory;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        /* 三大畫面 */
        center.add(factory.createSalesPanel(store, members), "SALE");
        center.add(factory.createReceivingPanel(store), "RECV");
        center.add(factory.createManagementPanel(store, members), "MGMT");
        add(center, BorderLayout.CENTER);

        /* Menu */
        JMenu menu = new JMenu("畫面：售貨");
        JMenuItem miSale = new JMenuItem("售貨");
        JMenuItem miRecv = new JMenuItem("進貨");
        JMenuItem miMgmt = new JMenuItem("管理");
        menu.add(miSale);
        menu.add(miRecv);
        menu.add(miMgmt);
        menuBar.add(menu);

        miSale.addActionListener(e -> {
            cards.show(center, "SALE");
            menu.setText("畫面：售貨");
        });
        miRecv.addActionListener(e -> {
            cards.show(center, "RECV");
            menu.setText("畫面：進貨");
        });
        miMgmt.addActionListener(e -> {
            cards.show(center, "MGMT");
            menu.setText("畫面：管理");
        });
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public Store getStore() {
        return store;
    }
}
