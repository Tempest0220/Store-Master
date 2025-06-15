package com.store.gui.concretePanels;

import com.store.framework.*;
import com.store.real.normalStore.NormalSalesGUIStore;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class NormalSalesPanel extends JPanel {

    private final Store store;
    private final Map<String, Customer> members;

    public NormalSalesPanel(Store store, Map<String, Customer> members) {
        super(new BorderLayout());
        this.store = store;
        this.members = members;
        build();
    }

    private void build() {
        JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberPanel.add(new JLabel("會員ID："));
        JTextField txtMemberID = new JTextField(8);
        memberPanel.add(txtMemberID);
        add(memberPanel, BorderLayout.NORTH);

        DefaultTreeModel treeModel = new DefaultTreeModel(buildTreeRoot());
        JTree productTree = new JTree(treeModel);
        productTree.setRootVisible(false);
        productTree.setShowsRootHandles(true);
        productTree.setCellRenderer(new ProductTreeCellRenderer());

        JScrollPane treeScroll = new JScrollPane(productTree);
        treeScroll.setPreferredSize(new Dimension(300, 0));

        DefaultListModel<String> cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        JScrollPane cartScroll = new JScrollPane(cartList);
        cartScroll.setBorder(BorderFactory.createTitledBorder("暫存清單"));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, treeScroll, cartScroll);
        split.setResizeWeight(0.6);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTotal = new JLabel("總價：0.00");
        JButton btnClear = new JButton("清除清單");
        JButton btnCheckout = new JButton("結帳");
        btnCheckout.setEnabled(false);
        footer.add(lblTotal);
        footer.add(btnClear);
        footer.add(btnCheckout);

        add(split, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        Map<String, Integer> cart = new HashMap<>();

        Runnable updateTotal = () -> {
            String id = txtMemberID.getText().trim();
            Customer member = members.get(id);
            boolean validMember = id.isEmpty() || member != null;
            btnCheckout.setEnabled(validMember && !cart.isEmpty());

            double total = 0;
            for (var e : cart.entrySet()) {
                String name = e.getKey();
                int qty = e.getValue();
                ProductComponent p = ((NormalSalesGUIStore) store).getProduct(name);
                if (p == null) continue;

                if (!id.isEmpty() && member != null) {
                    total += ((NormalSalesGUIStore) store)
                            .applySale(p, qty, new Customer(id));
                } else {
                    total += p.getPrice() * qty;
                }
            }
            lblTotal.setText("總價：" + String.format("%.2f", total));
        };

        txtMemberID.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateTotal.run();
            }

            public void removeUpdate(DocumentEvent e) {
                updateTotal.run();
            }

            public void changedUpdate(DocumentEvent e) {
                updateTotal.run();
            }
        });

        productTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    var path = productTree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) return;
                    Object obj = ((DefaultMutableTreeNode)
                            path.getLastPathComponent()).getUserObject();
                    if (obj instanceof ProductItem item) {
                        String name = item.getName();
                        String input = JOptionPane.showInputDialog(
                                NormalSalesPanel.this,
                                "請輸入要購買的數量：",
                                "加入購物清單 - " + name,
                                JOptionPane.PLAIN_MESSAGE
                        );

                        if (input != null) {
                            try {
                                int qty = Integer.parseInt(input.trim());
                                if (qty <= 0) throw new NumberFormatException();

                                // 檢查庫存限制
                                int currentQty = cart.getOrDefault(name, 0);
                                int newQty = currentQty + qty;

                                ProductComponent p = ((NormalSalesGUIStore) store).getProduct(name);
                                if (p == null) {
                                    JOptionPane.showMessageDialog(
                                            NormalSalesPanel.this,
                                            "找不到該商品資料",
                                            "錯誤",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                                    return;
                                }

                                if (newQty > p.getQuantity()) {
                                    JOptionPane.showMessageDialog(
                                            NormalSalesPanel.this,
                                            "庫存不足，最多可加入 " + (p.getQuantity() - currentQty) + " 個。",
                                            "庫存不足",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                                    return;
                                }

                                cart.put(name, newQty);
                                redrawCart(cartModel, cart);
                                updateTotal.run();

                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(
                                        NormalSalesPanel.this,
                                        "請輸入有效的正整數數量。",
                                        "數量錯誤",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    }
                }
            }
        });

        btnClear.addActionListener(e -> {
            cart.clear();
            cartModel.clear();
            updateTotal.run();
            treeModel.setRoot(buildTreeRoot());
        });

        btnCheckout.addActionListener(e -> {
            String id = txtMemberID.getText().trim();
            Customer member = members.get(id);
            StringBuilder sb = new StringBuilder();

            for (var entry : cart.entrySet()) {
                try {
                    store.sell(entry.getKey(), entry.getValue(), member);
                    sb.append("售出 ")
                            .append(entry.getValue()).append("×")
                            .append(entry.getKey()).append("\n");
                } catch (IllegalArgumentException ex) {
                    sb.append("失敗：").append(ex.getMessage()).append("\n");
                }
            }

            JOptionPane.showMessageDialog(this, sb.toString(),
                    "結帳結果", JOptionPane.INFORMATION_MESSAGE);
            cart.clear();
            cartModel.clear();
            updateTotal.run();
        });

        SwingUtilities.invokeLater(() -> split.setDividerLocation(0.6));
    }

    private DefaultMutableTreeNode buildTreeRoot() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        for (ProductCategory cat : store.CategoryGetCategories()) {
            DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(cat);
            buildChildren(catNode, cat);
            root.add(catNode);
        }
        return root;
    }

    private void buildChildren(DefaultMutableTreeNode parent, ProductComponent comp) {
        for (ProductComponent ch : comp.getChildren()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(ch);
            parent.add(node);
            buildChildren(node, ch);
        }
    }

    private void redrawCart(DefaultListModel<String> model, Map<String, Integer> cart) {
        model.clear();
        cart.forEach((k, v) -> model.addElement(k + " x " + v));
    }

    private static class ProductTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(
                JTree tree, Object value, boolean sel, boolean exp,
                boolean leaf, int row, boolean focus) {
            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, focus);

            Object obj = ((DefaultMutableTreeNode) value).getUserObject();
            if (obj instanceof ProductCategory c) {
                setText(c.getName());
            } else if (obj instanceof ProductItem p) {
                setText(p.toString());
            } else {
                setText(obj.toString());
            }
            return this;
        }
    }
}
