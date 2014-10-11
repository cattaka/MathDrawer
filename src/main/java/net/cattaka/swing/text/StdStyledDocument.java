package net.cattaka.swing.text;

import java.awt.FontMetrics;

import javax.swing.text.StyledDocument;

public interface StdStyledDocument extends StyledDocument {
	public StringBuilder getStringBuilder();
	public int getLineCount();
	public boolean canUndo();
	public boolean canRedo();
	public boolean undo();
	public boolean redo();
	public void resetUndo();
	public void addUndoSepalartor();
	public boolean isRecordUndoSepalartor();
	public void setRecordUndoSepalartor(boolean recordUndoSepalartor);
	public void setFontMetrics(FontMetrics fontMetrics, int tabNum);
	public TextLineInfo getLine(int pos);
	public TextLineInfo getPrevLine(TextLineInfo src);
	public TextLineInfo getNextLine(TextLineInfo src);
}
