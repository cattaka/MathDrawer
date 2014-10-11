package net.cattaka.mathdrawer.setting.entity;

import java.util.HashMap;

import net.cattaka.mathdrawer.setting.constraints.IntegerConstraint;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.util.OrderedMap;

public class RootMdSetting extends MdSetting {
	enum ValueName {
		HEIHGT,
		WIDTH,
		NUMBER_OF_FRAME
	};
	
	public RootMdSetting() {
		OrderedMap<String, SettingConstraint<?>> settingConstantMap = new OrderedMap<String, SettingConstraint<?>>(new HashMap<String, SettingConstraint<?>>());
		settingConstantMap.put(ValueName.WIDTH.name(), new IntegerConstraint("Width", "Width of project", false, 256));
		settingConstantMap.put(ValueName.HEIHGT.name(), new IntegerConstraint("Height", "Height of project", false, 256));
		settingConstantMap.put(ValueName.NUMBER_OF_FRAME.name(), new IntegerConstraint("NumberOfFrames", "Number Of Frames", false, 1));

		MdSettingConstraints mdSettingConstraints = new MdSettingConstraints();
		mdSettingConstraints.setSettingConstantMap(settingConstantMap);
		this.setMdSettingConstraints(mdSettingConstraints);
		
		// 初期値
		this.getValues().put(ValueName.WIDTH.name(), Integer.valueOf(256));
		this.getValues().put(ValueName.HEIHGT.name(), Integer.valueOf(256));
		this.getValues().put(ValueName.NUMBER_OF_FRAME.name(), Integer.valueOf(1));
	}
	public Integer getWidth() {
		return (Integer)this.getValues().get(ValueName.WIDTH.name());
	}
	public void setWidth(Integer width) {
		this.getValues().put(ValueName.WIDTH.name(), width);
	}
	public Integer getHeight() {
		return (Integer)this.getValues().get(ValueName.HEIHGT.name());
	}
	public void setHeight(Integer height) {
		this.getValues().put(ValueName.HEIHGT.name(), height);
	}
	public Integer getNumberOfFrames() {
		Integer result = (Integer)this.getValues().get(ValueName.NUMBER_OF_FRAME.name());
		if (result == null || result <= 0) {
			result = 1;
		}
		return result;
	}
	public void setNumberOfFrames(Integer height) {
		this.getValues().put(ValueName.NUMBER_OF_FRAME.name(), height);
	}
}
