package net.cattaka.mathdrawer.setting.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import net.cattaka.mathdrawer.setting.constraints.CustomDrawerConstraint;
import net.cattaka.mathdrawer.setting.constraints.CustomPixelShaderConstraint;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.util.OrderedMap;

public class CustomMdSetting extends MdSetting {
	public enum ValueName {
		CUSTOM_DRAWER,
		CUSTOM_PIXEL_SHADER
	}
	
	public CustomMdSetting() {
		OrderedMap<String,SettingConstraint<?>> settingConstantMap = new OrderedMap<String, SettingConstraint<?>>(new HashMap<String,SettingConstraint<?>>());
		CustomDrawerConstraint customDrawerConstraint = new CustomDrawerConstraint("CustomDrawer","CustomDrawer",true, null);
		CustomPixelShaderConstraint customPixelShaderConstraint = new CustomPixelShaderConstraint("CustomPixelShader","CustomPixelShader",true, null);
		customDrawerConstraint.setApplyImmediate(true);
		customPixelShaderConstraint.setApplyImmediate(true);
		settingConstantMap.put(ValueName.CUSTOM_DRAWER.name(), customDrawerConstraint);
		settingConstantMap.put(ValueName.CUSTOM_PIXEL_SHADER.name(), customPixelShaderConstraint);

		MdSettingConstraints mdSettingConstraints = new MdSettingConstraints();
		mdSettingConstraints.setSettingConstantMap(settingConstantMap);
		this.setMdSettingConstraints(mdSettingConstraints);
		
		setEnable(ValueName.CUSTOM_DRAWER.name(), false);
		setEnable(ValueName.CUSTOM_PIXEL_SHADER.name(), false);
		setEnableAll(false);
	}
	
	public void setEnable(String valueName, boolean enable) {
		OrderedMap<String, SettingConstraint<?>> scm = getMdSettingConstraints().getSettingConstantMap();
		SettingConstraint<?> sc = scm.get(valueName);
		if ( sc != null) {
			sc.setEnable(enable);
		}
	}
	
	public void setEnableAll(boolean enable) {
		OrderedMap<String, SettingConstraint<?>> scm = getMdSettingConstraints().getSettingConstantMap();
		for (Entry<String, SettingConstraint<?>> entry:scm.entrySet()) {
			if (entry.getKey().equals(ValueName.CUSTOM_DRAWER.name()) || entry.getKey().equals(ValueName.CUSTOM_PIXEL_SHADER.name())) {
				// 無視
			} else {
				entry.getValue().setEnable(enable);
			}
		}
	}
	
	public void removeExtraAll() {
		OrderedMap<String, SettingConstraint<?>> scm = getMdSettingConstraints().getSettingConstantMap();
		
		Set<String> keySet = new HashSet<String>(scm.keySet());
		keySet.remove(ValueName.CUSTOM_DRAWER.name());
		keySet.remove(ValueName.CUSTOM_PIXEL_SHADER.name());
		for (String key:keySet) {
			scm.remove(key);
		}
	}
	
	public void removeRedundantValues() {
		OrderedMap<String, SettingConstraint<?>> scm = getMdSettingConstraints().getSettingConstantMap();
		Set<String> scKeySet = new HashSet<String>(scm.keySet());
		Set<String> valuesKeySet = new HashSet<String>(getValues().keySet());
		for (String key:valuesKeySet) {
			if (!scKeySet.contains(key)) {
				getValues().remove(key);
			}
		}
	}
	
	public String getCustomDrawerName() {
		return (String)getValues().get(CustomMdSetting.ValueName.CUSTOM_DRAWER.name());
	}

	public void setCustomDrawerName(String customDrawerName) {
		this.getValues().put(CustomMdSetting.ValueName.CUSTOM_DRAWER.name(), customDrawerName);
	}
	
	public String getCustomPixelShaderName() {
		return (String)getValues().get(CustomMdSetting.ValueName.CUSTOM_PIXEL_SHADER.name());
	}

	public void setCustomPixelShaderName(String customDrawerName) {
		this.getValues().put(CustomMdSetting.ValueName.CUSTOM_PIXEL_SHADER.name(), customDrawerName);
	}
}
