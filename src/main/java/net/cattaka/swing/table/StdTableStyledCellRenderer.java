package net.cattaka.swing.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class StdTableStyledCellRenderer extends StdTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1); 
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	public StdTableStyledCellRenderer() {
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof StdStyledCell) {
			StdStyledCell cell = (StdStyledCell)value;
			if (isSelected) {
				super.setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				super.setForeground((cell != null && cell.getForeground() != null) ? cell.getForeground() : table.getForeground());
				super.setBackground((cell != null && cell.getBackground() != null) ? cell.getBackground() : table.getBackground());
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
				Border border = (cell != null) ? cell.getBorder() : null;
				if (border == null) {
					border = getNoFocusBorder();
				}
				setBorder(border);
			}
	
			if (cell != null) {
				setDisplayValue(cell.getValue());
			} else {
				setDisplayValue(null);
			}
		} else {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		return this;
	}
	
	private static Border getNoFocusBorder() {
	    if (System.getSecurityManager() != null) {
	        return SAFE_NO_FOCUS_BORDER;
	    } else {
	        return noFocusBorder;
	    }
	}
}
