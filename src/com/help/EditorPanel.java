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
		// �����༭�������
		editor = new JTextPane();
		editor.setCaretColor(Color.white);
		editor.setBackground(Utils.BACKGROUD_COLOR);
		DataService.getInstance().setEditor(editor);
		DataService.getInstance().showIndex();

		// �����༭�������
		JPanel editorControlPanel = new CustomControlPanel(editor);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(new JScrollPane(editor), BorderLayout.CENTER);
		rightPanel.add(editorControlPanel, BorderLayout.NORTH);

		// ��ʼ����ߵ����νṹ
		CustomTreeNode root = new CustomTreeNode("����ϵͳ");
		CustomTreeNode load = new CustomTreeNode(CustomTreeNode.noFlag);
		root.add(load);
		customTree = new CustomTree(root);
		customTree.addHelpNode(root, new HelpNode("����ϵͳ"));
		// ���ĸ��ڵ��Ӧ�������ĸ��ڵ�
		// customTree.setCurrentPane(editor);
		DataService.getInstance().setCustomTree(customTree);
		JScrollPane treeScrollPanel = new JScrollPane(customTree);
		treeScrollPanel.setPreferredSize(new Dimension(HelpEditor.WIDTH / 5, HelpEditor.HEIGHT));

		// TODO TEST Ĭ�ϼ���help.xml
		/*try {
			HelpNode helpRootNode = DataService.getInstance().load("help.xml");
			CustomTreeNode rootNode = customTree.getRootNode();
			customTree.addHelpNode(rootNode, helpRootNode);// ���ڵ�ӳ���Ӧ
			rootNode.removeAllChildren();
			customTree.updateAllNode(helpRootNode, rootNode);
			customTree.updateUI();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		add(treeScrollPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.CENTER);

		DataService.getInstance().startStoreTimer();

		// ���һЩ�¼�����
		// ���ͣ�ڱ༭������
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
