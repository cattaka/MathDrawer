package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;
import net.cattaka.mathdrawer.setting.editor.SelectEditor;

public class CustomPixelShaderConstraint extends SettingConstraint<String> {
	private String[] items;
	
	public CustomPixelShaderConstraint(String name, String description, boolean nullable, String defaultValue) {
		super(name, description, nullable, defaultValue);
		this.items = new String[0];
	}

	public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil) {
		this.items = drawerUtil.createCustomPixelShaderNameArray();
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
			if (items.length > 0) {
				for (String str:items) {
					if (value.equals(str)) {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}
}
