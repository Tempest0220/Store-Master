package com.store.gui.concreteFactory;

import com.store.gui.StorePanelFactory;
import com.store.gui.concretePanels.NormalSalesPanel;
import com.store.gui.concretePanels.NormalReceivingPanel;
import com.store.gui.concretePanels.NormalManagementPanel;
import com.store.framework.Store;
import com.store.framework.Customer;

import javax.swing.*;
import java.util.Map;

/**
 * 將舊 StoreGUI 內嵌的三個畫面抽出成獨立類別，
 * 再由預設工廠統一產生。
 */
public class DefaultStorePanelFactory implements StorePanelFactory {

    @Override
    public JPanel createSalesPanel(Store store, Map<String, Customer> members) {
        return new NormalSalesPanel(store, members);
    }

    @Override
    public JPanel createReceivingPanel(Store store) {
        return new NormalReceivingPanel(store);
    }

    @Override
    public JPanel createManagementPanel(Store store,
                                        Map<String, Customer> members) {
        return new NormalManagementPanel(store, members);
    }
}
