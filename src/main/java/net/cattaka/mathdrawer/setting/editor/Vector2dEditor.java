package net.cattaka.mathdrawer.setting.editor;

import net.cattaka.mathdrawer.entity.Vector2d;
import net.cattaka.mathdrawer.exception.InvalidValueException;

public class Vector2dEditor extends StringEditor {
	
	public Vector2dEditor(boolean nullable) {
		super(nullable);
		this.valueField.setText(new Vector2d().toString());
	}

	@Override
	public Object getValue() throws InvalidValueException {
		if (nullCheck.isSelected()) {
			try {
				this.valueField.setBackground(succeedColor);
				return Vector2d.valueOf(this.valueField.getText());
			} catch (NumberFormatException e) {
				this.valueField.setBackground(errorColor);
				throw new InvalidValueException(e);
			}
		} else {
			this.valueField.setBackground(succeedColor);
			return null;
		}
	}
	
	public Vector2d getVector2dValue() throws InvalidValueException {
		return (Vector2d)this.getValue();
	}
}
