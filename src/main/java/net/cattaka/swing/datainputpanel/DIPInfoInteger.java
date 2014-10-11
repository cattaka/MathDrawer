/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class DIPInfoInteger implements DIPInfo {
	private String label;
	private String defaultData;
	private JTextField field;

	public DIPInfoInteger(String label, String defaultData)
			throws InvalidDataTypeException {
		super();
		if (label == null || defaultData == null)
			throw new NullPointerException();

		this.label = label;
		this.defaultData = defaultData;
		this.field = new JTextField(defaultData, 0);
	}

	public Object getValue() {
		String arg = field.getText();
		try {
			return Integer.valueOf(arg);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public int getIntValue() {
		String arg = field.getText();
		try {
			return Integer.parseInt(arg);
		} catch (NumberFormatException e) {
			// 誤った使い方をしなければ起こらない。
			return 0;
		}
	}

	public void setValue(int value) {
		field.setText(String.valueOf(value));
	}

	public void makeDefault() {
		field.setText(defaultData);
	}

	public String getLabel() {
		return label;
	}

	public JComponent getComponent() {
		return field;
	}

	public boolean isEnable() {
		return field.isEnabled();
	}

	public void setEnable(boolean enable) {
		field.setEnabled(enable);
	}
}
