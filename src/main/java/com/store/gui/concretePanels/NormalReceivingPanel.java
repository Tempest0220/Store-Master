package com.store.gui.concretePanels;

import com.store.framework.Store;

import javax.swing.*;
import java.awt.*;

public class NormalReceivingPanel extends JPanel {
    private final Store store;

    public NormalReceivingPanel(Store store){
        super(new FlowLayout());
        this.store = store;
        build();
    }

    private void build(){
        JTextField txtName = new JTextField(10);
        JTextField txtQty  = new JTextField(3);
        JButton btnRecv    = new JButton("進貨");

        add(new JLabel("品名："));
        add(txtName);
        add(new JLabel("數量："));
        add(txtQty);
        add(btnRecv);

        btnRecv.addActionListener(e -> {
            String name = txtName.getText().trim();
            String qtyStr = txtQty.getText().trim();

            if (!store.RegistryIsProductNameExist(name)) {
                JOptionPane.showMessageDialog(this,
                        "商品不存在：" + name,
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int qty = Integer.parseInt(qtyStr);
                if (qty <= 0) throw new NumberFormatException();

                store.receive(name, qty);

                JOptionPane.showMessageDialog(this,
                        "成功進貨 " + qty + " 個 [" + name + "]",
                        "進貨成功",
                        JOptionPane.INFORMATION_MESSAGE);

                txtName.setText("");
                txtQty.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "請輸入正整數的數量。",
                        "格式錯誤",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "進貨時發生錯誤: " + ex.getMessage(),
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
