package net.cattaka.swing.text;

import javax.swing.text.AbstractDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class TextUndoManager extends UndoManager {
	private static final long serialVersionUID = 1L;
	
	@Override
	public synchronized void undo() throws CannotUndoException {
		if (!canUndo())
			return;
		AbstractDocument.DefaultDocumentEvent lastUndo = null;
		AbstractDocument.DefaultDocumentEvent nextUndo = null;
		UndoableEdit ue;
		while((ue = super.editToBeUndone()) != null) {
			if (!canUndo()) {
				break;
			}
			if (ue instanceof AbstractDocument.DefaultDocumentEvent) {
				nextUndo = (AbstractDocument.DefaultDocumentEvent)ue;
				if (lastUndo == null || lastUndo.getPresentationName().equals(nextUndo.getPresentationName())) {
					boolean breakFlag = false;
					if (lastUndo != null) {
						breakFlag = (Math.abs(nextUndo.getOffset() - lastUndo.getOffset()) > 1);
					}
					if (breakFlag) {
						break;
					} else {
						super.undo();
						lastUndo = nextUndo;
					}
				} else {
					break;
				}
			} else {
				// あり得ない
				throw new RuntimeException();
				//super.undo();
				//break;
			}
		}
	}
	@Override
	public synchronized void redo() throws CannotRedoException {
		if (!canRedo())
			return;
		
		AbstractDocument.DefaultDocumentEvent lastRedo = null;
		AbstractDocument.DefaultDocumentEvent nextRedo;
		UndoableEdit ue;
		while((ue = super.editToBeRedone()) != null) {
			if (!canRedo()) {
				break;
			}
			if (ue instanceof AbstractDocument.DefaultDocumentEvent) {
				nextRedo = (AbstractDocument.DefaultDocumentEvent)ue;
				if (lastRedo == null || lastRedo.getPresentationName().equals(nextRedo.getPresentationName())) {
					boolean breakFlag = false;
					if (lastRedo != null) {
						breakFlag = (Math.abs(nextRedo.getOffset() - lastRedo.getOffset()) > 1);
					}
					super.redo();
					lastRedo = nextRedo;
					if (breakFlag) {
						break;
					}
				} else {
					break;
				}
			} else {
				// あり得ない
				throw new RuntimeException();
				//super.redo();
				//break;
			}
		}
	}
	
}
