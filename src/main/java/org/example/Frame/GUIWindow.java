package org.example.Frame;

import javax.swing.*;
import java.awt.*;


public abstract class GUIWindow extends JFrame {

    protected CardLayout cardLayout;
    protected JPanel cardPanel;
    protected JMenuBar menuBar;

    protected void initialize(){
        setTitle(title()); // 從可被複寫的函式設定標題
        setSize(1000, 500); // 設定長寬
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 關閉視窗時關閉程式
        setLocationRelativeTo(null); // 讓視窗出現在中間

        // 設定Menu相關字體
        Font barFont = new Font("微軟正黑體", Font.PLAIN, 20);
        UIManager.put("Menu.font", barFont);
        UIManager.put("MenuItem.font", barFont);
        UIManager.put("CheckBoxMenuItem.font", barFont);
        UIManager.put("RadioButtonMenuItem.font", barFont);

        // 從可被複寫的函式取得MenuItem的名字
        String[] menuItemNames = getMenuItemNames();
        JMenuItem sellingMenuItem = getMenuItem(menuItemNames[0]);
        JMenuItem receivingMenuItem = getMenuItem(menuItemNames[1]);
        JMenuItem managingMenuItem = getMenuItem(menuItemNames[2]);

        // 將切換畫面的MenuItem加進一個Menu
        JMenu switchMenu = new JMenu("畫面：" + menuItemNames[0]);
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
        JPanel[] panels = getPanels();
        for(int i=0;i<3;i++){
            cardPanel.add(panels[i], menuItemNames[i]); // 第二個參數是當切換畫面時定位用的
        }

        add(cardPanel);

        sellingMenuItem.addActionListener(e -> {
            cardLayout.show(cardPanel, menuItemNames[0]); // 切換畫面，切換的畫面是根據cardPanel.add()的第二個參數
            switchMenu.setText("畫面：" + menuItemNames[0]); // 同時切換MenuBar上面的字
        });
        receivingMenuItem.addActionListener(e -> {
            cardLayout.show(cardPanel, menuItemNames[1]);
            switchMenu.setText("畫面：" + menuItemNames[1]);
        });
        managingMenuItem.addActionListener(e -> {
            cardLayout.show(cardPanel, menuItemNames[2]);
            switchMenu.setText("畫面：" + menuItemNames[2]);
        });
    }

    public GUIWindow(){
        initialize(); // 設定畫面的建構子
    }

    protected String title(){
        return "GUI介面"; // 可被複寫的取得標題方法
    }

    protected JMenuItem getMenuItem(String name){
        return new JMenuItem(name); // 如果有要特殊的MenuItem，複寫此方法
    }

    protected String[] getMenuItemNames(){
        return new String[]{"售貨", "進貨", "管理"}; // 可被複寫的畫面的Menu名字
    }

    abstract protected JPanel[] getPanels(); // 取得三個畫面，需實作此方法

    public void run(){
        setVisible(true);
    }

    public static void main(String[] args){
        GUIWindow exampleWindow = new GUIWindow() {
            @Override
            protected JPanel[] getPanels() {
                Font ctFont = new Font("微軟正黑體", Font.PLAIN, 20);

                JPanel panel1 = new JPanel();
                panel1.setBackground(Color.BLUE);
                JLabel sellingLabel = new JLabel("售貨畫面");
                sellingLabel.setFont(ctFont);
                sellingLabel.setForeground(Color.WHITE);
                panel1.add(sellingLabel);

                JPanel panel2 = new JPanel();
                panel2.setBackground(Color.YELLOW);
                JLabel receivingLabel = new JLabel("進貨畫面");
                receivingLabel.setFont(ctFont);
                panel2.add(receivingLabel);

                JPanel panel3 = new JPanel();
                panel3.setBackground(Color.PINK);
                JLabel managingLabel = new JLabel("管理畫面");
                managingLabel.setFont(ctFont);
                panel3.add(managingLabel);

                return new JPanel[]{panel1, panel2, panel3};
            }
        };
        exampleWindow.run();
    }
}
