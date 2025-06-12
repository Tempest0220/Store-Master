package com.store.stationery;

import com.store.framework.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StationeryStoreGUI extends GUIWindow {
    private Map<String, Customer> members = new HashMap<>();
    /* --- 會員清單 UI 元件 --- */
    private DefaultListModel<String> memberModel = new DefaultListModel<>();
    private JList<String>            memberList  = new JList<>(memberModel);
    /* --- 商品樹狀結構 UI --- */
    private DefaultTreeModel productTreeModel;
    private JTree            productTree;

    public StationeryStoreGUI(StationeryStore store) {
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
            try{
                String name = txtProduct.getText();
                int qty = Integer.parseInt(txtQty.getText());
                Customer c = members.get(txtMember.getText());
                store.sell(name, qty, c);
                log.append("售出 " + qty + "×" + name + (c == null ? " (無會員折扣)\n" : "\n"));
            }catch(Exception ex){
                JOptionPane.showMessageDialog(panel, ex.getMessage(),
                        "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(log), BorderLayout.CENTER);
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
            try{
                ((StationeryStore)store).restockWarehouse(txtName.getText(),
                        Integer.parseInt(txtQty.getText()));
                JOptionPane.showMessageDialog(panel, "已進貨 " + txtQty.getText() + "×" + txtName.getText());
            }catch(Exception ex){/* ignore */}
        });
        return panel;
    }

    /* ========== 三、管理面板（新功能） ========== */
    @Override
    protected JPanel getManagementPanel() {
        JPanel root = new JPanel(new BorderLayout());

        /* ---------------- 1. 上／下半分割 ---------------- */
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.6);
        root.add(split, BorderLayout.CENTER);

        /* ■ 上半：商品管理區 */
        split.setTopComponent(createProductManagePanel());

        /* ■ 下半：會員管理區 */
        split.setBottomComponent(createMemberManagePanel());

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
                String price  = JOptionPane.showInputDialog(panel, "新售價：", item.getPrice());
                String qty    = JOptionPane.showInputDialog(panel, "新庫存：", item.getQuantity());
                if(price != null) item.setPrice(Double.parseDouble(price));
                if(qty   != null) item.setQuantity(Integer.parseInt(qty));
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
                ((StationeryStore)store).deleteProduct(item.getName());
            }
            else if (obj instanceof ProductCategory cat) {
                ((StationeryStore)store).removeCategory(cat.getName());
            }
            refreshProductTree();
        });

        /* 3-3 移動商品 */
        mMove.addActionListener(e -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) productTree.getLastSelectedPathComponent();
            if (node == null) return;
            Object obj = node.getUserObject();

            List<String> cats =
                    ((StationeryStore)store).getCategories().stream()
                            .map(ProductCategory::getName)
                            .toList();
            if (cats.isEmpty()) return;

            String dest = (String) JOptionPane.showInputDialog(
                    panel, "選擇目標分類：", "移動節點",
                    JOptionPane.QUESTION_MESSAGE, null,
                    cats.toArray(), cats.get(0));
            if (dest == null) return;

            if (obj instanceof ProductItem item) {
                ((StationeryStore)store).moveProductToCategory(item.getName(), dest);
            }
            else if (obj instanceof ProductCategory cat) {
                ((StationeryStore)store).moveCategory(cat.getName(), dest);
            }
            refreshProductTree();
        });

        /* 4. 新增分類 */
        btnAddCat.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(panel, "分類名稱：");
            if(name == null || name.isBlank()) return;
            ((StationeryStore)store).addCategory(name);
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

            if (((StationeryStore) store).getCategories().isEmpty()) {
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

            StationeryStore s = (StationeryStore) store;

            if (s.isProductNameExist(name)) {
                JOptionPane.showMessageDialog(panel, "商品名稱重複，無法新增", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            s.addNewProductType(name, price, qty);
            s.moveProductToCategory(name, cat);

            refreshProductTree();
        });

        return panel;
    }

    /* ---------- 會員管理區 ---------- */
    private JPanel createMemberManagePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel addMem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtId = new JTextField(6);
        JButton btnAdd   = new JButton("新增會員");
        addMem.add(new JLabel("會員ID：")); addMem.add(txtId); addMem.add(btnAdd);
        panel.add(addMem, BorderLayout.NORTH);

        panel.add(new JScrollPane(memberList), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            String id = txtId.getText();
            if(id.isBlank() || members.containsKey(id)) return;
            members.put(id, new Customer(id));
            memberModel.addElement(id);
        });

        return panel;
    }

    /* ========== 工具方法 ========== */
    private DefaultMutableTreeNode buildTreeRoot(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        for(ProductCategory cat : ((StationeryStore)store).getCategories()){
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
        List<String> cats = ((StationeryStore) store).getCategories()
                .stream().map(ProductCategory::getName).toList();
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
                setText(p.getName() + "   (qty=" + p.getQuantity() + ", price=" + p.getPrice() + ")");
            }else{
                setText(obj.toString());
            }
            return this;
        }
    }
}
