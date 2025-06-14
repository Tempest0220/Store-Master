package com.store.gui.concreteFactory;

import com.store.gui.StorePanelFactory;
import com.store.gui.concretePanels.*;
import com.store.framework.Store;
import com.store.framework.Customer;
import com.store.real.restaurant.RestaurantStore;

import javax.swing.*;
import java.util.Map;

/** 餐廳 GUI 工廠：售貨畫面改用 RestaurantSalesPanel。 */
public class RestaurantPanelFactory implements StorePanelFactory {
    @Override
    public JPanel createSalesPanel(Store store, Map<String, Customer> members) {
        return new RestaurantSalesPanel((RestaurantStore) store, members);
    }
    @Override
    public JPanel createReceivingPanel(Store store) {
        return new NormalReceivingPanel(store);
    }
    @Override
    public JPanel createManagementPanel(Store store, Map<String,Customer> members){
        return new NormalManagementPanel(store, members);
    }
}
