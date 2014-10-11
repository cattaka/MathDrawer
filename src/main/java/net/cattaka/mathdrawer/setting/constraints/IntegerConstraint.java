package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.editor.IntegerEditor;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;

public class IntegerConstraint extends SettingConstraint<Integer> {
	public IntegerConstraint(String name, String description, boolean nullable, Integer defaultValue) {
		super(name, description, nullable, defaultValue);
	}

	public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil) {
		IntegerEditor result = new IntegerEditor(this.isNullable());
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
		return (value instanceof Integer);
	}
}
