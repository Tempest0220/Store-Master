package com.store.gui;

import com.store.framework.Store;
import com.store.framework.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 只負責外框與畫面切換的精簡版 GUI。
 * 具體畫面由 StorePanelFactory 生成，保持未來可替換彈性。
 */
public class StoreGUI extends JFrame {

    private final Store store;
    private final StorePanelFactory factory;
    private final Map<String, Customer> members = new HashMap<>();

    private final CardLayout cards  = new CardLayout();
    private final JPanel     center = new JPanel(cards);

    public StoreGUI(Store store, StorePanelFactory factory) {
        this.store   = store;
        this.factory = factory;
        initUI();
    }

    private void initUI() {
        setTitle(store.getName() + " 管理介面");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* 三大畫面 */
        center.add(factory.createSalesPanel(store, members), "SALE");
        center.add(factory.createReceivingPanel(store),      "RECV");
        center.add(factory.createManagementPanel(store, members), "MGMT");
        add(center, BorderLayout.CENTER);

        /* Menu */
        JMenuBar bar = new JMenuBar();
        JMenu menu  = new JMenu("畫面：售貨");
        JMenuItem miSale = new JMenuItem("售貨");
        JMenuItem miRecv = new JMenuItem("進貨");
        JMenuItem miMgmt = new JMenuItem("管理");
        menu.add(miSale); menu.add(miRecv); menu.add(miMgmt);
        bar.add(menu);
        setJMenuBar(bar);

        miSale.addActionListener(e -> { cards.show(center, "SALE"); menu.setText("畫面：售貨"); });
        miRecv.addActionListener(e -> { cards.show(center, "RECV"); menu.setText("畫面：進貨"); });
        miMgmt.addActionListener(e -> { cards.show(center, "MGMT"); menu.setText("畫面：管理"); });
    }

    public void run() { setVisible(true); }
}
