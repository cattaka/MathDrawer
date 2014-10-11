package net.cattaka.mathdrawer.setting.editor;

import net.cattaka.mathdrawer.exception.InvalidValueException;

public class DoubleEditor extends StringEditor {
	
	public DoubleEditor(boolean nullable) {
		super(nullable);
		this.valueField.setText("0");
	}

	@Override
	public Object getValue() throws InvalidValueException {
		if (nullCheck.isSelected()) {
			try {
				this.valueField.setBackground(succeedColor);
				return Double.valueOf(this.valueField.getText());
			} catch (NumberFormatException e) {
				this.valueField.setBackground(errorColor);
				throw new InvalidValueException(e);
			}
		} else {
			this.valueField.setBackground(succeedColor);
			return null;
		}
	}
	
	public Double getDoubleValue() throws InvalidValueException {
		return (Double)this.getValue();
	}
}
