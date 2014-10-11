package net.cattaka.swing.text;

import javax.swing.JComponent;
import javax.swing.text.Document;

public interface StdTextComponent {
	public boolean canUndo();
	public boolean canRedo();
	
	public void undo();
	public void redo();
	public void cut();
	public void copy();
	public void paste();
	public void selectAll();
	public int getSelectionStart();
	public int getSelectionEnd();
	public Document getDocument();
	
	public JComponent getJComponent();
}
