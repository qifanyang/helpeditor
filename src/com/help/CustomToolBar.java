package com.help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.TreePath;

/**
 * 处理打开，保存,.....,的工具条
 *
 * @author qifan.yang
 */
public class CustomToolBar extends JToolBar {

    private static final long serialVersionUID = 1L;
    private EditorPanel editorPanel;

    public CustomToolBar(EditorPanel editorPanel) {
        this.editorPanel = editorPanel;
        setFloatable(false);
        JButton newButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("com/help/res/new_folder.png")));
        JButton openButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("com/help/res/open_folder_32.png")));
        JButton saveButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("com/help/res/save.png")));
        JButton saveAsButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("com/help/res/saveAs.png")));
        // JButton exitButton = new JButton("关闭");
        newButton.setToolTipText("新建");
        openButton.setToolTipText("打开");
        saveButton.setToolTipText("保存");
        saveAsButton.setToolTipText("另存为");
        add(newButton);
        add(openButton);
        add(saveButton);
        add(saveAsButton);
        // add(exitButton);
        openButton.addActionListener(new OpenAction());
        saveAsButton.addActionListener(new SaveAsAction());
        // exitButton.addActionListener(new OpenAction());
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理当前编辑的数据
                CustomTree customTree = DataService.getInstance().getCustomTree();
                TreePath selectionPath = customTree.getSelectionPath();
                if (selectionPath != null) {
                    CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
                    if (lastPathComponent != null) {
                        HelpNode helpNode = customTree.getHelpNode(lastPathComponent);
                        String analysisDocument = DataService.getInstance().analysisDocument();
                        helpNode.setContent(analysisDocument);
                        // DataService.getInstance().showIndex();
                        DataService.getInstance().export(null);// null表示采用默认打开的路径保存，或者保存到默认路径
                    }
                }
            }
        });
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(getParent(), "请输入文档名称！", "新建文档", JOptionPane.OK_CANCEL_OPTION);
                System.out.println("sdf");
                if (result != null && result.length() > 0) {
                    CustomTree customTree = DataService.getInstance().getCustomTree();
                    CustomTreeNode rootNode = customTree.getRootNode();
                    HelpNode helpNode = customTree.getHelpNode(rootNode);
                    //移除旧的数据
                    helpNode.removeAll();
                    rootNode.removeAllChildren();
                    customTree.clearHelpTreeMap();
                    customTree.addHelpNode(rootNode, helpNode);

                    rootNode.setUserObject(result);
                    rootNode.addContentTitle(CustomTreeNode.noFlag);
//		    customTree.getHelpNode(rootNode).removeAll();
                    DataService.getInstance().showIndex();
                    customTree.clearSelection();
                    DataService.getInstance().stopStoreTimer();
                    DataService.getInstance().startStoreTimer();
                    customTree.updateUI();
                }
            }
        });

    }

    // 保存处理，先分析当前编辑区域，再遍历左边的树形结构，生成文件保存
    class SaveAsAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooser.setDialogTitle("另存为");
            // fileChooser.setApproveButtonText("保存");
            // editor.getStyledDocument().getDefaultRootElement();
            if (editorPanel.getEditor().getStyledDocument() == null) {
                System.out.println("editor is NULL ");
            } else {
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().toLowerCase().endsWith(".cfg") || f.getName().toLowerCase().endsWith(".xm") || f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "输入文件名，例如：xx.cfg";
                    }
                });
                int returnVal = fileChooser.showSaveDialog(getParent());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getPath();
                    if (!filePath.toLowerCase().endsWith(".cfg")) {
                        // 处理文件名为
                        filePath = filePath + ".cfg";
                    }

                    System.out.println(filePath);
                    // 处理当前编辑的数据
                    CustomTree customTree = DataService.getInstance().getCustomTree();
                    TreePath selectionPath = customTree.getSelectionPath();
                    if (selectionPath != null) {
                        CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
                        // 不是首页的时候，才分析编辑区域内的内容
                        if (lastPathComponent != null && !DataService.getInstance().isHome()) {
                            HelpNode helpNode = customTree.getHelpNode(lastPathComponent);
                            String analysisDocument = DataService.getInstance().analysisDocument();
                            helpNode.setContent(analysisDocument);
                            // 更新路径
                            // DataService.getInstance().showIndex();
                        }
                    }
                    DataService.getInstance().setPath(filePath);
                    DataService.getInstance().export(filePath);

                    //
                    // ImageIcon(selectedFile.getPath());
                    // try {
                    // ImageIcon imageIcon = new
                    // ImageIcon(ImageIO.read(selectedFile));
                    // imageIcon.setDescription(selectedFile.getPath());
                    // currentEditor.insertIcon(imageIcon);
                    // } catch (IOException e1) {
                    // e1.printStackTrace();
                    // }
                }
            }
        }
    }

    class OpenAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setSelectedFile(new File("sdf.txt"));
            if (editorPanel.getEditor().getStyledDocument() == null) {
                System.out.println("editor is NULL ");
            } else {

                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().toLowerCase().endsWith(".cfg") || f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "文件名，例如：xx.cfg";
                    }
                });
                int returnVal = fileChooser.showOpenDialog(getParent());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getPath();
                    // 打开文件时，更新保存路径，定时保存将采用本路径
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
                        String name2 = selectedFile.getName();
                        rootNode.setUserObject("[文件名:" + name2.substring(0, name2.indexOf(".")) + "]");
                        customTree.updateUI();

                        DataService.getInstance().showIndex();
                        DataService.getInstance().stopStoreTimer();
                        DataService.getInstance().startStoreTimer();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }

}
