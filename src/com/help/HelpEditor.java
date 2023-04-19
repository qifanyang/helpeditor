package com.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 帮助系统编辑器
 * 
 * @author qifan.yang
 * 
 */
public class HelpEditor extends JFrame {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;

    public static BufferedImage icon;
    public static HelpEditor instancEditor;

    private static final long serialVersionUID = 1L;

    public HelpEditor() {

    }

    private void initUI() {
	try {
	    // icon =
	    // ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/help/seasky32.png"));
	    icon = ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/help/res/long.png"));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	// icon = this.getToolkit().getImage("seasky16.png");
	setIconImage(icon);
	setTitle("帮助系统编辑器");
	setLayout(new BorderLayout());

	// create editor panel
	EditorPanel editorPanel = new EditorPanel();

//	setJMenuBar(new TopMenu());
	JToolBar toolBar = new CustomToolBar(editorPanel);

	add(toolBar, BorderLayout.PAGE_START);
	add(editorPanel, BorderLayout.CENTER);
	setSize(new Dimension(WIDTH, HEIGHT));
	setExtendedState(JFrame.MAXIMIZED_BOTH); 
	setLocationRelativeTo(null);
	setVisible(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	instancEditor = this;

	
    }

    public static HelpEditor getIntance() {
	return instancEditor;
    }

    public static void main(String[] dd) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	HelpEditor helpEditor = new HelpEditor();
	helpEditor.initUI();
    }
}
