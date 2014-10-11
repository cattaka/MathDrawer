package net.cattaka.mathdrawer.setting.editor;

import net.cattaka.mathdrawer.exception.InvalidValueException;

public class IntegerEditor extends StringEditor {
	
	public IntegerEditor(boolean nullable) {
		super(nullable);
		this.valueField.setText("0");
	}

	@Override
	public Object getValue() throws InvalidValueException {
		if (nullCheck.isSelected()) {
			try {
				this.valueField.setBackground(succeedColor);
				return Integer.valueOf(this.valueField.getText());
			} catch (NumberFormatException e) {
				this.valueField.setBackground(errorColor);
				throw new InvalidValueException(e);
			}
		} else {
			this.valueField.setBackground(succeedColor);
			return null;
		}
	}
	
	public Integer getIntegerValue() throws InvalidValueException {
		return (Integer)this.getValue();
	}
}
