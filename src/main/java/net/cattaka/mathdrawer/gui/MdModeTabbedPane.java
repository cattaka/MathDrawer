package net.cattaka.mathdrawer.gui;

import java.awt.Component;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.cattaka.mathdrawer.MdMessageConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.gui.drawer.MdDrawerModePanel;
import net.cattaka.mathdrawer.gui.pixelshader.MdPixelShaderModePanel;
import net.cattaka.util.MessageBundle;

public class MdModeTabbedPane extends JTabbedPane implements MdModeInterface {
	private static final long serialVersionUID = 1L;
	private MdGuiInterface parentComponent;
	private MdDocumentTabbedPane mdDocumentTabbedPane;
	private MdDrawerModePanel mdDrawerModePanel;
	private MdPixelShaderModePanel mdPixelShaderModePanel;
	
	class ChangeListenerImpl implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			sendMdMessage(new MdMessage(MdMessageConstants.MD_PANEL_UPDATEMENU, null, MdModeTabbedPane.this, null));
		}
	}

	public MdModeTabbedPane(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		initialize();
	}
	
	private void initialize() {
		this.mdDocumentTabbedPane = new MdDocumentTabbedPane(this);
		this.mdDrawerModePanel = new MdDrawerModePanel(this);
		this.mdPixelShaderModePanel = new MdPixelShaderModePanel(this);
		this.add(MessageBundle.getMessage("title_project"), this.mdDocumentTabbedPane);
		this.add(MessageBundle.getMessage("title_drawer"), this.mdDrawerModePanel);
		this.add(MessageBundle.getMessage("title_pixelshader"), this.mdPixelShaderModePanel);

		// タブ操作についての処理を追加
		this.addChangeListener(new ChangeListenerImpl());
	}
	
	public MdModeInterface getCurrentPanel() {
		Component comp = this.getSelectedComponent();
		if (comp instanceof MdModeInterface) {
			return (MdModeInterface)comp;
		} else {
			return null;
		}
	}
	
	public void openDocument(File file) {
		this.mdDocumentTabbedPane.openDocument(file);
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
		this.mdDocumentTabbedPane.reloadMdConfig();
		this.mdDrawerModePanel.reloadMdConfig();
		this.mdPixelShaderModePanel.reloadMdConfig();
	}

	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}
	
	public JMenu[] getExtraMenu() {
		return this.getCurrentPanel().getExtraMenu();
	}
	public boolean updateMenu(TargetMenu targetMenu, JMenu menu) {
		return this.getCurrentPanel().updateMenu(targetMenu, menu);
	}

}
