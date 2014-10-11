/*
 * Copyright (c) 2009, Takao Sumitomo
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the 
 *       above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce
 *       the above copyright notice, this list of
 *       conditions and the following disclaimer in the
 *       documentation and/or other materials provided
 *       with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should
 * not be interpreted as representing official policies,
 * either expressed or implied.
 */
/*
 * $Id: RdbAssistantPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.gui;

import java.awt.Desktop;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.cattaka.mathdrawer.MdMessageConstants;
import net.cattaka.mathdrawer.MdResourceConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.config.MdConfigUtil;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.custom.CustomDrawerProcess;
import net.cattaka.mathdrawer.drawer.custom.CustomPixelShader;
import net.cattaka.mathdrawer.gui.MdModeInterface.TargetMenu;
import net.cattaka.mathdrawer.gui.config.MdConfigEditorDialog;
import net.cattaka.mathdrawer.gui.drawer.MdDrawerJavaSourceConverter;
import net.cattaka.mathdrawer.gui.pixelshader.MdPixelShaderJavaSourceConverter;
import net.cattaka.mathdrawer.jspf.MdJspfBundle;
import net.cattaka.mathdrawer.util.DocumentDialog;
import net.cattaka.mathdrawer.util.MdGuiUtil;
import net.cattaka.swing.StdStatusBar;
import net.cattaka.swing.TextFileChooser;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.text.FindConditionDialog;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.swing.util.ExceptionViewDialog;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.ExtFileFilter;
import net.cattaka.util.MessageBundle;

public class MathDrawerPanel extends JPanel implements MdGuiInterface {
	private static final long serialVersionUID = 1L;
//	private MdDocumentTabbedPane mdDocumentTabbedPane;
	private MdModeTabbedPane mdModeTabbedPane;
	private MdConfig mdConfig = new MdConfig();
	private FindConditionDialogEx findConditionDialog;
	private StdStatusBar stdStatusBar;
	private JMenuBar menuBar;
	private MdSingletonBundle mdSingletonBundle;
	private JFileChooser configFileChooser;
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("license")) {
				DocumentDialog dd = new DocumentDialog(MdGuiUtil.getParentFrame(MathDrawerPanel.this), MessageBundle.getMessage("license"), "net/cattaka/mathdrawer/docs/license.utf8.txt");
				dd.setSize(500, 500);
				dd.setModal(true);
				dd.setLocationRelativeTo(MathDrawerPanel.this);
				dd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dd.setVisible(true);
			} else if (e.getActionCommand().equals("readme")) {
				DocumentDialog dd = new DocumentDialog(MdGuiUtil.getParentFrame(MathDrawerPanel.this), MessageBundle.getMessage("readme"), "net/cattaka/mathdrawer/docs/readme.utf8.txt");
				dd.setSize(500, 500);
				dd.setModal(true);
				dd.setLocationRelativeTo(MathDrawerPanel.this);
				dd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dd.setVisible(true);
			} else if (e.getActionCommand().equals("about")) {
				DocumentDialog dd = new DocumentDialog(MdGuiUtil.getParentFrame(MathDrawerPanel.this), MessageBundle.getMessage("about"), "net/cattaka/mathdrawer/docs/about.utf8.txt");
				dd.setSize(400, 200);
				dd.setModal(true);
				dd.setLocationRelativeTo(MathDrawerPanel.this);
				dd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dd.setVisible(true);
			} else if (e.getActionCommand().equals("javadocOfScript")) {
				try {
					File file = new File("docs/apidocs/index.html");
					Desktop.getDesktop().browse(file.toURI());
				} catch (IOException ex) {
					ExceptionHandler.error(ex);
				}
			} else if (e.getActionCommand().equals("config")) {
				MdConfigEditorDialog dialog = new MdConfigEditorDialog(MdGuiUtil.getParentFrame(MathDrawerPanel.this));
				MdConfig mdConfig = MathDrawerPanel.this.getMdConfig();
				dialog.loadMdConfig(mdConfig);
				if (dialog.showEditor()) {
					dialog.saveMdConfig(mdConfig);
					MathDrawerPanel.this.reloadMdConfig();
				}
				dialog.dispose();
			} else if (e.getActionCommand().equals("showLogList")) {
				ExceptionViewDialog dialog = ExceptionHandler.getExceptionViewDialog();
				dialog.setLocationRelativeTo(MathDrawerPanel.this);
				dialog.setVisible(true);
			} else if (e.getActionCommand().equals("import_config")) {
				doImportConfig();
			} else if (e.getActionCommand().equals("export_config")) {
				doExportConfig();
			} else if (e.getActionCommand().equals("menu_exit")) {
				doExit();
			}
		}
	}
	
	class FindConditionDialogEx extends FindConditionDialog {
		private static final long serialVersionUID = 1L;
		
		public FindConditionDialogEx(Frame owner) throws HeadlessException {
			super(owner);
		}

		@Override
		public void doAction(FindCondition findCondition) {
			MdMessage mm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_ACTION,null,null,findCondition);
			relayMdMessage(mm);
		}
	}
	
	public MathDrawerPanel(MdConfig mdConfig) {
		this.mdConfig = mdConfig;
		mdSingletonBundle = new MdSingletonBundle();
		makeLayout();
		this.reloadMdConfig();
	}
	
	public void makeLayout() {
		// JSPF
		{
			assert(false);
			MdJspfBundle<CustomDrawerProcess> customDrawerBundle = new MdJspfBundle<CustomDrawerProcess>(new MdDrawerJavaSourceConverter(), CustomDrawerProcess.class);
			MdJspfBundle<CustomPixelShader> customPixelShaderBundle = new MdJspfBundle<CustomPixelShader>(new MdPixelShaderJavaSourceConverter(), CustomPixelShader.class);
			mdSingletonBundle.setCustomDrawerBundle(customDrawerBundle);
			mdSingletonBundle.setCustomPixelShaderBundle(customPixelShaderBundle);
		}
		// コンフィグファイル用ダイアログ
		{
			configFileChooser = new JFileChooser();
			configFileChooser.setFileFilter(new ExtFileFilter(".xml", MessageBundle.getMessage("config_xml_file")));
		}
		
		Icon gcIcon = (Icon)this.getMdSingletonBundle().getResource(MdResourceConstants.ICON_GC);
		this.menuBar = new JMenuBar();
		this.stdStatusBar = new StdStatusBar(gcIcon);
		this.mdModeTabbedPane = new MdModeTabbedPane(this);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(mdModeTabbedPane,gbc);
		gbc.gridy++;
		gbc.weighty=0;
		gbl.setConstraints(stdStatusBar,gbc);
		
		this.setLayout(gbl);
		this.add(mdModeTabbedPane);
		this.add(stdStatusBar);
		stdStatusBar.startMemoryUpdate();
		
		// メニューバーを作成(作成のみで配置は親パネル任せ)
		updateMenuBar();
	}
	
	private void updateMenuBar() {
		ActionListenerImpl al = new ActionListenerImpl();
		this.menuBar.removeAll();
		JMenu fileMenu = new JMenu();
		ButtonsBundle.applyButtonDifinition(fileMenu, "menu_file");
		{
			JMenuItem importConfigMenu = new JMenuItem();
			JMenuItem exportConfigMenu = new JMenuItem();
			JMenuItem exitMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(importConfigMenu, "menu_import_config");
			ButtonsBundle.applyMenuDifinition(exportConfigMenu, "menu_export_config");
			ButtonsBundle.applyMenuDifinition(exitMenu, "menu_exit");
			importConfigMenu.setActionCommand("import_config");
			exportConfigMenu.setActionCommand("export_config");
			exitMenu.setActionCommand("menu_exit");
			importConfigMenu.addActionListener(al);
			exportConfigMenu.addActionListener(al);
			exitMenu.addActionListener(al);
			
			if (this.mdModeTabbedPane.updateMenu(TargetMenu.FILE_MENU, fileMenu)) {
				fileMenu.addSeparator();
			}
			fileMenu.add(importConfigMenu);
			fileMenu.add(exportConfigMenu);
			fileMenu.addSeparator();
			fileMenu.add(exitMenu);

			menuBar.add(fileMenu);
		}
		JMenu editMenu = new JMenu();
		ButtonsBundle.applyButtonDifinition(editMenu, "menu_edit");
		{
			JMenuItem configMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(configMenu, "menu_config");
			configMenu.setActionCommand("config");
			configMenu.addActionListener(al);
			
			if (this.mdModeTabbedPane.updateMenu(TargetMenu.EDIT_MENU, editMenu)) {
				editMenu.addSeparator();
			}
			editMenu.add(configMenu);

			menuBar.add(editMenu);
		}
		// Extraメニューを追加する
		{
			JMenu[] extraMenu = this.mdModeTabbedPane.getExtraMenu();
			for (JMenu menu:extraMenu) {
				menuBar.add(menu);
			}
		}
		JMenu helpMenu = new JMenu();
		ButtonsBundle.applyButtonDifinition(helpMenu, "menu_help");
		{
			JMenuItem readmeMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(readmeMenu, "menu_readme");
			readmeMenu.setActionCommand("readme");
			readmeMenu.addActionListener(al);
			helpMenu.add(readmeMenu);

			if(Desktop.isDesktopSupported()) {
				JMenuItem javadocOfScriptMenu = new JMenuItem();
				ButtonsBundle.applyMenuDifinition(javadocOfScriptMenu, "menu_script_javadoc");
				javadocOfScriptMenu.setActionCommand("javadocOfScript");
				javadocOfScriptMenu.addActionListener(al);
				helpMenu.add(javadocOfScriptMenu);
			}

			helpMenu.addSeparator();

			JMenuItem showLogList = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(showLogList, "menu_show_log_list");
			showLogList.setActionCommand("showLogList");
			showLogList.addActionListener(al);
			helpMenu.add(showLogList);

			helpMenu.addSeparator();

			JMenuItem licenseMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(licenseMenu, "menu_license");
			licenseMenu.setActionCommand("license");
			licenseMenu.addActionListener(al);
			helpMenu.add(licenseMenu);

			JMenuItem aboutMenu = new JMenuItem();
			ButtonsBundle.applyMenuDifinition(aboutMenu, "menu_about");
			aboutMenu.setActionCommand("about");
			aboutMenu.addActionListener(al);
			helpMenu.add(aboutMenu);
			
			menuBar.add(helpMenu);
		}
	}
	
	public JMenuBar getMenuBar() {
		return this.menuBar;
	}
		
	public void saveMdConfig() {
		MdConfigUtil.saveMdConfig(this.mdConfig);
	}
	
	public void createWindowRelatedObject() {
		// 検索ダイアログ
		Frame owner = MdGuiUtil.getParentFrame(this);
		findConditionDialog = new FindConditionDialogEx(owner);
		this.findConditionDialog.setLocationRelativeTo(this);

		File currentDirFile = new File(".");
		
		// ファイル選択ダイアログ
		TextFileChooser sqlFileChooser = new TextFileChooser(owner);
		sqlFileChooser.setCurrentDirectory(currentDirFile);
		mdSingletonBundle.setCustomPixcelShaderFileChooser(sqlFileChooser);

		TextFileChooser scriptFileChooser = new TextFileChooser(owner);
		scriptFileChooser.setCurrentDirectory(currentDirFile);
		mdSingletonBundle.setCustomDrawerFileChooser(scriptFileChooser);
		
		JFileChooser imageFileChooser = new JFileChooser();
		imageFileChooser.setCurrentDirectory(currentDirFile);
		mdSingletonBundle.setImageFileChooser(imageFileChooser);
		
		JFileChooser mdProjectFileChooser = new JFileChooser();
		mdProjectFileChooser.setCurrentDirectory(currentDirFile);
		mdSingletonBundle.setMdProjectFileChooser(mdProjectFileChooser);
	}
	
	public void doGuiLayout() {
		this.mdModeTabbedPane.doGuiLayout();
	}
	public MdConfig getMdConfig() {
		return this.mdConfig;
	}
	public void reloadMdConfig() {
		this.mdSingletonBundle.getCustomDrawerBundle().setWorkDir(mdConfig.getCustomDrawerRoot());
		this.mdSingletonBundle.getCustomPixelShaderBundle().setWorkDir(mdConfig.getCustomPixelShaderRoot());
		this.mdModeTabbedPane.reloadMdConfig();
	}
	
	public void doImportConfig() {
		if (configFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File configFile = configFileChooser.getSelectedFile();
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(configFile);
				this.mdConfig.loadFromXML(fin);
				this.reloadMdConfig();
			} catch(IOException e) {
				ExceptionHandler.warn(e);
				JOptionPane.showMessageDialog(this, e.getMessage());
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch(IOException e2) {
						ExceptionHandler.warn(e2);
					}
				}
			}
		}
	}
	public void doExportConfig() {
		if (configFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File configFile = configFileChooser.getSelectedFile();
			if (!configFile.exists() || JOptionPane.showConfirmDialog(this, String.format(MessageBundle.getMessage("already_exists_overwrite_file"),configFile.getName()), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				FileOutputStream fout = null;
				try {
					fout = new FileOutputStream(configFile);
					this.mdConfig.storeToXML(fout,"");
				} catch(IOException e) {
					ExceptionHandler.warn(e);
					JOptionPane.showMessageDialog(this, e.getMessage());
				} finally {
					if (fout != null) {
						try {
							fout.close();
						} catch(IOException e2) {
							ExceptionHandler.warn(e2);
						}
					}
				}
			}
		}
	}
	public void doExit() {
		MdGuiUtil.getParentFrame(this).setVisible(false);
	}
	
	public void sendMdMessage(MdMessage mdMessage) {
		// このパネルが一番上
		relayMdMessage(mdMessage);
	}
	public void relayMdMessage(MdMessage mdMessage) {
		if (mdMessage.getMessage().equals(MdMessageConstants.MD_PANEL_UPDATEMENU)) {
			this.updateMenuBar();
			this.menuBar.repaint();
		} else if (mdMessage.getMessage().equals(MdMessageConstants.FINDCONDITIONDIALOG_SHOW)) {
			if (mdMessage.getData() != null) {
				String data = mdMessage.getData().toString();
				if (data.length() > 0) {
					FindCondition fc = findConditionDialog.getFindCondition();
					fc.setSearch(data);
					findConditionDialog.setFindCondition(fc);
				}
			}
			findConditionDialog.setVisible(true);
		} else if (mdMessage.getMessage().equals(MdMessageConstants.FINDCONDITIONDIALOG_DIRECT)) {
			FindCondition findCondition;
			if (mdMessage.getData() != null) {
				findCondition = (FindCondition)mdMessage.getData();
				this.findConditionDialog.setFindCondition(findCondition);
			} else {
				findCondition = this.findConditionDialog.getFindCondition();
			}
			MdMessage rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_ACTION,null,null,findCondition);
			relayMdMessage(rm);
		} else if (mdMessage.getMessage().equals(MdMessageConstants.MD_STATUSBAR_MESSAGE)) {
			Object message = mdMessage.getData();
			if (message != null) {
				this.stdStatusBar.setMessage(message.toString());
			}
		} else {
			this.mdModeTabbedPane.relayMdMessage(mdMessage);
		}
	}
	public MdSingletonBundle getMdSingletonBundle() {
		return this.mdSingletonBundle;
	}

	
	// -- delegate --------------------------------------------------
	public void openDocument(File file) {
		mdModeTabbedPane.openDocument(file);
	}
	
}
