package net.cattaka.mathdrawer.drawer;

import java.awt.image.BufferedImage;
import java.io.StringWriter;

import net.cattaka.jspf.JspfException;
import net.cattaka.mathdrawer.drawer.custom.CustomPixelShader;
import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.setting.entity.CustomMdSetting;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.PrintWriterEx;

public class CustomPixelShaderDrawer extends Drawer {
	private CustomMdSetting customPixelShaderMdSetting;

	public CustomPixelShaderDrawer() {
		this.customPixelShaderMdSetting = new CustomMdSetting();
		this.customPixelShaderMdSetting.setEnable(CustomMdSetting.ValueName.CUSTOM_PIXEL_SHADER.name(), true);
	}
	
	@Override
	public String getLabel() {
		String result = null;
		String labelName = this.customPixelShaderMdSetting.getCustomPixelShaderName();
		if (labelName != null) {
			try {
				CustomPixelShader cps = this.getDrawerUtil().getCustomPixelShader(labelName);
				if (cps != null && cps.getName() != null && cps.getName().length() > 0) {
					labelName = cps.getName();
				}
			} catch (JspfException e) {
				ExceptionHandler.debug(e);
			}
		}
		if (labelName != null) {
			result = "PS:" + labelName.toString();
		} else {
			result = "PS";
		}
		return result;
	}
	@Override
	public MdSetting getMdSetting() {
		String labelName = this.customPixelShaderMdSetting.getCustomPixelShaderName();
		CustomPixelShader cps = null;
		if (labelName != null) {
			try {
				cps = this.getDrawerUtil().getCustomPixelShader(labelName);
				this.customPixelShaderMdSetting.setMessage(cps.getDescription());
			} catch (JspfException e) {
				ExceptionHandler.debug(e);
				this.customPixelShaderMdSetting.setMessage(e.getContents());
			}
		}
		if (cps != null) {
			this.customPixelShaderMdSetting.removeExtraAll();
			cps.getCustomMdSettingUtil().updateCustomMdSetting(this.customPixelShaderMdSetting);
		} else {
			this.customPixelShaderMdSetting.removeExtraAll();
		}
		this.customPixelShaderMdSetting.removeRedundantValues();
		return this.customPixelShaderMdSetting;
	}
	
	@Override
	public void draw(BufferedImage[] baseImage) throws MdDrawerException {
		DrawerUtil drawerUtil = this.getDrawerUtil();
		String customPixelShaderName = this.customPixelShaderMdSetting.getCustomPixelShaderName();
		CustomPixelShader customPixelShader = null;
		
		if (customPixelShaderName != null) {
			try {
				customPixelShader = drawerUtil.getCustomPixelShader(customPixelShaderName);
			} catch (JspfException e) {
				MdDrawerException e2 = new MdDrawerException(e);
				e2.setContents(e.getContents());
				throw e2;
			}
		}
		
		if (customPixelShader != null) {
			customPixelShader.setDrawerUtil(this.getDrawerUtil());
			customPixelShader.setDebug(false);
			customPixelShader.setSettingValues(customPixelShaderMdSetting.getValues());
			
			StringWriter outputSw = new StringWriter();
			PrintWriterEx outputWriter = new PrintWriterEx(outputSw);
			customPixelShader.setOut(outputWriter);
			
			BufferedImage[] cachedImages = this.getCachedImage();
			for (int i=0;i<cachedImages.length;i++) {

				PresetImage presetImage = new PresetImage();
				presetImage.setBufferedImage(baseImage[i]);
				customPixelShader.setBaseImage(presetImage);
		
				customPixelShader.setFrameCount(i);
				customPixelShader.setMaxFrameCount(cachedImages.length);
				
				Color4d color4d = new Color4d();
				BufferedImage cachedImage = cachedImages[i];
				int w = cachedImage.getWidth();
				int h = cachedImage.getHeight();
				
				try {
					for (int x=0;x<w;x++) {
						for (int y=0;y<h;y++) {
							customPixelShader.shadePixel(color4d, x, y, w, h);
							cachedImage.setRGB(x, y, color4d.toRgb());
						}
					}
				} catch (Exception e) {
					ExceptionHandler.error(e);
				}
			}
		}
	}
}
