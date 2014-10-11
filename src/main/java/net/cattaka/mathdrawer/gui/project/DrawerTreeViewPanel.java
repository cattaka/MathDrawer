package net.cattaka.mathdrawer.gui.project;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.cattaka.mathdrawer.MdResourceConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.BundleDrawer;
import net.cattaka.mathdrawer.drawer.ColorDrawer;
import net.cattaka.mathdrawer.drawer.CustomDrawer;
import net.cattaka.mathdrawer.drawer.CustomPixelShaderDrawer;
import net.cattaka.mathdrawer.drawer.Drawer;
import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.drawer.ImageDrawer;
import net.cattaka.mathdrawer.drawer.RootDrawer;
import net.cattaka.mathdrawer.gui.DrawerTreeView;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.gui.DrawerTreeView.DrawerTreeNode;
import net.cattaka.mathdrawer.setting.entity.MdProject;
import net.cattaka.mathdrawer.util.MdGuiUtil;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;

public class DrawerTreeViewPanel extends JPanel implements MdEditorInterface {
	private static final long serialVersionUID = 1L;
	private DrawerTreeView drawerTreeView;
	private MdEditorInterface parentComponent;
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			TreePath treePath = drawerTreeView.getSelectionPath();
			if (treePath == null) {
				Object obj = drawerTreeView.getModel().getRoot();
				treePath = new TreePath(obj);
			}
			if (treePath != null && treePath.getLastPathComponent() instanceof DrawerTreeNode) {
				DrawerTreeNode drawerTreeNode = (DrawerTreeNode)treePath.getLastPathComponent();
				if (e.getActionCommand().equals("add_drawer_item")) {
					addDrawerItem(drawerTreeNode);
				} else if (e.getActionCommand().equals("add_drawer_bundle")) {
					addDrawerBundle(drawerTreeNode);
				} else if (e.getActionCommand().equals("remove_drawer")) {
					removeDrawer(drawerTreeNode);
				} else if (e.getActionCommand().equals("move_up_drawer")) {
					moveUpDrawer(drawerTreeNode);
				} else if (e.getActionCommand().equals("move_down_drawer")) {
					moveDownDrawer(drawerTreeNode);
				}
			}
		}
	}
	
	class KeyListenerImpl extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			TreePath treePath = drawerTreeView.getSelectionPath();
			if (treePath != null && treePath.getLastPathComponent() instanceof DrawerTreeNode) {
				DrawerTreeNode drawerTreeNode = (DrawerTreeNode)treePath.getLastPathComponent();
				if (e.getKeyCode() == KeyEvent.VK_UP && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
					moveUpDrawer(drawerTreeNode);
					e.consume();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
					moveDownDrawer(drawerTreeNode);
					e.consume();
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					removeDrawer(drawerTreeNode);
					e.consume();
				}
			}
		}
	}
	
	class TreeSelectionListenerImpl implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			Object component = e.getPath().getLastPathComponent();
			if (component != null && component instanceof DrawerTreeNode) {
				selectedDrawerChanged();
			}
		}
	}

	public DrawerTreeViewPanel(MdEditorInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}

	private void makeLayout() {
		this.drawerTreeView = new DrawerTreeView();
		this.drawerTreeView.addTreeSelectionListener(new TreeSelectionListenerImpl());
		this.drawerTreeView.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.drawerTreeView.addKeyListener(new KeyListenerImpl());
		StdScrollPane drawerTreeViewPane = new StdScrollPane(drawerTreeView); 
		
		ActionListenerImpl al = new ActionListenerImpl();
		JToolBar toolBar = new JToolBar();
		{
			JButton addItemButton = new JButton();
			JButton addBundleButton = new JButton();
			JButton removeButton = new JButton();
			JButton moveUpButton = new JButton();
			JButton moveDownButton = new JButton();
		
			MdSingletonBundle mdSingletonBundle = getMdSingletonBundle();
			Icon iconAddItem = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_ADD_ITEM);
			Icon iconAddBundle = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_ADD_BUNDLE);
			Icon iconRemove = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_REMOVE);
			Icon iconMoveUp = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_ARROW_UP);
			Icon iconMoveDown = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_ARROW_DOWN);
			ButtonsBundle.applyButtonDifinition(addItemButton, iconAddItem, "add_drawer_item", true);
			ButtonsBundle.applyButtonDifinition(addBundleButton, iconAddBundle, "add_drawer_bundle", true);
			ButtonsBundle.applyButtonDifinition(removeButton, iconRemove, "remove_drawer", true);
			ButtonsBundle.applyButtonDifinition(moveUpButton, iconMoveUp, "move_up_drawer", true);
			ButtonsBundle.applyButtonDifinition(moveDownButton, iconMoveDown, "move_down_drawer", true);
	
			addItemButton.addActionListener(al);
			addItemButton.setActionCommand("add_drawer_item");
			addBundleButton.addActionListener(al);
			addBundleButton.setActionCommand("add_drawer_bundle");
			removeButton.addActionListener(al);
			removeButton.setActionCommand("remove_drawer");
			moveUpButton.addActionListener(al);
			moveUpButton.setActionCommand("move_up_drawer");
			moveDownButton.addActionListener(al);
			moveDownButton.setActionCommand("move_down_drawer");
	
			toolBar.setFloatable(false);
			toolBar.add(addItemButton);
			toolBar.add(addBundleButton);
			toolBar.add(removeButton);
			toolBar.add(moveUpButton);
			toolBar.add(moveDownButton);
		}
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(toolBar, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(drawerTreeViewPane, gbc);
		
		this.setLayout(gbl);
		this.add(toolBar);
		this.add(drawerTreeViewPane);
	}
	
	/**
	 * @param drawerTreeNode
	 */
	public void addDrawerItem(DrawerTreeNode drawerTreeNode) {
		Drawer drawer = null;
		{
			DrawerSelectDialog drawerSelectDialog = new DrawerSelectDialog();
			try {
				Drawer[] drawerSelection = new Drawer[] {
					new ImageDrawer(),
					new ColorDrawer()	,
					new CustomDrawer(),
					new CustomPixelShaderDrawer()
				};
				drawer = drawerSelectDialog.showSelectDrawerDialog(drawerSelection);
			} finally {
				drawerSelectDialog.dispose();
			}
		}
		if (drawer != null) {
			insertDrawer(drawerTreeNode, drawer);
		}
	}

	/**
	 * @param drawerTreeNode
	 */
	public void addDrawerBundle(DrawerTreeNode drawerTreeNode) {
		Drawer drawer = null;
		{
			drawer = new BundleDrawer();
//			DrawerSelectDialog drawerSelectDialog = new DrawerSelectDialog();
//			try {
//				Drawer[] drawerSelection = new Drawer[] {
//					new BundleDrawer()
//				};
//				drawer = drawerSelectDialog.showSelectDrawerDialog(drawerSelection);
//			} finally {
//				drawerSelectDialog.dispose();
//			}
		}
		if (drawer != null) {
			insertDrawer(drawerTreeNode, drawer);
		}
	}

	/**
	 * @param drawerTreeNode
	 */
	public void removeDrawer(DrawerTreeNode drawerTreeNode) {
		DrawerTreeNode node = drawerTreeNode;
		if (JOptionPane.showConfirmDialog(MdGuiUtil.getParentFrame(this), MessageBundle.getMessage("confirm_remove_drawer"), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
			DrawerTreeNode parentNode = node.getParent();
			if (parentNode != null) {
				drawerTreeView.getModel().removeNodeFromParent(node);
			}
		}
	}
	private void insertDrawer(DrawerTreeNode position, Drawer target) {
		DrawerTreeNode newChild = new DrawerTreeView.DrawerTreeNode(null, target);
		DrawerTreeNode node = position;
		int pos = 0;
		if (!node.isLeaf()) {
			pos = node.getChildCount();
		}
		while (node != null) {
			if (!node.isLeaf()) {
				drawerTreeView.getModel().insertNodeInto(newChild, node, pos);
				newChild.getDrawer().setParentDrawer(node.getDrawer());
				break;
			} else {
				DrawerTreeNode child = node; 
				node = node.getParent();
				pos = node.getIndex(child);
			}
		}
	}
	
	public void moveUpDrawer(DrawerTreeNode target) {
		DrawerTreeNode moveTo = target;
		int pos = -1;
		if (moveTo.getParent() != null) {
			pos = moveTo.getParent().getIndex(moveTo) - 1;
			moveTo = moveTo.getParent();
			if (pos >= 0) {
				if (!moveTo.getChildAt(pos).isLeaf() && drawerTreeView.isExpanded(treeNodeToTreePath(moveTo.getChildAt(pos)))) {
					moveTo = (DrawerTreeNode)moveTo.getChildAt(pos);
					pos = moveTo.getChildCount();
				}
			} else if (moveTo.getParent() != null) {
				pos = moveTo.getParent().getIndex(moveTo);
				moveTo = moveTo.getParent();
			} else {
				pos = -1;
			}
		}
		if (pos >= 0) {
			drawerTreeView.getModel().removeNodeFromParent(target);
			drawerTreeView.getModel().insertNodeInto(target, moveTo, pos);
			drawerTreeView.addSelectionPath(treeNodeToTreePath(target));
		}
	}
	public void moveDownDrawer(DrawerTreeNode target) {
		DrawerTreeNode moveTo = target;
		int pos = -1;
		if (moveTo.getParent() != null) {
			pos = moveTo.getParent().getIndex(moveTo) + 1;
			moveTo = moveTo.getParent();
			if (0 <= pos && pos < moveTo.getChildCount()) {
				if (!moveTo.getChildAt(pos).isLeaf() && drawerTreeView.isExpanded(treeNodeToTreePath(moveTo.getChildAt(pos)))) {
					moveTo = (DrawerTreeNode)moveTo.getChildAt(pos);
					pos = 0;
				}
			} else if (moveTo.getParent() != null) {
				pos = moveTo.getParent().getIndex(moveTo) + 1;
				moveTo = moveTo.getParent();
			} else {
				pos = -1;
			}
		}
		if (0 <= pos && pos <= moveTo.getChildCount()) {
			drawerTreeView.getModel().removeNodeFromParent(target);
			drawerTreeView.getModel().insertNodeInto(target, moveTo, pos);
			drawerTreeView.addSelectionPath(treeNodeToTreePath(target));
		}
	}
	private TreePath treeNodeToTreePath(TreeNode src) {
		TreeNode node = src;
		int i=0;
		while(node != null) {
			node = node.getParent();
			i++;
		}
		
		node = src;
		Object[] path = new Object[i];
		while(node != null) {
			i--;
			path[i] = node;
			node = node.getParent();
		}
		return new TreePath(path);
	}
	
	/**
	 * オーバーライド用
	 * @param drawerTreeNode
	 */
	public void selectedDrawerChanged() {
	}
	
	public DrawerTreeNode getCurrentNode() {
		DrawerTreeNode result = null;
		TreePath treePath = drawerTreeView.getSelectionPath();
		if (treePath != null && treePath.getLastPathComponent() instanceof DrawerTreeNode) {
			result = (DrawerTreeNode)treePath.getLastPathComponent();
		}
		
		return result;
	}

	public DrawerTreeView getDrawerTreeView() {
		return drawerTreeView;
	}

	public DrawerUtil getDrawerUtil() {
		return parentComponent.getDrawerUtil();
	}

	public MdProject getMdProject() {
		return parentComponent.getMdProject();
	}

	public void reloadMdProject() {
		RootDrawer rootDrawer = getMdProject().getRootDrawer();
		this.drawerTreeView.setRootDrawer(rootDrawer);
	}

	public void doGuiLayout() {
		// TODO Auto-generated method stub
		
	}

	public MdConfig getMdConfig() {
		return parentComponent.getMdConfig();
	}

	public MdSingletonBundle getMdSingletonBundle() {
		return parentComponent.getMdSingletonBundle();
	}

	public void relayMdMessage(MdMessage mdMessage) {
		// TODO Auto-generated method stub
	}

	public void reloadMdConfig() {
		// TODO Auto-generated method stub
	}

	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}
}
