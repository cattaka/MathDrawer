package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;
import net.cattaka.mathdrawer.setting.editor.SelectEditor;

public class SelectConstraint extends SettingConstraint<String> {
	private String[] items;
	
	public SelectConstraint(String name, String description, String[] items, boolean nullable, String defaultValue) {
		super(name, description, nullable, defaultValue);
		this.items = new String[items.length];
		System.arraycopy(items, 0, this.items, 0, items.length);
	}
	public SelectConstraint(String name, String description, Enum<?>[] items, boolean nullable, Enum<?> defaultValue) {
		super(name, description, nullable, (defaultValue != null) ? defaultValue.name() : null);
		this.items = new String[items.length];
		for (int i=0;i<this.items.length;i++) {
			this.items[i] = String.valueOf(items[i]);
		}
	}

	public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil) {
		SelectEditor result = new SelectEditor(this.items, this.isNullable());
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
		if (value instanceof String) {
			for (String str:items) {
				if (value.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}
}
