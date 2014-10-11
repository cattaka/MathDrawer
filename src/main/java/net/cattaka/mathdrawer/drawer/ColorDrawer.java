package net.cattaka.mathdrawer.drawer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.setting.constraints.Color4dConstraint;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.util.BufferedImageUtil;
import net.cattaka.util.OrderedMap;

public class ColorDrawer extends Drawer {
	private ColorMdSetting colorMdSetting;
	
	enum ValueName {
		FOREGROUND_COLOR,
		BACKGROUND_COLOR
	}
	
	public static class ColorMdSetting extends MdSetting {
		public ColorMdSetting() {
			OrderedMap<String, SettingConstraint<?>> settingConstantMap = new OrderedMap<String, SettingConstraint<?>>(new HashMap<String, SettingConstraint<?>>());
			settingConstantMap.put(ValueName.FOREGROUND_COLOR.name(), new Color4dConstraint("Foreground", "Foreground Color", true, null));
			settingConstantMap.put(ValueName.BACKGROUND_COLOR.name(), new Color4dConstraint("Background", "Background Color", true, null));
			
			MdSettingConstraints mdSettingConstraints = new MdSettingConstraints();
			mdSettingConstraints.setSettingConstantMap(settingConstantMap);
			this.setMdSettingConstraints(mdSettingConstraints);
			
			// 初期値
			this.setForegroundColor(new Color4d(1,1,0,0));
			this.setBackgroundColor(null);
		}
		
		public Color4d getForegroundColor() {
			return (Color4d)getValues().get(ValueName.FOREGROUND_COLOR.name());
		}
		public void setForegroundColor(Color4d foregroundColor) {
			this.getValues().put(ValueName.FOREGROUND_COLOR.name(), foregroundColor);
		}
		public Color4d getBackgroundColor() {
			return (Color4d)getValues().get(ValueName.BACKGROUND_COLOR.name());
		}
		public void setBackgroundColor(Color4d backgroundColor) {
			this.getValues().put(ValueName.BACKGROUND_COLOR.name(), backgroundColor);
		}
	}
	
	public ColorDrawer() {
		this.colorMdSetting = new ColorMdSetting();
	}
	
	@Override
	public String getLabel() {
		return "Color";
	}
	
	@Override
	public MdSetting getMdSetting() {
		return this.colorMdSetting;
	}
	@Override
	public void draw(BufferedImage[] baseImage) throws MdDrawerException {
		super.draw(baseImage);
		
		Color4d color4d = this.colorMdSetting.getForegroundColor();
		if (color4d != null) {
			BufferedImage[] cachedImages = this.getCachedImage();
			for (BufferedImage cachedImage:cachedImages) {
				BufferedImageUtil.clearImage(cachedImage);
				Color color = color4d.toColor();
				Graphics2D g = cachedImage.createGraphics();
				g.setColor(color);
				g.fillRect(0, 0, cachedImage.getWidth(), cachedImage.getHeight());
				g.dispose();
			}
		}
	}
}
