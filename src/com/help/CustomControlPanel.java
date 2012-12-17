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
 * ����������ֱ༭���ܣ���ɫ����С�����壬�Ӵ�......
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
		final JButton editBtn = new JButton("��ʼ�༭");
		bBtn = new JButton("�Ӵ�(B)");
		iBtn = new JButton("��б(I)");// ��б�ݲ�����0

		// ������ɫ�ؼ�
		Box fontColorBox = Box.createHorizontalBox();
		fontColorCombox = new JComboBox(Utils.ColorName.values());
		fontColorCombox.addItem("����...");
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

		insetPicBtn = new JButton("����ͼƬ");
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
		// �ı��ϸ
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

		// ����б
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
		// ����ͼƬ
		insetPicBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("ѡ��Ҫ�����ͼƬ");
				fileChooser.setApproveButtonText("ȷ��");
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
							editor.insertIcon(imageIcon);
							// hack ����ͼƬ�������뺺��ʱ��Ĭ����ɫ�ı�
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

		// �ı���ɫ���ṩ��ɫѡ�����
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
						// ��ɫ���
						Color color = JColorChooser.showDialog(getParent(), "��ѡ����ɫ", Utils.RIGHT_FONT_COLOR);
						if (color != null) {
							//����ͼ��ض�Ӧ��Ҫ����
                            StyleConstants.setForeground(attr, color);
						}
					}
					setCharacterAttributes(editor, attr, false);
				}
			}
		});

		// �ı��С
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
