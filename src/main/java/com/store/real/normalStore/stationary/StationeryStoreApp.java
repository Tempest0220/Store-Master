package com.store.real.normalStore.stationary;

import com.store.framework.Store;
import com.store.framework.MembershipSalesRule;
import com.store.gui.StoreGUI;
import com.store.gui.concreteFactory.DefaultStorePanelFactory;

import javax.swing.SwingUtilities;

public class StationeryStoreApp {
    public static void main(String[] args) {
        Store store = new StationeryStore(
                "My Stationery Shop",
                new MembershipSalesRule(0.05));

        SwingUtilities.invokeLater(() ->
                new StoreGUI(store, new DefaultStorePanelFactory()).run());
    }
}
