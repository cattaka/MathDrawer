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
 * $Id: MdDrawerEditorTabbedPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.gui.drawer;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.mathdrawer.MdMessageConstants;
import net.cattaka.mathdrawer.MdResourceConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.gui.MdGuiInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.gui.drawer.MdDrawerEditorPanel.RESULT_PANEL;
import net.cattaka.mathdrawer.util.CloseableTabbedPane;
import net.cattaka.mathdrawer.util.CloseableTabbedPaneListener;
import net.cattaka.mathdrawer.util.MdGuiUtil;
import net.cattaka.swing.TextFileChooser;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;

public class MdDrawerEditorTabbedPanel extends JPanel implements MdGuiInterface {
	private static final long serialVersionUID = 1L;

	private MdGuiInterface parentComponent;
	private CloseableTabbedPane tabbedPane;

	class ButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("new_drawer")) {
				addTab();
			} else if (e.getActionCommand().equals("open_drawer")) {
				openDrawer();
			} else if (e.getActionCommand().equals("save_drawer")) {
				saveDrawer(null);
			} else if (e.getActionCommand().equals("saveas_drawer")) {
				saveAsDrawer(null);
			} else if (e.getActionCommand().equals("run_drawer")) {
				Component comp = tabbedPane.getSelectedComponent();
				if (comp instanceof MdDrawerEditorPanel) {
					((MdDrawerEditorPanel)comp).runDrawer();
				}
			} else if (e.getActionCommand().equals("compile_drawer")) {
				Component comp = tabbedPane.getSelectedComponent();
				if (comp instanceof MdDrawerEditorPanel) {
					((MdDrawerEditorPanel)comp).compileDrawer();
				}
			} else if (e.getActionCommand().equals("edit_find")) {
				MdMessage rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_SHOW, null, MdDrawerEditorTabbedPanel.this, null);
				sendMdMessage(rm);
			}
		}
	}
	
	class CloseableTabbedPaneListenerEx implements CloseableTabbedPaneListener {
		public boolean closeTab(int tabIndexToClose) {
			boolean result = true;
			Component comp = tabbedPane.getComponentAt(tabIndexToClose);
			if (comp instanceof MdDrawerEditorPanel) {
				MdDrawerEditorPanel rsep = ((MdDrawerEditorPanel)comp); 
				result = disposeTab(rsep);
			}
			return result;
		}
	}
	
	public MdDrawerEditorTabbedPanel(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		ButtonAction al = new ButtonAction();
		JToolBar toolBar = new JToolBar();
		JButton newDrawerButton = new JButton();
		JButton openDrawerButton = new JButton();
		JButton saveDrawerButton = new JButton();
		JButton saveAsDrawerButton = new JButton();
		JButton runDrawerButton = new JButton();
		JButton compileDrawerButton = new JButton();
		JButton editFindButton = new JButton();
		{
			MdSingletonBundle rdbaSingletonBundle = getMdSingletonBundle();
			Icon iconNew = (Icon)rdbaSingletonBundle.getResource(MdResourceConstants.ICON_NEW);
			Icon iconOpen = (Icon)rdbaSingletonBundle.getResource(MdResourceConstants.ICON_OPEN);
			Icon iconSave = (Icon)rdbaSingletonBundle.getResource(MdResourceConstants.ICON_SAVE);
			Icon iconSaveAs = (Icon)rdbaSingletonBundle.getResource(MdResourceConstants.ICON_SAVEAS);
			Icon iconRun = (Icon)rdbaSingletonBundle.getResource(MdResourceConstants.ICON_RUN);
			Icon iconCompile = (Icon)rdbaSingletonBundle.getResource(MdResourceConstants.ICON_COMPILE);
			Icon iconFind = (Icon)rdbaSingletonBundle.getResource(MdResourceConstants.ICON_FIND);
			ButtonsBundle.applyButtonDifinition(newDrawerButton, iconNew, "file_new", true);
			ButtonsBundle.applyButtonDifinition(openDrawerButton, iconOpen, "file_open", true);
			ButtonsBundle.applyButtonDifinition(saveDrawerButton, iconSave, "file_save", true);
			ButtonsBundle.applyButtonDifinition(saveAsDrawerButton, iconSaveAs, "file_save_as", true);
			ButtonsBundle.applyButtonDifinition(runDrawerButton, iconRun, "run_drawer", true);
			ButtonsBundle.applyButtonDifinition(compileDrawerButton, iconCompile, "compile_drawer", true);
			ButtonsBundle.applyButtonDifinition(editFindButton, iconFind, "search_replace", true);
		}

		newDrawerButton.addActionListener(al);
		newDrawerButton.setActionCommand("new_drawer");
		runDrawerButton.addActionListener(al);
		runDrawerButton.setActionCommand("run_drawer");
		compileDrawerButton.addActionListener(al);
		compileDrawerButton.setActionCommand("compile_drawer");
		openDrawerButton.addActionListener(al);
		openDrawerButton.setActionCommand("open_drawer");
		saveDrawerButton.addActionListener(al);
		saveDrawerButton.setActionCommand("save_drawer");
		saveAsDrawerButton.addActionListener(al);
		saveAsDrawerButton.setActionCommand("saveas_drawer");
		editFindButton.addActionListener(al);
		editFindButton.setActionCommand("edit_find");

		toolBar.setFloatable(false);
		toolBar.add(newDrawerButton);
		toolBar.add(openDrawerButton);
		toolBar.add(saveDrawerButton);
		toolBar.add(saveAsDrawerButton);
		toolBar.addSeparator();
		toolBar.add(runDrawerButton);
		toolBar.add(compileDrawerButton);
		toolBar.addSeparator();
		toolBar.add(editFindButton);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		tabbedPane = new CloseableTabbedPane();
		tabbedPane.addCloseableTabbedPaneListener(new CloseableTabbedPaneListenerEx());
		//this.addTab();
		
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
		gbl.setConstraints(tabbedPane, gbc);
		
		this.setLayout(gbl);
		this.add(toolBar);
		this.add(tabbedPane);
	}
	
	public MdDrawerEditorPanel addTab() {
		int count = 0;
		String name = "t";
		outer:while (true) {
			count++;
			name = String.format("new%02d" + MdConstants.MD_DRAWER_EXT, count);
			for (int i=0;i<tabbedPane.getTabCount();i++) {
				if (name.equals(tabbedPane.getTitleAt(i))) {
					continue outer;
				}
			}
			break;
		}
		
		MdDrawerEditorPanel rdbaDrawerEditorPanel = new MdDrawerEditorPanel(this);
		tabbedPane.addTab(name, rdbaDrawerEditorPanel);
		tabbedPane.setSelectedComponent(rdbaDrawerEditorPanel);
		rdbaDrawerEditorPanel.reloadMdConfig();
		MdGuiUtil.doLayout(rdbaDrawerEditorPanel);
		
		return rdbaDrawerEditorPanel;
	}
	
	/**
	 * カーソル位置に文字列を挿入する。
	 * もしカーソル位置が空白でなければ、カンマで区切った後に挿入する。
	 */
	public void appendString(String str) {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.appendString(str);
		}
	}
	
	public MdDrawerEditorPanel getCurrentMdDrawerEditorPanel() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp != null && comp instanceof MdDrawerEditorPanel) {
			return ((MdDrawerEditorPanel)comp);
		} else {
			return null;
		}
	}
	
	private int getTabIndex(MdDrawerEditorPanel target) {
		for (int i=0;i<this.tabbedPane.getTabCount();i++) {
			if (this.tabbedPane.getComponentAt(i) == target) {
				return i;
			}
		}
		return -1;
	}
	
	private void setTabTitle(MdDrawerEditorPanel target, String title) {
		int index = getTabIndex(target);
		if (index != -1) {
			this.tabbedPane.setTitleAt(index, title);
		}
	}
	
	public boolean openDrawer() {
		boolean result = false;
		TextFileChooser fileChooser = getMdSingletonBundle().getCustomDrawerFileChooser();
		fileChooser.setLocationRelativeTo(this);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Charset charset = fileChooser.getCharset();
			result = openDrawer(file, charset);
		}
		return result;
	}
	public boolean openDrawer(File file, Charset charset) {
		boolean result = false;
		boolean existFlag = false;
		// 既に開いている場合はそれを表示
		for (int i=0;i<this.tabbedPane.getTabCount();i++) {
			Component comp = this.tabbedPane.getComponent(i);
			if (comp instanceof MdDrawerEditorPanel) {
				MdDrawerEditorPanel rsep = (MdDrawerEditorPanel)comp;
				if (rsep.getFile() != null && rsep.getFile().equals(file)) {
					this.tabbedPane.setSelectedComponent(comp);
					existFlag = true;
					break;
				}
			}
		}
		
		if (!existFlag) {
			MdDrawerEditorPanel rsep = addTab();
			result = rsep.loadDrawer(file, charset);
			setTabTitle(rsep, file.getName());
		}
		return result;
	}
	
	public boolean saveDrawer(MdDrawerEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentMdDrawerEditorPanel();
		}
		if (rsep != null) {
			if (rsep.getFile() != null && rsep.getCharset() != null) {
				result = rsep.saveDrawer(rsep.getFile(), rsep.getCharset());
				File file = rsep.getFile();
				setTabTitle(rsep, file.getName());
			} else {
				result = saveAsDrawer(rsep);
			}
		}
		return result;
	}
	
	public boolean saveAsDrawer(MdDrawerEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentMdDrawerEditorPanel();
		}
		if (rsep != null) {
			TextFileChooser fileChooser = getMdSingletonBundle().getCustomDrawerFileChooser();
			fileChooser.setLocationRelativeTo(this);
			if (rsep.getFile() != null) {
				fileChooser.setSelectedFile(rsep.getFile());
			}
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Charset charset = fileChooser.getCharset();
				result = rsep.saveDrawer(file, charset);
				setTabTitle(rsep, file.getName());
			}
		}
		return result;
	}
	public boolean closeTab() {
		boolean result = false;
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null && disposeTab(rsep)) {
			this.tabbedPane.removeTabAt(getTabIndex(rsep));
			result = true;
		}
		return result;
	}

	public void runDrawer() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp instanceof MdDrawerEditorPanel) {
			((MdDrawerEditorPanel)comp).runDrawer();
		}
	}
	public void compileDrawer() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp instanceof MdDrawerEditorPanel) {
			((MdDrawerEditorPanel)comp).compileDrawer();
		}
	}
	public void nextDrawer() {
		int idx = this.tabbedPane.getSelectedIndex() + 1;
		if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
			this.tabbedPane.setSelectedIndex(idx);
		}
	}		
	public void prevDrawer() {
		int idx = this.tabbedPane.getSelectedIndex() - 1;
		if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
			this.tabbedPane.setSelectedIndex(idx);
		}
	}
	
	public boolean disposeTab(MdDrawerEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentMdDrawerEditorPanel();
		}
		if (rsep != null) {
			if (rsep.isModified()) {
				int ans = JOptionPane.showConfirmDialog(this, MessageBundle.getMessage("save_the_changes_to_document"), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_CANCEL_OPTION);
				if (ans == JOptionPane.YES_OPTION) {
					result = saveDrawer(rsep);
				} else if (ans == JOptionPane.NO_OPTION) {
					result = true;
				}
			} else {
				result = true;
			}
			rsep.dispose();
		}
		return result;
	}
	
	// ===========================================================================
	public void doGuiLayout() {
		for (int i=0;i<tabbedPane.getComponentCount();i++) {
			Component comp = tabbedPane.getComponentAt(i);
			if (comp instanceof MdGuiInterface) {
				((MdGuiInterface)comp).doGuiLayout();
			}
		}
	}
	public MdConfig getMdConfig() {
		return this.parentComponent.getMdConfig();
	}
	
	public MdSingletonBundle getMdSingletonBundle() {
		return this.parentComponent.getMdSingletonBundle();
	}
	
	public void relayMdMessage(MdMessage rdbaMessage) {
		if (rdbaMessage.getTarget() == null) {
			// オーナー無しのメッセージの場合
			if (rdbaMessage.getMessage().equals(MdMessageConstants.FINDCONDITIONDIALOG_ACTION)) {
				// 検索処理：フォーカスされているタブに対して検索
				MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
				if (rsep != null) {
					FindCondition findCondition = (FindCondition)rdbaMessage.getData();
					rsep.doFindAction(findCondition);
				}
			}
		} else if (rdbaMessage.getTarget() == this) {
//			if (rdbaMessage.getMessage().equals(MdMessageConstants.RDBASQLEDITORPANEL_PREVDOC)) {
//				// Swingが拾ってくれないので仕方なく
//				int idx = this.tabbedPane.getSelectedIndex() - 1;
//				if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
//					this.tabbedPane.setSelectedIndex(idx);
//				}
//			} else if (rdbaMessage.getMessage().equals(MdMessageConstants.RDBASQLEDITORPANEL_NEXTDOC)) {
//				// Swingが拾ってくれないので仕方なく
//				int idx = this.tabbedPane.getSelectedIndex() + 1;
//				if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
//					this.tabbedPane.setSelectedIndex(idx);
//				}
//			} else {
				MdDrawerEditorPanel rsep = null;
				// 指定されたエディタを取得する。無ければ現在選択中の物
				{
					if (rdbaMessage.getOwner() != null && rdbaMessage.getOwner() instanceof MdDrawerEditorPanel) {
						rsep = (MdDrawerEditorPanel)rdbaMessage.getOwner();
					}
					if (rsep == null || this.getTabIndex(rsep) == -1) {
						rsep = getCurrentMdDrawerEditorPanel();
					}
				}
				int tabIndex = this.tabbedPane.indexOfComponent(rsep);
				
				if (rdbaMessage.getMessage().equals(MdMessageConstants.MD_PANEL_MODIFIED)) {
					if (tabIndex != -1) {
						String newTitle = "*"+this.tabbedPane.getTitleAt(tabIndex);
						this.tabbedPane.setTitleAt(tabIndex, newTitle);
					}
				}
//			}
		} else {
			// その他
			Component comp = tabbedPane.getSelectedComponent();
			if (comp instanceof MdGuiInterface) {
				((MdGuiInterface)comp).relayMdMessage(rdbaMessage);
			}
		}
	}
	
	public void reloadMdConfig() {
		for (int i=0;i<tabbedPane.getComponentCount();i++) {
			Component comp = tabbedPane.getComponentAt(i);
			if (comp instanceof MdGuiInterface) {
				((MdGuiInterface)comp).reloadMdConfig();
			}
		}
	}
	public void sendMdMessage(MdMessage rdbaMessage) {
		// 子コンポーネントからの特定のメッセージはその場で処理する
		this.parentComponent.sendMdMessage(rdbaMessage);
	}

	/** メニューイベント用 */
	public void openFindDialog() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.openFindDialog();
		}
	}
	/** メニューイベント用 */
	public void doCommentOut() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.doCommentOut();
		}
	}
	/** メニューイベント用 */
	public boolean canRedo() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			return rsep.canRedo();
		} else {
			return false;
		}
	}
	/** メニューイベント用 */
	public boolean canUndo() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			return rsep.canUndo();
		} else {
			return false;
		}
	}
	/** メニューイベント用 */
	public void redo() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.redo();
		}
	}
	/** メニューイベント用 */
	public void undo() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.undo();
		}
	}
	/** メニューイベント用 */
	public void copy() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.copy();
		}
	}
	/** メニューイベント用 */
	public void cut() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.cut();
		}
	}
	/** メニューイベント用 */
	public void paste() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.paste();
		}
	}
	/** メニューイベント用 */
	public void selectAll() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.selectAll();
		}
	}
	/** メニューイベント用 */
	public void searchNext() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.searchNext();
		}
	}
	/** メニューイベント用 */
	public void searchPrev() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.searchPrev();
		}
	}
	/** メニューイベント用 */
	public void switchResultPanel() {
		MdDrawerEditorPanel rsep = getCurrentMdDrawerEditorPanel();
		if (rsep != null) {
			rsep.switchResultPanel(RESULT_PANEL.RESULT_PANEL_ALTERNATE);
		}
	}
}
