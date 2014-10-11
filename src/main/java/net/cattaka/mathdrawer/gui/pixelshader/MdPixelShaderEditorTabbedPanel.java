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
package net.cattaka.mathdrawer.gui.pixelshader;

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
import net.cattaka.mathdrawer.gui.pixelshader.MdPixelShaderEditorPanel.RESULT_PANEL;
import net.cattaka.mathdrawer.util.CloseableTabbedPane;
import net.cattaka.mathdrawer.util.CloseableTabbedPaneListener;
import net.cattaka.mathdrawer.util.MdGuiUtil;
import net.cattaka.swing.TextFileChooser;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;

public class MdPixelShaderEditorTabbedPanel extends JPanel implements MdGuiInterface {
	private static final long serialVersionUID = 1L;

	private MdGuiInterface parentComponent;
	private CloseableTabbedPane tabbedPane;

	class ButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("new_pixelshader")) {
				addTab();
			} else if (e.getActionCommand().equals("open_pixelshader")) {
				openPixelShader();
			} else if (e.getActionCommand().equals("save_pixelshader")) {
				savePixelShader(null);
			} else if (e.getActionCommand().equals("saveas_pixelshader")) {
				saveAsPixelShader(null);
			} else if (e.getActionCommand().equals("run_pixelshader")) {
				Component comp = tabbedPane.getSelectedComponent();
				if (comp instanceof MdPixelShaderEditorPanel) {
					((MdPixelShaderEditorPanel)comp).runPixelShader();
				}
			} else if (e.getActionCommand().equals("compile_pixelshader")) {
				Component comp = tabbedPane.getSelectedComponent();
				if (comp instanceof MdPixelShaderEditorPanel) {
					((MdPixelShaderEditorPanel)comp).compilePixelShader();
				}
			} else if (e.getActionCommand().equals("edit_find")) {
				MdMessage rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_SHOW, null, MdPixelShaderEditorTabbedPanel.this, null);
				sendMdMessage(rm);
			}
		}
	}
	
	class CloseableTabbedPaneListenerEx implements CloseableTabbedPaneListener {
		public boolean closeTab(int tabIndexToClose) {
			boolean result = true;
			Component comp = tabbedPane.getComponentAt(tabIndexToClose);
			if (comp instanceof MdPixelShaderEditorPanel) {
				MdPixelShaderEditorPanel rsep = ((MdPixelShaderEditorPanel)comp); 
				result = disposeTab(rsep);
			}
			return result;
		}
	}
	
	public MdPixelShaderEditorTabbedPanel(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		ButtonAction al = new ButtonAction();
		JToolBar toolBar = new JToolBar();
		JButton newPixelShaderButton = new JButton();
		JButton openPixelShaderButton = new JButton();
		JButton savePixelShaderButton = new JButton();
		JButton saveAsPixelShaderButton = new JButton();
		JButton runPixelShaderButton = new JButton();
		JButton compilePixelShaderButton = new JButton();
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
			ButtonsBundle.applyButtonDifinition(newPixelShaderButton, iconNew, "file_new", true);
			ButtonsBundle.applyButtonDifinition(openPixelShaderButton, iconOpen, "file_open", true);
			ButtonsBundle.applyButtonDifinition(savePixelShaderButton, iconSave, "file_save", true);
			ButtonsBundle.applyButtonDifinition(saveAsPixelShaderButton, iconSaveAs, "file_save_as", true);
			ButtonsBundle.applyButtonDifinition(runPixelShaderButton, iconRun, "run_pixelshader", true);
			ButtonsBundle.applyButtonDifinition(compilePixelShaderButton, iconCompile, "compile_pixelshader", true);
			ButtonsBundle.applyButtonDifinition(editFindButton, iconFind, "search_replace", true);
		}

		newPixelShaderButton.addActionListener(al);
		newPixelShaderButton.setActionCommand("new_pixelshader");
		runPixelShaderButton.addActionListener(al);
		runPixelShaderButton.setActionCommand("run_pixelshader");
		compilePixelShaderButton.addActionListener(al);
		compilePixelShaderButton.setActionCommand("compile_pixelshader");
		openPixelShaderButton.addActionListener(al);
		openPixelShaderButton.setActionCommand("open_pixelshader");
		savePixelShaderButton.addActionListener(al);
		savePixelShaderButton.setActionCommand("save_pixelshader");
		saveAsPixelShaderButton.addActionListener(al);
		saveAsPixelShaderButton.setActionCommand("saveas_pixelshader");
		editFindButton.addActionListener(al);
		editFindButton.setActionCommand("edit_find");

		toolBar.setFloatable(false);
		toolBar.add(newPixelShaderButton);
		toolBar.add(openPixelShaderButton);
		toolBar.add(savePixelShaderButton);
		toolBar.add(saveAsPixelShaderButton);
		toolBar.addSeparator();
		toolBar.add(runPixelShaderButton);
		toolBar.add(compilePixelShaderButton);
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
	
	public MdPixelShaderEditorPanel addTab() {
		int count = 0;
		String name = "t";
		outer:while (true) {
			count++;
			name = String.format("new%02d" + MdConstants.MD_PIXELSHADER_EXT, count);
			for (int i=0;i<tabbedPane.getTabCount();i++) {
				if (name.equals(tabbedPane.getTitleAt(i))) {
					continue outer;
				}
			}
			break;
		}
		
		MdPixelShaderEditorPanel rdbaPixelShaderEditorPanel = new MdPixelShaderEditorPanel(this);
		tabbedPane.addTab(name, rdbaPixelShaderEditorPanel);
		tabbedPane.setSelectedComponent(rdbaPixelShaderEditorPanel);
		rdbaPixelShaderEditorPanel.reloadMdConfig();
		MdGuiUtil.doLayout(rdbaPixelShaderEditorPanel);
		
		return rdbaPixelShaderEditorPanel;
	}
	
	/**
	 * カーソル位置に文字列を挿入する。
	 * もしカーソル位置が空白でなければ、カンマで区切った後に挿入する。
	 */
	public void appendString(String str) {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.appendString(str);
		}
	}
	
	public MdPixelShaderEditorPanel getCurrentMdPixelShaderEditorPanel() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp != null && comp instanceof MdPixelShaderEditorPanel) {
			return ((MdPixelShaderEditorPanel)comp);
		} else {
			return null;
		}
	}
	
	private int getTabIndex(MdPixelShaderEditorPanel target) {
		for (int i=0;i<this.tabbedPane.getTabCount();i++) {
			if (this.tabbedPane.getComponentAt(i) == target) {
				return i;
			}
		}
		return -1;
	}
	
	private void setTabTitle(MdPixelShaderEditorPanel target, String title) {
		int index = getTabIndex(target);
		if (index != -1) {
			this.tabbedPane.setTitleAt(index, title);
		}
	}
	
	public boolean openPixelShader() {
		boolean result = false;
		TextFileChooser fileChooser = getMdSingletonBundle().getCustomPixelShaderFileChooser();
		fileChooser.setLocationRelativeTo(this);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			Charset charset = fileChooser.getCharset();
			result = openPixelShader(file, charset);
		}
		return result;
	}
	public boolean openPixelShader(File file, Charset charset) {
		boolean result = false;
		boolean existFlag = false;
		// 既に開いている場合はそれを表示
		for (int i=0;i<this.tabbedPane.getTabCount();i++) {
			Component comp = this.tabbedPane.getComponent(i);
			if (comp instanceof MdPixelShaderEditorPanel) {
				MdPixelShaderEditorPanel rsep = (MdPixelShaderEditorPanel)comp;
				if (rsep.getFile() != null && rsep.getFile().equals(file)) {
					this.tabbedPane.setSelectedComponent(comp);
					existFlag = true;
					break;
				}
			}
		}
		
		if (!existFlag) {
			MdPixelShaderEditorPanel rsep = addTab();
			result = rsep.loadPixelShader(file, charset);
			setTabTitle(rsep, file.getName());
		}
		return result;
	}
	
	public boolean savePixelShader(MdPixelShaderEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentMdPixelShaderEditorPanel();
		}
		if (rsep != null) {
			if (rsep.getFile() != null && rsep.getCharset() != null) {
				result = rsep.savePixelShader(rsep.getFile(), rsep.getCharset());
				File file = rsep.getFile();
				setTabTitle(rsep, file.getName());
			} else {
				result = saveAsPixelShader(rsep);
			}
		}
		return result;
	}
	
	public boolean saveAsPixelShader(MdPixelShaderEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentMdPixelShaderEditorPanel();
		}
		if (rsep != null) {
			TextFileChooser fileChooser = getMdSingletonBundle().getCustomPixelShaderFileChooser();
			fileChooser.setLocationRelativeTo(this);
			if (rsep.getFile() != null) {
				fileChooser.setSelectedFile(rsep.getFile());
			}
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Charset charset = fileChooser.getCharset();
				result = rsep.savePixelShader(file, charset);
				setTabTitle(rsep, file.getName());
			}
		}
		return result;
	}
	public boolean closeTab() {
		boolean result = false;
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null && disposeTab(rsep)) {
			this.tabbedPane.removeTabAt(getTabIndex(rsep));
			result = true;
		}
		return result;
	}

	public void runPixelShader() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp instanceof MdPixelShaderEditorPanel) {
			((MdPixelShaderEditorPanel)comp).runPixelShader();
		}
	}
	public void compilePixelShader() {
		Component comp = tabbedPane.getSelectedComponent();
		if (comp instanceof MdPixelShaderEditorPanel) {
			((MdPixelShaderEditorPanel)comp).compilePixelShader();
		}
	}
	public void nextPixelShader() {
		int idx = this.tabbedPane.getSelectedIndex() + 1;
		if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
			this.tabbedPane.setSelectedIndex(idx);
		}
	}		
	public void prevPixelShader() {
		int idx = this.tabbedPane.getSelectedIndex() - 1;
		if (0 <= idx && idx < this.tabbedPane.getTabCount()) {
			this.tabbedPane.setSelectedIndex(idx);
		}
	}
	
	public boolean disposeTab(MdPixelShaderEditorPanel rsep) {
		boolean result = false;
		if (rsep == null) {
			rsep = getCurrentMdPixelShaderEditorPanel();
		}
		if (rsep != null) {
			if (rsep.isModified()) {
				int ans = JOptionPane.showConfirmDialog(this, MessageBundle.getMessage("save_the_changes_to_document"), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_CANCEL_OPTION);
				if (ans == JOptionPane.YES_OPTION) {
					result = savePixelShader(rsep);
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
				MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
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
				MdPixelShaderEditorPanel rsep = null;
				// 指定されたエディタを取得する。無ければ現在選択中の物
				{
					if (rdbaMessage.getOwner() != null && rdbaMessage.getOwner() instanceof MdPixelShaderEditorPanel) {
						rsep = (MdPixelShaderEditorPanel)rdbaMessage.getOwner();
					}
					if (rsep == null || this.getTabIndex(rsep) == -1) {
						rsep = getCurrentMdPixelShaderEditorPanel();
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
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.openFindDialog();
		}
	}
	/** メニューイベント用 */
	public void doCommentOut() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.doCommentOut();
		}
	}
	/** メニューイベント用 */
	public boolean canRedo() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			return rsep.canRedo();
		} else {
			return false;
		}
	}
	/** メニューイベント用 */
	public boolean canUndo() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			return rsep.canUndo();
		} else {
			return false;
		}
	}
	/** メニューイベント用 */
	public void redo() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.redo();
		}
	}
	/** メニューイベント用 */
	public void undo() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.undo();
		}
	}
	/** メニューイベント用 */
	public void copy() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.copy();
		}
	}
	/** メニューイベント用 */
	public void cut() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.cut();
		}
	}
	/** メニューイベント用 */
	public void paste() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.paste();
		}
	}
	/** メニューイベント用 */
	public void selectAll() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.selectAll();
		}
	}
	/** メニューイベント用 */
	public void searchNext() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.searchNext();
		}
	}
	/** メニューイベント用 */
	public void searchPrev() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.searchPrev();
		}
	}
	/** メニューイベント用 */
	public void switchResultPanel() {
		MdPixelShaderEditorPanel rsep = getCurrentMdPixelShaderEditorPanel();
		if (rsep != null) {
			rsep.switchResultPanel(RESULT_PANEL.RESULT_PANEL_ALTERNATE);
		}
	}
}
