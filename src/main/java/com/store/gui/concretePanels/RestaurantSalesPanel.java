package com.store.gui.concretePanels;

import com.store.framework.Customer;
import com.store.framework.ProductCategory;
import com.store.framework.ProductComponent;
import com.store.real.restaurant.TableContext;
import com.store.real.restaurant.RestaurantStore;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 餐廳「座位管理／點餐」畫面。
 *   - 左側四張桌子按鈕
 *   - 右側點餐/明細區 (於 OccupiedState 時開啟)
 */
public class RestaurantSalesPanel extends JPanel {

    private final RestaurantStore store;
    private final Map<String,Customer> members;
    private final TableContext[] tables = new TableContext[4];

    /* 右側共用元件 */
    private final CardLayout rightCards = new CardLayout();
    private final JPanel     rightPane  = new JPanel(rightCards);
    private final JPanel     blankPane  = new JPanel(); // 空白佔位

    public RestaurantSalesPanel(RestaurantStore store, Map<String,Customer> members){
        super(new BorderLayout());
        this.store   = store;
        this.members = members;
        for(int i=0;i<4;i++) tables[i] = new TableContext(i+1, store);
        build();
    }

    private void build(){
        /* 左：四張桌子 */
        JPanel left = new JPanel(new GridLayout(2,2,10,10));
        for(TableContext t: tables){
            JButton btn = new JButton(labelOf(t));
            btn.setFont(new Font("Dialog",Font.BOLD,22));
            btn.addActionListener(e-> handleTableClick(t, btn));
            left.add(btn);
        }
        add(left, BorderLayout.WEST);

        /* 右：空白 + 動態點餐畫面 */
        rightPane.add(blankPane,"BLANK");
        add(rightPane, BorderLayout.CENTER);
        rightCards.show(rightPane,"BLANK");
    }

    /* 依狀態顯示可執行動作 */
    private void handleTableClick(TableContext table, JButton btn){
        JPopupMenu menu = new JPopupMenu();
        switch(table.getStateName()){
            case "空桌" -> {
                JMenuItem miReserve = new JMenuItem("預約");
                JMenuItem miSeat    = new JMenuItem("帶位");
                menu.add(miReserve); menu.add(miSeat);

                miReserve.addActionListener(e->{ table.reserve(); refresh(btn); });
                miSeat.addActionListener(e->{
                    Customer mem = chooseMember();
                    table.seat(mem);
                    refresh(btn);
                    openOrderPane(table, btn);
                });
            }
            case "預約中" -> {
                JMenuItem miSeat = new JMenuItem("帶位");
                JMenuItem miCancel = new JMenuItem("取消預約");
                menu.add(miSeat); menu.add(miCancel);
                miSeat.addActionListener(e->{
                    Customer mem = chooseMember();
                    table.seat(mem); refresh(btn); openOrderPane(table, btn);
                });
                miCancel.addActionListener(e->{ table.cancel(); refresh(btn); });
            }
            case "用餐中" -> {
                JMenuItem miOrder   = new JMenuItem("點餐 / 修改");
                JMenuItem miCheckout= new JMenuItem("結帳");
                menu.add(miOrder); menu.add(miCheckout);
                miOrder.addActionListener(e-> openOrderPane(table, btn));
                miCheckout.addActionListener(e->{
                    double bill = table.checkout();
                    JOptionPane.showMessageDialog(this,
                            "結帳完成，金額："+bill,"完成",JOptionPane.INFORMATION_MESSAGE);
                    rightCards.show(rightPane,"BLANK");
                    refresh(btn);
                });
            }
        }
        menu.show(btn, 0, btn.getHeight());
    }

    private void openOrderPane(TableContext table, JButton btn){
        JPanel pane = buildOrderingPane(table, btn);
        String key = "T"+table.getTableNo();
        rightPane.add(pane,key);
        rightCards.show(rightPane,key);
    }

    /* 建立點餐 UI（樹狀菜單 + 已選清單 + 小計/結帳） */
    private JPanel buildOrderingPane(TableContext table, JButton btn){
        JPanel panel = new JPanel(new BorderLayout());

        /* 左：菜單樹 */
        DefaultTreeModel model = new DefaultTreeModel(buildTreeRoot());
        JTree tree = new JTree(model);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new MenuTreeRenderer());

        /* 中：購物車列表 */
        DefaultListModel<String> cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree),
                new JScrollPane(cartList)
        );
        split.setResizeWeight(0.6);
        panel.add(split, BorderLayout.CENTER);

        /* 下：總計 + 結帳（僅 OccupiedState） */
        JLabel lblTotal = new JLabel("總價：0.00");
        JButton btnOrder = new JButton("加入餐點");
        JButton btnCheckout = new JButton("結帳");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(lblTotal); bottom.add(btnOrder); bottom.add(btnCheckout);
        panel.add(bottom, BorderLayout.SOUTH);

        /* 內部購物車 */
        Map<String,Integer> cart = new HashMap<>();

        Runnable recalc = ()->{
            double tot = table.getStore().settleOrder(new HashMap<>(cart), table.getMember(), false);
            lblTotal.setText("總價："+String.format("%.2f", tot));
        };

        /* 加入餐點 – 先選樹節點再按按鈕 */
        btnOrder.addActionListener(e->{
            var sel = tree.getLastSelectedPathComponent();
            if(sel==null) return;
            Object obj = ((DefaultMutableTreeNode)sel).getUserObject();
            if(!(obj instanceof ProductComponent item)) return;
            String q = JOptionPane.showInputDialog(panel,"數量：","1");
            if(q==null) return;
            try{
                int qty = Integer.parseInt(q.trim());
                if(qty<=0) throw new NumberFormatException();
                cart.merge(item.getName(), qty, Integer::sum);
                redraw(cartModel, cart); recalc.run();
                table.order(cart); // 更新 Table 內部訂單
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(panel,"請輸入正整數。","錯誤",JOptionPane.ERROR_MESSAGE);
            }
        });

        /* 右鍵刪除餐點 */
        cartList.setComponentPopupMenu(new JPopupMenu());
        cartList.getComponentPopupMenu().add(new JMenuItem("移除")).addActionListener(e->{
            int idx = cartList.getSelectedIndex();
            if(idx<0) return;
            String s = cartModel.get(idx);
            String name = s.substring(0, s.lastIndexOf(" x "));
            cart.remove(name);
            redraw(cartModel, cart); recalc.run();
            table.order(cart); // 重設訂單
        });

        /* 結帳 */
        btnCheckout.addActionListener(e->{
            double bill = table.checkout();
            JOptionPane.showMessageDialog(panel,"結帳完成，金額："+bill,
                    "完成",JOptionPane.INFORMATION_MESSAGE);
            rightCards.show(rightPane,"BLANK");
            refresh(btn);
        });

        SwingUtilities.invokeLater(()->split.setDividerLocation(0.55));
        return panel;
    }

    /* ========== 工具 ========== */
    private void redraw(DefaultListModel<String> m, Map<String,Integer> cart){
        m.clear(); cart.forEach((k,v)-> m.addElement(k+" x "+v)); }

    private DefaultMutableTreeNode buildTreeRoot(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        for(ProductCategory cat: store.CategoryGetCategories()){
            DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(cat);
            buildChildren(catNode, cat); root.add(catNode);
        }
        return root;
    }
    private void buildChildren(DefaultMutableTreeNode parent, ProductComponent comp){
        for(ProductComponent ch: comp.getChildren()){
            DefaultMutableTreeNode n = new DefaultMutableTreeNode(ch);
            parent.add(n); buildChildren(n, ch);
        }
    }
    private static class MenuTreeRenderer extends DefaultTreeCellRenderer{
        @Override public Component getTreeCellRendererComponent(
                JTree t,Object v,boolean s,boolean e,boolean l,int r,boolean f){
            super.getTreeCellRendererComponent(t,v,s,e,l,r,f);
            Object obj=((DefaultMutableTreeNode)v).getUserObject();
            if(obj instanceof ProductCategory c) setText(c.getName());
            else if(obj instanceof ProductComponent p) setText(p.getName()+" ($"+p.getPrice()+")");
            else setText(obj.toString());
            return this;
        }
    }

    private Customer chooseMember(){
        String id = JOptionPane.showInputDialog(this,"會員ID(空白=無)：");
        if(id==null || id.isBlank()) return null;
        return members.computeIfAbsent(id, Customer::new);
    }
    private void refresh(JButton btn){ btn.setText(labelOf(find(btn))); btn.repaint(); }
    private String labelOf(TableContext t){ return "桌 "+t.getTableNo()+"\n["+t.getStateName()+"]"; }
    private TableContext find(JButton btn){
        int idx=((Container)btn.getParent()).getComponentZOrder(btn);
        return tables[idx];
    }
}
