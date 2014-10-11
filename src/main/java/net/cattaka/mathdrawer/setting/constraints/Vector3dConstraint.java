package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.entity.Vector3d;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;
import net.cattaka.mathdrawer.setting.editor.Vector3dEditor;

public class Vector3dConstraint extends SettingConstraint<Vector3d> {
	public Vector3dConstraint(String name, String description, boolean nullable, Vector3d defaultValue) {
		super(name, description, nullable, defaultValue);
	}

	public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil) {
		Vector3dEditor result = new Vector3dEditor(this.isNullable());
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
		return (value instanceof Vector3d);
	}
}
