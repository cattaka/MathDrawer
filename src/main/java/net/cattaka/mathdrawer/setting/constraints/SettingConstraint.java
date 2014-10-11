package net.cattaka.mathdrawer.setting.constraints;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditor;

abstract public class SettingConstraint<V> {
	private String name;
	private String description;
	private boolean nullable;
	private boolean enable;
	private boolean applyImmediate;
	private V defaultValue;
	
	public SettingConstraint(String name, String description, boolean nullable, V defaultValue) {
		super();
		this.name = name;
		this.description = description;
		this.nullable = nullable;
		this.enable = true;
		this.defaultValue = defaultValue;
		
		this.applyImmediate = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isApplyImmediate() {
		return applyImmediate;
	}

	public void setApplyImmediate(boolean applyImmediate) {
		this.applyImmediate = applyImmediate;
	}

	public V getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(V defaultValue) {
		this.defaultValue = defaultValue;
	}

	abstract public boolean isAcceptable(Object value);

	abstract public MdSettingEditor createMdSettingEditor(DrawerUtil drawerUtil);
}
