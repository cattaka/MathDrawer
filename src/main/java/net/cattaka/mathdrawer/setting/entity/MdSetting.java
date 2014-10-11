package net.cattaka.mathdrawer.setting.entity;

import java.util.HashMap;
import java.util.Map;

import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;

public class MdSetting {
	private Map<String, Object> values = new HashMap<String, Object>();
	private MdSettingConstraints mdSettingConstraints;
	private String message;
	
	public MdSetting() {
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public boolean setValues(Map<String, Object> values) {
		if (values == null) {
			throw new NullPointerException();
		}
		if (this.mdSettingConstraints.isAcceptable(values)) {
			this.values = DrawerSetting.cloneMap(values);
			return true;
		} else{
			return false;
		}
	}

	public MdSettingConstraints getMdSettingConstraints() {
		return mdSettingConstraints;
	}

	public void setMdSettingConstraints(MdSettingConstraints mdSettingConstraints) {
		this.mdSettingConstraints = mdSettingConstraints;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
