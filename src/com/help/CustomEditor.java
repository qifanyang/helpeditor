package com.help;


import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;

/**
 * �Զ����JTextPane����Ҫ����Ӵ���ʱ����ʼ����������
 * 
 * @author qifan.yang
 * 
 */
//todo ��ʱ��ɾ���������й��ܴ���
public class CustomEditor extends JTextPane {

    private static final long serialVersionUID = 1L;

    public CustomEditor() {
	// Font initFont = new Font(null, Font.PLAIN, 15);
	// // ͳһ����������ɫ
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
     * ����helpNode�ڵ�����
     * 
     * @param node
     * @throws BadLocationException
     */
    public void showCotent(HelpNode node) throws BadLocationException {
	System.out.println("Document Lenth :"+getDocument().getLength());
	MutableAttributeSet attr = new SimpleAttributeSet();
//	String ww = "sfasdjflsj�����뿪����ʹ���� ����ʥ���ڷ������ݷ����Ƿ����ʥ���ڷ��˿�˯���ķ��ܷ���ʦ�����Ǹ���ʿ���\n�뿪ʱ��ҷɾ������ÿ츽����ʵ�ʵ÷ֽ��͵������������Ͽ�ʼ���÷�����ʷ������˹�´���·��\n�򿪸�����������sfasdjflsj�����뿪����ʹ���� ����ʥ���ڷ������ݷ����Ƿ����ʥ���ڷ��˿�˯���ķ��ܷ���ʦ�����Ǹ���ʿ���\n�뿪ʱ��ҷɾ������ÿ츽����ʵ�ʵ÷ֽ��͵������������Ͽ�ʼ���÷�����ʷ������˹�´���·��\n�򿪸�����������";
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
	// // �������ı�
	// if (!next.contains(">>") && !next.contains(">")) {
	// // û�а�����>>����˵���Ǵ��ı�
	// this.getDocument().insertString(getCaretPosition(), next, null);
	// // System.out.println(next);
	// next = null;
	//
	// } else if (next.trim().startsWith("br>>")) {// �������� ��Ҫ�ṹ 1:br>> ,2:
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
	// // ����ͼƬ
	// } else if (next.startsWith("pic>")) {
	// String[] picSplit = next.split(">");
	// // picSplit[1]==image name
	// // ��ʱ��һ��ͼƬ���
	// if (picSplit.length > 1) {
	// ImageIcon imageIcon = new
	// ImageIcon(getClass().getResource("help.png"));
	// this.insertIcon(imageIcon);
	// }
	// // let gc do it work
	// for (int i = 0; i < picSplit.length; i++) {
	// picSplit[i] = null;
	// }
	// // �����и�ʽ������
	// } else {
	// if (next.toLowerCase().startsWith("font ")) {
	// String[] splitAC = next.split(">>");// 0:���ԣ�1������
	//
	// // ��������
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
	// StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);// û��ƥ��
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
	// // ��������
	// this.getDocument().insertString(getCaretPosition(), splitAC[1],
	// attr);
	// // System.out.println(splitAC[1]);
	// // ��������
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
//	    this.getDocument().insertString(getCaretPosition(), "  �����ĵ��༭�� V2.0", attr);
//	    this.insertIcon(imageIcon);
//	    this.setEditable(false);
//	    // currentEditor.setCharacterAttributes(attr2, false);
//	} catch (BadLocationException e) {
//	    e.printStackTrace();
//	}
    }
}
