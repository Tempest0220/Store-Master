package com.store.gui.concretePanels;

import com.store.framework.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class NormalManagementPanel extends JPanel {
    private final Store store;
    private final Map<String, Customer> members;

    /* --- 會員清單 UI 元件 --- */
    private DefaultListModel<String> memberModel;
    private JList<String>            memberList ;
    /* --- 商品樹狀結構 UI --- */
    private DefaultTreeModel productTreeModel;
    private JTree            productTree;

    public NormalManagementPanel(Store store, Map<String, Customer> members) {
        super(new BorderLayout());
        this.store   = store;
        this.members = members;
        build();
    }

    private void build(){
        memberModel = new DefaultListModel<>();
        memberList  = new JList<>(memberModel);
        String defaultMemberId = "A1001";
        memberModel.addElement(defaultMemberId);
        members.put(defaultMemberId, new Customer(defaultMemberId));


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
        add(split, BorderLayout.CENTER);
    }
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
