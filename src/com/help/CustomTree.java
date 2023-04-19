package com.help;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomTree extends JTree {
    private static final long serialVersionUID = 1L;
    // public static final String loadFlag = "loading...";
    // public static final String noFlag = "无内容...";
    private JLabel label;
    private CustomTreeNode rootNode;
    private Map<CustomTreeNode, HelpNode> helpTreeMap = Collections.synchronizedMap(new HashMap<CustomTreeNode, HelpNode>());

    public CustomTree(CustomTreeNode root) {
	super(root);
	addTreeSelectionListener(new HelpTreeSelecListener());
	this.rootNode = root;
	// this.editorPanel = editorPanel;
	// inti node
	// CustomTreeNode root = new CustomTreeNode("帮助系统");
	// CustomTreeNode load = new CustomTreeNode(CustomTreeNode.noFlag);
	// root.add(load);

	// 创建弹出菜单
	final JPopupMenu treePopupMenu = new JPopupMenu();
	final JMenuItem addCatalogMenuItem = new JMenuItem("添加目录");
	final JMenuItem addContentMenuItem = new JMenuItem("添加正文");
	// final JMenuItem editContentMenuItem = new JMenuItem("编辑正文");
	final JMenuItem renameMenuItem = new JMenuItem("重命名");
	final JMenuItem delMenuItem = new JMenuItem("删除");
	treePopupMenu.setInvoker(this);
	treePopupMenu.add(addCatalogMenuItem);
	// treePopupMenu.add(new JPopupMenu.Separator());
	treePopupMenu.add(addContentMenuItem);
	treePopupMenu.add(new JPopupMenu.Separator());
	// treePopupMenu.add(editContentMenuItem);
	// treePopupMenu.add(new JPopupMenu.Separator());
	treePopupMenu.add(renameMenuItem);
	treePopupMenu.add(new JPopupMenu.Separator());
	treePopupMenu.add(delMenuItem);
	treePopupMenu.setBorderPainted(true);
	this.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
		    int x = e.getX();
		    int y = e.getY();
		    TreePath closestPathForLocation = CustomTree.this.getClosestPathForLocation(x, y);
		    // if
		    // (!closestPathForLocation.getLastPathComponent().toString().equalsIgnoreCase(noFlag))
		    // {
		    CustomTree.this.setSelectionPath(closestPathForLocation);
		    // }
		    CustomTreeNode lastPathComponent = (CustomTreeNode) closestPathForLocation.getLastPathComponent();

		    addCatalogMenuItem.setEnabled(true);
			addContentMenuItem.setEnabled(true);
			renameMenuItem.setEnabled(true);
			delMenuItem.setEnabled(true);
		    // 弹出菜单过滤，当为根节点时，不可编辑，不可重命名，不可删除
		    if (lastPathComponent.getParent() == null) {
			// editContentMenuItem.setEnabled(false);
			renameMenuItem.setEnabled(false);
			delMenuItem.setEnabled(false);
			addCatalogMenuItem.setEnabled(true);
			addContentMenuItem.setEnabled(true);
		    } else {
			// editContentMenuItem.setEnabled(true);
			renameMenuItem.setEnabled(true);
			delMenuItem.setEnabled(true);
		    }

		    // 当为正文节点时，不可添加目录，不可添加正文
//		    if (lastPathComponent.getChildCount() <= 0 && !lastPathComponent.toString().equalsIgnoreCase(CustomTreeNode.noFlag)) {
//			addCatalogMenuItem.setEnabled(false);
//			addContentMenuItem.setEnabled(false);
//			// editContentMenuItem.setEnabled(true);
//			renameMenuItem.setEnabled(true);
//			delMenuItem.setEnabled(true);
//		    } else {
//			addCatalogMenuItem.setEnabled(true);
//			addContentMenuItem.setEnabled(true);
//		    }

		    // 弹出菜单过滤，当为noflag节点时,do nothing
		    if (lastPathComponent.toString().equalsIgnoreCase(CustomTreeNode.noFlag)) {
			addCatalogMenuItem.setEnabled(false);
			addContentMenuItem.setEnabled(false);
			// editContentMenuItem.setEnabled(false);
			renameMenuItem.setEnabled(false);
			delMenuItem.setEnabled(false);
		    }

		    // treePopupMenu.setLocation(e.getLocationOnScreen());//JDK5
		    // no method e.getLocationOnScreen()
		    Point windowPos = CustomTree.this.getParent().getLocationOnScreen();
		    treePopupMenu.setLocation((int) windowPos.getX() + e.getX(), (int) windowPos.getY() + e.getY());
		    treePopupMenu.setVisible(true);
		}
	    }
	});

	// 在这里处理添加目录事件 ，添加一个新的目录节点
	addCatalogMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String catalogName = JOptionPane.showInputDialog(getParent(), "请输入目录名称", "新增目录", JOptionPane.INFORMATION_MESSAGE);
		if (catalogName != null) {
		    if (catalogName.length() == 0) {// JDK6 use result.isEmpty()
			JOptionPane.showMessageDialog(getParent(), "目录名不能为空 ！", "警告", JOptionPane.WARNING_MESSAGE);
		    } else {
			TreePath selectionPath = getSelectionPath();
			CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
			CustomTreeNode parenTreeNode = (CustomTreeNode) lastPathComponent.getParent();
			CustomTreeNode addCatalogNode = lastPathComponent.addCatalog(catalogName.trim());
			// 添加目录时需要添加映射到map中
			HelpNode helpNode = new HelpNode(catalogName);
			if (parenTreeNode == null) {// 是树根节点
			    HelpNode helpRootNode = getHelpNode(lastPathComponent);
			    helpRootNode.addNode(helpNode);
			} else {
			    HelpNode helpparentNode = getHelpNode(parenTreeNode);
			    helpparentNode.addNode(helpNode);
			}
			addHelpNode(addCatalogNode, helpNode);
		    }
		}
		updateUI();
	    }
	});

	// 在这里处理添加正文事件 ，添加一个新的正文节点
	addContentMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// 添加节点
		String contentTitle = JOptionPane.showInputDialog(getParent(), "请输入正文名称", "新增正文", JOptionPane.INFORMATION_MESSAGE);
		if (contentTitle != null) {
		    if (contentTitle.length() == 0) {// JDK6 use
			JOptionPane.showMessageDialog(getParent(), "正文名不能为空 ！", "警告", JOptionPane.WARNING_MESSAGE);
		    } else {
			TreePath selectionPath = getSelectionPath();
			CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
			CustomTreeNode parenTreeNode = (CustomTreeNode) lastPathComponent.getParent();
			CustomTreeNode addContentNode = lastPathComponent.addContentTitle(contentTitle.trim());
			// 添加正文时需要添加映射到map中
			HelpNode helpNode = new HelpNode(contentTitle);
			if (parenTreeNode == null) {// 是树根节点
			    HelpNode helpRootNode = getHelpNode(lastPathComponent);
			    helpRootNode.addNode(helpNode);

			} else {
			    HelpNode helpparentNode = getHelpNode(parenTreeNode);
			    helpparentNode.addNode(helpNode);
			}
			addHelpNode(addContentNode, helpNode);
		    }
		}
		updateUI();
	    }
	});
	// rename node
	renameMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		CustomTreeNode lastPathComponent = (CustomTreeNode) getSelectionPath().getLastPathComponent();
		if (lastPathComponent != null) {
		    String newName = JOptionPane.showInputDialog(getParent(), "请输入新名称", "重命名", JOptionPane.INFORMATION_MESSAGE);
		    if (newName != null) {
			if (newName.length() == 0) {// JDK6 use result.isEmpty()
			    JOptionPane.showMessageDialog(getParent(), "名称不能为空 ！", "警告", JOptionPane.WARNING_MESSAGE);
			} else {
			    // if (lastPathComponent.isLeaf()) {
			    lastPathComponent.setUserObject(newName);
			    getHelpNode(lastPathComponent).setName(newName);
			    updateUI();
			    // }
			}
		    }
		}
	    }
	});

	// delItem
	delMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		TreePath selectionPath = getSelectionPath();
		if (selectionPath != null) {
		    CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
		    CustomTreeNode parent = (CustomTreeNode) lastPathComponent.getParent();
		    int result = JOptionPane.showConfirmDialog(getParent(), "确认是否删除，删除后数据不可恢复 ？", "警告", JOptionPane.YES_NO_OPTION);
		    if (result == JOptionPane.OK_OPTION) {
			System.out.println("yes del");
			// 移除树形结构中的节点
			HelpNode childrenHelpNode = getHelpNode(lastPathComponent);
			HelpNode parentHelpNode = getHelpNode(parent);
			parentHelpNode.removeNode(childrenHelpNode);
			parent.remove(lastPathComponent);
			if (parent.getChildCount() <= 0) {
			    parent.add(new CustomTreeNode(CustomTreeNode.noFlag));
			}
			updateUI();
		    } else {
			System.out.println("no del");
		    }
		}

	    }
	});
    }

    /**
     * 添加新节点时，检查节点node下面的节点，移除noflag标志
     * 
     * @param node
     */
    public void checkChildNode(CustomTreeNode node) {
	// 遍历 检查是否有重名
	for (int i = 0; i < node.getChildCount(); i++) {
	    TreeNode child = node.getChildAt(i);
	    if (child.toString().equalsIgnoreCase(CustomTreeNode.noFlag)) {
		node.remove(i);
	    }
	}
    }

    public CustomTreeNode getRootNode() {
	return rootNode;
    }

    // public void setRootNode(CustomTreeNode rootNode) {
    // this.rootNode = rootNode;
    // }

    public void treeExpanded(TreeExpansionEvent event) {
	label.setIcon(((JLabel) cellRenderer).getIcon());
	label.setText("dsfgfdg");
	label.updateUI();
    }

    public HelpNode getHelpNode(CustomTreeNode customTreeNode) {
	return helpTreeMap.get(customTreeNode);
    }

    public void addHelpNode(CustomTreeNode customTreeNode, HelpNode helpNode) {
	helpTreeMap.put(customTreeNode, helpNode);
    }

    public void clearHelpTreeMap() {
	helpTreeMap.clear();
    }

    /**
     * 递归将helpNode下面的节点对应到treeNode下面
     * 
     * @param helpNode
     * @param treeNode
     */
    public void updateAllNode(HelpNode helpNode, CustomTreeNode treeNode) {
	ArrayList<HelpNode> childrenNode = helpNode.getChildrenNode();
	for (int i = 0; i < childrenNode.size(); i++) {
	    HelpNode node = childrenNode.get(i);
	    if (node.getChildrenNode().size() > 0) {
		// CustomTreeNode catalogNode = addCatalog(node.getName());
		CustomTreeNode catalogNode = treeNode.addCatalog(node.getName());
		helpTreeMap.put(catalogNode, node);
		updateAllNode(node, catalogNode);
	    } else {
		CustomTreeNode contentNode = treeNode.addContentTitle(node.getName());
		helpTreeMap.put(contentNode, node);
		// treeNode.add(contentNode);
	    }
	}

    }

    /**
     * 
     * 树形监听类：当切换文档时，根据接点信息先保存，然后再打开新的文档
     * 
     * 
     * 
     */
    // todo 需要实现自动保存,用定时器实现
    class HelpTreeSelecListener implements TreeSelectionListener {

	public void valueChanged(TreeSelectionEvent e) {

	    TreePath oldTreePath = e.getOldLeadSelectionPath();
	    // 首先保存上一个文档，不保存首页
	    if (oldTreePath != null && !DataService.getInstance().isHome()) {
		CustomTreeNode oldNode = (CustomTreeNode) oldTreePath.getLastPathComponent();
		HelpNode helpNode = getHelpNode(oldNode);
		// 分析编辑器里面的内容，更新HelpNode里面的记录
		String analysisResult = DataService.getInstance().analysisDocument();
		if (analysisResult != null && helpNode != null && !analysisResult.startsWith("请在这里输入正文.....")) {
		    helpNode.setContent(analysisResult);
		}
	    }
	    // 显示新的文档，当点击根节点显示首页
	    CustomTreeNode lastPathComponent = (CustomTreeNode) e.getPath().getLastPathComponent();
	    if (lastPathComponent != null) {
		if (lastPathComponent.toString().equalsIgnoreCase(CustomTreeNode.noFlag)) {
		    JOptionPane.showMessageDialog(getParent(), "该节点不可编辑，这只是一个无内容标识，不会保存 ！", "警告", JOptionPane.WARNING_MESSAGE);
		    Object[] path = e.getPath().getPath();
		    ArrayList<Object> arrayList = null;
		    for (int i = 0; i < path.length - 1; i++) {
			arrayList = new ArrayList<Object>();
			arrayList.add(path[i]);
		    }
		    setSelectionPath(new TreePath(arrayList.toArray()));
		    return;
		} else if (lastPathComponent == rootNode) {
		    DataService.getInstance().showIndex();
		    DataService.getInstance().setHome(true);
		} else {
		    DataService.getInstance().setHome(false);
		    HelpNode helpNode = getHelpNode(lastPathComponent);
		    if (helpNode != null) {
			try {
			    // TextAnalysisKit.getInstance().showCotent(currentPane,
			    // helpNode);
			    DataService.getInstance().showCotent(helpNode);
			} catch (BadLocationException e2) {
			    e2.printStackTrace();
			}
		    }
		}
	    }
	}
    }

    //
    // public void setCurrentPane(JTextPane currentPane) {
    // this.currentPane = currentPane;
    // }
    // Test
    // private JTextPane currentPane;
    //
    // public static void main(String[] args) throws ClassNotFoundException,
    // InstantiationException, IllegalAccessException,
    // UnsupportedLookAndFeelException, IOException {
    // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    // JFrame testFrame = new JFrame();
    // testFrame.setTitle("Custom Tree");
    // // testFrame.setLocationRelativeTo(null);
    // testFrame.setSize(1000, 800);
    // testFrame.setLayout(new BorderLayout());
    // testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //
    // JTextPane textPane = new JTextPane();
    // textPane.setBackground(Utils.BACKGROUD_COLOR);
    //
    // CustomTreeNode root = new CustomTreeNode("帮助系统");
    // CustomTreeNode load = new CustomTreeNode(CustomTreeNode.noFlag);
    // root.add(load);
    // CustomTree myTree = new CustomTree(root);
    //
    // myTree.setCurrentPane(textPane);
    // HelpNode helpNode = DataService.getInstance().load("help.xml");
    // CustomTreeNode rootNode = myTree.getRootNode();
    // HelpNode helpRootNode = helpNode.getChildrenNode().get(0);
    // rootNode.removeAllChildren();
    // myTree.updateAllNode(helpRootNode, rootNode);
    // myTree.updateUI();
    //
    // // JLabel testLabel = new JLabel("yes");
    // // myTree.setLabel(testLabel);
    // testFrame.add(new JScrollPane(myTree), BorderLayout.WEST);
    // testFrame.add(textPane, BorderLayout.CENTER);
    // // myTree.addTreeExpansionListener(myTree);
    //
    // testFrame.setVisible(true);

    // }
}
