package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.entity.Vector4d;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;
import net.cattaka.mathdrawer.setting.editor.Vector4dEditor;

public class Vector4dConstraint extends SettingConstraint<Vector4d> {
	public Vector4dConstraint(String name, String description, boolean nullable, Vector4d defaultValue) {
		super(name, description, nullable, defaultValue);
	}

	public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil) {
		Vector4dEditor result = new Vector4dEditor(this.isNullable());
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
		return (value instanceof Vector4d);
	}
}
