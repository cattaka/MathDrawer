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
 * $Id: MdDrawerEditorPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */

package net.cattaka.mathdrawer.gui.drawer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import net.cattaka.jspf.JspfBundle;
import net.cattaka.jspf.JspfException;
import net.cattaka.jspf.ToolsJarBundle;
import net.cattaka.mathdrawer.MdMessageConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.custom.CustomDrawerProcess;
import net.cattaka.mathdrawer.gui.MdGuiInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.gui.MdTextInterface;
import net.cattaka.mathdrawer.gui.common.ImagePreviewPanel;
import net.cattaka.mathdrawer.jspf.MdJspfBundle;
import net.cattaka.swing.JPopupMenuForStandardText;
import net.cattaka.swing.JTextPaneForLine;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.swing.text.StdTextComponent;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.ExceptionUtil;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.PrintWriterEx;
import net.cattaka.util.ExceptionHandler;

public class MdDrawerEditorPanel extends JPanel implements MdGuiInterface, MdTextInterface {
	private static final long serialVersionUID = 1L;

	private MdGuiInterface parentComponent;
	private JTextPaneForLine drawerLineNumPane;
	private DrawerTextPanel drawerTextPane;
	private JTabbedPane resultTabbedPane;
	private JTextArea resultLog;
	private JTextArea compiledSource;
	private JTextArea outputLog;
	private JSplitPane splitPane;
	private File file;
	private Charset charset;
	private ModifiedListener modifiedlistener;
	private boolean modified = false;
	private FindCondition lastFindCondition;

	private ImagePreviewPanel customDrawerPreviewPanel;
	
	public enum RESULT_PANEL {
		RESULT_PANEL_RESULT_LOG,
		RESULT_PANEL_SOURCE,
		RESULT_PANEL_OUTPUT_TABLE,
		RESULT_PANEL_OUTPUT_LOG,
		RESULT_PANEL_ALTERNATE,
	}
	
	class ButtonAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("run_drawer")) {
				runDrawer();
			} else if (e.getActionCommand().equals("compile_drawer")) {
				compileDrawer();
			} else if (e.getActionCommand().equals("show_search_replace")) {
				MdMessage rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_SHOW, parentComponent, MdDrawerEditorPanel.this, drawerTextPane.getSelectedText());
				sendMdMessage(rm);
			} else if (e.getActionCommand().equals("search_next")) {
				searchNext();
			} else if (e.getActionCommand().equals("search_prev")) {
				searchPrev();
			}
		}
	}
	
	class JPopupMenuForDrawerModePanel extends JPopupMenuForStandardText {
		private static final long serialVersionUID = 1L;
		public JPopupMenuForDrawerModePanel(StdTextComponent component, ActionListener al) {
			super(false);
			JMenuItem runDrawerItem = new JMenuItem();
			runDrawerItem.setActionCommand("run_drawer");
			runDrawerItem.addActionListener(al);
			ButtonsBundle.applyButtonDifinition(runDrawerItem, "run_drawer");

			JMenuItem searchReplaceItem = new JMenuItem();
			JMenuItem searchNextItem = new JMenuItem();
			JMenuItem searchPrevItem = new JMenuItem();
			searchReplaceItem.setActionCommand("show_search_replace");
			searchNextItem.setActionCommand("search_next");
			searchPrevItem.setActionCommand("search_prev");
			searchReplaceItem.addActionListener(al);
			searchNextItem.addActionListener(al);
			searchPrevItem.addActionListener(al);
			ButtonsBundle.applyMenuDifinition(searchReplaceItem, "search_replace");
			ButtonsBundle.applyMenuDifinition(searchNextItem, "search_next");
			ButtonsBundle.applyMenuDifinition(searchPrevItem, "search_prev");

			this.add(runDrawerItem);
			this.addSeparator();
			this.createMenuItems();
			this.addSeparator();
			this.add(searchReplaceItem);
			this.add(searchNextItem);
			this.add(searchPrevItem);
		}
	}

	class ModifiedListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			updateEvent(e);
		}

		public void insertUpdate(DocumentEvent e) {
			updateEvent(e);
		}

		public void removeUpdate(DocumentEvent e) {
			updateEvent(e);
		}
		private void updateEvent(DocumentEvent e) {
			if (e.getType() == DocumentEvent.EventType.INSERT || e.getType() == DocumentEvent.EventType.REMOVE) {
				if (!modified) {
					// 変更されたことを親にコンポーネントに伝える。
					MdMessage rm = new MdMessage(MdMessageConstants.MD_PANEL_MODIFIED, parentComponent, MdDrawerEditorPanel.this, null);
					sendMdMessage(rm);
					modified = true;
				}
				// 行番号を更新
				{
					StdStyledDocument ssd = drawerTextPane.getStdStyledDocument();
					drawerLineNumPane.setLineCount(ssd.getLineCount());
				}
			}
		}
	}
	
	public MdDrawerEditorPanel(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		this.lastFindCondition = new FindCondition();
		this.modifiedlistener = new ModifiedListener();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.customDrawerPreviewPanel = new ImagePreviewPanel(this);
		
		ButtonAction al = new ButtonAction();
		JPopupMenuForDrawerModePanel popup = new JPopupMenuForDrawerModePanel(drawerTextPane, al);
		{
			// テキストエリアを作成
			StdScrollPane drawerTextScrollPane;
			drawerTextPane = new DrawerTextPanel(popup);
			drawerTextPane.setStdStyledDocument(new MdDrawerStyledDocument());
			drawerTextPane.getStdStyledDocument().addDocumentListener(this.modifiedlistener);
			drawerTextScrollPane = new StdScrollPane();
			drawerLineNumPane = new JTextPaneForLine();

			drawerTextScrollPane.setViewportView(drawerTextPane);
			drawerTextScrollPane.setRowHeaderView(drawerLineNumPane);
			drawerTextPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			
			// 結果テーブルを作成。
			StdScrollPane resultLogScrollPane;
			StdScrollPane outputLogScrollPane;
			StdScrollPane compiledSourceScrollPane;
			resultTabbedPane = new JTabbedPane();
			resultLog = new JTextArea();
			resultLog.setEditable(false);
			outputLog = new JTextArea();
			outputLog.setEditable(false);
			resultLogScrollPane = new StdScrollPane(resultLog);
			outputLogScrollPane = new StdScrollPane(outputLog);
			compiledSource = new JTextArea();
			compiledSource.setEditable(false);
			compiledSourceScrollPane = new StdScrollPane(compiledSource);
			
			resultTabbedPane.add(resultLogScrollPane,MessageBundle.getMessage("result_log"));
			resultTabbedPane.add(compiledSourceScrollPane,MessageBundle.getMessage("compiled_source"));
			resultTabbedPane.add(outputLogScrollPane,MessageBundle.getMessage("output_log"));
			resultTabbedPane.add(customDrawerPreviewPanel,MessageBundle.getMessage("output_image"));
			
			// スプリットパネルに上記で作成した物を設定。
			splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,drawerTextScrollPane, resultTabbedPane);
			splitPane.setTopComponent(drawerTextScrollPane);
			splitPane.setBottomComponent(resultTabbedPane);
		
			splitPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			this.add(splitPane);
		}
	}
	
	/**
	 * コンパイルを行う。
	 * 
	 * @return 成功ならtrue、それ以外はfalse
	 */
	public boolean compileDrawer() {
		MdJspfBundle<CustomDrawerProcess> pixelShaderBundle = getMdSingletonBundle().getCustomDrawerBundle();
		String labelName = pixelShaderBundle.getLabelName(this.file);
		String sourceText = drawerTextPane.getText();
		return compileDrawer(labelName, sourceText);
	}
	/**
	 * コンパイルを行う。
	 * 
	 * @return 成功ならtrue、それ以外はfalse
	 */
	public boolean compileDrawer(String labelName, String sourceText) {
		ToolsJarBundle toolsJarBundle = getMdConfig().getToolsJarBundle();
		if (!toolsJarBundle.isAvailable()) {
			// tools.jarの設定がされていない。
			JOptionPane.showMessageDialog(this, MessageBundle.getMessage("tools_jar_not_configured"));
			appendResultLog(MessageBundle.getMessage("tools_jar_not_configured"));
			appendResultLog("\n");
			return false;
		}

		appendResultLog(MessageBundle.getMessage("compile_result_separator_raw"));
		appendResultLog("\n");
		boolean result = true;
		StringWriter resultSw = new StringWriter();
		PrintWriterEx resultWriter = new PrintWriterEx(resultSw);
		JspfBundle<CustomDrawerProcess> drawerBundle = getMdSingletonBundle().getCustomDrawerBundle();
		try {
			drawerBundle.compile(toolsJarBundle, labelName, sourceText, resultWriter);
		} catch (ClassNotFoundException e) {
			result = false;
			ExceptionHandler.info(e);
			this.switchResultPanel(RESULT_PANEL.RESULT_PANEL_RESULT_LOG);
		} catch (JspfException e) {
			result = false;
			resultWriter.append(e.getMessage());
			resultWriter.append('\n');
			ExceptionHandler.info(e);
			this.switchResultPanel(RESULT_PANEL.RESULT_PANEL_RESULT_LOG);
		}
		if (result) {
			// クラスの取得
			CustomDrawerProcess drawer = drawerBundle.getNewInstance(labelName);
			if (drawer == null) {
				result = false;
			}
		}
		resultWriter.flush();
		String compileLog = resultSw.getBuffer().toString();
		appendResultLog(compileLog);
		if (compileLog.length() > 0) {
			appendResultLog("\n");
		}
		
		if (result) {
			appendResultLog(MessageBundle.getMessage("compile_drawer_succeed"));
			appendResultLog("\n");
		} else {
			appendResultLog(MessageBundle.getMessage("compile_drawer_failed"));
			appendResultLog("\n");
		}
		
		appendResultLog("\n");
		this.setCompiledSource(drawerBundle.getJavaSource(labelName));
		return result;
	}
	
	public void runDrawer() {
		MdJspfBundle<CustomDrawerProcess> pixelShaderBundle = getMdSingletonBundle().getCustomDrawerBundle();
		String labelName = pixelShaderBundle.getLabelName(this.file);
		JspfBundle<CustomDrawerProcess> drawerBundle = getMdSingletonBundle().getCustomDrawerBundle();
		CustomDrawerProcess drawer = null;
		{
			String sourceText = drawerTextPane.getText();
			if (drawerBundle.isNeedUpdate(labelName, sourceText)) {
				if (compileDrawer(labelName, sourceText)) {
					drawer = drawerBundle.getNewInstance(labelName);
				}
			} else {
				drawer = drawerBundle.getNewInstance(labelName);
			}
		}
		
		if (drawer != null) {
			boolean result = true;
			StringWriter resultSw = new StringWriter();
			PrintWriterEx resultWriter = new PrintWriterEx(resultSw);
			StringWriter outputSw = new StringWriter();
			PrintWriterEx outputWriter = new PrintWriterEx(outputSw);
			
			appendResultLog(MessageBundle.getMessage("drawer_result_separator_raw"));
			appendResultLog("\n");
			try {
				this.customDrawerPreviewPanel.resetCachedImage();
				drawer.setDebug(true);
				drawer.setOut(outputWriter);
				drawer.setSettingValues(new HashMap<String, Object>());
				drawer.setCachedImage(this.customDrawerPreviewPanel.getCachedImage());
				drawer.draw(this.customDrawerPreviewPanel.getBaseImage());
				this.customDrawerPreviewPanel.repaint();
			} catch (Exception e) {
				result = false;
				resultWriter.println(ExceptionUtil.toString(e));
				ExceptionHandler.info(e);
				this.switchResultPanel(RESULT_PANEL.RESULT_PANEL_RESULT_LOG);
			}
			// 出力を表示
			outputWriter.flush();
			this.outputLog.setText(outputSw.getBuffer().toString());

			resultWriter.flush();
			
			String drawerLog = resultSw.getBuffer().toString();
			appendResultLog(drawerLog);
			if (drawerLog.length() > 0) {
				appendResultLog("\n");
			}
			if (result) {
				appendResultLog(MessageBundle.getMessage("executing_drawer_succeed"));
			} else {
				appendResultLog(MessageBundle.getMessage("executing_drawer_failed"));
			}
			appendResultLog("\n");
			appendResultLog("\n");
		} else {
			// コンパイルに失敗している。
		}
	}
	
	public void doGuiLayout() {
		this.splitPane.setDividerLocation(0.5);
	}
	
	public MdConfig getMdConfig() {
		return this.parentComponent.getMdConfig();
	}
	
	public void reloadMdConfig() {
		MdConfig mdConfig = getMdConfig();
		Font fontForEditor = mdConfig.getFontForEditor();
		this.drawerTextPane.setDocumentFont(fontForEditor, mdConfig.getCharactersPerTab());
		this.resultLog.setFont(fontForEditor);
		this.compiledSource.setFont(fontForEditor);
		this.outputLog.setFont(fontForEditor);

		FontMetrics fontMetrics = this.drawerTextPane.getFontMetrics(fontForEditor);
		this.drawerLineNumPane.setFontMetrics(fontMetrics);
	}

	public void sendMdMessage(MdMessage mdMessage) {
		this.parentComponent.sendMdMessage(mdMessage);
	}
	public void relayMdMessage(MdMessage mdMessage) {
		// 無し
	}
	public MdSingletonBundle getMdSingletonBundle() {
		return this.parentComponent.getMdSingletonBundle();
	}

	public void switchResultPanel(RESULT_PANEL resultPanel) {
		switch(resultPanel) {
		case RESULT_PANEL_RESULT_LOG:
			resultTabbedPane.setSelectedIndex(0);
			break;
		case RESULT_PANEL_SOURCE:
			resultTabbedPane.setSelectedIndex(1);
			break;
		case RESULT_PANEL_OUTPUT_TABLE:
			resultTabbedPane.setSelectedIndex(2);
			break;
		case RESULT_PANEL_OUTPUT_LOG:
			resultTabbedPane.setSelectedIndex(3);
			break;
		case RESULT_PANEL_ALTERNATE:
			int idx = resultTabbedPane.getSelectedIndex();
			idx++;
			if (idx > 3) {
				idx = 0;
			}
			resultTabbedPane.setSelectedIndex(idx);
			break;
		}
	}
	
	/**
	 * カーソル位置に文字列を挿入する。
	 * もしカーソル位置が空白でなければ、カンマで区切った後に挿入する。
	 */
	public void appendString(String str) {
		try {
			StdStyledDocument doc = drawerTextPane.getStdStyledDocument();
			if (doc != null) {
				int pos = drawerTextPane.getCaretPosition();
				if (doc.getLength() == 0 || pos==0 || doc.getText(pos-1, 1).matches("[\\s,\\.]")) {
					doc.insertString(pos, str, null);
				} else {
					doc.insertString(pos, ",\n" + str, null);
				}
				drawerTextPane.requestFocusInWindow();
			}
		} catch (BadLocationException e) {
			// 何もしない
		}
	}
	
	public void appendResultLog(String str) {
		resultLog.append(str);
		resultLog.setCaretPosition(resultLog.getDocument().getLength());
	}
	
	public void setCompiledSource(File file) {
		if (file != null && file.isFile()) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
				StringBuilder buf = new StringBuilder();
				int r;
				while((r=reader.read()) != -1) {
					buf.append((char)r);
				}
				this.compiledSource.setText(buf.toString());
			} catch (IOException e) {
				ExceptionHandler.error(e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e2) {
						// ありえない
						ExceptionHandler.warn(e2);
					}
				}
			}
		} else {
			this.compiledSource.setText("");
		}
	}
	
	public void setText(String text) {
		try {
			Document doc = drawerTextPane.getDocument();
			doc.remove(0, doc.getLength());
			doc.insertString(0, text, null);
		} catch (BadLocationException e) {
			// 起こりえないし起こると困る
			ExceptionHandler.fatal(e);
		}
	}
	
	public String getText() {
		String result = null;
		try {
			Document doc = drawerTextPane.getDocument();
			result = doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			// 起こりえないし起こると困る
			ExceptionHandler.fatal(e);
		}
		return result;
	}
	
	public boolean loadDrawer(File file, Charset charset) {
		boolean result = false;
		try {
			Reader reader = new InputStreamReader(new FileInputStream(file), charset);
			StringBuilder sb = new StringBuilder();
			int r;
			while ((r = reader.read()) != -1) {
				sb.append((char)r);
			}
			reader.close();
			setText(sb.toString());
			setFile(file);
			setCharset(charset);
			this.drawerTextPane.getStdStyledDocument().resetUndo();
			modified = false;
			result = true;
			drawerTextPane.setCaretPosition(0);
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return result;
	}

	public boolean saveDrawer(File file, Charset charset) {
		boolean result = false;
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
			String text = getText();
			writer.write(text);
			writer.close();
			setFile(file);
			setCharset(charset);
			modified = false;
			result = true;
			sendMdMessage(new MdMessage(MdMessageConstants.MDDRAWERACCESSPANEL_REFLESH, null, this, file.getParentFile()));
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return result;
	}
	
	public void doFindAction(FindCondition findCondition) {
		lastFindCondition = new FindCondition(findCondition);
		drawerTextPane.doFindAction(findCondition);
	}
	
	public void searchNext() {
		String str = drawerTextPane.getSelectedText();
		if (str != null && str.length() > 0) {
			lastFindCondition.setSearch(str);
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(true);
		} else {
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(true);
		}
		MdMessage rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_DIRECT,null,null,lastFindCondition);
		sendMdMessage(rm);
	}
	public void searchPrev() {
		String str = drawerTextPane.getSelectedText();
		if (str != null && str.length() > 0) {
			lastFindCondition.setSearch(str);
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(false);
		} else {
			lastFindCondition.setAction(FindCondition.ACTION.FIND);
			lastFindCondition.setDownward(false);
		}
		MdMessage rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_DIRECT,null,null,lastFindCondition);
		sendMdMessage(rm);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
	public boolean isModified() {
		return modified;
	}

	public void dispose() {
		// JRE1.5以前のメモリリーク対策
		this.drawerTextPane.getCaret().deinstall(this.drawerTextPane);
	}

	/** メニューイベント用 */
	public void openFindDialog() {
		String str = this.drawerTextPane.getSelectedText();
		MdMessage rm;
		if (str != null && str.length() > 0) {
			rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_SHOW, null, this, str);
		} else {
			rm = new MdMessage(MdMessageConstants.FINDCONDITIONDIALOG_SHOW, null, this, null);
		}
		sendMdMessage(rm);
	}
	/** メニューイベント用 */
	public void doCommentOut() {
		drawerTextPane.doCommentOut("//");
	}
	/** メニューイベント用 */
	public boolean canRedo() {
		return drawerTextPane.canRedo();
	}
	/** メニューイベント用 */
	public boolean canUndo() {
		return drawerTextPane.canUndo();
	}
	/** メニューイベント用 */
	public void redo() {
		drawerTextPane.redo();
	}
	/** メニューイベント用 */
	public void undo() {
		drawerTextPane.undo();
	}
	/** メニューイベント用 */
	public void copy() {
		drawerTextPane.copy();
	}
	/** メニューイベント用 */
	public void cut() {
		drawerTextPane.cut();
	}
	/** メニューイベント用 */
	public void paste() {
		drawerTextPane.paste();
	}
	/** メニューイベント用 */
	public void selectAll() {
		drawerTextPane.selectAll();
	}
}
