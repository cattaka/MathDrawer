package net.cattaka.swing;

import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import net.cattaka.mathdrawer.MdConfigConstants;
import net.cattaka.util.ExceptionHandler;

public class JTextPaneForLine extends JTextPane {
	private static final long serialVersionUID = 1L;
	private int lineCount = 0;

	public JTextPaneForLine() {
		this.setEditable(false);
	}

	public void setFontMetrics(FontMetrics fontMetrics) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		if (fontMetrics != null) {
			StyleConstants.setFontFamily(def, fontMetrics.getFont().getFamily());
			StyleConstants.setFontSize(def, fontMetrics.getFont().getSize());
		} else {
			StyleConstants.setFontFamily(def, MdConfigConstants.DEFAULT_FONT_EDITOR);
		}
		
		Style lineNumFont = this.addStyle("lineNum", def);

		StyleConstants.setForeground(lineNumFont,Color.BLUE);
		StyledDocument sd = getStyledDocument();
		sd.setLogicalStyle(0, lineNumFont);
		sd.setCharacterAttributes(0, sd.getLength(), lineNumFont, true);
	}
	
	public int getLineCount() {
		return lineCount;
	}
	
	public void setLineCount(int lineCount) {
		int oldLineCount = this.lineCount;
		this.lineCount = lineCount;
		if (this.lineCount < 0) {
			this.lineCount = 0;
		}
		try {
			if (oldLineCount < this.lineCount) {
				StyledDocument sd = this.getStyledDocument();
				for (int i=oldLineCount+1;i<=this.lineCount;i++) {
					String lineLabel = String.valueOf(i) + '\n';
					sd.insertString(sd.getLength(), lineLabel, null);
				}
			}
			if (oldLineCount > this.lineCount) {
				StyledDocument sd = this.getStyledDocument();
				for (int i=oldLineCount;i>this.lineCount;i--) {
					String lineLabel = String.valueOf(i) + '\n';
					sd.remove(sd.getLength()-lineLabel.length(), lineLabel.length());
				}
			}
		} catch (BadLocationException e) {
			ExceptionHandler.error(e);
		}
	}
}
