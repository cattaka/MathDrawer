/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import javax.swing.JComboBox;
import javax.swing.JComponent;

public class DIPInfoSelect implements DIPInfo {
	private String label;
	private String[] items;
	private Object[] values;
	private int defaultData;
	private JComboBox comboBox;

	public DIPInfoSelect(String label, String[] items, int defaultData)
			throws InvalidDataTypeException {
		super();
		if (label == null || items == null)
			throw new NullPointerException();
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				throw new NullPointerException();
		}
		if (defaultData < 0 || items.length <= defaultData)
			throw new ArrayIndexOutOfBoundsException();

		this.label = label;
		this.items = items;
		this.values = items;
		this.defaultData = defaultData;
		this.comboBox = new JComboBox(items);
		this.comboBox.setSelectedIndex(defaultData);
	}

	public DIPInfoSelect(String label, String[] items, String defaultItem)
			throws InvalidDataTypeException {
		super();
		if (label == null || items == null)
			throw new NullPointerException();
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				throw new NullPointerException();
		}
		int defaultData;
		for (defaultData = 0; defaultData < items.length; defaultData++) {
			if (defaultItem.equals(items[defaultData])) {
				break;
			}
		}
		if (items.length <= defaultData)
			throw new IllegalArgumentException();

		this.label = label;
		this.items = items;
		this.values = items;
		this.defaultData = defaultData;
		this.comboBox = new JComboBox(items);
		this.comboBox.setSelectedIndex(defaultData);
	}

	public DIPInfoSelect(String label, String[] items, Object[] values, Object defaultValue)
			throws InvalidDataTypeException {
		super();
		if (label == null || items == null)
			throw new NullPointerException();
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				throw new NullPointerException();
		}
		int defaultData;
		for (defaultData = 0; defaultData < values.length; defaultData++) {
			if (defaultValue.equals(values[defaultData])) {
				break;
			}
		}
		if (items.length <= defaultData)
			throw new IllegalArgumentException();

		this.label = label;
		this.items = items;
		this.values = values;
		this.defaultData = defaultData;
		this.comboBox = new JComboBox(items);
		this.comboBox.setSelectedIndex(defaultData);
	}
	
	public Object getValue() {
		return new Integer(comboBox.getSelectedIndex());
	}

	public void setValue(int value) {
		if (value < 0 || comboBox.getItemCount() <= value)
			throw new ArrayIndexOutOfBoundsException();
		comboBox.setSelectedIndex(value);
	}

	public String getStringValue() {
		return this.items[comboBox.getSelectedIndex()];
	}
	
	public void setStringValue(String value) {
		int idx;
		for (idx = 0; idx < this.items.length; idx++) {
			if (value.equals(this.items[idx])) {
				break;
			}
		}
		if (this.items.length <= idx) {
			throw new ArrayIndexOutOfBoundsException();
		}
		comboBox.setSelectedIndex(idx);
	}

	public Object getObjectValue() {
		return this.values[comboBox.getSelectedIndex()];
	}

	public void setObjectValue(Object value) {
		int idx;
		for (idx = 0; idx < this.values.length; idx++) {
			if (value.equals(this.values[idx])) {
				break;
			}
		}
		if (this.values.length <= idx) {
			throw new ArrayIndexOutOfBoundsException();
		}
		comboBox.setSelectedIndex(idx);
	}

	public void makeDefault() {
		this.comboBox.setSelectedIndex(defaultData);
	}

	public String getLabel() {
		return label;
	}

	public JComponent getComponent() {
		return comboBox;
	}

	public boolean isEnable() {
		return comboBox.isEnabled();
	}

	public void setEnable(boolean enable) {
		comboBox.setEnabled(enable);
	}
}
