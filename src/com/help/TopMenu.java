package com.help;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author qifan.yang
 * Date: 11-11-28
 */
public class TopMenu extends JMenuBar {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //    private static final Logger logger = Logger.getLogger(TopMenu.class.getName());
    private JMenu fileMenu;
    private JMenuItem openFileItem;
    private JMenuItem saveFileItem;
    private JMenuItem exitFileItem;
    private JMenu edit;

    private JMenu tools;
    private JMenu languageToolsItem;
    private JMenu lookAndFellToolsItem;
    private JMenuItem zhFileItem;
    private JMenuItem enFileItem;
    private JMenuItem javaItem;
    private JMenuItem windowsItem;

    private JMenu help;
    private JMenuItem aboutSoftWareItem;

    public TopMenu() {
//        ResourceBundle res = ResourceBundle.getBundle("UI");
        //file menu
        fileMenu = new JMenu("文件");
        openFileItem = new JMenuItem("打开");
        saveFileItem = new JMenuItem("保存");
        exitFileItem = new JMenuItem("退出");
        fileMenu.add(openFileItem);
        fileMenu.add(saveFileItem);
        fileMenu.add(exitFileItem);
        openFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                new OpenDialog();
//                Engine.getInstance().setGameIng(true);
//                ((BottomPanel) Engine.getInstance().getPanelManager().getPanelByName("bottomPanel")).enableBtn();
            }
        });
        exitFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Engine.getInstance().dispose();
//                System.exit(0);
            }
        });
        //edit menu
//        edit = new JMenu(res.getString("edit"));
        edit = new JMenu("编辑");

//        tools = new JMenu(res.getString("tools"));
        tools = new JMenu("工具");
        languageToolsItem = new JMenu("语言");
        lookAndFellToolsItem = new JMenu("界面风格");
//        sysCfgToolsItem = new JMenu(res.getString("lookAndFellToolsItem"));
        JMenuItem sysCfgToolsItem = new JMenuItem("系统配置");
        zhFileItem = new JMenuItem("中文");
        enFileItem = new JMenuItem("英文");
        languageToolsItem.add(zhFileItem);
        languageToolsItem.add(enFileItem);

        windowsItem = new JMenuItem("windowsItem");
        javaItem = new JMenuItem("javaItem");
        lookAndFellToolsItem.add(windowsItem);
        lookAndFellToolsItem.add(javaItem);
        tools.add(languageToolsItem);
        tools.add(lookAndFellToolsItem);
        tools.add(sysCfgToolsItem);

        enFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Locale locale = new Locale("en", "US");
//                Engine.getInstance().setLocale(locale);
//                updateI18N();
//                ((BottomPanel) Engine.getInstance().getPanelManager().getPanelByName(PanelType.BottomPanel.toString())).updateI18N();
//                ((IndexPanel) Engine.getInstance().getPanelManager().getPanelByName(PanelType.IndexPanel.toString())).updateI18N();
//                Engine.getInstance().updateI18N();
            }
        });
        zhFileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Locale locale = new Locale("zh", "CN");
//                Engine.getInstance().setLocale(locale);
//                updateI18N();
//                ((BottomPanel) Engine.getInstance().getPanelManager().getPanelByName(PanelType.BottomPanel.toString())).updateI18N();
//                ((IndexPanel) Engine.getInstance().getPanelManager().getPanelByName(PanelType.IndexPanel.toString())).updateI18N();
//                Engine.getInstance().updateI18N();
            }
        });
        windowsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Engine.getInstance().setLookAndFeel("windows");
//                JOptionPane.showMessageDialog(Engine.getInstance(), "椋庢牸璁剧疆灏嗗湪涓嬫鍚姩鏃剁敓鏁堬紒", "娉ㄦ剰", JOptionPane.WARNING_MESSAGE);
            }
        });
        javaItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Engine.getInstance().setLookAndFeel("java");
//                JOptionPane.showMessageDialog(Engine.getInstance(), "椋庢牸璁剧疆灏嗗湪涓嬫鍚姩鏃剁敓鏁堬紒", "娉ㄦ剰", JOptionPane.WARNING_MESSAGE);
            }
        });

        sysCfgToolsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                new SystemConfig();
            }
        });

//        JMenu depot = new JMenu(res.getString("depot"));
        JMenu depot = new JMenu("depot");
        depot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                new OpenDialog();
            }
        });
        help = new JMenu("帮助");
//        help = new JMenu(res.getString("help"));
//        aboutSoftWareItem = new JMenuItem(res.getString("about"));
        aboutSoftWareItem = new JMenuItem("关于");
        aboutSoftWareItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                ResourceBundle resourceBundle = ResourceBundle.getBundle("UI");
//                JOptionPane.showMessageDialog(Engine.getInstance(), resourceBundle.getString("softInfo"), resourceBundle.getString("aboutSoftWare"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help.add(aboutSoftWareItem);

        this.add(fileMenu);
        this.add(edit);
        this.add(tools);
//        this.add(depot);
        this.add(help);

    }

//    public void updateI18N() {
////        ResourceBundle res = ResourceBundle.getBundle("UI", Engine.getInstance().getLocale());
//        fileMenu.setText(res.getString("file"));
//        openFileItem.setText(res.getString("open"));
//        saveFileItem.setText(res.getString("save"));
//        exitFileItem.setText(res.getString("exit"));
//        edit.setText(res.getString("edit"));
//        tools.setText(res.getString("tools"));
//        languageToolsItem.setText(res.getString("languageToolsItem"));
//        zhFileItem.setText(res.getString("zh"));
//        enFileItem.setText(res.getString("en"));
//        lookAndFellToolsItem.setText(res.getString("lookAndFellToolsItem"));
//        windowsItem.setText(res.getString("windowsItem"));
//        javaItem.setText(res.getString("javaItem"));
//        help.setText(res.getString("help"));
//        aboutSoftWareItem.setText(res.getString("about"));
//        fileMenu.setText(res.getString("file"));
//        fileMenu.setText(res.getString("file"));
//    }
}