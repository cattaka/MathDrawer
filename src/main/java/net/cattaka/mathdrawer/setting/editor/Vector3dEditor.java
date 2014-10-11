package net.cattaka.mathdrawer.setting.editor;

import net.cattaka.mathdrawer.entity.Vector3d;
import net.cattaka.mathdrawer.exception.InvalidValueException;

public class Vector3dEditor extends StringEditor {
	
	public Vector3dEditor(boolean nullable) {
		super(nullable);
		this.valueField.setText(new Vector3d().toString());
	}

	@Override
	public Object getValue() throws InvalidValueException {
		if (nullCheck.isSelected()) {
			try {
				this.valueField.setBackground(succeedColor);
				return Vector3d.valueOf(this.valueField.getText());
			} catch (NumberFormatException e) {
				this.valueField.setBackground(errorColor);
				throw new InvalidValueException(e);
			}
		} else {
			this.valueField.setBackground(succeedColor);
			return null;
		}
	}
	
	public Vector3d getVector3dValue() throws InvalidValueException {
		return (Vector3d)this.getValue();
	}
}
