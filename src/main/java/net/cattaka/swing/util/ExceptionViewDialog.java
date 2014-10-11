package net.cattaka.swing.util;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import net.cattaka.swing.StdScrollPane;
import net.cattaka.util.MessageBundle;

public class ExceptionViewDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private ExceptionListTableModel exceptionListTableModel;
	private JTextArea textArea;
	private JTable table;
	private JSplitPane splitPane;
	
	public ExceptionViewDialog(ExceptionListTableModel exceptionListTableModel) {
		super();
		this.exceptionListTableModel = exceptionListTableModel;
		makeLayout();
	}
	
	public ExceptionViewDialog(Frame owner, ExceptionListTableModel exceptionListTableModel) {
		super(owner);
		this.exceptionListTableModel = exceptionListTableModel;
		makeLayout();
	}
	
	private void makeLayout() {
		this.setTitle(MessageBundle.getMessage("log_list"));
		this.setSize(600,500);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {
				int row = this.getSelectedRow();
				int col = this.getSelectedColumn();
				if (row != -1 && col != -1) {
					Object item = table.getValueAt(row, col);
					if (item != null) {
						textArea.setText(item.toString());
						textArea.setCaretPosition(0);
					}
				}
				super.columnSelectionChanged(e);
			}
		};
		table.setColumnSelectionAllowed(true);
		table.setModel(exceptionListTableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		StdScrollPane textAreaPane = new StdScrollPane(textArea);
		StdScrollPane tablePane = new StdScrollPane(table);
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePane, textAreaPane);
		getContentPane().add(splitPane);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			splitPane.setDividerLocation(0.5);
		}
	}
}
