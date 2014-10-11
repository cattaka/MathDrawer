package net.cattaka.mathdrawer.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.util.CloseableTabbedPane;
import net.cattaka.mathdrawer.util.MdGuiUtil;
import net.cattaka.swing.util.ButtonsBundle;

public class MdDocumentTabbedPane extends CloseableTabbedPane implements MdModeInterface {
	private static final long serialVersionUID = 1L;
	private MdGuiInterface parentComponent;
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("file_new")) {
				newDocument();
			} else if (e.getActionCommand().equals("file_open")) {
				openDocument();
			} else if (e.getActionCommand().equals("file_save")) {
				saveDocument();
			} else if (e.getActionCommand().equals("file_save_as")) {
				saveAsDocument();
			}
		}
	}

	public MdDocumentTabbedPane(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
	}
	
	private String generateNewTabLabel() {
		return "new01";
	}
	
	public MdDocumentPanel getCurrentPanel() {
		Component comp = this.getSelectedComponent();
		if (comp instanceof MdDocumentPanel) {
			return (MdDocumentPanel)comp;
		} else {
			return null;
		}
	}
	
	public void newDocument() {
		MdDocumentPanel mdDocumentPanel = new MdDocumentPanel(MdDocumentTabbedPane.this);
		mdDocumentPanel.createNewDocument();
		addTab(generateNewTabLabel(), mdDocumentPanel);
		mdDocumentPanel.reloadMdConfig();
		MdGuiUtil.doLayout(mdDocumentPanel);
	}
	public void openDocument() {
		MdDocumentPanel mdDocumentPanel = new MdDocumentPanel(MdDocumentTabbedPane.this);
		if (mdDocumentPanel != null && mdDocumentPanel.openDocument()) {
			addTab(mdDocumentPanel.getDocumentFile().getName(), mdDocumentPanel);
			mdDocumentPanel.reloadMdConfig();
			MdGuiUtil.doLayout(mdDocumentPanel);
		}
	}
	
	public void openDocument(File file) {
		MdDocumentPanel mdDocumentPanel = new MdDocumentPanel(MdDocumentTabbedPane.this);
		if (mdDocumentPanel != null && mdDocumentPanel.openDocument(file)) {
			addTab(mdDocumentPanel.getDocumentFile().getName(), mdDocumentPanel);
			mdDocumentPanel.reloadMdConfig();
			MdGuiUtil.doLayout(mdDocumentPanel);
		}
	}
	
	public void saveDocument() {
		MdDocumentPanel mdDocumentPanel = getCurrentPanel();
		if (mdDocumentPanel != null) {
			mdDocumentPanel.saveDocument();
		}
	}
	
	public void saveAsDocument() {
		MdDocumentPanel mdDocumentPanel = getCurrentPanel();
		if (mdDocumentPanel != null) {
			mdDocumentPanel.saveAsDocument();
		}
	}

	// Interface ------------------------------------
	
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
		this.getCurrentPanel().relayMdMessage(mdMessage);
	}

	public void reloadMdConfig() {
		// TODO Auto-generated method stub
	}

	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}
	public JMenu[] getExtraMenu() {
		// TODO Auto-generated method stub
		return new JMenu[0];
	}
	public boolean updateMenu(TargetMenu targetMenu, JMenu menu) {
		boolean result = false;
		switch (targetMenu) {
			case FILE_MENU:
			{
				ActionListenerImpl al = new ActionListenerImpl();
				
				JMenuItem newMenu = new JMenuItem();
				JMenuItem openMenu = new JMenuItem();
				JMenuItem saveMenu = new JMenuItem();
				JMenuItem saveAsMenu = new JMenuItem();
				ButtonsBundle.applyMenuDifinition(newMenu, "file_new");
				ButtonsBundle.applyMenuDifinition(openMenu, "file_open");
				ButtonsBundle.applyMenuDifinition(saveMenu, "file_save");
				ButtonsBundle.applyMenuDifinition(saveAsMenu, "file_save_as");
				newMenu.setActionCommand("file_new");
				openMenu.setActionCommand("file_open");
				saveMenu.setActionCommand("file_save");
				saveAsMenu.setActionCommand("file_save_as");
				newMenu.addActionListener(al);
				openMenu.addActionListener(al);
				saveMenu.addActionListener(al);
				saveAsMenu.addActionListener(al);
				
				menu.add(newMenu);
				menu.add(openMenu);
				menu.add(saveMenu);
				menu.add(saveAsMenu);
				result = true;
				break;
			}
			default:
			{
				break;
			}
		}
		return result;
	}

}
