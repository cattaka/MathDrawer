package net.cattaka.mathdrawer.setting.constraints;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.cattaka.util.OrderedMap;

public class MdSettingConstraints {
	private OrderedMap<String, SettingConstraint<?>> settingConstantMap = new OrderedMap<String, SettingConstraint<?>>(new HashMap<String, SettingConstraint<?>>());

	public MdSettingConstraints() {
	}
	
	public OrderedMap<String, SettingConstraint<?>> getSettingConstantMap() {
		return settingConstantMap;
	}

	public void setSettingConstantMap(OrderedMap<String, SettingConstraint<?>> settingConstantMap) {
		this.settingConstantMap.clear();
		this.settingConstantMap.putAll(settingConstantMap);
	}

	public boolean isAcceptable(Map<String, Object> values) {
		for (Entry<String, Object> entry:values.entrySet()) {
			SettingConstraint<?> sc = this.settingConstantMap.get(entry.getKey()); 
			if (sc != null && !sc.isAcceptable(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
}
