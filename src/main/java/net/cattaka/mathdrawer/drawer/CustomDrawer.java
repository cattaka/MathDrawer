package net.cattaka.mathdrawer.drawer;

import java.awt.image.BufferedImage;
import java.io.StringWriter;

import net.cattaka.jspf.JspfException;
import net.cattaka.mathdrawer.drawer.custom.CustomDrawerProcess;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.setting.entity.CustomMdSetting;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.PrintWriterEx;

public class CustomDrawer extends Drawer {
	private CustomMdSetting customDrawerMdSetting;
	
	public CustomDrawer() {
		this.customDrawerMdSetting = new CustomMdSetting();
		this.customDrawerMdSetting.setEnable(CustomMdSetting.ValueName.CUSTOM_DRAWER.name(), true);
	}
	
	@Override
	public String getLabel() {
		String result = null;
		String labelName = this.customDrawerMdSetting.getCustomDrawerName();
		if (labelName != null) {
			try {
				CustomDrawerProcess cdp = this.getDrawerUtil().getCustomDrawerProcess(labelName);
				if (cdp != null && cdp.getName() != null && cdp.getName().length() > 0) {
					labelName = cdp.getName();
				}
			} catch (JspfException e) {
				ExceptionHandler.debug(e);
			}
		}
		if (labelName != null) {
			result = "DW:" + labelName.toString();
		} else {
			result = "DW";
		}
		return result;
	}
	
	@Override
	public MdSetting getMdSetting() {
		String labelName = this.customDrawerMdSetting.getCustomDrawerName();
		CustomDrawerProcess cdp = null;
		if (labelName != null) {
			try {
				cdp = this.getDrawerUtil().getCustomDrawerProcess(labelName);
				this.customDrawerMdSetting.setMessage(cdp.getDescription());
			} catch (JspfException e) {
				ExceptionHandler.debug(e);
				this.customDrawerMdSetting.setMessage(e.getContents());
			}
		}
		if (cdp != null) {
			this.customDrawerMdSetting.removeExtraAll();
			cdp.getCustomMdSettingUtil().updateCustomMdSetting(customDrawerMdSetting);
		} else {
			this.customDrawerMdSetting.removeExtraAll();
		}
		this.customDrawerMdSetting.removeRedundantValues();
		return this.customDrawerMdSetting;
	}
	@Override
	public void draw(BufferedImage[] baseImage) throws MdDrawerException {
		DrawerUtil drawerUtil = this.getDrawerUtil();
		String customDrawerName = this.customDrawerMdSetting.getCustomDrawerName();
		CustomDrawerProcess customDrawerProcess = null;
		
		if (customDrawerName != null) {
			try {
				customDrawerProcess = drawerUtil.getCustomDrawerProcess(customDrawerName);
			} catch (JspfException e) {
				MdDrawerException e2 = new MdDrawerException(e);
				e2.setContents(e.getContents());
				throw e2;
			}
		}
		
		if (customDrawerProcess != null) {
			customDrawerProcess.setDrawerUtil(this.getDrawerUtil());
			customDrawerProcess.setSettingValues(this.customDrawerMdSetting.getValues());
			customDrawerProcess.setDebug(false);
			
			StringWriter outputSw = new StringWriter();
			PrintWriterEx outputWriter = new PrintWriterEx(outputSw);

			customDrawerProcess.setOut(outputWriter);
			BufferedImage[] cachedImages = this.getCachedImage();
			for (int i=0;i<cachedImages.length;i++) {
				customDrawerProcess.setFrameCount(i);
				customDrawerProcess.setMaxFrameCount(cachedImages.length);
				customDrawerProcess.setCachedImage(cachedImages[i]);
				customDrawerProcess.draw(baseImage[i]);
			}
		}
	}
}
