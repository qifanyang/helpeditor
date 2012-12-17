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
    // public static final String noFlag = "������...";
    private JLabel label;
    private CustomTreeNode rootNode;
    private Map<CustomTreeNode, HelpNode> helpTreeMap = Collections.synchronizedMap(new HashMap<CustomTreeNode, HelpNode>());

    public CustomTree(CustomTreeNode root) {
	super(root);
	addTreeSelectionListener(new HelpTreeSelecListener());
	this.rootNode = root;
	// this.editorPanel = editorPanel;
	// inti node
	// CustomTreeNode root = new CustomTreeNode("����ϵͳ");
	// CustomTreeNode load = new CustomTreeNode(CustomTreeNode.noFlag);
	// root.add(load);

	// ���������˵�
	final JPopupMenu treePopupMenu = new JPopupMenu();
	final JMenuItem addCatalogMenuItem = new JMenuItem("���Ŀ¼");
	final JMenuItem addContentMenuItem = new JMenuItem("�������");
	// final JMenuItem editContentMenuItem = new JMenuItem("�༭����");
	final JMenuItem renameMenuItem = new JMenuItem("������");
	final JMenuItem delMenuItem = new JMenuItem("ɾ��");
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
		    // �����˵����ˣ���Ϊ���ڵ�ʱ�����ɱ༭������������������ɾ��
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

		    // ��Ϊ���Ľڵ�ʱ���������Ŀ¼�������������
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

		    // �����˵����ˣ���Ϊnoflag�ڵ�ʱ,do nothing
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

	// �����ﴦ�����Ŀ¼�¼� �����һ���µ�Ŀ¼�ڵ�
	addCatalogMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String catalogName = JOptionPane.showInputDialog(getParent(), "������Ŀ¼����", "����Ŀ¼", JOptionPane.INFORMATION_MESSAGE);
		if (catalogName != null) {
		    if (catalogName.length() == 0) {// JDK6 use result.isEmpty()
			JOptionPane.showMessageDialog(getParent(), "Ŀ¼������Ϊ�� ��", "����", JOptionPane.WARNING_MESSAGE);
		    } else {
			TreePath selectionPath = getSelectionPath();
			CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
			CustomTreeNode parenTreeNode = (CustomTreeNode) lastPathComponent.getParent();
			CustomTreeNode addCatalogNode = lastPathComponent.addCatalog(catalogName.trim());
			// ���Ŀ¼ʱ��Ҫ���ӳ�䵽map��
			HelpNode helpNode = new HelpNode(catalogName);
			if (parenTreeNode == null) {// �������ڵ�
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

	// �����ﴦ����������¼� �����һ���µ����Ľڵ�
	addContentMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// ��ӽڵ�
		String contentTitle = JOptionPane.showInputDialog(getParent(), "��������������", "��������", JOptionPane.INFORMATION_MESSAGE);
		if (contentTitle != null) {
		    if (contentTitle.length() == 0) {// JDK6 use
			JOptionPane.showMessageDialog(getParent(), "����������Ϊ�� ��", "����", JOptionPane.WARNING_MESSAGE);
		    } else {
			TreePath selectionPath = getSelectionPath();
			CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
			CustomTreeNode parenTreeNode = (CustomTreeNode) lastPathComponent.getParent();
			CustomTreeNode addContentNode = lastPathComponent.addContentTitle(contentTitle.trim());
			// �������ʱ��Ҫ���ӳ�䵽map��
			HelpNode helpNode = new HelpNode(contentTitle);
			if (parenTreeNode == null) {// �������ڵ�
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
		    String newName = JOptionPane.showInputDialog(getParent(), "������������", "������", JOptionPane.INFORMATION_MESSAGE);
		    if (newName != null) {
			if (newName.length() == 0) {// JDK6 use result.isEmpty()
			    JOptionPane.showMessageDialog(getParent(), "���Ʋ���Ϊ�� ��", "����", JOptionPane.WARNING_MESSAGE);
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
		    int result = JOptionPane.showConfirmDialog(getParent(), "ȷ���Ƿ�ɾ����ɾ�������ݲ��ɻָ� ��", "����", JOptionPane.YES_NO_OPTION);
		    if (result == JOptionPane.OK_OPTION) {
			System.out.println("yes del");
			// �Ƴ����νṹ�еĽڵ�
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
     * ����½ڵ�ʱ�����ڵ�node����Ľڵ㣬�Ƴ�noflag��־
     * 
     * @param node
     */
    public void checkChildNode(CustomTreeNode node) {
	// ���� ����Ƿ�������
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
     * �ݹ齫helpNode����Ľڵ��Ӧ��treeNode����
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
     * ���μ����ࣺ���л��ĵ�ʱ�����ݽӵ���Ϣ�ȱ��棬Ȼ���ٴ��µ��ĵ�
     * 
     * 
     * 
     */
    // todo ��Ҫʵ���Զ�����,�ö�ʱ��ʵ��
    class HelpTreeSelecListener implements TreeSelectionListener {

	public void valueChanged(TreeSelectionEvent e) {

	    TreePath oldTreePath = e.getOldLeadSelectionPath();
	    // ���ȱ�����һ���ĵ�����������ҳ
	    if (oldTreePath != null && !DataService.getInstance().isHome()) {
		CustomTreeNode oldNode = (CustomTreeNode) oldTreePath.getLastPathComponent();
		HelpNode helpNode = getHelpNode(oldNode);
		// �����༭����������ݣ�����HelpNode����ļ�¼
		String analysisResult = DataService.getInstance().analysisDocument();
		if (analysisResult != null && helpNode != null && !analysisResult.startsWith("����������������.....")) {
		    helpNode.setContent(analysisResult);
		}
	    }
	    // ��ʾ�µ��ĵ�����������ڵ���ʾ��ҳ
	    CustomTreeNode lastPathComponent = (CustomTreeNode) e.getPath().getLastPathComponent();
	    if (lastPathComponent != null) {
		if (lastPathComponent.toString().equalsIgnoreCase(CustomTreeNode.noFlag)) {
		    JOptionPane.showMessageDialog(getParent(), "�ýڵ㲻�ɱ༭����ֻ��һ�������ݱ�ʶ�����ᱣ�� ��", "����", JOptionPane.WARNING_MESSAGE);
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
    // CustomTreeNode root = new CustomTreeNode("����ϵͳ");
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
