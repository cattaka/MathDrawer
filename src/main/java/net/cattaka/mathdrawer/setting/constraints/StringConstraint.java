package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;
import net.cattaka.mathdrawer.setting.editor.StringEditor;

public class StringConstraint extends SettingConstraint<String> {
	public StringConstraint(String name, String description, boolean nullable, String defaultValue) {
		super(name, description, nullable, defaultValue);
	}

	public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil) {
		StringEditor result = new StringEditor(this.isNullable());
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
		return (value instanceof String);
	}
}
