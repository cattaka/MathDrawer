package net.cattaka.swing.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class StdTableCellRenderer extends DefaultTableCellRenderer.UIResource {
	private static final long serialVersionUID = 1L;

	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	private Color unselectedForeground;
	private Color unselectedBackground;
	private Color selectedNullBackground;
	private Color nullBackground;
	private String nullString = null;
	
	public StdTableCellRenderer() {
		this.selectedNullBackground = new Color(224, 224, 255);
		this.nullBackground = new Color(224, 224, 224);
		;
	}

	@Override
	public void setForeground(Color c) {
		super.setForeground(c);
		unselectedForeground = c;
	}

	@Override
	public void setBackground(Color c) {
		super.setBackground(c);
		unselectedBackground = c;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			super.setForeground(table.getSelectionForeground());
			if (value == null) {
				super.setBackground(this.selectedNullBackground);
			} else {
				super.setBackground(table.getSelectionBackground());
			}
		} else {
			super.setForeground((unselectedForeground != null)
					? unselectedForeground : table.getForeground());
			if (value == null) {
				super.setBackground(this.nullBackground);
			} else {
				super.setBackground((unselectedBackground != null)
					? unselectedBackground : table.getBackground());
			}
		}

		setFont(table.getFont());

		if (hasFocus) {
			Border border = null;
			if (isSelected) {
				border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
			}
			if (border == null) {
				border = UIManager.getBorder("Table.focusCellHighlightBorder");
			}
			setBorder(border);

			if (!isSelected && table.isCellEditable(row, column)) {
				Color col;
				col = UIManager.getColor("Table.focusCellForeground");
				if (col != null) {
					super.setForeground(col);
				}
				col = UIManager.getColor("Table.focusCellBackground");
				if (col != null) {
					super.setBackground(col);
				}
			}
		} else {
			setBorder(getNoFocusBorder());
		}

		setDisplayValue(value);

		return this;
	}

	protected void setDisplayValue(Object value) {
		if (value == null) {
			setValue(this.nullString);
		} else if (value instanceof Number) {
			this.setHorizontalAlignment(JLabel.RIGHT);
			setValue(value);
		} else {
			this.setHorizontalAlignment(JLabel.LEFT);
			setValue(value);
		}
	}

	private static Border getNoFocusBorder() {
		if (System.getSecurityManager() != null) {
			return SAFE_NO_FOCUS_BORDER;
		} else {
			return noFocusBorder;
		}
	}

	public String getNullString() {
		return nullString;
	}

	public void setNullString(String nullString) {
		this.nullString = nullString;
	}
}
