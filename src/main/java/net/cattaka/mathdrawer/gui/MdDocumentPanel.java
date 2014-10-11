package net.cattaka.mathdrawer.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.ColorDrawer;
import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.drawer.RootDrawer;
import net.cattaka.mathdrawer.gui.MdGuiInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.gui.MdModeInterface.TargetMenu;
import net.cattaka.mathdrawer.gui.preset.MdPresetModePanel;
import net.cattaka.mathdrawer.gui.project.MdProjectModePanel;
import net.cattaka.mathdrawer.setting.entity.MdProject;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class MdDocumentPanel extends JTabbedPane implements MdEditorInterface {
	private static final long serialVersionUID = 1L;
	private MdGuiInterface parentComponent;
	private MdProjectModePanel mdProjectModePanel;
	private MdPresetModePanel mdPresetModePanel;
	private MdProject mdProject;
	private DrawerUtil drawerUtil;
	private File documentFile;
	
	public MdDocumentPanel(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;

		this.makeLayout();
	}
	private void makeLayout() {
		this.drawerUtil = new DrawerUtil(this);
		
		this.mdProjectModePanel = new MdProjectModePanel(this);
		this.mdPresetModePanel = new MdPresetModePanel(this);
		
		this.add(MessageBundle.getMessage("title_project"), this.mdProjectModePanel);
		this.add(MessageBundle.getMessage("title_preset"), this.mdPresetModePanel);
	}
	
	public void createNewDocument() {
		MdProject newMdProject = new MdProject();
		RootDrawer rootDrawer = newMdProject.getRootDrawer();
		ColorDrawer colorDrawer = new ColorDrawer();
		rootDrawer.addChild(colorDrawer);
		colorDrawer.setParentDrawer(rootDrawer);
		this.mdProject = newMdProject;
		this.documentFile = null;
		this.mdProject.initialize(this);
		this.reloadMdProject();
	}

	public boolean openDocument() {
		JFileChooser fileChooser = this.getMdSingletonBundle().getMdProjectFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return this.openDocument(file);
		} else {
			return false;
		}
	}

	public boolean openDocument(File file) {
		MdProject mp = null;
		ZipInputStream zin = null;
		try {
			zin = new ZipInputStream(new FileInputStream(file));
			mp = MdProject.readMdProject(zin);
			// 成功
			this.mdProject = mp;
			this.mdProject.initialize(this);
			this.documentFile = file;
			this.reloadMdProject();
		} catch (IOException e) {
			ExceptionHandler.error(e);
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch(IOException e) {
					ExceptionHandler.error(e);
				}
			}
		}
		return (mp != null);
	}

	public boolean saveDocument() {
		boolean result = false;
		if (this.documentFile != null) {
			ZipOutputStream zout = null;
			try {
				zout = new ZipOutputStream(new FileOutputStream(this.documentFile));
				MdProject.writeMdProject(zout, this.mdProject);
				// 成功
			} catch (IOException e) {
				ExceptionHandler.error(e);
			} finally {
				if (zout != null) {
					try {
						zout.close();
					} catch(IOException e) {
						ExceptionHandler.error(e);
					}
				}
			}
		} else {
			result = saveAsDocument();
		}
		return result;
	}
	
	public boolean saveAsDocument() {
		boolean result = false;
		JFileChooser fileChooser = this.getMdSingletonBundle().getMdProjectFileChooser();
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			ZipOutputStream zout = null;
			try {
				zout = new ZipOutputStream(new FileOutputStream(file));
				MdProject.writeMdProject(zout, this.mdProject);
				this.documentFile = file;
				// 成功
			} catch (IOException e) {
				ExceptionHandler.error(e);
			} finally {
				if (zout != null) {
					try {
						zout.close();
					} catch(IOException e) {
						ExceptionHandler.error(e);
					}
				}
			}
		}
		return result;
	}

	public File getDocumentFile() {
		return documentFile;
	}
	
	// Interface ---------------------------------
	public void doGuiLayout() {
		this.mdProjectModePanel.doGuiLayout();
		this.mdPresetModePanel.doGuiLayout();
	}

	public MdConfig getMdConfig() {
		return parentComponent.getMdConfig();
	}

	public MdSingletonBundle getMdSingletonBundle() {
		return parentComponent.getMdSingletonBundle();
	}

	public void relayMdMessage(MdMessage mdMessage) {
		this.mdProjectModePanel.relayMdMessage(mdMessage);
		this.mdPresetModePanel.relayMdMessage(mdMessage);
	}

	public void reloadMdConfig() {
		this.mdProjectModePanel.reloadMdConfig();
		this.mdPresetModePanel.reloadMdConfig();
	}

	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}

	public JMenu[] getExtraMenu() {
		return new JMenu[0];
	}

	public boolean updateMenu(TargetMenu targetMenu, JMenu menu) {
		return false;
	}
	public DrawerUtil getDrawerUtil() {
		return this.drawerUtil;
	}
	public MdProject getMdProject() {
		return this.mdProject;
	}
	public void reloadMdProject() {
		this.mdProjectModePanel.reloadMdProject();
		this.mdPresetModePanel.reloadMdProject();
	}
}
