package net.cattaka.swing.util;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;



public class ExceptionListTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<ExceptionInfo> exceptionInfoList;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private String[] columnNames;

	public ExceptionListTableModel(List<ExceptionInfo> exceptionInfoList) {
		if (exceptionInfoList == null) {
			throw new NullPointerException();
		}
		this.columnNames = new String[]{"TimeStamp", "Type", "Message", "StackTrace"};
		this.exceptionInfoList = exceptionInfoList;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return this.exceptionInfoList.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String result;
		ExceptionInfo ei = this.exceptionInfoList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			result = dateFormat.format(ei.getTimestamp());
			break;
		case 1:
			result = ei.getType();
			break;
		case 2:
			result = ei.getMessage();
			break;
		case 3:
			result = ei.getStackTrace();
			break;
		default:
			throw new ArrayIndexOutOfBoundsException(columnIndex);	
		}
		return result;
	}
}
