package org.example.Frame;

import javax.swing.*;
import java.awt.*;


public abstract class GUIWindow extends JFrame {

    protected CardLayout cardLayout;
    protected JPanel cardPanel;
    protected JMenuBar menuBar;

    private void initialize(){
        setTitle(title());
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font barFont = new Font("微軟正黑體", Font.PLAIN, 20);
        UIManager.put("Menu.font", barFont);
        UIManager.put("MenuItem.font", barFont);
        UIManager.put("CheckBoxMenuItem.font", barFont);
        UIManager.put("RadioButtonMenuItem.font", barFont);

        String[] menuItemNames = getMenuItemNames();
        JMenuItem sellingMenuItem = getMenuItem(menuItemNames[0]);
        JMenuItem receivingMenuItem = getMenuItem(menuItemNames[1]);
        JMenuItem managingMenuItem = getMenuItem(menuItemNames[2]);

        JMenu switchMenu = new JMenu("畫面：" + menuItemNames[0]);
        switchMenu.add(sellingMenuItem);
        switchMenu.add(receivingMenuItem);
        switchMenu.add(managingMenuItem);

        menuBar = new JMenuBar();
        menuBar.add(switchMenu);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel[] panels = getPanels();
        for(int i=0;i<3;i++){
            cardPanel.add(panels[i], menuItemNames[i]);
        }

        add(cardPanel);

        sellingMenuItem.addActionListener(e -> {
            cardLayout.show(cardPanel, menuItemNames[0]);
            switchMenu.setText("畫面：" + menuItemNames[0]);
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
        initialize();
    }

    protected String title(){
        return "GUI介面";
    }

    protected JMenuItem getMenuItem(String name){
        return new JMenuItem(name);
    }

    protected String[] getMenuItemNames(){
        return new String[]{"售貨", "進貨", "管理"};
    }

    abstract protected JPanel[] getPanels();

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
