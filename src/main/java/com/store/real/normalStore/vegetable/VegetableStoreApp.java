package com.store.real.normalStore.vegetable;

import com.store.framework.Store;
import com.store.framework.SimpleSalesRule;
import com.store.gui.StoreGUI;
import com.store.gui.concreteFactory.DefaultStorePanelFactory;

import javax.swing.SwingUtilities;

public class VegetableStoreApp {
    public static void main(String[] args) {
        Store store = new VegetableStore(
                "My Vegetable Shop",
                new SimpleSalesRule());   // 改用不含會員的銷售規則

        SwingUtilities.invokeLater(() ->
                new StoreGUI(store, new DefaultStorePanelFactory()).run());
    }
}
