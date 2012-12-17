package com.help;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class CustomTreeNode extends DefaultMutableTreeNode {
	public static final String loadFlag = "loading...";
	public static final String noFlag = "������...";

	private static final long serialVersionUID = 1L;
	
	public CustomTreeNode(String name){
		super(name);
	}

	/**
	 * ����Ŀ¼����catalogName�����Ŀ¼�ڵ�
	 * 
	 * @param catalogName
	 */
	public CustomTreeNode addCatalog(String catalogName) {
		CustomTreeNode depotNode = null;
		if (catalogName != null) {
			if (catalogName.length() == 0) {// JDK6 use result.isEmpty()
				JOptionPane.showMessageDialog((Component) getParent(), "Ŀ¼������Ϊ�� ��", "����", JOptionPane.WARNING_MESSAGE);
			} else {
				// TreePath selectionPath = getSelectionPath();
				// DefaultMutableTreeNode lastPathComponent =
				// (DefaultMutableTreeNode)
				// selectionPath.getLastPathComponent();
				int childCount = this.getChildCount();
				// ���� ����Ƿ�������
				for (int i = 0; i < childCount; i++) {
					TreeNode child = this.getChildAt(i);
					if (child.toString().equalsIgnoreCase(catalogName)) {
						JOptionPane.showMessageDialog((Component) getParent(), "��Ŀ¼�����Ѿ����ڣ������� ��", "����", JOptionPane.WARNING_MESSAGE);
						return null;
					}

				}
				checkChildNode(this);
				// ƴ��treePath
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
			throw new NullPointerException("����Ŀ¼�ڵ�ʧ�� ��");
		}
	}

	public CustomTreeNode addContentTitle(String contentTitle) {
		CustomTreeNode depotNode = null;
		if (contentTitle != null) {
			if (contentTitle.length() == 0) {// JDK6 use result.isEmpty()
				JOptionPane.showMessageDialog((Component) getParent(), "����������Ϊ�� ��", "����", JOptionPane.WARNING_MESSAGE);
			} else {
				int childCount = this.getChildCount();
				// ���� ����Ƿ�������
				for (int i = 0; i < childCount; i++) {
					TreeNode child = this.getChildAt(i);
					if (child.toString().equalsIgnoreCase(contentTitle)) {
						JOptionPane.showMessageDialog((Component) getParent(), "���������� �� " + child.toString() + " ���Ѿ����ڣ������� ��", "����", JOptionPane.WARNING_MESSAGE);
						return null;
					}
				}
				checkChildNode(this);
				// ƴ��treePath
				Object[] nodePath = this.getPath();
				StringBuilder path = new StringBuilder();
				for (int i = 0; i < nodePath.length; i++) {
					path.append(nodePath[i].toString()).append("/");
				}
				depotNode = new CustomTreeNode(contentTitle);
				this.add(depotNode);
				// updateUI();
				// ������Ӧeditor
				// CustomEditor customEditor = new CustomEditor();
				// editorPanel.addTextPaneMap(depotNode, customEditor);

			}
		}
		if (depotNode != null) {
			return depotNode;
		} else {
			throw new NullPointerException("����Ŀ¼�ڵ�ʧ�� ��");
		}

	}

	/**
	 * ����½ڵ�ʱ�����ڵ�node����Ľڵ㣬�Ƴ�noflag��־
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
