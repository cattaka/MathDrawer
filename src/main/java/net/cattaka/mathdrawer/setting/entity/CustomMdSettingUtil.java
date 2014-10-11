package net.cattaka.mathdrawer.setting.entity;

import java.util.HashMap;

import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.Vector2d;
import net.cattaka.mathdrawer.entity.Vector3d;
import net.cattaka.mathdrawer.entity.Vector4d;
import net.cattaka.mathdrawer.exception.MdException;
import net.cattaka.mathdrawer.setting.constraints.Color4dConstraint;
import net.cattaka.mathdrawer.setting.constraints.DoubleConstraint;
import net.cattaka.mathdrawer.setting.constraints.ImageConstraint;
import net.cattaka.mathdrawer.setting.constraints.IntegerConstraint;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.mathdrawer.setting.constraints.StringConstraint;
import net.cattaka.mathdrawer.setting.constraints.Vector2dConstraint;
import net.cattaka.mathdrawer.setting.constraints.Vector3dConstraint;
import net.cattaka.mathdrawer.setting.constraints.Vector4dConstraint;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.OrderedMap;

public class CustomMdSettingUtil {
	public static final String[] AVAILABLE_TYPES = new String[] {
		"Integer",
		"Double",
		"Vector2d",
		"Vector3d",
		"Vector4d",
		"Color4d",
		"Image"
	};
	
	static class SettingConstraintsInfo {
		private String type;
		private String key;
		private String name;
		private String description;
		private String defaultValue;
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getName() {
			return name;
		}
		public String getDescription() {
			return description;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}
		public SettingConstraint<?> createSettingConstraint() {
			SettingConstraint<?> sc = null;
			if ("String".equals(type)) {
				sc = new StringConstraint(name, description, true, (defaultValue != null) ? String.valueOf(defaultValue) : null);
			} else if ("Integer".equals(type)) {
				sc = new IntegerConstraint(name, description, true, (defaultValue != null) ? Integer.valueOf(defaultValue) : null);
			} else if ("Double".equals(type)) {
				sc = new DoubleConstraint(name, description, true, (defaultValue != null) ? Double.valueOf(defaultValue) : null);
			} else if ("Vector2d".equals(type)) {
				sc = new Vector2dConstraint(name, description, true, (defaultValue != null) ? Vector2d.valueOf(defaultValue) : null);
			} else if ("Vector3d".equals(type)) {
				sc = new Vector3dConstraint(name, description, true, (defaultValue != null) ? Vector3d.valueOf(defaultValue) : null);
			} else if ("Vector4d".equals(type)) {
				sc = new Vector4dConstraint(name, description, true, (defaultValue != null) ? Vector4d.valueOf(defaultValue) : null);
			} else if ("Color4d".equals(type)) {
				sc = new Color4dConstraint(name, description, true, (defaultValue != null) ? Color4d.valueOf(defaultValue) : null);
			} else if ("Image".equals(type)) {
				sc = new ImageConstraint(name, description, true, (defaultValue != null) ? String.valueOf(defaultValue) : null);
			}
			return sc;
		}
	};

	private OrderedMap<String, SettingConstraintsInfo> settingConstraintsInfoMap = new OrderedMap<String, SettingConstraintsInfo>(new HashMap<String, SettingConstraintsInfo>()); 

	public void addSettingConstraintsInfo(String key, String type, String name, String description, String defaultValue) throws MdException {
		SettingConstraintsInfo sci = new SettingConstraintsInfo();
		sci.setType(type);
		sci.setKey(key);
		sci.setName(name);
		sci.setDescription(description);
		sci.setDefaultValue(defaultValue);
		
		if ("String".equals(type)) {
		} else if ("Integer".equals(type)) {
		} else if ("Double".equals(type)) {
		} else if ("Vector2d".equals(type)) {
		} else if ("Vector3d".equals(type)) {
		} else if ("Vector4d".equals(type)) {
		} else if ("Color4d".equals(type)) {
		} else if ("Image".equals(type)) {
		} else {
			String message = String.format(MessageBundle.getMessage("this_is_invalid_type"), type);
			throw new MdException(message);
		}
		
		if (settingConstraintsInfoMap.containsKey(key)) {
			String message = String.format(MessageBundle.getMessage("this_key_already_exists"), key);
			throw new MdException(message);
		}
		
		settingConstraintsInfoMap.put(key, sci);
	}

	public void updateCustomMdSetting(CustomMdSetting customMdSetting) {
		OrderedMap<String, SettingConstraint<?>> mdSettingConstraints = customMdSetting.getMdSettingConstraints().getSettingConstantMap();
		for (String key:this.settingConstraintsInfoMap.getKeyList()) {
			SettingConstraintsInfo sci = this.settingConstraintsInfoMap.get(key);
			mdSettingConstraints.put(sci.getKey(), sci.createSettingConstraint());
		}
	}
}
