package com.help;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class CustomTreeNode extends DefaultMutableTreeNode {
	public static final String loadFlag = "loading...";
	public static final String noFlag = "无内容...";

	private static final long serialVersionUID = 1L;
	
	public CustomTreeNode(String name){
		super(name);
	}

	/**
	 * 根据目录名字catalogName，添加目录节点
	 * 
	 * @param catalogName
	 */
	public CustomTreeNode addCatalog(String catalogName) {
		CustomTreeNode depotNode = null;
		if (catalogName != null) {
			if (catalogName.length() == 0) {// JDK6 use result.isEmpty()
				JOptionPane.showMessageDialog((Component) getParent(), "目录名不能为空 ！", "警告", JOptionPane.WARNING_MESSAGE);
			} else {
				// TreePath selectionPath = getSelectionPath();
				// DefaultMutableTreeNode lastPathComponent =
				// (DefaultMutableTreeNode)
				// selectionPath.getLastPathComponent();
				int childCount = this.getChildCount();
				// 遍历 检查是否有重名
				for (int i = 0; i < childCount; i++) {
					TreeNode child = this.getChildAt(i);
					if (child.toString().equalsIgnoreCase(catalogName)) {
						JOptionPane.showMessageDialog((Component) getParent(), "该目录名称已经存在，另命名 ！", "警告", JOptionPane.WARNING_MESSAGE);
						return null;
					}

				}
				checkChildNode(this);
				// 拼接treePath
				Object[] nodePath = getPath();
				StringBuilder path = new StringBuilder();
				for (int i = 0; i < nodePath.length; i++) {
					path.append(nodePath[i].toString()).append("/");
				}
				CustomTreeNode noFlagNode = new CustomTreeNode(noFlag);
				depotNode = new CustomTreeNode(catalogName);
				depotNode.add(noFlagNode);
				this.add(depotNode);
				// updateUI();
			}
		}
		if (depotNode != null) {
			return depotNode;
		} else {
			throw new NullPointerException("创建目录节点失败 ！");
		}
	}

	public CustomTreeNode addContentTitle(String contentTitle) {
		CustomTreeNode depotNode = null;
		if (contentTitle != null) {
			if (contentTitle.length() == 0) {// JDK6 use result.isEmpty()
				JOptionPane.showMessageDialog((Component) getParent(), "正文名不能为空 ！", "警告", JOptionPane.WARNING_MESSAGE);
			} else {
				int childCount = this.getChildCount();
				// 遍历 检查是否有重名
				for (int i = 0; i < childCount; i++) {
					TreeNode child = this.getChildAt(i);
					if (child.toString().equalsIgnoreCase(contentTitle)) {
						JOptionPane.showMessageDialog((Component) getParent(), "该正文名称 【 " + child.toString() + " 】已经存在，另命名 ！", "警告", JOptionPane.WARNING_MESSAGE);
						return null;
					}
				}
				checkChildNode(this);
				// 拼接treePath
				Object[] nodePath = this.getPath();
				StringBuilder path = new StringBuilder();
				for (int i = 0; i < nodePath.length; i++) {
					path.append(nodePath[i].toString()).append("/");
				}
				depotNode = new CustomTreeNode(contentTitle);
				this.add(depotNode);
				// updateUI();
				// 创建对应editor
				// CustomEditor customEditor = new CustomEditor();
				// editorPanel.addTextPaneMap(depotNode, customEditor);

			}
		}
		if (depotNode != null) {
			return depotNode;
		} else {
			throw new NullPointerException("创建目录节点失败 ！");
		}

	}

	/**
	 * 添加新节点时，检查节点node下面的节点，移除noflag标志
	 * 
	 * @param node
	 */
	public void checkChildNode(CustomTreeNode node) {
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode child = node.getChildAt(i);
			if (child.toString().equalsIgnoreCase(CustomTreeNode.noFlag)) {
				node.remove(i);
			}
		}
	}

}
