package com.help;


import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;

/**
 * 自定义的JTextPane，主要是添加创建时，初始化字体属性
 * 
 * @author qifan.yang
 * 
 */
//todo 暂时不删除，这里有功能代码
public class CustomEditor extends JTextPane {

    private static final long serialVersionUID = 1L;

    public CustomEditor() {
	// Font initFont = new Font(null, Font.PLAIN, 15);
	// // 统一设置文字颜色
	// MutableAttributeSet attr = new SimpleAttributeSet();
	// StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
	// this.setCharacterAttributes(attr, true);
	// ((StyledEditorKit)
	// this.getEditorKit()).getInputAttributes().addAttributes(attr);
	// this.setFont(initFont);
	// this.setCaretColor(Utils.RIGHT_FONT_COLOR);
	// this.setBackground(Utils.BACKGROUD_COLOR);

    }

    /**
     * 解析helpNode节点内容
     * 
     * @param node
     * @throws BadLocationException
     */
    public void showCotent(HelpNode node) throws BadLocationException {
	System.out.println("Document Lenth :"+getDocument().getLength());
	MutableAttributeSet attr = new SimpleAttributeSet();
//	String ww = "sfasdjflsj萨达离开积分使肌肤 距离圣诞节分类数据法律是否距离圣诞节疯了快睡觉的冯绍峰老师积分是福建士大夫\n离开时大家飞就是来得快附近拉实际得分解释道发生江东父老开始觉得翻开历史大姐夫了斯柯达解放路上\n打开附件神龙岛飞sfasdjflsj萨达离开积分使肌肤 距离圣诞节分类数据法律是否距离圣诞节疯了快睡觉的冯绍峰老师积分是福建士大夫\n离开时大家飞就是来得快附近拉实际得分解释道发生江东父老开始觉得翻开历史大姐夫了斯柯达解放路上\n打开附件神龙岛飞";
	this.getDocument().remove(0, this.getDocument().getLength());
	String content = node.getContent();
	if (content == null)
	    return;
	StringBuffer buffer = new StringBuffer(content);
	String string = buffer.toString();
//	String nodeContent = new String(content);
//	String res = node.getName() + ww;
	System.out.println("LLLLLeng::::::"+string.length());
	this.getDocument().insertString(0, string.substring(0,string.length()), attr);
//	System.out.println(nodeContent);
	// Scanner scanner = new Scanner(content);
	// scanner.useDelimiter("<<?");
	// while (scanner.hasNext()) {
	// String next = scanner.next();
	// System.out.println(next);
	// // 解析纯文本
	// if (!next.contains(">>") && !next.contains(">")) {
	// // 没有包含”>>“，说明是纯文本
	// this.getDocument().insertString(getCaretPosition(), next, null);
	// // System.out.println(next);
	// next = null;
	//
	// } else if (next.trim().startsWith("br>>")) {// 解析换行 主要结构 1:br>> ,2:
	// // br>> sdfsdfsdfd
	// String[] splitBr = next.split(">>");
	//
	// this.getDocument().insertString(getCaretPosition(), "\n", null);
	// // System.out.println(splitBr[0]);
	// if (splitBr.length > 1) {
	// // br>> sdfsdfsdfd
	// StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
	// this.getDocument().insertString(getCaretPosition(), splitBr[1],
	// attr);
	//
	// }
	// for (int i = 0; i < splitBr.length; i++) {
	// splitBr[i] = null;
	// }
	// // 解析图片
	// } else if (next.startsWith("pic>")) {
	// String[] picSplit = next.split(">");
	// // picSplit[1]==image name
	// // 暂时用一张图片替代
	// if (picSplit.length > 1) {
	// ImageIcon imageIcon = new
	// ImageIcon(getClass().getResource("help.png"));
	// this.insertIcon(imageIcon);
	// }
	// // let gc do it work
	// for (int i = 0; i < picSplit.length; i++) {
	// picSplit[i] = null;
	// }
	// // 解析有格式的文字
	// } else {
	// if (next.toLowerCase().startsWith("font ")) {
	// String[] splitAC = next.split(">>");// 0:属性，1：内容
	//
	// // 处理属性
	// String[] fontAttr = splitAC[0].split(" ");
	// // MutableAttributeSet attr = new SimpleAttributeSet();
	// StyleConstants.setFontSize(attr, 16);
	// for (int i = 1; i < fontAttr.length; i++) {
	// int index = fontAttr[i].indexOf("=");
	// String key = fontAttr[i].substring(0, index);
	// String value = fontAttr[i].substring(index + 2, fontAttr[i].length()
	// - 1);
	// if (key.equalsIgnoreCase("size")) {
	// StyleConstants.setFontSize(attr, Integer.parseInt(value));
	//
	// } else if (key.equalsIgnoreCase("color")) {
	// // String color = fontAttr[i].substring(index + 2,
	// // fontAttr[i].length() - 1);
	// ColorName[] colorNames = ColorName.values();
	// for (int j = 0; j < colorNames.length; j++) {
	// String lowerCase = colorNames[j].name().toLowerCase();
	// if (lowerCase.equalsIgnoreCase(value)) {
	// StyleConstants.setForeground(attr, colorNames[j].getColor());
	// break;
	// } else {
	// StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);// 没有匹配
	// }
	// }
	// // StyleConstants.setForeground(attr, fg)
	// } else if (key.equalsIgnoreCase("name")) {
	// StyleConstants.setFontFamily(attr, value);
	// } else if (key.equalsIgnoreCase("style")) {
	// StyleConstants.setBold(attr, value.equalsIgnoreCase("B") ? true :
	// false);
	// }
	// key = null;
	// value = null;
	// }
	//
	// // Utils.setCharacterAttributes(this, attr, true);
	// setCharacterAttributes(attr, false);
	// // 处理内容
	// this.getDocument().insertString(getCaretPosition(), splitAC[1],
	// attr);
	// // System.out.println(splitAC[1]);
	// // 结束属性
	// // scanner.useDelimiter(">>");
	// scanner.next();
	// // scanner.useDelimiter("<<");
	// }
	// }
	// }
	// scanner.close();
    }

   

   
    public void showIndex() {
//	MutableAttributeSet attr = new SimpleAttributeSet();
//	StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
//	StyleConstants.setFontSize(attr, 35);
//	StyleConstants.setForeground(attr, Utils.ColorName.YELLOW.getColor());
//	StyleConstants.setBold(attr, true);
//	ImageIcon imageIcon = new ImageIcon(getClass().getResource("long.png"));
//	try {
//	    this.getDocument().remove(0, this.getDocument().getLength());
//	    this.getDocument().insertString(getCaretPosition(), "  帮助文档编辑器 V2.0", attr);
//	    this.insertIcon(imageIcon);
//	    this.setEditable(false);
//	    // currentEditor.setCharacterAttributes(attr2, false);
//	} catch (BadLocationException e) {
//	    e.printStackTrace();
//	}
    }
}
