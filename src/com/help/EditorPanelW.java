package com.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;

import com.help.Utils;
import com.help.Utils.ColorName;


//todo ��ʱ��ɾ�������﹦�ܴ���
public class EditorPanelW extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextPane currentEditor = new JTextPane();
    // Ԥ������ʾ
    private JTextArea showArea;

    // �������ڵ㵽JTextPane��ӳ���MAP��ѡ���ݽڵ�ʱ���ڲ��� ��Ӧ��TextPane
    // private Map<CustomTreeNode, CustomEditor> textPaneMap = new
    // HashMap<CustomTreeNode, CustomEditor>();
    // �������ڵ㵽����ı���ӳ��MAP������ʱ�ɼӿ챣���ٶȣ�����ע���޸ĺ�Ҫ����MAP
    // private Map<CustomTreeNode, String> outMap = new
    // HashMap<CustomTreeNode, String>();
    private JPanel editorTabPanel;
    private Box toolBar;
//    private JButton saveBtn;
    private CustomTree customTree;
    private JComboBox fontColorCombox;
    private JComboBox fontSizeCombox;
    private JComboBox fontTypeCombox;
    private JButton bBtn;
    private JButton iBtn;
    private JButton insetPicBtn;
    private Box fontColorBox;

    public EditorPanelW() {

	setLayout(new BorderLayout());
	JTree helpTree = createTree();
	JPanel editorAreaPanel = createEditorArea();

	JScrollPane treeScrollPanel = new JScrollPane(helpTree);
	treeScrollPanel.setPreferredSize(new Dimension(HelpEditor.WIDTH / 5, HelpEditor.HEIGHT));

//	currentEditor.showIndex();
	// create operate panel
	JPanel operatePanel = creatOperatePanel();
	add(treeScrollPanel, BorderLayout.WEST);
	add(new JScrollPane(editorAreaPanel), BorderLayout.CENTER);
	add(operatePanel, BorderLayout.SOUTH);
    }

    private JPanel creatOperatePanel() {
	JPanel operatePanel = new JPanel();
	// saveBtn = new JButton("����");
	// JButton cancelBtn = new JButton("ȡ��");
	// JButton openBtn = new JButton("��");
	// operatePanel.add(openBtn);
	// operatePanel.add(saveBtn);
	// operatePanel.add(cancelBtn);
	// saveBtn.addActionListener(new SaveAction());
	// openBtn.addActionListener(new OpenAction());
	return operatePanel;
    }

    private JPanel createEditorArea() {
	JTabbedPane tabbedPane = new JTabbedPane();

	editorTabPanel = new JPanel();
	editorTabPanel.setLayout(new BorderLayout());
	toolBar = Box.createHorizontalBox();
	final JButton editBtn = new JButton("��ʼ�༭");
	bBtn = new JButton("�Ӵ�(B)");
	// bBtn = new JButton(new
	// ImageIcon(getClass().getClassLoader().getResource("com/help/resources/bold.gif")));
	// JButton styleBtn = new JButton("��б(I)");
	iBtn = new JButton("��б(I)");
	JButton sizeBtn = new JButton("��С(S)");

	// ������ɫ�ؼ�
	fontColorBox = Box.createHorizontalBox();
	fontColorCombox = new JComboBox(Utils.ColorName.values());
	fontColorBox.setBorder(BorderFactory.createTitledBorder(""));
	fontColorBox.add(new JLabel("��ɫ��"));
	fontColorBox.add(fontColorCombox);

	// �����С�ؼ�
	fontSizeCombox = new JComboBox();
	Box fontSizeBox = Box.createHorizontalBox();
	// fontSizeBox.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	fontSizeBox.setBorder(BorderFactory.createTitledBorder(""));
	for (int i = 10; i < 50; i++) {
	    fontSizeCombox.addItem(i);
	}
	fontSizeBox.add(new JLabel("��С: "));
	fontSizeBox.add(fontSizeCombox);

	// �������Ϳؼ�
	Box fontTypeBox = Box.createHorizontalBox();
	fontTypeCombox = new JComboBox(Utils.FontName.values());
	fontTypeBox.setBorder(BorderFactory.createTitledBorder(""));
	fontTypeBox.add(new JLabel("���ͣ�"));
	fontTypeBox.add(fontTypeCombox);

	// --------------
	JButton colorBtn = new JButton("��ɫ(C)");
	insetPicBtn = new JButton("����ͼƬ");
	// Font font = new Font(null, Font.PLAIN, 13);
	// System.out.println(font.getFontName());
	toolBar.add(editBtn);
	toolBar.add(bBtn);
	toolBar.add(iBtn);
	toolBar.add(insetPicBtn);
	toolBar.add(fontColorBox);
	toolBar.add(fontSizeBox);
	toolBar.add(fontTypeBox);
	toolBar.add(Box.createHorizontalStrut(3000));

	currentEditor.setBorder(BorderFactory.createEtchedBorder());
	toolBar.setBorder(BorderFactory.createEtchedBorder());
	editorTabPanel.add(toolBar, BorderLayout.NORTH);
	editorTabPanel.add(currentEditor, BorderLayout.CENTER);

	// ���濪ʼ�����¼�.......
	// ���ͣ�ڱ༭������
//	currentEditor.addCaretListener(new CaretListener() {
//
//	    public void caretUpdate(CaretEvent e) {
//		// // TreePath selectionPath = customTree.getSelectionPath();
//		// // if (selectionPath != null) {
//		// // CustomTreeNode lastPathComponent = (CustomTreeNode)
//		// // selectionPath.getLastPathComponent();
//		// // HelpNode helpNode =
//		// // customTree.getHelpNode(lastPathComponent);
//		// // if (helpNode != null) {
//		// // helpNode.setChange(true);
//		// // }
//		// // }
//		MutableAttributeSet attr = new SimpleAttributeSet();
//		StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
//		currentEditor.setCharacterAttributes(attr, false);
//		// currentEditor.repaint();
//		// currentEditor.validate();
//
//	    }
//	});
//
//	currentEditor.addMouseListener(new MouseAdapter() {
//
//	    @Override
//	    public void mouseReleased(MouseEvent e) {
//		System.out.println("mouse listener !");
//		if (e.getSource() == currentEditor) {
//		    StyledEditorKit kit = (StyledEditorKit) currentEditor.getEditorKit();
//		    MutableAttributeSet attr = kit.getInputAttributes();
//		    int fontSize = StyleConstants.getFontSize(attr);
//		    Color foreground = StyleConstants.getForeground(attr);
//		    // System.out.println("color :" + foreground.getRed() + " :"
//		    // + foreground.getGreen() + " :" + foreground.getBlue());
//		    boolean isBold = StyleConstants.isBold(attr);
//		    fontSizeCombox.setSelectedItem(fontSize);
//
//		    if (foreground.equals(Color.RED)) {
//			fontColorCombox.setSelectedItem(ColorName.RED);
//		    } else if (foreground.equals(Color.YELLOW)) {
//			fontColorCombox.setSelectedItem(ColorName.YELLOW);
//		    } else if (foreground.equals(Color.GREEN)) {
//			fontColorCombox.setSelectedItem(ColorName.GREEN);
//		    } else if (foreground.equals(Utils.RIGHT_FONT_COLOR)) {
//			fontColorCombox.setSelectedItem(ColorName.NORMAL_COLOR);
//		    } else if (foreground.equals(Color.BLACK)) {
//			currentEditor.setSelectedTextColor(Utils.RIGHT_FONT_COLOR);
//		    }
//
//		    if (isBold) {
//			bBtn.setFocusPainted(true);
//			bBtn.setFocusable(true);
//		    } else {
//			bBtn.setFocusPainted(false);
//		    }
//
//		}
//	    }
//
//	});

	// �ı��ϸ
	bBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		StyledEditorKit kit = (StyledEditorKit) currentEditor.getEditorKit();
		MutableAttributeSet attr = kit.getInputAttributes();
		boolean bold = (StyleConstants.isBold(attr)) ? false : true;
		SimpleAttributeSet sas = new SimpleAttributeSet();
		StyleConstants.setBold(sas, bold);
		setCharacterAttributes(currentEditor, sas, false);
	    }
	});

	// ����б
	iBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		StyledEditorKit kit = (StyledEditorKit) currentEditor.getEditorKit();
		MutableAttributeSet attr = kit.getInputAttributes();
		boolean italic = (StyleConstants.isItalic(attr)) ? false : true;
		SimpleAttributeSet sas = new SimpleAttributeSet();
		StyleConstants.setItalic(sas, italic);
		setCharacterAttributes(currentEditor, sas, false);
	    }
	});

	// ����ͼƬ
	insetPicBtn.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("ѡ��Ҫ�����ͼƬ");
		fileChooser.setApproveButtonText("ȷ��");
		// editor.getStyledDocument().getDefaultRootElement();
		if (currentEditor.getStyledDocument() == null) {
		    System.out.println("editor is NULL ");
		} else {

		    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		    fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
			    return f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg") || f.isDirectory();
			}

			@Override
			public String getDescription() {
			    return "ͼƬ";
			}
		    });
		    int returnVal = fileChooser.showOpenDialog(getParent());
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			// System.out.println(selectedFile.getAbsolutePath());
			System.out.println(selectedFile.getPath());
			// ImageIcon imageIcon = new
			// ImageIcon(selectedFile.getPath());
			try {
			    ImageIcon imageIcon = new ImageIcon(ImageIO.read(selectedFile));
			    imageIcon.setDescription(selectedFile.getPath());
			    currentEditor.insertIcon(imageIcon);
			    // hack ����ͼƬ�������뺺��ʱ��Ĭ����ɫ�ı�
			    MutableAttributeSet attr = new SimpleAttributeSet();
			    StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
			    // Utils.setCharacterAttributes(currentEditor, attr,
			    // false);
			    currentEditor.setCharacterAttributes(attr, false);
			    // currentEditor.getStyledDocument().setCharacterAttributes(startPosition,
			    // 2, attr, false);
			} catch (IOException e1) {
			    e1.printStackTrace();
			}
		    }
		}
	    }
	});

	// �ı���ɫ
	colorBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setForeground(attr, Color.red);
		setCharacterAttributes(currentEditor, attr, false);
	    }
	});
	fontColorCombox.addItemListener(new ItemListener() {

	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		    MutableAttributeSet attr = new SimpleAttributeSet();
		    Utils.ColorName value = (ColorName) e.getItem();
		    if (value == ColorName.NORMAL_COLOR) {
			StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
		    } else if (value == ColorName.GREEN) {
			StyleConstants.setForeground(attr, Color.GREEN);
		    } else if (value == ColorName.RED) {
			StyleConstants.setForeground(attr, Color.RED);
		    } else if (value == ColorName.YELLOW) {
			StyleConstants.setForeground(attr, Color.YELLOW);
		    }
		    setCharacterAttributes(currentEditor, attr, false);
		    // System.out.println(editor.getSelectedText());
		}
	    }
	});

	// �ı��С
	sizeBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setFontSize(attr, 16);
		setCharacterAttributes(currentEditor, attr, false);
	    }
	});
	fontSizeCombox.addItemListener(new ItemListener() {

	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		    MutableAttributeSet attr = new SimpleAttributeSet();
		    String value = e.getItem().toString();
		    // for (int i = 0; i < fontColor.length; i++) {
		    // StyleConstants.setForeground(attr, );
		    // }
		    StyleConstants.setFontSize(attr, Integer.parseInt(value));
		    setCharacterAttributes(currentEditor, attr, false);
		    // System.out.println(editor.getSelectedText());
		}
	    }
	});

	// �����ĵ��ṹ
	editBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// �����ĵ��ṹ
		// DataService.getInstance().analysisDocument(currentEditor);
		// JOptionPane.showMessageDialog(getParent(), "������ϣ���ʽ��ȷ ��");
		boolean editable = currentEditor.isEditable();
		if (!editable) {
		    currentEditor.setEditable(true);
		    // enableOprator(true);
		    editBtn.setText("ֹͣ�༭");
		} else {
		    currentEditor.setEditable(false);
		    // enableOprator(false);
		    editBtn.setText("��ʼ�༭");
		}
	    }
	});

	// fontBtn.addActionListener(new ActionListener() {
	//
	// public void actionPerformed(ActionEvent e) {
	// if (fontEditorDialog == null) {
	// fontEditorDialog = new FontEditorDialog();
	// } else {
	// fontEditorDialog.setVisible(true);
	// }
	// }
	// });

	// Ԥ�����
	JPanel previewPanel = new JPanel();
	previewPanel.setLayout(new BorderLayout());
	JLabel tipLabel = new JLabel("��Ҫ�����ļ��ĸ�ʽ���£�������Ҫ�����ֶ��༭ ......");
	showArea = new JTextArea();
	JPanel areaPanel = new JPanel();
	areaPanel.setLayout(new BorderLayout());
	areaPanel.add(showArea);
	previewPanel.add(tipLabel, BorderLayout.NORTH);
	previewPanel.add(new JScrollPane(showArea), BorderLayout.CENTER);

	tabbedPane.add(editorTabPanel, "�ļ��༭��");
	tabbedPane.add(new JScrollPane(previewPanel), "   Ԥ��    ");

	JPanel mainPanel = new JPanel();
	mainPanel.setBorder(BorderFactory.createEmptyBorder());
	mainPanel.setLayout(new BorderLayout());
	mainPanel.add(tabbedPane);
	// mainPanel.add(editorPanel);
	// mainPanel.setBackground(bgColor);
	tabbedPane.addChangeListener(new ChangeListener() {

	    public void stateChanged(ChangeEvent e) {

		JTabbedPane source = (JTabbedPane) e.getSource();
		int selectedIndex = source.getSelectedIndex();
		if (selectedIndex == 1) {
		    showArea.setText("");

		    // showArea.append(DataService.getInstance().analysisDocument(currentEditor));
		    showArea.append(DataService.getInstance().analysisDocument());
		}
	    }
	});
	// enableOprator(false);
	// fontColorCombox.setEnabled(false);
	return mainPanel;

    }

    public void setCharacterAttributes(JEditorPane editor, AttributeSet attr, boolean replace) {
	int p0 = editor.getSelectionStart();
	int p1 = editor.getSelectionEnd();
	if (p0 != p1) {
	    StyledDocument doc = (StyledDocument) editor.getDocument();
	    doc.setCharacterAttributes(p0, p1 - p0, attr, replace);
	}
	StyledEditorKit k = (StyledEditorKit) editor.getEditorKit();
	MutableAttributeSet inputAttributes = k.getInputAttributes();
	if (replace) {
	    inputAttributes.removeAttributes(inputAttributes);
	}
	inputAttributes.addAttributes(attr);
    }

    // public void addTextPaneMap(CustomTreeNode node, CustomEditor textPane) {
    // textPaneMap.put(node, textPane);
    // }

    // public void removeNode(CustomTreeNode lastPathComponent) {
    // textPaneMap.remove(lastPathComponent);
    //
    // }

    // public void updateTextPaneMap(CustomTreeNode node, JTextPane
    // textPane) {
    // textPaneMap.put(node, textPane);
    // }

    public JTextPane getCurrentEditor() {
	return currentEditor;
    }

    public CustomTree getCustomTree() {
	return customTree;
    }

    private JTree createTree() {
	CustomTreeNode root = new CustomTreeNode("����ϵͳ");
	CustomTreeNode load = new CustomTreeNode(CustomTreeNode.noFlag);
	root.add(load);
	customTree = new CustomTree(root);

	try {
	    HelpNode helpNode = DataService.getInstance().load("help.xml");
	    CustomTreeNode rootNode = customTree.getRootNode();
	    HelpNode helpRootNode = helpNode.getChildrenNode().get(0);
	    rootNode.removeAllChildren();
	    customTree.updateAllNode(helpRootNode, rootNode);

	    customTree.updateUI();

	} catch (IOException e1) {
	    e1.printStackTrace();
	}

	// customTree.setBackground(bgColor);

	// test data
	// Object[] objects = { root };
	// customTree.setSelectionPath(new TreePath(objects));
	// customTree.addCatalog("��������");
	// CustomTreeNode lastPathComponent = (CustomTreeNode)
	// root.getChildAt(0);
	// Object[] objects1 = { root, lastPathComponent };
	// customTree.setSelectionPath(new TreePath(objects1));
	// customTree.addContentTitle("���������");
	// customTree.addContentTitle("��ݼ�ʹ��");
	// customTree.addContentTitle("��������");

	// CustomTreeNode catalogNode = root.addCatalog("��������");
	// CustomTreeNode one = catalogNode.addContentTitle("���������");
	// CustomTreeNode two = catalogNode.addContentTitle("��ݼ�ʹ��");
	// CustomTreeNode three = catalogNode.addContentTitle("��������");
	// customTree.updateUI();

	// ������Ӧeditor
	// CustomEditor customEditor = new CustomEditor();
	// addTextPaneMap(one, customEditor);
	// addTextPaneMap(two, customEditor);
	// addTextPaneMap(three, customEditor);

	// ��������¼�����
	customTree.addTreeSelectionListener(new HelpTreeSelecListener());
	return customTree;

    }

    // /**
    // * ����ĵ�Ƭ�θ�ʽ�Ƿ�����ͨ��ʽ��
    // * ������ĵ�Ƭ���У��Ӵ֣����岻����15����ɫ������Ĭ��ֵ�����֮һ�򽫸�ʽ��Ϣ��ȡ�������ڹ��������ʽ��Ϣ���ַ���������ֱ�ӷ����ַ���
    // *
    // * @param attributeSet
    // * ������ĵ�Ƭ�εĸ�ʽ����
    // * @param result
    // * ������ĵ�Ƭ��
    // * @return
    // */
    // public String analysisStyle(AttributeSet attributeSet, String result) {
    // StringBuffer stringBuffer = new StringBuffer();
    // int fontSize = StyleConstants.getFontSize(attributeSet);
    // Color foreground = StyleConstants.getForeground(attributeSet);
    // boolean isBold = StyleConstants.isBold(attributeSet);
    //
    // String conventColorString = convertToColorName(foreground);
    // // �����ʽ��ֱ�ӷ���
    // if (fontSize == 15 && foreground == rightFontColor && !isBold) {
    // return result;
    // } else {
    // // <<font size='15' color='normal_color' name='����' style='B'>>
    // stringBuffer.append("<<font size='").append(fontSize).append("' color='").append(conventColorString).append("' name='����'").append(isBold
    // ? " style='B'>>" : ">>").append(result)
    // .append("<</font>>");
    // return stringBuffer.toString();
    // }
    //
    // }
    // todo ���ʹ�ܰ�ť���
//    private void enableOprator(boolean isAble) {
//	fontTypeCombox.setEditable(isAble);
//	fontSizeCombox.setEditable(isAble);
//	fontColorBox.setEnabled(isAble);
//
//	fontColorCombox.setEditable(isAble);
//	iBtn.setEnabled(isAble);
//	bBtn.setEnabled(isAble);
//	insetPicBtn.setEnabled(isAble);
//
//    }

    /**
     * ���νṹѡ���������ѡ��һ���ڵ�ʱ����Ҫ�����ýڵ��content������ʾ
     * 
     * @author qifan.yang
     * 
     */
    class HelpTreeSelecListener implements TreeSelectionListener {

	public void valueChanged(TreeSelectionEvent e) {

	    CustomTreeNode lastPathComponent = (CustomTreeNode) e.getPath().getLastPathComponent();
	    if (lastPathComponent != null) {
		// textPaneMap.put(lastPathComponent, customEditor);
		HelpNode helpNode = customTree.getHelpNode(lastPathComponent);
		try {
//		    currentEditor.showCotent(helpNode);
		    DataService.getInstance().showCotent(helpNode);
//		    TextAnalysisKit.getInstance().showCotent(currentEditor,helpNode);
		} catch (BadLocationException e2) {
		    e2.printStackTrace();
		}
	    }
	    // TreePath oldTreePath = e.getOldLeadSelectionPath();
	    // �ÿ�ʼ�༭�����ť�����Ƹ������Ƿ��Ѿ������ı�
	    // if (oldTreePath != null) {
	    // CustomTreeNode oldNode = (CustomTreeNode)
	    // oldTreePath.getLastPathComponent();
	    // HelpNode helpNode = customTree.getHelpNode(oldNode);
	    // if (helpNode != null && helpNode.isChange()) {
	    // String analysisResult =
	    // DataService.getInstance().analysisDocument(currentEditor);
	    // �����༭����������ݣ�
	    // String analysisResult = currentEditor.analysisDocument();
	    // helpNode.setContent(analysisResult);// �������е����ݣ����Ʊ���
	    // }
	    // System.out.println("=================================================================");
	    // System.out.println(analysisResult);
	    // System.out.println("=================================================================");
	    // }

	    // many pane
	    // ====================
	    // CustomEditor textPane = textPaneMap.get(lastPathComponent);
	    // if (textPane != null) {
	    // currentEditor = textPane;
	    // editorTabPanel.remove(currentEditor);
	    // // editorTabPanel.add(toolBar, BorderLayout.NORTH);
	    // editorTabPanel.add(currentEditor, BorderLayout.CENTER);
	    // editorTabPanel.repaint();
	    // editorTabPanel.validate();
	    // // currentEditor.repaint();
	    // } else {
	    // �Ż���ͬ��һ��Editor
	    // ===================
	    // final HelpNode currentHelpNode =
	    // customTree.getHelpNode(lastPathComponent);
	    // SwingUtilities.invokeLater(new Runnable() {
	    //
	    // public void run() {
	    //
	    // try {
	    // currentEditor.getStyledDocument().remove(0,
	    // currentEditor.getStyledDocument().getLength());
	    // if (currentHelpNode != null)
	    // currentEditor.showCotent(currentHelpNode);
	    // // currentEditor.getStyledDocument().remove(0,
	    // // currentEditor.getStyledDocument().getLength());
	    // // currentEditor.getStyledDocument().insertString(0,
	    // // currentHelpNode.getContent(), null);
	    // System.out.println("ok");
	    // if
	    // (lastPathComponent.getUserObject().toString().equals("����ϵͳ"))
	    // {
	    // currentEditor.showIndex();
	    // }
	    // } catch (BadLocationException e1) {
	    // e1.printStackTrace();
	    // }
	    // }
	    // });

	    // }

	}

    }
}
