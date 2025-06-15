package com.store.gui;

import com.store.framework.Store;
import com.store.framework.Customer;

import javax.swing.*;
import java.util.Map;

/**
 * 提供 GUI 畫面的抽象工廠。<br>
 * 不同商店若要客製畫面，只需提供自己的 Factory 實作。
 */
public interface StorePanelFactory {
    JPanel createSalesPanel(Store store, Map<String, Customer> members);
    JPanel createReceivingPanel(Store store);
    JPanel createManagementPanel(Store store, Map<String, Customer> members);
}
