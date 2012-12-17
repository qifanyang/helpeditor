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
 * ����򿪣�����,.....,�Ĺ�����
 * 
 * @author qifan.yang
 * 
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
	// JButton exitButton = new JButton("�ر�");
	newButton.setToolTipText("�½�");
	openButton.setToolTipText("��");
	saveButton.setToolTipText("����");
	saveAsButton.setToolTipText("���Ϊ");
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
		// ����ǰ�༭������
		CustomTree customTree = DataService.getInstance().getCustomTree();
		TreePath selectionPath = customTree.getSelectionPath();
		if (selectionPath != null) {
		    CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
		    if (lastPathComponent != null) {
			HelpNode helpNode = customTree.getHelpNode(lastPathComponent);
			String analysisDocument = DataService.getInstance().analysisDocument();
			helpNode.setContent(analysisDocument);
			// DataService.getInstance().showIndex();
			DataService.getInstance().export(null);// null��ʾ����Ĭ�ϴ򿪵�·�����棬���߱��浽Ĭ��·��
		    }
		}
	    }
	});
	newButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		String result = JOptionPane.showInputDialog(getParent(), "�������ĵ����ƣ�", "�½��ĵ�", JOptionPane.OK_CANCEL_OPTION);
		System.out.println("sdf");
		if (result != null && result.length() > 0) {
		    CustomTree customTree = DataService.getInstance().getCustomTree();
		    CustomTreeNode rootNode = customTree.getRootNode();
		    HelpNode helpNode = customTree.getHelpNode(rootNode);
		    //�Ƴ��ɵ�����
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

    // ���洦���ȷ�����ǰ�༭�����ٱ�����ߵ����νṹ�������ļ�����
    class SaveAsAction implements ActionListener {

	public void actionPerformed(ActionEvent e) {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
	    fileChooser.setDialogTitle("���Ϊ");
	    // fileChooser.setApproveButtonText("����");
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
			return "�����ļ��������磺xx.cfg";
		    }
		});
		int returnVal = fileChooser.showSaveDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    String filePath = selectedFile.getPath();
		    if (!filePath.toLowerCase().endsWith(".cfg")) {
			// �����ļ���Ϊ
			filePath = filePath + ".cfg";
		    }

		    System.out.println(filePath);
		    // ����ǰ�༭������
		    CustomTree customTree = DataService.getInstance().getCustomTree();
		    TreePath selectionPath = customTree.getSelectionPath();
		    if (selectionPath != null) {
			CustomTreeNode lastPathComponent = (CustomTreeNode) selectionPath.getLastPathComponent();
			// ������ҳ��ʱ�򣬲ŷ����༭�����ڵ�����
			if (lastPathComponent != null && !DataService.getInstance().isHome()) {
			    HelpNode helpNode = customTree.getHelpNode(lastPathComponent);
			    String analysisDocument = DataService.getInstance().analysisDocument();
			    helpNode.setContent(analysisDocument);
			    // ����·��
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
			return "�ļ��������磺xx.cfg";
		    }
		});
		int returnVal = fileChooser.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    String filePath = selectedFile.getPath();
		    // ���ļ�ʱ�����±���·������ʱ���潫���ñ�·��
		    DataService.getInstance().setPath(filePath);
		    // if (!filePath.toLowerCase().endsWith(".cfg")) {
		    // // �����ļ���Ϊ
		    // filePath = filePath + ".cfg";
		    // }

		    System.out.println(filePath);
		    // DataService.getInstance().export(textPaneMap, customTree,
		    // filePath);
		    try {
			// ���ص���helpRootNode��Ϊ���ڵ㣬��������Ϊ������ϵͳ
			CustomTree customTree = DataService.getInstance().getCustomTree();
			CustomTreeNode rootNode = customTree.getRootNode();
			HelpNode helpRootNode = customTree.getHelpNode(rootNode);
			//�Ƴ��ɵ�����
			helpRootNode.removeAll();
			rootNode.removeAllChildren();
			customTree.clearHelpTreeMap();
			
			customTree.addHelpNode(rootNode, helpRootNode);
			//load��Ҫ���ڵ㡣������Ҫ����Ĵ���
			HelpNode newRootNode = DataService.getInstance().load(filePath);
			customTree.clearHelpTreeMap();
			customTree.addHelpNode(rootNode, newRootNode);
			// HelpNode helpNode =
			// helpRootNode.getChildrenNode().get(0);// <help1>

			customTree.updateAllNode(helpRootNode, rootNode);
			String name2 = selectedFile.getName();
			rootNode.setUserObject("[�ļ���:"+name2.substring(0, name2.indexOf("."))+"]");
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
