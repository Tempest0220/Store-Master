package com.store.app;

import com.store.framework.*;
import com.store.stationery.StationeryStore;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StationeryStoreGUI extends GUIWindow {
    // 管理已註冊的會員（簡單示範）
    private Map<String, Customer> members = new HashMap<>();

    public StationeryStoreGUI(StationeryStore store) {
        super(store);
        // 可以預先加入一個示範會員
        members.put("A1001", new Customer("A1001"));
    }

    @Override
    protected String title() {
        return store.getName() + " 管理介面";
    }

    @Override
    protected JPanel getSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtProduct = new JTextField(10);
        JTextField txtQty     = new JTextField(3);
        JTextField txtMember  = new JTextField(6);
        JButton btnSell       = new JButton("結帳");
        JTextArea log         = new JTextArea(8, 40);
        log.setEditable(false);

        form.add(new JLabel("品名：")); form.add(txtProduct);
        form.add(new JLabel("數量：")); form.add(txtQty);
        form.add(new JLabel("會員ID：")); form.add(txtMember);
        form.add(btnSell);

        btnSell.addActionListener(e -> {
            String name = txtProduct.getText();
            int qty = Integer.parseInt(txtQty.getText());
            String mid = txtMember.getText();
            Customer c = members.get(mid);  // mid 不在 members 時，c == null → 無折扣
            try {
                store.sell(name, qty, c);
                log.append("售出 " + qty + "×" + name + (c == null ? " (無會員折扣)" : "") + "\n");
            } catch (IllegalArgumentException ex) {
                // 庫存不足的錯誤訊息顯示在 GUI
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(log), BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected JPanel getReceivingPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtName = new JTextField(10);
        JTextField txtQty  = new JTextField(3);
        JButton btnRecv    = new JButton("進貨");
        panel.add(new JLabel("品名：")); panel.add(txtName);
        panel.add(new JLabel("數量：")); panel.add(txtQty);
        panel.add(btnRecv);

        btnRecv.addActionListener(e -> {
            String name = txtName.getText();
            int qty = Integer.parseInt(txtQty.getText());
            store.restockWarehouse(name, qty);
            JOptionPane.showMessageDialog(panel, "已進貨 " + qty + "×" + name);
        });
        return panel;
    }

    @Override
    protected JPanel getManagementPanel() {
        JPanel panel = new JPanel(new GridLayout(2,1));
        // 1) 新增貨品
        JPanel addProd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField prodName = new JTextField(10);
        JTextField prodPrice= new JTextField(5);
        JButton btnAddProd  = new JButton("新增貨品");
        addProd.add(new JLabel("貨品名：")); addProd.add(prodName);
        addProd.add(new JLabel("價格："));   addProd.add(prodPrice);
        addProd.add(btnAddProd);

        btnAddProd.addActionListener(e -> {
            String name = prodName.getText();
            double price = Double.parseDouble(prodPrice.getText());
            ((StationeryStore)store).addNewProductType(name, price);
            JOptionPane.showMessageDialog(panel, "已新增貨品：" + name);
        });

        // 2) 管理會員
        JPanel manageMem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtNewId = new JTextField(6);
        JButton btnAddMem   = new JButton("新增會員");
        manageMem.add(new JLabel("會員ID：")); manageMem.add(txtNewId);
        manageMem.add(btnAddMem);

        btnAddMem.addActionListener(e -> {
            String mid = txtNewId.getText();
            members.put(mid, new Customer(mid));
            JOptionPane.showMessageDialog(panel, "已新增會員：" + mid);
        });

        panel.add(addProd);
        panel.add(manageMem);
        return panel;
    }
}
