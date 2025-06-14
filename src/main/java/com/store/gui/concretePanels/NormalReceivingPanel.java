package com.store.gui.concretePanels;

import com.store.framework.Store;

import javax.swing.*;
import java.awt.*;

public class NormalReceivingPanel extends JPanel {
    private final Store store;

    public NormalReceivingPanel(Store store){
        super(new BorderLayout());
        this.store = store;
        build();
    }
    private void build(){
        JTextField txtName = new JTextField(10);
        JTextField txtQty  = new JTextField(3);
        JButton btnRecv    = new JButton("進貨");
        add(new JLabel("品名：")); add(txtName);
        add(new JLabel("數量：")); add(txtQty);
        add(btnRecv);

        btnRecv.addActionListener(e -> {
            String name = txtName.getText().trim();
            // 檢查商品是否存在
            if (!store.RegistryIsProductNameExist(name)) {
                JOptionPane.showMessageDialog(this,
                        "商品不存在：" + name,
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int qty;
            try {
                qty = Integer.parseInt(txtQty.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "數量格式錯誤",
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            (store).RegistrySetProductStock(name, qty);
            JOptionPane.showMessageDialog(this,
                    "已進貨 " + qty + "×" + name,
                    "完成",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
