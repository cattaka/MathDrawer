package net.cattaka.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import net.cattaka.swing.text.StdTextComponent;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.ExceptionHandler;

public class JPopupMenuForStandardText extends JPopupMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	private StdTextComponent textComponent;
	private JMenuItem undoItem = new JMenuItem();
	private JMenuItem redoItem = new JMenuItem();
	
	public JPopupMenuForStandardText(boolean createMenuItems) {
		if (createMenuItems) {
			createMenuItems();
		}
	}
	
	public void createMenuItems() {
		ActionListener ac = this;

		JMenuItem cutItem = new JMenuItem();
		JMenuItem copyItem = new JMenuItem();
		JMenuItem pasteItem = new JMenuItem();
		JMenuItem deleteItem = new JMenuItem();
		JMenuItem selectAllItem = new JMenuItem();
		ButtonsBundle.applyMenuDifinition(undoItem, "editor_undo");
		ButtonsBundle.applyMenuDifinition(redoItem, "editor_redo");
		ButtonsBundle.applyMenuDifinition(cutItem, "editor_cut");
		ButtonsBundle.applyMenuDifinition(copyItem, "editor_copy");
		ButtonsBundle.applyMenuDifinition(pasteItem, "editor_paste");
		ButtonsBundle.applyMenuDifinition(deleteItem, "editor_delete");
		ButtonsBundle.applyMenuDifinition(selectAllItem, "editor_select_all");
		
		undoItem.setActionCommand("editor_undo");
		undoItem.addActionListener(ac);
		redoItem.setActionCommand("editor_redo");
		redoItem.addActionListener(ac);
		cutItem.setActionCommand("editor_cut");
		cutItem.addActionListener(ac);
		copyItem.setActionCommand("editor_copy");
		copyItem.addActionListener(ac);
		pasteItem.setActionCommand("editor_paste");
		pasteItem.addActionListener(ac);
		deleteItem.setActionCommand("editor_delete");
		deleteItem.addActionListener(ac);
		selectAllItem.setActionCommand("editor_select_all");
		selectAllItem.addActionListener(ac);
		
		this.add(undoItem);
		this.add(redoItem);
		this.add(new JSeparator());
		this.add(cutItem);
		this.add(copyItem);
		this.add(pasteItem);
		this.add(deleteItem);
		this.add(new JSeparator());
		this.add(selectAllItem);
		
		this.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent e) {
				// 無し
			}
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// 無し
			}
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				undoItem.setEnabled(textComponent.canUndo());
				redoItem.setEnabled(textComponent.canRedo());
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("editor_undo")) {
			textComponent.undo();
		} else if (e.getActionCommand().equals("editor_redo")) {
			textComponent.redo();
		} else if (e.getActionCommand().equals("editor_cut")) {
			textComponent.cut();
		} else if (e.getActionCommand().equals("editor_copy")) {
			textComponent.copy();
		} else if (e.getActionCommand().equals("editor_paste")) {
			textComponent.paste();
		} else if (e.getActionCommand().equals("editor_delete")) {
			int ss = textComponent.getSelectionStart();
			int se = textComponent.getSelectionEnd();
			try {
				textComponent.getDocument().remove(ss, se-ss);
			} catch(BadLocationException exc) {
				ExceptionHandler.error(exc);
			}
		} else if (e.getActionCommand().equals("editor_select_all")) {
			textComponent.selectAll();
		}
	}

	public void install(StdTextComponent component) {
		this.textComponent = component;
		component.getJComponent().setComponentPopupMenu(this);
	}
}
