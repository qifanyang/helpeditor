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
 * 此类用于编辑数据的导入和导出 ,导出的数据格式需要能够正确导入并能正确显示
 * 
 * @author qifan.yang
 * 
 */
public class DataService {
	private static final DataService INSTANCE = new DataService();
	private int time = 600;// 自动保存间隔时间，单位秒，默认60秒
	private CustomTree customTree;
	private JTextPane editor;
	private boolean isHome = true;// 表示当前是否显示的是首页
	private String path = "C:/help.cfg";// 文件保存的默认位置,打开一个文件时更新此路径

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

	// 加载xml，创建树形结构，
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
			if (startNodeFlag.equalsIgnoreCase("<help>")) {// 处理有"<help>"和无"<help>"这两种情况
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

		return rootNode.getNode(0);// 有节点<help>和无节点<help>返回不同
	}

	private void analysisHelpNode(Scanner scanner, HelpNode parentNode, String startNodeFlag) throws IOException {

		// System.out.println(startNodeFlag);
		String endNodeFlag = startNodeFlag.replace("<", "</");

		HelpNode helpNode = new HelpNode(startNodeFlag);
		// helpNode.setParentNode(parentNode);
		parentNode.addNode(helpNode);

		// StringBuffer contentBuffer = new StringBuffer();
		if (scanner.hasNext()) {
			String content = scanner.next();// 开始解析内容

			while (content != null && !content.equalsIgnoreCase(endNodeFlag)) {// 第一层
				if (content.trim().startsWith("content=")) { // 当存储为content=
					int eqIndex = content.indexOf("=");
					if (!content.trim().equalsIgnoreCase("content=")) {
						helpNode.setContent(content.substring(eqIndex + 1));
					}
					// helpNode.setContentHash(helpNode.getContent().hashCode());//用于检验
				} else if (content.trim().startsWith("name")) {
					int nameIndex = content.indexOf("=");
					helpNode.setName(content.substring(nameIndex + 1));
				} else if (content.trim().startsWith("<help")) {// 这里需用正则表达式，可以提高验证正确性
					analysisHelpNode(scanner, helpNode, content);
				} else if (content.length() > 0) {
					// 如果一个节点下面没有上面三个，则说明此节点有问题，或则上一个节点有问题
					// 例如：<help2-5 content=ALT+ 走/跑切换<<br>>ALT+A
					// 停止加载
					JOptionPane.showMessageDialog(null, "加载文档出错，可能该文档格式有问题：" + content);
					// 恢复初始化环境
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
	 * 加载和保存需注意：应该加上<help>做为根节点，但游戏中没有这个节点。
	 * 
	 * 所以程序保存应该需要去掉<help>，加载需注意，有<help>和无<help>加载结果应该一样
	 * 
	 * 保存editorMap中所有editor的内容到路径filePath
	 * 
	 * @param editorMap
	 *            要保存内容的editorMap
	 * @param filePath
	 *            保存的路径
	 * @return true or false
	 */
	public synchronized boolean export(String filePath) {
		// todo 遍历树保存
		// customTree.getRootPane()
		CustomTreeNode rootNode = customTree.getRootNode();
		StringBuffer catalogNodeBuffer = new StringBuffer();
		recursionExport(catalogNodeBuffer, rootNode, "", "", "<help");
		// catalogNodeBuffer.append("<help>\n");//标准到处格式需要添加这个标记，但游戏中不使用
		// 遍历第一层节点
		// for (int i = 1; i <= rootNode.getChildCount(); i++) {
		// CustomTreeNode firstTreeNode = (CustomTreeNode) rootNode.getChildAt(i
		// - 1);
		// HelpNode firstHelpNode = customTree.getHelpNode(firstTreeNode);
		// if (firstHelpNode == null)
		// return false;
		// catalogNodeBuffer.append("<help").append(i).append(">\n");
		// String content = firstHelpNode.getContent();
		// String name = firstHelpNode.getName();
		// if (content != null && content.length() > 0) {// 空的也加起
		// catalogNodeBuffer.append("\tcontent=").append(content).append("\n");
		// } else {
		// catalogNodeBuffer.append("\tcontent=\n");
		// }
		// if (name != null && name.length() > 0) {
		// catalogNodeBuffer.append("\tname=").append(name).append("\n");
		// }
		// // 遍历第二层节点
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
		// catalogNodeBuffer.append("</help>\n");//标准到处格式需要添加这个标记，但游戏中不使用

		// 保存
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
		// 每次递归，添加一个"\t" , 用于排版
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
	 * 分析editor的内容，返回分析结果
	 * 
	 * @param editor
	 * @return 将要预览或保存的字符串
	 */
	public String analysisDocument() {
		StringBuffer outBuffer = new StringBuffer();
		// outBuffer.append("<help1-1> \n");
		// 取得根元素,根元素下面有两个元素，section root 和 bidi root，我们只解析section root下面的内容
		Element[] root = editor.getDocument().getRootElements();
		for (int i = 0; i < root.length; i++) {
			// Element element = root[0];//可以直接解析第一个节点元素
			Element element = root[i];//
			int sunElementCount = element.getElementCount();// 如果是section则有4个子节点
			for (int j = 0; j < sunElementCount; j++) {
				// 取得第一层节点元素，带有换行的图片，换行符号当作文本
				Element firstLayerElement = element.getElement(j);
				// 遍历一个元素，带有换行的图片先解析图片再解析换行
				for (int k = 0; k < firstLayerElement.getElementCount(); k++) {
					// 进入第二层
					Element secondLayerElement = firstLayerElement.getElement(k);
					AttributeSet secondLayerAttr = secondLayerElement.getAttributes();

					Icon icon = StyleConstants.getIcon(secondLayerAttr);
					if (icon != null) {// 处理图片，读取图片的地址
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
								// System.out.println("文档中位置 :[" + startOff +
								// "-----" + endOff + " ]是图片，名字：" + imageName);
							}
						}

					} else {
						int startOff = secondLayerElement.getStartOffset();
						int endOff = secondLayerElement.getEndOffset();
						try {
							String text = editor.getText(startOff, endOff - startOff);
							// String copy=text;
							if (text.equals("\n")) {// 情况3,4 ：直接加<br>
								outBuffer.append("<<br>>");
								// System.out.println("文档中位置 :[" + startOff +
								// "-----" + endOff + " ]是换行符号!");
							} else if (text.trim().length() == 0) {
								// System.out.println("kong bai"+text+"jsdjflksdjf");
								outBuffer.append(text);

							} else {
								// System.out.println("文档中位置 :[" + startOff +
								// "-----" + endOff + " ]是纯文本!");
								/**
								 * 在这里处理读入的文本,有以下几种情况 ；1：格式一样的文本为一个Element
								 * 2：有多种格式的一行文本； 3：图片后面的换行； 4：单纯的换行
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
	 * 检查文档片段格式是否是普通格式，
	 * 如果该文档片段有：加粗，字体不等于15，颜色不等于默认值等情况之一则将格式信息提取出来用于构造包含格式信息的字符串，否则直接返回字符串
	 * 
	 * @param attributeSet
	 *            待检查文档片段的格式属性
	 * @param result
	 *            待检查文档片段
	 * @return
	 */
	private String analysisStyle(AttributeSet attributeSet, String result) {
		StringBuffer stringBuffer = new StringBuffer();
		int fontSize = StyleConstants.getFontSize(attributeSet);
		Color foreground = StyleConstants.getForeground(attributeSet);
		boolean isBold = StyleConstants.isBold(attributeSet);

		String conventColorString = convertToColorName(foreground);
		// 常规格式则直接返回,黑色当常规处理
		if (fontSize == 15 && foreground.equals(Utils.RIGHT_FONT_COLOR) && !isBold || foreground.equals(Color.BLACK)) {
			return result;
		} else {
			// <<font size='15' color='normal_color' name='宋体' style='B'>>
			stringBuffer.append("<<font size='").append(fontSize).append("' color='").append(conventColorString).append("' name='宋体'").append(isBold ? " style='B'>>" : ">>").append(result).append("<</font>>");
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
		if (content == null) {// is null说明为新建的文档，显示提示内容
			StyleConstants.setFontSize(attr, 15);
			StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
			editor.getDocument().insertString(0, "请在这里输入正文.....", attr);
		} else {

			Scanner scanner = new Scanner(content);
			scanner.useDelimiter("<<?");
			while (scanner.hasNext()) {
				String next = scanner.next();
				StyleConstants.setFontSize(attr, 15);
				StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
				// // 解析纯文本
				// /font>>【拳】【刀】【剑】【琴】
				if (!next.contains(">>") && !next.contains(">")) {
					// // 没有包含”>>“，说明是纯文本
					editor.getDocument().insertString(editor.getCaretPosition(), next, attr);
					// // System.out.println(next);
					// next = null;
					//
				} else if (next.trim().startsWith("br>>")) {// 解析换行 主要结构 1:br>>
					// ,2:
					// // br>> sdfsdfsdfd
					String[] splitBr = next.split(">>");

					editor.getDocument().insertString(editor.getCaretPosition(), "\n", null);
					// // System.out.println(splitBr[0]);
					if (splitBr.length > 1) {
						// // br>> sdfsdfsdfd ：换行符号后面有紧跟文字内容的情况
						// <<br>><<br>> <<font 分割后为： “br>> ”
						StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
						StyleConstants.setBold(attr, false);// 需设置
						editor.getDocument().insertString(editor.getCaretPosition(), splitBr[1], attr);

					}
					for (int i = 0; i < splitBr.length; i++) {
						splitBr[i] = null;
					}
					// // 解析图片
				} else if (next.startsWith("pic>")) {
					String[] picSplit = next.split(">");
					// picSplit[1]==image name
					// 暂时用一张图片替代,但是保存时还是保存原来的名字
					if (picSplit.length > 1) {
						ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("com/help/res/help.png"));
						imageIcon.setDescription(picSplit[1]);
						editor.insertIcon(imageIcon);
					}
					// // let gc do it work
					for (int i = 0; i < picSplit.length; i++) {
						picSplit[i] = null;
					}
					// // 解析有格式的文字
				} else if (next.startsWith("/font>")) {
					String[] fontSplit = next.split(">>");
					if (fontSplit.length > 1) {
						StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
						StyleConstants.setBold(attr, false);// 需设置
						StyleConstants.setFontSize(attr, 15);// 需设置
						editor.getDocument().insertString(editor.getCaretPosition(), fontSplit[1], attr);
					}

				} else {
					if (next.toLowerCase().startsWith("font ")) {
						String[] splitAC = next.split(">>");// 0:属性，1：内容
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
								// 颜色处理
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
											StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);// 没有匹配
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
						// // 处理内容
						editor.getDocument().insertString(editor.getCaretPosition(), splitAC[1], attr);
						// 因为font属性是成对出现的，在这里去掉介素标志
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
			editor.getDocument().insertString(editor.getDocument().getLength(), "帮助文档编辑器 \n\n", attr);
			StyleConstants.setFontSize(attr, 20);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  1：顶部工具栏提供按钮功能依次为【新建|打开|保存|另存为】\n\n", attr);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  2：选中左边树形节点，右击鼠标可以【新建|删除|重命名】子目录或正文\n\n", attr);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  3：新建文档自动默认保存在【C:/help.cfg】\n\n", attr);
			editor.getDocument().insertString(editor.getDocument().getLength(), "  4：文档默认自动保存，时间间隔：60秒，也可点击工具栏保存按钮保存修改\n\n", attr);
			// editor.insertIcon(imageIcon);
			// editor.setEditable(false);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// 启动定时保存,暂时设置为30秒自动保存一次
	public void startStoreTimer() {
		timer = new Timer();
		timer.schedule(new StoreTimer(), time * 1000, time * 1000);
		System.out.println("启动定时保存........");
	}

	public void stopStoreTimer() {
		timer.cancel();
	}

	// 定时保存，需要当编辑文档时启动
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
					System.out.println("自动保存完成: Path:" + path + "........");
				}
			}
		}

	}

	// public static void main(String[] args) {
	// try {
	// DataService.getInstance().load("C:/Documents and Settings/Administrator/桌面/help/help.xml");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
