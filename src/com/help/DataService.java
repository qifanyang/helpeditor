package com.help;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.help.Utils.ColorName;

/**
 * 
 * �������ڱ༭���ݵĵ���͵��� ,���������ݸ�ʽ��Ҫ�ܹ���ȷ���벢����ȷ��ʾ
 * 
 * @author qifan.yang
 * 
 */
public class DataService {
	private static final DataService INSTANCE = new DataService();
	private int time = 600;// �Զ�������ʱ�䣬��λ�룬Ĭ��60��
	private CustomTree customTree;
	private JTextPane editor;
	private boolean isHome = true;// ��ʾ��ǰ�Ƿ���ʾ������ҳ
	private String path = "C:/help.cfg";// �ļ������Ĭ��λ��,��һ���ļ�ʱ���´�·��

	private Timer timer = new Timer();

	public static DataService getInstance() {
		return INSTANCE;
	}

	public CustomTree getCustomTree() {
		return customTree;
	}

	public void setCustomTree(CustomTree customTree) {
		this.customTree = customTree;
	}

	public JTextPane getEditor() {
		return editor;
	}

	public void setEditor(JTextPane editor) {
		this.editor = editor;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isHome() {
		return isHome;
	}

	public void setHome(boolean isHome) {
		this.isHome = isHome;
	}

	// ����xml���������νṹ��
	public HelpNode load(String filePath) throws IOException {
		// FileInputStream cfgFileInputStream = new FileInputStream(filePath);
		InputStream cfgFileInputStream = getClass().getClassLoader().getResourceAsStream(filePath);
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(cfgFileInputStream));
		if (cfgFileInputStream == null) {
			cfgFileInputStream = new FileInputStream(filePath);
		}
		Scanner scanner = new Scanner(cfgFileInputStream);
		HelpNode rootNode = customTree.getHelpNode(customTree.getRootNode());
		scanner.useDelimiter("\\n\\t*");
		if (scanner.hasNext()) {
			String startNodeFlag = scanner.next();// <help1>
			if (startNodeFlag.equalsIgnoreCase("<help>")) {// ������"<help>"����"<help>"���������
				analysisHelpNode(scanner, rootNode, startNodeFlag);
			} else {
				analysisHelpNode(scanner, rootNode, startNodeFlag);
				while (scanner.hasNext()) {
					analysisHelpNode(scanner, rootNode, scanner.next());
				}
				scanner.close();
				cfgFileInputStream.close();
				return rootNode;
			}
		}
		scanner.close();
		cfgFileInputStream.close();

		return rootNode.getNode(0);// �нڵ�<help>���޽ڵ�<help>���ز�ͬ
	}

	private void analysisHelpNode(Scanner scanner, HelpNode parentNode, String startNodeFlag) throws IOException {

		// System.out.println(startNodeFlag);
		String endNodeFlag = startNodeFlag.replace("<", "</");

		HelpNode helpNode = new HelpNode(startNodeFlag);
		// helpNode.setParentNode(parentNode);
		parentNode.addNode(helpNode);

		// StringBuffer contentBuffer = new StringBuffer();
		if (scanner.hasNext()) {
			String content = scanner.next();// ��ʼ��������

			while (content != null && !content.equalsIgnoreCase(endNodeFlag)) {// ��һ��
				if (content.trim().startsWith("content=")) { // ���洢Ϊcontent=
					int eqIndex = content.indexOf("=");
					if (!content.trim().equalsIgnoreCase("content=")) {
						helpNode.setContent(content.substring(eqIndex + 1));
					}
					// helpNode.setContentHash(helpNode.getContent().hashCode());//���ڼ���
				} else if (content.trim().startsWith("name")) {
					int nameIndex = content.indexOf("=");
					helpNode.setName(content.substring(nameIndex + 1));
				} else if (content.trim().startsWith("<help")) {// ��������������ʽ�����������֤��ȷ��
					analysisHelpNode(scanner, helpNode, content);
				} else if (content.length() > 0) {
					// ���һ���ڵ�����û��������������˵���˽ڵ������⣬������һ���ڵ�������
					// ���磺<help2-5 content=ALT+ ��/���л�<<br>>ALT+A
					// ֹͣ����
					JOptionPane.showMessageDialog(null, "�����ĵ��������ܸ��ĵ���ʽ�����⣺" + content);
					// �ָ���ʼ������
					System.exit(0);
				}

				// contentBuffer.append(content);
				if (scanner.hasNext()) {
					content = scanner.next();
				} else {
					break;
				}

			}
		}
		// System.out.println("ok");
		// System.out.println(endNodeFlag);

	}

	/**
	 * ���غͱ�����ע�⣺Ӧ�ü���<help>��Ϊ���ڵ㣬����Ϸ��û������ڵ㡣
	 * 
	 * ���Գ��򱣴�Ӧ����Ҫȥ��<help>��������ע�⣬��<help>����<help>���ؽ��Ӧ��һ��
	 * 
	 * ����editorMap������editor�����ݵ�·��filePath
	 * 
	 * @param editorMap
	 *            Ҫ�������ݵ�editorMap
	 * @param filePath
	 *            �����·��
	 * @return true or false
	 */
	public synchronized boolean export(String filePath) {
		// todo ����������
		// customTree.getRootPane()
		CustomTreeNode rootNode = customTree.getRootNode();
		StringBuffer catalogNodeBuffer = new StringBuffer();
		recursionExport(catalogNodeBuffer, rootNode, "", "", "<help");
		// catalogNodeBuffer.append("<help>\n");//��׼������ʽ��Ҫ��������ǣ�����Ϸ�в�ʹ��
		// ������һ��ڵ�
		// for (int i = 1; i <= rootNode.getChildCount(); i++) {
		// CustomTreeNode firstTreeNode = (CustomTreeNode) rootNode.getChildAt(i
		// - 1);
		// HelpNode firstHelpNode = customTree.getHelpNode(firstTreeNode);
		// if (firstHelpNode == null)
		// return false;
		// catalogNodeBuffer.append("<help").append(i).append(">\n");
		// String content = firstHelpNode.getContent();
		// String name = firstHelpNode.getName();
		// if (content != null && content.length() > 0) {// �յ�Ҳ����
		// catalogNodeBuffer.append("\tcontent=").append(content).append("\n");
		// } else {
		// catalogNodeBuffer.append("\tcontent=\n");
		// }
		// if (name != null && name.length() > 0) {
		// catalogNodeBuffer.append("\tname=").append(name).append("\n");
		// }
		// // �����ڶ���ڵ�
		// for (int j = 1; j <= firstTreeNode.getChildCount(); j++) {
		// CustomTreeNode node = (CustomTreeNode) firstTreeNode.getChildAt(j -
		// 1);
		// catalogNodeBuffer.append("\t<help").append(i).append("-").append(j).append(">\n");
		// String content2 = customTree.getHelpNode(node).getContent();
		// if (content2 != null && content2.length() > 0) {
		// catalogNodeBuffer.append("\t\tcontent=").append(content2).append("\n");
		// } else {
		// catalogNodeBuffer.append("\t\tcontent=\n");
		// }
		// catalogNodeBuffer.append("\t\tname=").append(customTree.getHelpNode(node).getName()).append("\n");
		// catalogNodeBuffer.append("\t</help").append(i).append("-").append(j).append(">\n");
		// }
		// catalogNodeBuffer.append("</help").append(i).append(">\n");
		// }
		// catalogNodeBuffer.append("</help>\n");//��׼������ʽ��Ҫ��������ǣ�����Ϸ�в�ʹ��

		// ����
		try {
			if (filePath == null) {
				filePath = path;
			}
			FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
			fileOutputStream.write(catalogNodeBuffer.toString().getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void recursionExport(StringBuffer outBuffer, CustomTreeNode treeNode, String tFlag, String hxFlag, String head) {
		// ÿ�εݹ飬���һ��"\t" , �����Ű�
		// tBuffer.append("\t");
		String tt = new String(tFlag);
		for (int j = 1; j <= treeNode.getChildCount(); j++) {
			CustomTreeNode node = (CustomTreeNode) treeNode.getChildAt(j - 1);
			HelpNode helpNode = customTree.getHelpNode(node);
			if (helpNode != null) {
				// outBuffer.append("\t<help").append(j).append("-").append(j).append(">\n");
				String num = hxFlag.length() > 0 ? hxFlag : "";
				String header = head + num;
				outBuffer.append(tt).append(header).append(j).append(">\n");
				String content = helpNode.getContent();
				if (content != null && content.length() > 0) {
					outBuffer.append(tt).append("\tcontent=").append(content).append("\n");
				} else {
					outBuffer.append(tt).append("\tcontent=\n");
				}
				outBuffer.append(tt).append("\tname=").append(helpNode.getName()).append("\n");
				if (node.getChildCount() > 0) {
					recursionExport(outBuffer, node, tFlag + "\t", "-", header + j);
				}
				outBuffer.append(tt).append(header.replace("<", "</")).append(j).append(">\n");
			}
		}

	}

	/**
	 * ����editor�����ݣ����ط������
	 * 
	 * @param editor
	 * @return ��ҪԤ���򱣴���ַ���
	 */
	public String analysisDocument() {
		StringBuffer outBuffer = new StringBuffer();
		// outBuffer.append("<help1-1> \n");
		// ȡ�ø�Ԫ��,��Ԫ������������Ԫ�أ�section root �� bidi root������ֻ����section root���������
		Element[] root = editor.getDocument().getRootElements();
		for (int i = 0; i < root.length; i++) {
			// Element element = root[0];//����ֱ�ӽ�����һ���ڵ�Ԫ��
			Element element = root[i];//
			int sunElementCount = element.getElementCount();// �����section����4���ӽڵ�
			for (int j = 0; j < sunElementCount; j++) {
				// ȡ�õ�һ��ڵ�Ԫ�أ����л��е�ͼƬ�����з��ŵ����ı�
				Element firstLayerElement = element.getElement(j);
				// ����һ��Ԫ�أ����л��е�ͼƬ�Ƚ���ͼƬ�ٽ�������
				for (int k = 0; k < firstLayerElement.getElementCount(); k++) {
					// ����ڶ���
					Element secondLayerElement = firstLayerElement.getElement(k);
					AttributeSet secondLayerAttr = secondLayerElement.getAttributes();

					Icon icon = StyleConstants.getIcon(secondLayerAttr);
					if (icon != null) {// ����ͼƬ����ȡͼƬ�ĵ�ַ
						// int startOff = secondLayerElement.getStartOffset();
						// int endOff = secondLayerElement.getEndOffset();
						Enumeration<?> attributeNames = secondLayerAttr.getAttributeNames();
						while (attributeNames.hasMoreElements()) {
							Object nextElement = attributeNames.nextElement();
							if (nextElement.toString().equals("icon")) {
								// System.out.println(icon.toString());
								String replace = icon.toString().replace("\\", "/");
								int lastIndexOf = replace.lastIndexOf("/");
								String imageName = replace.substring(lastIndexOf + 1);
								outBuffer.append("<pic>").append(imageName).append("<pic>");
								// System.out.println("�ĵ���λ�� :[" + startOff +
								// "-----" + endOff + " ]��ͼƬ�����֣�" + imageName);
							}
						}

					} else {
						int startOff = secondLayerElement.getStartOffset();
						int endOff = secondLayerElement.getEndOffset();
						try {
							String text = editor.getText(startOff, endOff - startOff);
							// String copy=text;
							if (text.equals("\n")) {// ���3,4 ��ֱ�Ӽ�<br>
								outBuffer.append("<<br>>");
								// System.out.println("�ĵ���λ�� :[" + startOff +
								// "-----" + endOff + " ]�ǻ��з���!");
							} else if (text.trim().length() == 0) {
								// System.out.println("kong bai"+text+"jsdjflksdjf");
								outBuffer.append(text);

							} else {
								// System.out.println("�ĵ���λ�� :[" + startOff +
								// "-----" + endOff + " ]�Ǵ��ı�!");
								/**
								 * �����ﴦ�������ı�,�����¼������ ��1����ʽһ�����ı�Ϊһ��Element
								 * 2���ж��ָ�ʽ��һ���ı��� 3��ͼƬ����Ļ��У� 4�������Ļ���
								 * 
								 */
								String analysisResult = analysisStyle(secondLayerAttr, text);
								String replace = analysisResult.replace("\n", "<br>");
								outBuffer.append(replace);
								// outBuffer.append("<br>");
							}
							// System.out.println("Font Size :" +
							// StyleConstants.getFontSize(secondLayerAttr));
							// System.out.println("Font Color :" +
							// StyleConstants.getForeground(secondLayerAttr));
							// System.out.println("Font isBold :" +
							// StyleConstants.isBold(secondLayerAttr));
							// System.out.println("Font FontFamily :" +
							// StyleConstants.getFontFamily(secondLayerAttr));
						} catch (BadLocationException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		// outBuffer.append("\n<help1-1>");
		// System.out.println(outBuffer.toString());
		return outBuffer.toString();
	}

	/**
	 * ����ĵ�Ƭ�θ�ʽ�Ƿ�����ͨ��ʽ��
	 * ������ĵ�Ƭ���У��Ӵ֣����岻����15����ɫ������Ĭ��ֵ�����֮һ�򽫸�ʽ��Ϣ��ȡ�������ڹ��������ʽ��Ϣ���ַ���������ֱ�ӷ����ַ���
	 * 
	 * @param attributeSet
	 *            ������ĵ�Ƭ�εĸ�ʽ����
	 * @param result
	 *            ������ĵ�Ƭ��
	 * @return
	 */
	private String analysisStyle(AttributeSet attributeSet, String result) {
		StringBuffer stringBuffer = new StringBuffer();
		int fontSize = StyleConstants.getFontSize(attributeSet);
		Color foreground = StyleConstants.getForeground(attributeSet);
		boolean isBold = StyleConstants.isBold(attributeSet);

		String conventColorString = convertToColorName(foreground);
		// �����ʽ��ֱ�ӷ���,��ɫ�����洦��
		if (fontSize == 15 && foreground.equals(Utils.RIGHT_FONT_COLOR) && !isBold || foreground.equals(Color.BLACK)) {
			return result;
		} else {
			// <<font size='15' color='normal_color' name='����' style='B'>>
			stringBuffer.append("<<font size='").append(fontSize).append("' color='").append(conventColorString).append("' name='����'").append(isBold ? " style='B'>>" : ">>").append(result).append("<</font>>");
			return stringBuffer.toString();
		}

	}

	private String convertToColorName(Color color) {
		if (color.equals(Utils.RIGHT_FONT_COLOR)) {
			return "normal_color";
		} else if (color.equals(Color.red)) {
			return "red";
		} else if (color.equals(Color.green)) {
			return "green";
		} else if (color.equals(Color.yellow)) {
			return "yellow";
		} else if (color.equals(Color.black)) {
			return "normal_color";
		} else {
			int red = color.getRed();
			int green = color.getGreen();
			int blue = color.getBlue();
			String redHex = Integer.toHexString(red).length() < 2 ? "0" + Integer.toHexString(red) : Integer.toHexString(red);
			String greenHex = Integer.toHexString(green).length() < 2 ? "0" + Integer.toHexString(green) : Integer.toHexString(green);
			String blueHex = Integer.toHexString(blue).length() < 2 ? "0" + Integer.toHexString(blue) : Integer.toHexString(blue);
			return "#" + redHex + greenHex + blueHex;
		}

	}

	public void showCotent(HelpNode node) throws BadLocationException {
		// System.out.println("Document Lenth :" +
		// editor.getDocument().getLength());
		MutableAttributeSet attr = new SimpleAttributeSet();
		editor.getDocument().remove(0, editor.getDocument().getLength());
		String content = node.getContent();
		if (content == null) {// is null˵��Ϊ�½����ĵ�����ʾ��ʾ����
			StyleConstants.setFontSize(attr, 15);
			StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
			editor.getDocument().insertString(0, "����������������.....", attr);
		} else {

			Scanner scanner = new Scanner(content);
			scanner.useDelimiter("<<?");
			while (scanner.hasNext()) {
				String next = scanner.next();
				StyleConstants.setFontSize(attr, 15);
				StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
				// // �������ı�
				// /font>>��ȭ�����������������١�
				if (!next.contains(">>") && !next.contains(">")) {
					// // û�а�����>>����˵���Ǵ��ı�
					editor.getDocument().insertString(editor.getCaretPosition(), next, attr);
					// // System.out.println(next);
					// next = null;
					//
				} else if (next.trim().startsWith("br>>")) {// �������� ��Ҫ�ṹ 1:br>>
					// ,2:
					// // br>> sdfsdfsdfd
					String[] splitBr = next.split(">>");

					editor.getDocument().insertString(editor.getCaretPosition(), "\n", null);
					// // System.out.println(splitBr[0]);
					if (splitBr.length > 1) {
						// // br>> sdfsdfsdfd �����з��ź����н����������ݵ����
						// <<br>><<br>> <<font �ָ��Ϊ�� ��br>> ��
						StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
						StyleConstants.setBold(attr, false);// ������
						editor.getDocument().insertString(editor.getCaretPosition(), splitBr[1], attr);

					}
					for (int i = 0; i < splitBr.length; i++) {
						splitBr[i] = null;
					}
					// // ����ͼƬ
				} else if (next.startsWith("pic>")) {
					String[] picSplit = next.split(">");
					// picSplit[1]==image name
					// ��ʱ��һ��ͼƬ���,���Ǳ���ʱ���Ǳ���ԭ��������
					if (picSplit.length > 1) {
						ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("com/help/res/help.png"));
						imageIcon.setDescription(picSplit[1]);
						editor.insertIcon(imageIcon);
					}
					// // let gc do it work
					for (int i = 0; i < picSplit.length; i++) {
						picSplit[i] = null;
					}
					// // �����и�ʽ������
				} else if (next.startsWith("/font>")) {
					String[] fontSplit = next.split(">>");
					if (fontSplit.length > 1) {
						StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
						StyleConstants.setBold(attr, false);// ������
						StyleConstants.setFontSize(attr, 15);// ������
						editor.getDocument().insertString(editor.getCaretPosition(), fontSplit[1], attr);
					}

				} else {
					if (next.toLowerCase().startsWith("font ")) {
						String[] splitAC = next.split(">>");// 0:���ԣ�1������
						String[] fontAttr = splitAC[0].split(" ");
						// MutableAttributeSet attr = new SimpleAttributeSet();
						// StyleConstants.setFontSize(attr, 16);
						for (int i = 1; i < fontAttr.length; i++) {
							int index = fontAttr[i].indexOf("=");
							String key = fontAttr[i].substring(0, index);
							String value = fontAttr[i].substring(index + 2, fontAttr[i].length() - 1);
							if (key.equalsIgnoreCase("size")) {
								StyleConstants.setFontSize(attr, Integer.parseInt(value));
							} else if (key.equalsIgnoreCase("color")) {
								// ��ɫ����
								if (value.startsWith("#")) {
									int rr = Integer.parseInt(value.substring(1, 3), 16);
									int gg = Integer.parseInt(value.substring(3, 5), 16);
									int bb = Integer.parseInt(value.substring(5, 7), 16);
									StyleConstants.setForeground(attr, new Color(rr, gg, bb));
								} else {
									ColorName[] colorNames = Utils.ColorName.values();
									for (int j = 0; j < colorNames.length; j++) {
										String lowerCase = colorNames[j].name().toLowerCase();
										if (lowerCase.equalsIgnoreCase(value)) {
											StyleConstants.setForeground(attr, colorNames[j].getColor());
											break;
										} else {
											StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);// û��ƥ��
										}
									}
								}
							} else if (key.equalsIgnoreCase("name")) {
								StyleConstants.setFontFamily(attr, value);
							} else if (key.equalsIgnoreCase("style")) {
								StyleConstants.setBold(attr, value.equalsIgnoreCase("B") ? true : false);
							}
							key = null;
							value = null;
						}
						// // ��������
						editor.getDocument().insertString(editor.getCaretPosition(), splitAC[1], attr);
						// ��Ϊfont�����ǳɶԳ��ֵģ�������ȥ�����ر�־
					}
				}
			}
			scanner.close();
		}
	}

	public void showIndex() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
		StyleConstants.setFontSize(attr, 30);
		StyleConstants.setForeground(attr, Utils.ColorName.YELLOW.getColor());
		StyleConstants.setBold(attr, true);
		// ImageIcon imageIcon = new
		// ImageIcon(getClass().getResource("long.png"));
		try {
			editor.getDocument().remove(0, editor.getDocument().getLength());
			editor.getDocument().insertString(editor.getDocument().getLength(), "�����ĵ��༭�� \n\n", attr);
			StyleConstants.setFontSize(attr, 20);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  1�������������ṩ��ť��������Ϊ���½�|��|����|���Ϊ��\n\n", attr);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  2��ѡ��������νڵ㣬�һ������ԡ��½�|ɾ��|����������Ŀ¼������\n\n", attr);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  3���½��ĵ��Զ�Ĭ�ϱ����ڡ�C:/help.cfg��\n\n", attr);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  4���ĵ�Ĭ���Զ����棬ʱ������60�룬Ҳ�ɵ�����������水ť�����޸�\n\n", attr);
			// editor.insertIcon(imageIcon);
			// editor.setEditable(false);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// ������ʱ����,��ʱ����Ϊ30���Զ�����һ��
	public void startStoreTimer() {
		timer = new Timer();
		timer.schedule(new StoreTimer(), time * 1000, time * 1000);
		System.out.println("������ʱ����........");
	}

	public void stopStoreTimer() {
		timer.cancel();
	}

	// ��ʱ���棬��Ҫ���༭�ĵ�ʱ����
	class StoreTimer extends TimerTask {

		@Override
		public void run() {
			CustomTreeNode selectedPathComponent = (CustomTreeNode) customTree.getLastSelectedPathComponent();
			// if (selectedPathComponent != null && editor != null && customTree
			// != null) {
			if (selectedPathComponent != null) {
				HelpNode helpNode = customTree.getHelpNode(selectedPathComponent);
				if (helpNode != null) {
					helpNode.setContent(analysisDocument());
					export(path);
					System.out.println("�Զ��������: Path:" + path + "........");
				}
			}
		}

	}

	// public static void main(String[] args) {
	// try {
	// DataService.getInstance().load("C:/Documents and Settings/Administrator/����/help/help.xml");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
