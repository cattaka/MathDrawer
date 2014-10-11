package net.cattaka.swing.text;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import net.cattaka.swing.text.FindCondition.ACTION;
import net.cattaka.util.MessageBundle;

public class FindConditionDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private FindConditionPanel findConditionPanel;

	public FindConditionDialog() throws HeadlessException {
		super();
		initialize();
	}

	public FindConditionDialog(Dialog owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
		initialize();
	}

	public FindConditionDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) throws HeadlessException {
		super(owner, title, modal, gc);
		initialize();
	}

	public FindConditionDialog(Dialog owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		initialize();
	}

	public FindConditionDialog(Dialog owner, String title)
			throws HeadlessException {
		super(owner, title);
		initialize();
	}

	public FindConditionDialog(Dialog owner) throws HeadlessException {
		super(owner);
		initialize();
	}

	public FindConditionDialog(Frame owner, boolean modal)
			throws HeadlessException {
		super(owner, modal);
		initialize();
	}

	public FindConditionDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		initialize();
	}

	public FindConditionDialog(Frame owner, String title, boolean modal)
			throws HeadlessException {
		super(owner, title, modal);
		initialize();
	}

	public FindConditionDialog(Frame owner, String title)
			throws HeadlessException {
		super(owner, title);
		initialize();
	}

	public FindConditionDialog(Frame owner) throws HeadlessException {
		super(owner);
		initialize();
	}

	private void initialize() {
		setTitle(MessageBundle.getMessage("search_replace"));
		setSize(400, 300);
		
		this.findConditionPanel = new FindConditionPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void doReplace() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.REPLACE);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doReplaceAll() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.REPLACE_ALL);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doReplaceSearch() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.REPLACE_FIND);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doSearch() {
				FindCondition fc = getFindCondition();
				fc.setAction(ACTION.FIND);
				FindConditionDialog.this.doAction(fc);
			}

			@Override
			public void doClose() {
				FindConditionDialog.this.setVisible(false);
			}
		};
		getContentPane().add(this.findConditionPanel);

		// ESC押下時に閉じる処理
		AbstractAction act = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		};
		InputMap imap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		getRootPane().getActionMap().put("close-it", act);
	}
	
	public void doAction(FindCondition findCondition) {
		// 空
	}

	public FindCondition getFindCondition() {
		return findConditionPanel.getFindCondition();
	}

	public ArrayList<String> getReplaceHistory() {
		return findConditionPanel.getReplaceHistory();
	}

	public ArrayList<String> getSearchHistory() {
		return findConditionPanel.getSearchHistory();
	}

	public void setFindCondition(FindCondition findCondition) {
		findConditionPanel.setFindCondition(findCondition);
	}

	public void setReplaceHistory(ArrayList<String> replaceHistory) {
		findConditionPanel.setReplaceHistory(replaceHistory);
	}

	public void setSearchHistory(ArrayList<String> searchHistory) {
		findConditionPanel.setSearchHistory(searchHistory);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			findConditionPanel.onShow();
		}
	}
}
