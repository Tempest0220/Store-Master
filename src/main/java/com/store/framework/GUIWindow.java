package com.store.framework;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public abstract class GUIWindow extends JFrame {

    protected ArrayList<Store> stores = new ArrayList<>();
    protected Store store;
    protected CardLayout cardLayout;
    protected JPanel cardPanel;
    protected JMenuBar menuBar;

    protected static final Font DEFAULT_FONT = new Font("微軟正黑體", Font.PLAIN, 20);

    protected void initialize(){
        setTitle(title()); // 從可被複寫的函式設定標題
        setSize(1000, 500); // 設定長寬
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 關閉視窗時關閉程式
        setLocationRelativeTo(null); // 讓視窗出現在中間

        // 設定Menu相關字體
        UIManager.put("Menu.font", DEFAULT_FONT);
        UIManager.put("MenuItem.font", DEFAULT_FONT);
        UIManager.put("CheckBoxMenuItem.font", DEFAULT_FONT);
        UIManager.put("RadioButtonMenuItem.font", DEFAULT_FONT);

        // 從可被複寫的函式取得MenuItem的名字
        String sellingName = getSalesName();
        String receivingName = getReceivingName();
        String managementName = getManagementName();
        JMenuItem sellingMenuItem = getMenuItem(sellingName);
        JMenuItem receivingMenuItem = getMenuItem(receivingName);
        JMenuItem managingMenuItem = getMenuItem(managementName);

        // 將切換畫面的MenuItem加進一個Menu
        JMenu switchMenu = new JMenu("畫面：" + sellingName);
        switchMenu.add(sellingMenuItem);
        switchMenu.add(receivingMenuItem);
        switchMenu.add(managingMenuItem);

        // 把設定好的Menu加進MenuBar裡
        menuBar = new JMenuBar();
        menuBar.add(switchMenu);
        setJMenuBar(menuBar);

        // 使用cardLayout製作可切換的panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 從抽象的方法取得panel，加入cardPanel內

        cardPanel.add(getSalesPanel(), getSalesName());
        cardPanel.add(getReceivingPanel(), getReceivingName());
        cardPanel.add(getManagementPanel(), getManagementName());

        add(cardPanel);

        sellingMenuItem.addActionListener(e -> {
            cardLayout.show(cardPanel, sellingName); // 切換畫面，切換的畫面是根據cardPanel.add()的第二個參數
            switchMenu.setText("畫面：" + sellingName); // 同時切換MenuBar上面的字
        });
        receivingMenuItem.addActionListener(e -> {
            cardLayout.show(cardPanel, receivingName);
            switchMenu.setText("畫面：" + receivingName);
        });
        managingMenuItem.addActionListener(e -> {
            cardLayout.show(cardPanel, managementName);
            switchMenu.setText("畫面：" + managementName);
        });
    }

    public GUIWindow(Store store){
        this.store = store;
        initialize(); // 設定畫面的建構子
    }

    protected String title(){
        return "GUI介面"; // 可被複寫的取得標題方法
    }

    protected JMenuItem getMenuItem(String name){
        return new JMenuItem(name); // 如果有要特殊的MenuItem，複寫此方法
    }

    protected String getSalesName(){ return "售貨";}
    protected String getReceivingName(){ return "進貨";}
    protected String getManagementName(){ return "管理";}

    abstract protected JPanel getSalesPanel();
    abstract protected JPanel getReceivingPanel();
    abstract protected JPanel getManagementPanel();

    public void run(){
        setVisible(true);
    }

}

