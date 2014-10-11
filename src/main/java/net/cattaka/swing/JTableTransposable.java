package net.cattaka.swing;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class JTableTransposable extends JTable {
	private static final long serialVersionUID = 1L;
	private boolean transposed;
	
	@Override
	public int getRowCount() {
		if (transposed) {
			return super.getColumnCount();
		} else {
			return super.getRowCount();
		}
	}
	@Override
    public String getColumnName(int column) {
		if (transposed) {
			return String.valueOf(column+1);
		} else {
			return super.getColumnName(column);
		}
    }
	@Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        TableColumn tableColumn = getColumnModel().getColumn(column);
        TableCellRenderer renderer = tableColumn.getCellRenderer();
        if (renderer == null) {
        	if (transposed) {
        		renderer = getDefaultRenderer(getColumnClass(row));
        	} else {
        		renderer = getDefaultRenderer(getColumnClass(column));
        	}
         }
        return renderer;
    }
}

