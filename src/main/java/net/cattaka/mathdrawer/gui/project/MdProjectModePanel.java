package net.cattaka.mathdrawer.gui.project;

import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.cattaka.mathdrawer.MdMessageConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.Drawer;
import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.gui.DrawerTreeView.DrawerTreeNode;
import net.cattaka.mathdrawer.gui.common.DrawerMdSettingEditorPanel;
import net.cattaka.mathdrawer.setting.editor.MdSettingChangeListener;
import net.cattaka.mathdrawer.setting.entity.MdProject;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.mathdrawer.util.MdGuiUtil;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.util.ExceptionHandler;

public class MdProjectModePanel extends JPanel implements MdEditorInterface {
	private static final long serialVersionUID = 1L;
	private MdEditorInterface parentComponent;
	private DrawerTreeViewPanelEx drawerTreeViewPanel;
	private DrawerMdSettingEditorPanel drawerMdSettingEditorPanel;
	private DrawerPreviewPanel drawerPreviewPanel;
	private JSplitPane editorPane;
	private JSplitPane splitPane;

	class MdSettingChangeListenerImpl implements MdSettingChangeListener {
		public void mdSettingChange(String key, Object value) {
//			System.out.println(this.getClass().getName() + " : " + key + " : " + value);
			DrawerTreeNode node = drawerTreeViewPanel.getCurrentNode();
			if (node != null) {
				Map<String, Object> values;
				values = node.getDrawer().getMdSetting().getValues();
				values.put(key, value);
				node.getDrawer().getMdSetting().setValues(values);
				node.getDrawer().setNeedUpdate(true);
				drawerPreviewPanel.displayCurrentDrawer(node.getDrawer());
				updateMdSetting();
				drawerTreeViewPanel.nodeChange(node);
			}
		}
	}
	
	class DrawerTreeViewPanelEx extends DrawerTreeViewPanel {
		private static final long serialVersionUID = 1L;

		public DrawerTreeViewPanelEx(MdEditorInterface parentComponent) {
			super(parentComponent);
		}

		@Override
		public void selectedDrawerChanged() {
			updateMdSetting();
		}
		
		public void nodeChange(DrawerTreeNode node) {
			getDrawerTreeView().getModel().nodeChanged(node);
		}
	}
	
	class DrawerMdSettingEditorPanelEx extends DrawerMdSettingEditorPanel {
		private static final long serialVersionUID = 1L;
		
		public DrawerMdSettingEditorPanelEx(MdEditorInterface parentComponent, MdSettingChangeListener mdSettingChangeListener) {
			super(parentComponent, mdSettingChangeListener);
		}
		@Override
		public void doOk() {
			DrawerTreeNode node = drawerTreeViewPanel.getCurrentNode();
			if (node != null) {
				Map<String, Object> values;
				try {
					values = drawerMdSettingEditorPanel.getValues();
					node.getDrawer().getMdSetting().setValues(values);
					node.getDrawer().setNeedUpdate(true);
					drawerPreviewPanel.displayCurrentDrawer(node.getDrawer());
				} catch (InvalidValueException e) {
					JOptionPane.showMessageDialog(MdGuiUtil.getParentFrame(MdProjectModePanel.this), e.getMessage());
				}
				drawerTreeViewPanel.nodeChange(node);
			}
		}
		
		@Override
		public void doCancel() {
			DrawerTreeNode node = drawerTreeViewPanel.getCurrentNode();
			if (node != null) {
				Map<String,Object> values;
				try {
					values = node.getDrawer().getMdSetting().getValues();
					drawerMdSettingEditorPanel.setValues(values);
				} catch (InvalidValueException e) {
					JOptionPane.showMessageDialog(MdGuiUtil.getParentFrame(MdProjectModePanel.this), e.getMessage());
				}
			}
		}
	}
	
	public MdProjectModePanel(MdEditorInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		MdSettingChangeListenerImpl mdSettingChangeListener = new MdSettingChangeListenerImpl();
		
		this.drawerTreeViewPanel = new DrawerTreeViewPanelEx(this);
		this.drawerMdSettingEditorPanel = new DrawerMdSettingEditorPanelEx(this, mdSettingChangeListener);
		this.drawerPreviewPanel = new DrawerPreviewPanel(this);
		StdScrollPane drawerMdSettingEditorPanelPane = new StdScrollPane(drawerMdSettingEditorPanel);
		StdScrollPane drawerPreviewPanelPane = new StdScrollPane(drawerPreviewPanel);
		
		editorPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, drawerMdSettingEditorPanelPane, drawerPreviewPanelPane);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.drawerTreeViewPanel, editorPane);
		
		this.setLayout(new GridLayout());
		this.add(splitPane);
		
		updateMdSetting();
	}
	
	private void updateMdSetting() {
		DrawerTreeNode currentNode = drawerTreeViewPanel.getCurrentNode();
		if (currentNode != null) {
			Drawer drawer = currentNode.getDrawer();
			MdSetting mdSetting = drawer.getMdSetting();
			drawerPreviewPanel.displayCurrentDrawer(drawer);
			drawerMdSettingEditorPanel.setMdSettingConstraints(mdSetting.getMdSettingConstraints(), drawer.getDrawerUtil());
			try {
				drawerMdSettingEditorPanel.setValues(mdSetting.getValues());
				drawerMdSettingEditorPanel.setMessage(mdSetting.getMessage());
			} catch (InvalidValueException e) {
				ExceptionHandler.error(e);
			}
		} else {
			drawerMdSettingEditorPanel.setMdSettingConstraints(null, null);
		}
		drawerMdSettingEditorPanel.validate();
		MdGuiUtil.doRepaint(drawerMdSettingEditorPanel);
	}
	// Interface -----------------------------------------
	
	public void doGuiLayout() {
		splitPane.setDividerLocation(0.3);
		editorPane.setDividerLocation(0.5);
	}

	public MdConfig getMdConfig() {
		return parentComponent.getMdConfig();
	}

	public MdSingletonBundle getMdSingletonBundle() {
		return parentComponent.getMdSingletonBundle();
	}

	public void relayMdMessage(MdMessage mdMessage) {
		if (mdMessage.getOwner() == this
			|| mdMessage.getOwner() == this.drawerTreeViewPanel
			|| mdMessage.getOwner() == this.drawerMdSettingEditorPanel
			|| mdMessage.getOwner() == this.drawerPreviewPanel
		) {
			if (MdMessageConstants.MD_PROJECT_DRAW_LAYER.equals(mdMessage.getMessage())) {
				if (mdMessage.getData() != null && mdMessage.getData() instanceof Drawer) {
					Drawer drawer = (Drawer)mdMessage.getData();
					getDrawerUtil().drawLayer(drawer);
					drawerPreviewPanel.setMessage(drawer.getMessage());
					drawerPreviewPanel.repaint();
				}
			} else if (MdMessageConstants.MD_PROJECT_DRAW_IMAGE.equals(mdMessage.getMessage())) {
				if (mdMessage.getData() != null && mdMessage.getData() instanceof Drawer) {
					Drawer drawer = (Drawer)mdMessage.getData();
					getDrawerUtil().drawImage(drawer);
					drawerPreviewPanel.setMessage(drawer.getMessage());
					drawerPreviewPanel.repaint();
				}
			}
		} else {
			this.drawerTreeViewPanel.relayMdMessage(mdMessage);
			this.drawerMdSettingEditorPanel.relayMdMessage(mdMessage);
			this.drawerPreviewPanel.relayMdMessage(mdMessage);
		}
	}

	public void reloadMdConfig() {
		this.drawerTreeViewPanel.reloadMdConfig();
	}

	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}

	public DrawerUtil getDrawerUtil() {
		return parentComponent.getDrawerUtil();
	}

	public MdProject getMdProject() {
		return parentComponent.getMdProject();
	}
	
	public void reloadMdProject() {
		this.drawerTreeViewPanel.reloadMdProject();
		this.drawerMdSettingEditorPanel.reloadMdProject();
	}
}
