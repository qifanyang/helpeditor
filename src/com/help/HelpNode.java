package com.help;

import java.util.ArrayList;

/**
 * 
 * @author qifan.yang
 * 
 */
public class HelpNode {
    private HelpNode parentNode;
    private String content;
    private String name;
    private ArrayList<HelpNode> children;
    // private int contentHash = -1;
//    private boolean isChange = false;

    /**
     * 
     * @param name
     *            节点名称
     * @param type
     *            节点类型，1-表示树节点，2-表示叶节点
     */
    public HelpNode(String name, int type) {
	parentNode = null;
	content = null;
	this.name = name;
	if (type == 1) {
	    children = new ArrayList<HelpNode>();
	} else if (type == 2) {
	    children = null;
	} else {
	    throw new IllegalArgumentException("not support node type :" + type);
	}
    }

    public HelpNode(String name) {
	parentNode = null;
	content = null;
	this.name = name;
	children = new ArrayList<HelpNode>();
    }

    public void addNode(HelpNode helpNode) {
	if (helpNode.getParentNode() == null || helpNode.getParentNode() != this) {
	    helpNode.setParentNode(this);
	}
	children.add(helpNode);
    }

    public void removeNode(HelpNode helpNode) {
	children.remove(helpNode);
    }

    public HelpNode getNode(int index) {
	return children.get(index);
    }

    public ArrayList<HelpNode> getChildrenNode() {
	return children;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public HelpNode getParentNode() {
	return parentNode;
    }

    // public int getContentHash() {
    // return contentHash;
    // }
    //
    // public void setContentHash(int contentHash) {
    // this.contentHash = contentHash;
    // }

//    public boolean isChange() {
//	return isChange;
//    }
//
//    public void setChange(boolean isChange) {
//	this.isChange = isChange;
//    }

    public void setParentNode(HelpNode parentNode) {
	// if (!parentNode.getChildrenNode().contains(this)) {
	// parentNode.addNode(this);
	// }
	this.parentNode = parentNode;
    }

    public void removeAll() {
	children.clear();
    }

}
