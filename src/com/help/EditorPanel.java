package com.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class EditorPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private CustomTree customTree;
    private JTextPane editor;

    public EditorPanel() {
        setLayout(new BorderLayout());
        // 创建编辑面板区域
        editor = new JTextPane();
        editor.setCaretColor(Color.white);
        editor.setBackground(Utils.BACKGROUD_COLOR);
        DataService.getInstance().setEditor(editor);
        DataService.getInstance().showIndex();

        // 创建编辑控制面板
        JPanel editorControlPanel = new CustomControlPanel(editor);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(new JScrollPane(editor), BorderLayout.CENTER);
        rightPanel.add(editorControlPanel, BorderLayout.NORTH);

        // 初始化左边的树形结构
        CustomTreeNode root = new CustomTreeNode("帮助系统");
        CustomTreeNode load = new CustomTreeNode(CustomTreeNode.noFlag);
        root.add(load);
        customTree = new CustomTree(root);
        customTree.addHelpNode(root, new HelpNode("帮助系统"));
        // 树的根节点对应数据树的根节点
        // customTree.setCurrentPane(editor);
        DataService.getInstance().setCustomTree(customTree);
        JScrollPane treeScrollPanel = new JScrollPane(customTree);
        treeScrollPanel.setPreferredSize(new Dimension(HelpEditor.WIDTH / 5, HelpEditor.HEIGHT));

        // TODO TEST 默认加载help.xml
		/*try {
			HelpNode helpRootNode = DataService.getInstance().load("help.xml");
			CustomTreeNode rootNode = customTree.getRootNode();
			customTree.addHelpNode(rootNode, helpRootNode);// 根节点映射对应
			rootNode.removeAllChildren();
			customTree.updateAllNode(helpRootNode, rootNode);
			customTree.updateUI();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

        add(treeScrollPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        DataService.getInstance().startStoreTimer();

        // 添加一些事件处理
        // 光标停在编辑区域内
        editor.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                // MutableAttributeSet attr = new SimpleAttributeSet();
                // StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
                // editor.setCharacterAttributes(attr, false);
            }

        });
    }

    public JTextPane getEditor() {
        return editor;
    }

    // public CustomTree getCustomTree() {
    // return customTree;
    // }

}
