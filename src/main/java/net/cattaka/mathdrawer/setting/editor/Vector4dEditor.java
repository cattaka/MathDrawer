package net.cattaka.mathdrawer.setting.editor;

import net.cattaka.mathdrawer.entity.Vector4d;
import net.cattaka.mathdrawer.exception.InvalidValueException;

public class Vector4dEditor extends StringEditor {
	
	public Vector4dEditor(boolean nullable) {
		super(nullable);
		this.valueField.setText(new Vector4d().toString());
	}

	@Override
	public Object getValue() throws InvalidValueException {
		if (nullCheck.isSelected()) {
			try {
				this.valueField.setBackground(succeedColor);
				return Vector4d.valueOf(this.valueField.getText());
			} catch (NumberFormatException e) {
				this.valueField.setBackground(errorColor);
				throw new InvalidValueException(e);
			}
		} else {
			this.valueField.setBackground(succeedColor);
			return null;
		}
	}
	
	public Vector4d getVector3dValue() throws InvalidValueException {
		return (Vector4d)this.getValue();
	}
}
