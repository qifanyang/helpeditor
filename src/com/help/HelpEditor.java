package com.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 帮助系统编辑器
 *
 * @author qifan.yang
 */
public class HelpEditor extends JFrame {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;

    public static BufferedImage icon;
    public static HelpEditor instancEditor;

    private static final long serialVersionUID = 1L;

    public HelpEditor() {

    }

    private void initUI() {
        try {
            // icon =
            // ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/help/seasky32.png"));
            icon = ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/help/res/long.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // icon = this.getToolkit().getImage("seasky16.png");
        setIconImage(icon);
        setTitle("帮助系统编辑器");
        setLayout(new BorderLayout());

        // create editor panel
        EditorPanel editorPanel = new EditorPanel();

//	setJMenuBar(new TopMenu());
        JToolBar toolBar = new CustomToolBar(editorPanel);

        add(toolBar, BorderLayout.PAGE_START);
        add(editorPanel, BorderLayout.CENTER);
        setSize(new Dimension(WIDTH, HEIGHT));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instancEditor = this;


    }

    public static HelpEditor getIntance() {
        return instancEditor;
    }

    public static void main(String[] dd) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        HelpEditor helpEditor = new HelpEditor();
        helpEditor.initUI();
        helpEditor.openDefaultFile();
    }

    private void openDefaultFile() {
        String filePath = System.getProperty("user.dir") + "/src/help.cfg";
        DataService.getInstance().setPath(filePath);
        // if (!filePath.toLowerCase().endsWith(".cfg")) {
        // // 处理文件名为
        // filePath = filePath + ".cfg";
        // }

        System.out.println(filePath);
        // DataService.getInstance().export(textPaneMap, customTree,
        // filePath);
        try {
            // 返回的是helpRootNode，为根节点，例如内容为：帮助系统
            CustomTree customTree = DataService.getInstance().getCustomTree();
            CustomTreeNode rootNode = customTree.getRootNode();
            HelpNode helpRootNode = customTree.getHelpNode(rootNode);
            //移除旧的数据
            helpRootNode.removeAll();
            rootNode.removeAllChildren();
            customTree.clearHelpTreeMap();

            customTree.addHelpNode(rootNode, helpRootNode);
            //load需要根节点。所以需要上面的处理
            HelpNode newRootNode = DataService.getInstance().load(filePath);
            customTree.clearHelpTreeMap();
            customTree.addHelpNode(rootNode, newRootNode);
            // HelpNode helpNode =
            // helpRootNode.getChildrenNode().get(0);// <help1>

            customTree.updateAllNode(helpRootNode, rootNode);
            String name2 = "help.cfg";
            rootNode.setUserObject("[文件名:" + name2.substring(0, name2.indexOf(".")) + "]");
            customTree.updateUI();

            DataService.getInstance().showIndex();
            DataService.getInstance().stopStoreTimer();
            DataService.getInstance().startStoreTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
