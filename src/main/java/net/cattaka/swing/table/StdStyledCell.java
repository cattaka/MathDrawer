package net.cattaka.swing.table;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class StdStyledCell {
	private Color foreground;
	private Color background;
	private Color borderColor;
	private int borderThickness = -1;
	private String value;
	private Border border;
	
	public StdStyledCell() {
		this.borderColor = Color.BLACK;
	}
	
	public StdStyledCell(StdStyledCell src) {
		this.foreground = src.getForeground();
		this.background = src.getBackground();
		this.borderColor = src.getBorderColor();
		this.borderThickness = src.getBorderThickness();
		this.value = src.getValue();
		this.border = src.getBorder();
	}
	
	public Border getBorder() {
		return border;
	}

	public Color getForeground() {
		return foreground;
	}
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	public Color getBackground() {
		return background;
	}
	public void setBackground(Color background) {
		this.background = background;
	}
	public Color getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		if (borderThickness >= 0 && borderColor != null) {
			this.border = BorderFactory.createLineBorder(this.borderColor, this.borderThickness);
		} else {
			this.border = null;
		}
	}
	public int getBorderThickness() {
		return borderThickness;
	}
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
		if (borderThickness >= 0 && borderColor != null) {
			this.border = BorderFactory.createLineBorder(this.borderColor, this.borderThickness);
		} else {
			this.border = null;
		}
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		// TODO : 暫定仕様
		return (this.value != null) ? this.value : "";
	}
}
