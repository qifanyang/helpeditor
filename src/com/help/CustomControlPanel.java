package com.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

import com.help.Utils.ColorName;

/**
 * 此类完成文字编辑功能，颜色，大小，字体，加粗......
 * 
 * @author qifan.yang
 * 
 */
public class CustomControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton bBtn;
	private JButton iBtn;
	private JComboBox fontColorCombox;
	private JComboBox fontSizeCombox;
	private JComboBox fontTypeCombox;
	private JButton insetPicBtn;

	private JTextPane editor;

	public CustomControlPanel(JTextPane editor) {
		this.editor = editor;
		setLayout(new BorderLayout());
		Box operatorBox = Box.createHorizontalBox();
		final JButton editBtn = new JButton("开始编辑");
		bBtn = new JButton("加粗(B)");
		iBtn = new JButton("倾斜(I)");// 倾斜暂不保存0

		// 字体颜色控件
		Box fontColorBox = Box.createHorizontalBox();
		fontColorCombox = new JComboBox(Utils.ColorName.values());
		fontColorCombox.addItem("其它...");
		fontColorBox.setBorder(BorderFactory.createTitledBorder(""));
		fontColorBox.add(new JLabel("颜色："));
		fontColorBox.add(fontColorCombox);

		// 字体大小控件
		fontSizeCombox = new JComboBox();
		Box fontSizeBox = Box.createHorizontalBox();
		// fontSizeBox.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		fontSizeBox.setBorder(BorderFactory.createTitledBorder(""));
		for (int i = 10; i < 50; i++) {
			fontSizeCombox.addItem(i);
		}
		fontSizeBox.add(new JLabel("大小: "));
		fontSizeBox.add(fontSizeCombox);

		// 字体类型控件
		Box fontTypeBox = Box.createHorizontalBox();
		fontTypeCombox = new JComboBox(Utils.FontName.values());
		fontTypeBox.setBorder(BorderFactory.createTitledBorder(""));
		fontTypeBox.add(new JLabel("类型："));
		fontTypeBox.add(fontTypeCombox);

		insetPicBtn = new JButton("插入图片");
		operatorBox.add(editBtn);
		operatorBox.add(bBtn);
		operatorBox.add(iBtn);
		operatorBox.add(insetPicBtn);
		operatorBox.add(fontColorBox);
		operatorBox.add(fontSizeBox);
		operatorBox.add(fontTypeBox);
		// operatorBox.add(Box.createHorizontalStrut(250));

		operatorBox.setBorder(BorderFactory.createEtchedBorder());
		add(operatorBox, BorderLayout.NORTH);

		initEventDeal();
	}

	private void initEventDeal() {
		// 改变粗细
		bBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyledEditorKit kit = (StyledEditorKit) editor.getEditorKit();
				MutableAttributeSet attr = kit.getInputAttributes();
				boolean bold = (StyleConstants.isBold(attr)) ? false : true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setBold(sas, bold);
				setCharacterAttributes(editor, sas, false);
			}
		});

		// 改倾斜
		iBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StyledEditorKit kit = (StyledEditorKit) editor.getEditorKit();
				MutableAttributeSet attr = kit.getInputAttributes();
				boolean italic = (StyleConstants.isItalic(attr)) ? false : true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setItalic(sas, italic);
				setCharacterAttributes(editor, sas, false);
			}
		});
		// 插入图片
		insetPicBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("选择要插入的图片");
				fileChooser.setApproveButtonText("确定");
				// editor.getStyledDocument().getDefaultRootElement();
				if (editor.getStyledDocument() == null) {
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
							return "图片";
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
							editor.insertIcon(imageIcon);
							// hack 出入图片后换行输入汉字时，默认颜色改变
							MutableAttributeSet attr = new SimpleAttributeSet();
							StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
							// Utils.setCharacterAttributes(currentEditor, attr,
							// false);
							editor.setCharacterAttributes(attr, false);
							// currentEditor.getStyledDocument().setCharacterAttributes(startPosition,
							// 2, attr, false);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		// 改变颜色，提供颜色选择面板
		fontColorCombox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {

					MutableAttributeSet attr = new SimpleAttributeSet();
					if (e.getItem() instanceof ColorName) {
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
					} else {
						// 颜色面板
						Color color = JColorChooser.showDialog(getParent(), "请选择颜色", Utils.RIGHT_FONT_COLOR);
						if (color != null) {
							//保存和加载对应需要处理
                            StyleConstants.setForeground(attr, color);
						}
					}
					setCharacterAttributes(editor, attr, false);
				}
			}
		});

		// 改变大小
		fontSizeCombox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {

					MutableAttributeSet attr = new SimpleAttributeSet();
					String value = e.getItem().toString();
					// for (int i = 0; i < fontColor.length; i++) {
					// StyleConstants.setForeground(attr, );
					// }
					StyleConstants.setFontSize(attr, Integer.parseInt(value));
					setCharacterAttributes(editor, attr, false);
					// System.out.println(editor.getSelectedText());
				}
			}
		});

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

}
