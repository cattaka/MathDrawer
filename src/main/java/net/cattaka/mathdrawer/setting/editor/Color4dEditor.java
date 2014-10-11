package net.cattaka.mathdrawer.setting.editor;

import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.exception.InvalidValueException;

public class Color4dEditor extends StringEditor {
	
	public Color4dEditor(boolean nullable) {
		super(nullable);
		this.valueField.setText(new Color4d().toString());
	}

	@Override
	public Object getValue() throws InvalidValueException {
		if (nullCheck.isSelected()) {
			try {
				Color4d vec = Color4d.valueOf(this.valueField.getText());
				this.valueField.setBackground(succeedColor);
				return vec;
			} catch (NumberFormatException e) {
				this.valueField.setBackground(errorColor);
				throw new InvalidValueException(e);
			} catch (IllegalArgumentException e) {
				this.valueField.setBackground(errorColor);
				throw new InvalidValueException(e);
			}
		} else {
			this.valueField.setBackground(succeedColor);
			return null;
		}
	}
	
	public Color4d getColor4dValue() throws InvalidValueException {
		return (Color4d)this.getValue();
	}
}
