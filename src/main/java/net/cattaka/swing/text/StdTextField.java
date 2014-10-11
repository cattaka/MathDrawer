package net.cattaka.swing.text;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import net.cattaka.swing.JPopupMenuForStandardText;

public class StdTextField extends JTextField implements StdTextComponent {
	private static final long serialVersionUID = 1L;

	private JPopupMenuForStandardText popupMenu;
	private UndoManager undoManager;

	public StdTextField() {
		super();
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}
	
	public StdTextField(JPopupMenuForStandardText popupMenu) {
		super();
		this.popupMenu = popupMenu;
		initialize();
	}
	
	public StdTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public StdTextField(int columns) {
		super(columns);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public StdTextField(String text, int columns) {
		super(text, columns);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public StdTextField(String text) {
		super(text);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public void initialize() {
		this.undoManager = new TextUndoManager();
		
		this.popupMenu.install(this);
		
		this.getDocument().addUndoableEditListener(undoManager);
		this.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_Z:	//CTRL+Zのとき、UNDO実行
					if (e.isControlDown() && undoManager.canUndo()) {
						undoManager.undo();
						e.consume();
					}
					break;
				case KeyEvent.VK_Y:	//CTRL+Yのとき、REDO実行
					if (e.isControlDown() && undoManager.canRedo()) {
						undoManager.redo();
						e.consume();
					}
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});
	}
	
	public boolean canUndo() {
		return undoManager.canUndo();
	}

	public boolean canRedo() {
		return undoManager.canRedo();
	}
	
	public void undo() {
		undoManager.undo();
	}
	
	public void redo() {
		undoManager.redo();
	}
	
	public JPopupMenuForStandardText getPopupMenu() {
		return popupMenu;
	}
	
	public JComponent getJComponent() {
		return this;
	}
}
