package com.store.real.store;

import com.store.framework.*;
import com.store.real.store.stationary.StationeryStore;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.HashMap;



public class StoreGUI extends GUIWindow {
    private final Map<String, Customer> members = new HashMap<>();
    /* --- 會員清單 UI 元件 --- */
    private DefaultListModel<String> memberModel;
    private JList<String>            memberList ;
    /* --- 商品樹狀結構 UI --- */
    private DefaultTreeModel productTreeModel;
    private JTree            productTree;

    public StoreGUI(Store store) {
        super(store);
        members.put("A1001", new Customer("A1001"));
        memberModel.addElement("A1001");
    }

    @Override
    protected String title() { return store.getName() + " 管理介面"; }

    /* ========== 一、售貨面板（原樣） ========== */
    @Override
    protected JPanel getSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 1. 會員輸入區
        JPanel memberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        memberPanel.add(new JLabel("會員ID："));
        JTextField txtMemberID = new JTextField(8);
        memberPanel.add(txtMemberID);
        panel.add(memberPanel, BorderLayout.NORTH);

        // 2. 左：商品樹
        DefaultTreeModel treeModel = new DefaultTreeModel(buildTreeRoot());
        JTree productTree = new JTree(treeModel);
        productTree.setRootVisible(false);
        productTree.setShowsRootHandles(true);
        productTree.setCellRenderer(new ProductTreeCellRenderer());
        JScrollPane treeScroll = new JScrollPane(productTree);
        treeScroll.setPreferredSize(new Dimension(300, 0));

        // 3. 右：暫存清單
        DefaultListModel<String> cartListModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartListModel);
        JScrollPane cartScroll = new JScrollPane(cartList);
        cartScroll.setBorder(BorderFactory.createTitledBorder("暫存清單"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treeScroll, cartScroll);
        split.setResizeWeight(0.6);

        // 4. 底部：總價、按鈕
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel totalLabel   = new JLabel("總價：0.00");
        JButton btnClear    = new JButton("清除清單");
        JButton btnCheckout = new JButton("結帳");
        btnCheckout.setEnabled(false); // 初始停用
        footer.add(totalLabel);
        footer.add(btnClear);
        footer.add(btnCheckout);

        panel.add(split, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        // 購物車資料結構
        Map<String,Integer> cart = new HashMap<>();

        // 更新總價＆按鈕狀態
        Runnable updateTotal = () -> {
            String memberId = txtMemberID.getText().trim();
            Customer member = members.get(memberId);
            boolean hasValidMember = memberId.isEmpty() || (member != null);
            btnCheckout.setEnabled(hasValidMember && !cart.isEmpty());

            double total = 0;
            for (var entry : cart.entrySet()) {
                String key = entry.getKey();
                int qty    = entry.getValue();
                ProductComponent p = ((StationeryStore)store).getProduct(key);
                if (p == null) continue;
                double base = p.getPrice() * qty;
                if (!memberId.isEmpty() && member != null) {
                    // 使用 applySale 但不影響真實會員點數
                    total += ((StationeryStore)store).applySale(p, qty, new Customer(memberId));
                } else {
                    total += base;
                }
            }
            totalLabel.setText("總價：" + String.format("%.2f", total));
        };

        // 會員輸入變動時
        txtMemberID.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { updateTotal.run(); }
            public void removeUpdate(DocumentEvent e)  { updateTotal.run(); }
            public void changedUpdate(DocumentEvent e) { updateTotal.run(); }
        });

        // 雙擊樹狀選商品加入暫存
        productTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    TreePath path = productTree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) return;
                    Object node = ((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
                    if (node instanceof ProductItem item) {
                        String name = item.getName();
                        cart.put(name, cart.getOrDefault(name, 0) + 1);
                        // 更新 listModel
                        cartListModel.clear();
                        for (var entry : cart.entrySet()) {
                            String key = entry.getKey();
                            int qty    = entry.getValue();
                            ProductComponent p = ((StationeryStore)store).getProduct(key);
                            double price = p.getPrice() * qty;
                            cartListModel.addElement(
                                    key + " x " + qty + " = " + String.format("%.2f", price)
                            );
                        }
                        updateTotal.run();
                    }
                }
            }
        });

        // 清除清單
        btnClear.addActionListener(e -> {
            cart.clear();
            cartListModel.clear();
            updateTotal.run();
        });

        // 結帳
        btnCheckout.addActionListener(e -> {
            String memberId = txtMemberID.getText().trim();
            Customer member = members.get(memberId);
            StringBuilder sb = new StringBuilder();
            for (var entry : cart.entrySet()) {
                String key = entry.getKey();
                int qty    = entry.getValue();
                try {
                    store.sell(key, qty, member);
                    sb.append("售出 ").append(qty).append("×").append(key).append("\n");
                } catch (IllegalArgumentException ex) {
                    sb.append("失敗：").append(ex.getMessage()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(panel, sb.toString(), "結帳結果",
                    JOptionPane.INFORMATION_MESSAGE);
            cart.clear();
            cartListModel.clear();
            updateTotal.run();
        });

        // 初始拆分位置
        SwingUtilities.invokeLater(() -> split.setDividerLocation(0.6));

        return panel;
    }




    /* ========== 二、進貨面板（原樣） ========== */
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
            String name = txtName.getText().trim();
            // 檢查商品是否存在
            if (!store.RegistryIsProductNameExist(name)) {
                JOptionPane.showMessageDialog(panel,
                        "商品不存在：" + name,
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int qty;
            try {
                qty = Integer.parseInt(txtQty.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                        "數量格式錯誤",
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            (store).RegistrySetProductStock(name, qty);
            JOptionPane.showMessageDialog(panel,
                    "已進貨 " + qty + "×" + name,
                    "完成",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        return panel;
    }

    /* ========== 三、管理面板（新功能） ========== */
    @Override
    protected JPanel getManagementPanel() {
        memberModel = new DefaultListModel<>();
        memberList  = new JList<>(memberModel);
        // 上：商品管理面板，下：會員管理面板
        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                createProductManagePanel(),
                createMemberManagePanel()
        );
        // 視窗調整時保持 6:4
        split.setResizeWeight(0.6);
        // 等元件都建立完才設定分割線位置
        SwingUtilities.invokeLater(() -> {
            // 用比例 API（Java8u40+），或用像素都可以
            split.setDividerLocation(0.6);
        });

        JPanel root = new JPanel(new BorderLayout());
        root.add(split, BorderLayout.CENTER);
        return root;
    }

    /* ---------- 商品管理區 ---------- */
    private JPanel createProductManagePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        /* 1. 工具列：新增分類、新增商品 */
        JToolBar tool = new JToolBar();
        tool.setFloatable(false);
        JButton btnAddCat  = new JButton("新增分類");
        JButton btnAddProd = new JButton("新增商品");
        tool.add(btnAddCat); tool.add(btnAddProd);
        panel.add(tool, BorderLayout.NORTH);

        /* 2. 商品樹 */
        productTreeModel = new DefaultTreeModel(buildTreeRoot());
        productTree      = new JTree(productTreeModel);
        productTree.setRootVisible(false);
        productTree.setShowsRootHandles(true);
        productTree.setCellRenderer(new ProductTreeCellRenderer());
        panel.add(new JScrollPane(productTree), BorderLayout.CENTER);

        /* 3. Context Menu：修改 / 刪除 / 移動 */
        JPopupMenu menu = new JPopupMenu();
        JMenuItem mEdit  = new JMenuItem("修改");
        JMenuItem mDel   = new JMenuItem("刪除");
        JMenuItem mMove  = new JMenuItem("移動到分類…");
        menu.add(mEdit); menu.add(mDel); menu.add(mMove);

        productTree.setComponentPopupMenu(menu);

        /* 3-1 修改商品 */
        mEdit.addActionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) productTree.getLastSelectedPathComponent();
            if(node == null) return;
            Object obj = node.getUserObject();
            if(obj instanceof ProductItem item){
                String priceStr  = JOptionPane.showInputDialog(panel, "新售價：", item.getPrice());
                String qtyStr    = JOptionPane.showInputDialog(panel, "新庫存：", item.getQuantity());
                String discountStr = JOptionPane.showInputDialog(panel, "折扣(0~1，例如0.8表示八折)：", "1.0");

                if(priceStr != null) {
                    try {
                        double newPrice = Double.parseDouble(priceStr);
                        item.setPrice(newPrice);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel, "價格格式錯誤", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if(qtyStr != null) {
                    try {
                        int newQty = Integer.parseInt(qtyStr);
                        item.setQuantity(newQty);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel, "庫存格式錯誤", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if(discountStr != null){
                    try {
                        double discountRate = Double.parseDouble(discountStr);
                        if(discountRate < 0 || discountRate > 1){
                            JOptionPane.showMessageDialog(panel, "折扣必須介於0和1之間", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
                        } else {
                            item.setDiscount(price -> price * discountRate);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(panel, "折扣格式錯誤", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                refreshProductTree();
            }
        });

        /* 3-2 刪除商品 */
        mDel.addActionListener(e -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) productTree.getLastSelectedPathComponent();
            if (node == null) return;
            Object obj = node.getUserObject();

            if (obj instanceof ProductItem item) {
                (store).RegistryDeleteProduct(item.getName());
            }
            else if (obj instanceof ProductCategory cat) {
                (store).CategoryRemoveCategoryAndProduct(cat.getName());
            }
            refreshProductTree();
        });

        /* 3-3 移動商品 */
        mMove.addActionListener(e -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) productTree.getLastSelectedPathComponent();
            if (node == null) return;
            Object obj = node.getUserObject();

            java.util.List<String> cats = new ArrayList<>(store.CategoryGetAllCategories().stream()
                    .map(ProductCategory::getName).toList());

            // 若為分類，要排除自己與其子孫，並加入「根目錄」選項
            if (obj instanceof ProductCategory cat) {
                cats.removeIf(n -> n.equals(cat.getName()) ||
                        store.isCategoryDescendant(cat.getName(), n));
                cats.add(0, "(根目錄)");
            }
            if (cats.isEmpty()) return;

            String dest = (String) JOptionPane.showInputDialog(
                    panel, "選擇目標分類：", "移動節點",
                    JOptionPane.QUESTION_MESSAGE, null,
                    cats.toArray(), cats.get(0));
            if (dest == null) return;

            if (obj instanceof ProductItem item) {
                store.CategoryAddProduct(item.getName(), dest);
            } else if (obj instanceof ProductCategory cat) {
                if ("(根目錄)".equals(dest)) dest = null;
                store.CategoryMoveCategoryAndProducts(cat.getName(), dest);
            }
            refreshProductTree();
        });

        /* 4. 新增分類 */
        btnAddCat.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(panel, "分類名稱：");
            if(name == null || name.isBlank()) return;
            (store).CategoryAddNewCategory(name);
            refreshProductTree();
        });

        /* 5. 新增商品 */
        btnAddProd.addActionListener(e -> {
            JTextField fName  = new JTextField();
            JTextField fPrice = new JTextField();
            JTextField fQty   = new JTextField("0");
            JPanel form = new JPanel(new GridLayout(3,2));
            form.add(new JLabel("品名："));        form.add(fName);
            form.add(new JLabel("售價："));        form.add(fPrice);
            form.add(new JLabel("初始庫存："));    form.add(fQty);

            if (JOptionPane.showConfirmDialog(panel, form, "新增商品",
                    JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

            if (store.CategoryGetCategories().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "請先建立分類！", "無分類", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String cat = chooseCategory(panel);
            if (cat == null) return;

            String name = fName.getText().trim();
            double price;
            int qty;
            try {
                price = Double.parseDouble(fPrice.getText().trim());
                qty = Integer.parseInt(fQty.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "價格或庫存格式錯誤", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (store.RegistryIsProductNameExist(name)) {
                JOptionPane.showMessageDialog(panel, "商品名稱重複，無法新增", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            store.RegistryAddNewProduct(name, price, qty);
            store.CategoryAddProduct(name, cat);

            refreshProductTree();
        });

        return panel;
    }


    /* ---------- 會員管理區 ---------- */
    private JPanel createMemberManagePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 上方：新增會員區
        JPanel addMem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtId = new JTextField(6);
        JButton btnAdd   = new JButton("新增會員");
        addMem.add(new JLabel("會員ID："));
        addMem.add(txtId);
        addMem.add(btnAdd);
        panel.add(addMem, BorderLayout.NORTH);

        // 下方：會員列表
        memberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberList.setVisibleRowCount(8);
        JScrollPane scroll = new JScrollPane(memberList);
        scroll.setBorder(BorderFactory.createTitledBorder("會員清單"));
        panel.add(scroll, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty() || members.containsKey(id)) {
                JOptionPane.showMessageDialog(panel,
                        "無效或重複的會員ID：" + id,
                        "錯誤",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            members.put(id, new Customer(id));
            memberModel.addElement(id);
            txtId.setText("");
            // 強制重繪，確保新加入的文字馬上 visible
            scroll.revalidate();
            scroll.repaint();
        });

        return panel;
    }

    /* ========== 工具方法 ========== */
    private DefaultMutableTreeNode buildTreeRoot(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        for(ProductCategory cat : store.CategoryGetCategories()){
            DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(cat);
            buildChildren(catNode, cat);
            root.add(catNode);
        }
        return root;
    }
    private void buildChildren(DefaultMutableTreeNode parent, ProductComponent comp){
        for(ProductComponent child : comp.getChildren()){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
            parent.add(node);
            buildChildren(node, child);
        }
    }
    private void refreshProductTree(){
        productTreeModel.setRoot(buildTreeRoot());
        productTreeModel.reload();
        for(int i=0;i<productTree.getRowCount();i++) productTree.expandRow(i);
    }
    private String chooseCategory(Component parent) {
        List<String> cats = store
                .CategoryGetAllCategories().stream()
                .map(ProductCategory::getName).toList();
        if (cats.isEmpty()) return null;

        return (String) JOptionPane.showInputDialog(
                parent, "放入分類：", "選擇分類",
                JOptionPane.QUESTION_MESSAGE, null,
                cats.toArray(), cats.get(0));
    }

    /* ========== 內部 Renderer：顯示庫存／售價 ========== */
    private static class ProductTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean exp, boolean leaf, int row, boolean focus){
            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, focus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object obj = node.getUserObject();
            if(obj instanceof ProductCategory c){
                setText(c.getName());
            }else if(obj instanceof ProductItem p){
//                setText(p.getName() + "   (qty=" + p.getQuantity() + ", price=" + p.getPrice() + ")");
                setText(p.toString());
            }else{
                setText(obj.toString());
            }
            return this;
        }
    }
}
