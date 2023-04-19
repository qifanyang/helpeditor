package com.help;

import java.awt.Color;

public class Utils {

	public static final Color RIGHT_FONT_COLOR = new Color(255, 243, 198);
	public static final Color BACKGROUD_COLOR = new Color(33, 24, 8);

	enum FontName {
		NORMAL("宋体");

		private String name;

		private FontName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public enum ColorName {
		NORMAL_COLOR("常规", RIGHT_FONT_COLOR), YELLOW("黄色", Color.YELLOW), RED("红色", Color.RED), GREEN("绿色", Color.GREEN);

		private String name;
		private Color color;

		private ColorName(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		@Override
		public String toString() {
			return name;
		}

		public Color getColor() {
			return color;
		}
	}

//	public static final void setCharacterAttributes(JEditorPane editor, AttributeSet attr, boolean replace) {
//		int p0 = editor.getSelectionStart();
//		int p1 = editor.getSelectionEnd();
//		if (p0 != p1) {
//			StyledDocument doc = getStyledDocument(editor);
//			doc.setCharacterAttributes(p0, p1 - p0, attr, replace);
//		}
//		StyledEditorKit k = (StyledEditorKit) editor.getEditorKit();
//		MutableAttributeSet inputAttributes = k.getInputAttributes();
//		if (replace) {
//			inputAttributes.removeAttributes(inputAttributes);
//		}
//		inputAttributes.addAttributes(attr);
//	}
//
//	public static final StyledDocument getStyledDocument(JEditorPane editor) {
//		Document document = editor.getDocument();
//		if (document instanceof StyledDocument) {
//			return (StyledDocument) document;
//		}
//		throw new IllegalArgumentException("document must be StyledDocument");
//	}
//
//	public static void setInputFontColor(JTextPane editor) {
//		Font initFont = new Font(null, Font.PLAIN, 15);
//		MutableAttributeSet attr = new SimpleAttributeSet();
//		StyleConstants.setForeground(attr, Utils.RIGHT_FONT_COLOR);
//		((StyledEditorKit) editor.getEditorKit()).getInputAttributes().addAttributes(attr);
//		editor.setFont(initFont);
//		editor.setCaretColor(Color.WHITE);
//		editor.setBackground(Utils.BACKGROUD_COLOR);
//	}

	public static void main(String[] args) {
		System.out.println(ColorName.values()[0].name().toLowerCase());
	}
}
