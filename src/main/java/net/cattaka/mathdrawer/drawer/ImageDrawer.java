package net.cattaka.mathdrawer.drawer;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.setting.constraints.ImageConstraint;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.util.OrderedMap;

public class ImageDrawer extends Drawer {
	enum ValueName {
		IMAGE
	}
	
	private ImageMdSetting imageMdSetting;
	
	public static class ImageMdSetting extends MdSetting {
		public ImageMdSetting() {
			OrderedMap<String, SettingConstraint<?>> settingConstantMap = new OrderedMap<String, SettingConstraint<?>>(new HashMap<String, SettingConstraint<?>>());
			settingConstantMap.put(ValueName.IMAGE.name(), new ImageConstraint("Image","Image",true, null));
			
			MdSettingConstraints mdSettingConstraints = new MdSettingConstraints();
			mdSettingConstraints.setSettingConstantMap(settingConstantMap);
			this.setMdSettingConstraints(mdSettingConstraints);
		}
		
		public String getImageName() {
			return (String)getValues().get(ValueName.IMAGE.name());
		}

		public void setImageName(String imageName) {
			this.getValues().put(ValueName.IMAGE.name(), imageName);
		}
	}
	
	public ImageDrawer() {
		this.imageMdSetting = new ImageMdSetting();
	}
	
	@Override
	public String getLabel() {
		return "Image";
	}
	
	@Override
	public MdSetting getMdSetting() {
		return this.imageMdSetting;
	}
	@Override
	public void draw(BufferedImage[] baseImage) throws MdDrawerException {
		PresetImage presetImage = null;
		{
			String imageName = this.imageMdSetting.getImageName();
			presetImage = this.getDrawerUtil().getPresetImage(imageName);
		}
		if (presetImage != null) {
			Color4d tc = new Color4d();
			BufferedImage[] outs = this.getCachedImage();
			for (BufferedImage out:outs) {
				double w = (double)out.getWidth();
				double h = (double)out.getHeight();
				for (int x = 0;x<out.getWidth();x++) {
					for (int y = 0;y<out.getHeight();y++) {
						presetImage.getColor4dLiner(tc, (double)x/w, (double)y/h);
						out.setRGB(x, y, tc.toRgb());
					}
				}
			}
		}
	}
}
