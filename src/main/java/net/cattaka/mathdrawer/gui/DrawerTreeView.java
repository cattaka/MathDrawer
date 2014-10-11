package net.cattaka.mathdrawer.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import net.cattaka.mathdrawer.drawer.Drawer;

public class DrawerTreeView extends JTree {
	private static final long serialVersionUID = 1L;

	public static class DrawerTreeNode implements MutableTreeNode {
		private Drawer drawer;
		private DrawerTreeNode parent;
		private ArrayList<DrawerTreeNode> children;
		
		public DrawerTreeNode(DrawerTreeNode parent, Drawer drawer) throws NullPointerException {
			if (drawer == null) {
				throw new NullPointerException();
			}
			this.drawer = drawer;
			this.parent = parent;
			List<Drawer> childList = this.drawer.getChildrenList();
			if (childList != null) {
				this.children = new ArrayList<DrawerTreeNode>();
				for (Drawer child:childList) {
					this.children.add(new DrawerTreeNode(this, child));
				}
			}
		}
		
		public Enumeration<DrawerTreeNode> children() {
			return Collections.enumeration(this.children);
		}

		public boolean getAllowsChildren() {
			return (this.children != null);
		}

		public TreeNode getChildAt(int childIndex) {
			return this.children.get(childIndex);
		}

		public int getChildCount() {
			return this.children.size();
		}

		public int getIndex(TreeNode node) {
			return this.children.indexOf(node);
		}

		public DrawerTreeNode getParent() {
			return this.parent;
		}

		public boolean isLeaf() {
			return (this.children == null);
		}
		
		@Override
		public String toString() {
			return drawer.getLabel();
		}

		public Drawer getDrawer() {
			return drawer;
		}

		// 以下、操作
		public void insert(MutableTreeNode child, int index) {
			DrawerTreeNode dtn = (DrawerTreeNode)child;
			child.setParent(this);
			this.children.add(index, dtn);
			this.drawer.addChild(index, dtn.getDrawer());
//			this.drawer.setParentDrawer(dtn.getDrawer());
		}

		public void remove(int index) {
			this.getDrawer().removeChild(index);
			DrawerTreeNode node = this.children.remove(index);
			if (node != null) {
				node.setParent(null);
//				node.getDrawer().setParentDrawer(null);
			}
		}

		public void remove(MutableTreeNode node) {
			DrawerTreeNode aChild = (DrawerTreeNode)node;
			if (aChild == null) {
			    throw new IllegalArgumentException("argument is null");
			}

			if (!isNodeChild(aChild)) {
			    throw new IllegalArgumentException("argument is not a child");
			}
			remove(getIndex(aChild));	// linear search
		}

		public void removeFromParent() {
			MutableTreeNode parent = (MutableTreeNode)getParent();
			if (parent != null) {
			    parent.remove(this);
			}
		}

		public void setParent(MutableTreeNode newParent) {
			this.parent = (DrawerTreeNode)newParent;
		}

		public void setUserObject(Object object) {
			// TODO Auto-generated method stub
		}

		public boolean isNodeChild(TreeNode aNode) {
			boolean retval;

			if (aNode == null) {
				retval = false;
			} else {
				if (getChildCount() == 0) {
					retval = false;
				} else {
					retval = (aNode.getParent() == this);
				}
			}

			return retval;
		}
	}
	/*
	 * public static class DrawerTreeNode implements TreeNode { private Drawer
	 * drawer; private DrawerTreeNode parent; private ArrayList<DrawerTreeNode>
	 * children;
	 * 
	 * public DrawerTreeNode(DrawerTreeNode parent, Drawer drawer) throws
	 * NullPointerException { if (drawer == null) { throw new
	 * NullPointerException(); } this.drawer = drawer; this.parent = parent;
	 * List<Drawer> childList = this.drawer.getChildrenList(); if (childList !=
	 * null) { this.children = new ArrayList<DrawerTreeNode>(); for (Drawer
	 * child:childList) { this.children.add(new DrawerTreeNode(this, child)); } } }
	 * 
	 * public Enumeration<DrawerTreeNode> children() { return
	 * Collections.enumeration(this.children); }
	 * 
	 * public boolean getAllowsChildren() { return (this.children != null); }
	 * 
	 * public TreeNode getChildAt(int childIndex) { return
	 * this.children.get(childIndex); }
	 * 
	 * public int getChildCount() { return this.children.size(); }
	 * 
	 * public int getIndex(TreeNode node) { return this.children.indexOf(node); }
	 * 
	 * public DrawerTreeNode getParent() { return this.parent; }
	 * 
	 * public boolean isLeaf() { return (this.children == null); }
	 * 
	 * @Override public String toString() { return drawer.getLabel(); }
	 * 
	 * public Drawer getDrawer() { return drawer; } }
	 */
	
	public void setRootDrawer(Drawer rootDrawer) {
		if (rootDrawer != null) {
			DrawerTreeNode rootNode = new DrawerTreeNode(null, rootDrawer);
			DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
			this.setModel(treeModel);
		} else {
			this.setModel(null);
		}
	}
	@Override
	public DefaultTreeModel getModel() {
		return (DefaultTreeModel)super.getModel();
	}
}
