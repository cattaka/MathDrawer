package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.editor.DoubleEditor;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;

public class DoubleConstraint extends SettingConstraint<Double> {
	public DoubleConstraint(String name, String description, boolean nullable, Double defaultValue) {
		super(name, description, nullable, defaultValue);
	}

	public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil) {
		DoubleEditor result = new DoubleEditor(this.isNullable());
		try {
			result.setValue(getDefaultValue());
		} catch (InvalidValueException e) {
			//　無視
		}
		return result;
	}

	@Override
	public boolean isAcceptable(Object value) {
		if (value == null) {
			return isNullable();
		}
		return (value instanceof Double);
	}
}
