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
 * $Id: MdDrawerModePanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.gui.drawer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.gui.MdGuiInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.gui.MdModeInterface;
import net.cattaka.swing.util.ButtonsBundle;

public class MdDrawerModePanel extends JPanel implements MdGuiInterface, MdModeInterface {
	private static final long serialVersionUID = 1L;

	private JSplitPane splitPane;
	private MdDrawerAssistTabbedPanel mdDrawerAssistTabbedPanel;
	private MdDrawerEditorTabbedPanel mdDrawerEditorPanel;
	private MdGuiInterface parentComponent;

	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("new_drawer")) {
				mdDrawerEditorPanel.addTab();
			} else if (e.getActionCommand().equals("open_drawer")) {
				mdDrawerEditorPanel.openDrawer();
			} else if (e.getActionCommand().equals("save_drawer")) {
				mdDrawerEditorPanel.saveDrawer(null);
			} else if (e.getActionCommand().equals("saveAs_drawer")) {
				mdDrawerEditorPanel.saveAsDrawer(null);
			} else if (e.getActionCommand().equals("close_drawer")) {
				mdDrawerEditorPanel.closeTab();
			} else if (e.getActionCommand().equals("editor_comment_out")) {
				mdDrawerEditorPanel.doCommentOut();
			} else if (e.getActionCommand().equals("editor_undo")) {
				mdDrawerEditorPanel.undo();
			} else if (e.getActionCommand().equals("editor_redo")) {
				mdDrawerEditorPanel.redo();
			} else if (e.getActionCommand().equals("editor_cut")) {
				mdDrawerEditorPanel.cut();
			} else if (e.getActionCommand().equals("editor_copy")) {
				mdDrawerEditorPanel.copy();
			} else if (e.getActionCommand().equals("editor_paste")) {
				mdDrawerEditorPanel.paste();
			} else if (e.getActionCommand().equals("editor_select_all")) {
				mdDrawerEditorPanel.selectAll();
			} else if (e.getActionCommand().equals("editor_find")) {
				mdDrawerEditorPanel.openFindDialog();
			} else if (e.getActionCommand().equals("editor_find_next")) {
				mdDrawerEditorPanel.searchNext();
			} else if (e.getActionCommand().equals("editor_find_prev")) {
				mdDrawerEditorPanel.searchPrev();
			} else if (e.getActionCommand().equals("run_drawer")) {
				mdDrawerEditorPanel.runDrawer();
			} else if (e.getActionCommand().equals("compile_drawer")) {
				mdDrawerEditorPanel.compileDrawer();
			} else if (e.getActionCommand().equals("next_drawer")) {
				mdDrawerEditorPanel.nextDrawer();
			} else if (e.getActionCommand().equals("prev_drawer")) {
				mdDrawerEditorPanel.prevDrawer();
			} else if (e.getActionCommand().equals("switch_result_panel")) {
				mdDrawerEditorPanel.switchResultPanel();
			}
		}
	}
	
	public MdDrawerModePanel(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	private void makeLayout() {
		mdDrawerAssistTabbedPanel = new MdDrawerAssistTabbedPanel(this.parentComponent);
		mdDrawerEditorPanel = new MdDrawerEditorTabbedPanel(this.parentComponent);
		mdDrawerAssistTabbedPanel.setMinimumSize(new Dimension(0,0));
		mdDrawerEditorPanel.setMinimumSize(new Dimension(0,0));
		mdDrawerAssistTabbedPanel.setMdDrawerEditorTabbedPanel(mdDrawerEditorPanel);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mdDrawerAssistTabbedPanel, mdDrawerEditorPanel);
		this.setLayout(new GridLayout());
		this.add(splitPane);
	}

	public void doGuiLayout() {
		this.splitPane.setDividerLocation(0.4);
		this.mdDrawerEditorPanel.doGuiLayout();
		this.mdDrawerAssistTabbedPanel.doGuiLayout();
	}

	public void reloadMdConfig() {
		this.mdDrawerEditorPanel.reloadMdConfig();
		this.mdDrawerAssistTabbedPanel.reloadMdConfig();
	}
	
	public void sendMdMessage(MdMessage mdMessage) {
		this.parentComponent.sendMdMessage(mdMessage);
	}
	public void relayMdMessage(MdMessage rbdaMessage) {
		this.mdDrawerAssistTabbedPanel.relayMdMessage(rbdaMessage);
		this.mdDrawerEditorPanel.relayMdMessage(rbdaMessage);
	}
	public MdSingletonBundle getMdSingletonBundle() {
		return this.parentComponent.getMdSingletonBundle();
	}

	public MdConfig getMdConfig() {
		return this.parentComponent.getMdConfig();
	}
	
	/** {@link MdModeInterface} */
	public JMenu[] getExtraMenu() {
		ActionListenerImpl al = new ActionListenerImpl();
		JMenu drawerMenu = new JMenu();
		ButtonsBundle.applyButtonDifinition(drawerMenu, "menu_drawer");
		{
			JMenuItem runDrawerItem = new JMenuItem();
			JMenuItem compileDrawerItem = new JMenuItem();
			JMenuItem switchResultPanelItem = new JMenuItem();
			JMenuItem commentOutItem = new JMenuItem();
			runDrawerItem.setActionCommand("run_drawer");
			compileDrawerItem.setActionCommand("compile_drawer");
			switchResultPanelItem.setActionCommand("switch_result_panel");
			commentOutItem.setActionCommand("editor_comment_out");
			
			runDrawerItem.addActionListener(al);
			compileDrawerItem.addActionListener(al);
			switchResultPanelItem.addActionListener(al);
			commentOutItem.addActionListener(al);
			
			ButtonsBundle.applyMenuDifinition(runDrawerItem, "run_drawer");
			ButtonsBundle.applyMenuDifinition(compileDrawerItem, "compile_drawer");
			ButtonsBundle.applyMenuDifinition(switchResultPanelItem, "switch_result_panel");
			ButtonsBundle.applyMenuDifinition(commentOutItem, "comment_out");
			
			drawerMenu.add(runDrawerItem);
			drawerMenu.add(compileDrawerItem);
			drawerMenu.add(switchResultPanelItem);
			drawerMenu.addSeparator();
			drawerMenu.add(commentOutItem);
		}
		return new JMenu[]{drawerMenu};
	}
	/** {@link MdModeInterface} */
	public boolean updateMenu(TargetMenu targetMenu, JMenu menu) {
		boolean result = false;
		ActionListenerImpl al = new ActionListenerImpl();
		if (targetMenu == TargetMenu.FILE_MENU) {
			JMenuItem newItem = new JMenuItem();
			JMenuItem openItem = new JMenuItem();
			JMenuItem saveItem = new JMenuItem();
			JMenuItem saveAsItem = new JMenuItem();
			JMenuItem closeItem = new JMenuItem();
			newItem.setActionCommand("new_drawer");
			openItem.setActionCommand("open_drawer");
			saveItem.setActionCommand("save_drawer");
			saveAsItem.setActionCommand("saveAs_drawer");
			closeItem.setActionCommand("close_drawer");
			
			newItem.addActionListener(al);
			openItem.addActionListener(al);
			saveItem.addActionListener(al);
			saveAsItem.addActionListener(al);
			closeItem.addActionListener(al);
			
			ButtonsBundle.applyMenuDifinition(newItem, "file_new");
			ButtonsBundle.applyMenuDifinition(openItem, "file_open");
			ButtonsBundle.applyMenuDifinition(saveItem, "file_save");
			ButtonsBundle.applyMenuDifinition(saveAsItem, "file_save_as");
			ButtonsBundle.applyMenuDifinition(closeItem, "file_close");
			
			menu.add(newItem);
			menu.add(openItem);
			menu.add(saveItem);
			menu.add(saveAsItem);
			menu.add(closeItem);
			result = true;
		} else if (targetMenu == TargetMenu.EDIT_MENU) {
			JMenuItem undoItem = new JMenuItem();
			JMenuItem redoItem = new JMenuItem();
			JMenuItem cutItem = new JMenuItem();
			JMenuItem copyItem = new JMenuItem();
			JMenuItem pasteItem = new JMenuItem();
			JMenuItem selectAllItem = new JMenuItem();
			JMenuItem findItem = new JMenuItem();
			JMenuItem findNextItem = new JMenuItem();
			JMenuItem findPrevItem = new JMenuItem();
			undoItem.setActionCommand("editor_undo");
			redoItem.setActionCommand("editor_redo");
			cutItem.setActionCommand("editor_cut");
			copyItem.setActionCommand("editor_copy");
			pasteItem.setActionCommand("editor_paste");
			selectAllItem.setActionCommand("editor_select_all");
			findItem.setActionCommand("editor_find");
			findNextItem.setActionCommand("editor_find_next");
			findPrevItem.setActionCommand("editor_find_prev");
			undoItem.addActionListener(al);
			redoItem.addActionListener(al);
			cutItem.addActionListener(al);
			copyItem.addActionListener(al);
			pasteItem.addActionListener(al);
			selectAllItem.addActionListener(al);
			findItem.addActionListener(al);
			findNextItem.addActionListener(al);
			findPrevItem.addActionListener(al);
			ButtonsBundle.applyMenuDifinition(undoItem, "editor_undo");
			ButtonsBundle.applyMenuDifinition(redoItem, "editor_redo");
			ButtonsBundle.applyMenuDifinition(cutItem, "editor_cut");
			ButtonsBundle.applyMenuDifinition(copyItem, "editor_copy");
			ButtonsBundle.applyMenuDifinition(pasteItem, "editor_paste");
			ButtonsBundle.applyMenuDifinition(selectAllItem, "editor_select_all");
			ButtonsBundle.applyMenuDifinition(findItem, "search_replace");
			ButtonsBundle.applyMenuDifinition(findNextItem, "search_next");
			ButtonsBundle.applyMenuDifinition(findPrevItem, "search_prev");
			
			menu.add(undoItem);
			menu.add(redoItem);
			menu.addSeparator();
			menu.add(cutItem);
			menu.add(copyItem);
			menu.add(pasteItem);
			menu.addSeparator();
			menu.add(selectAllItem);
			menu.addSeparator();
			menu.add(findItem);
			menu.add(findNextItem);
			menu.add(findPrevItem);
			result = true;
		}
		return result;
	}
}
