package net.cattaka.mathdrawer.drawer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.setting.entity.RootMdSetting;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.util.BufferedImageUtil;

public class RootDrawer extends Drawer {
	private RootMdSetting rootMdSetting;
	private MdEditorInterface parentComponent;
	private ArrayList<Drawer> childrenList;
	
	public RootDrawer() {
		this.rootMdSetting = new RootMdSetting();
		this.childrenList = new ArrayList<Drawer>();
	}
	
	public void initialize(MdEditorInterface parentComponent) {
		this.parentComponent = parentComponent;
	}

	@Override
	public String getLabel() {
		return "Root";
	}
	
	@Override
	public BufferedImage[] createBlankBufferedImage() {
		BufferedImage[] result = new BufferedImage[this.rootMdSetting.getNumberOfFrames()];
		for (int i=0;i<result.length;i++) {
			result[i] = new BufferedImage(this.rootMdSetting.getWidth(), this.rootMdSetting.getHeight(), BufferedImage.TYPE_INT_ARGB);
			BufferedImageUtil.clearImage(result[i]);
		}
		return result;
	}

	@Override
	public void draw(BufferedImage[] baseImage) throws MdDrawerException {
		Drawer lastDrawer = null;
		BufferedImage[] cachedImages = this.getCachedImage(); 
		for(BufferedImage cachedImage:cachedImages) {
			BufferedImageUtil.clearImage(cachedImage);
		}
		BufferedImage[] prevImage = cachedImages;
		for(Drawer drawer:childrenList) {
			drawer.draw(prevImage);
			drawer.setNeedUpdate(false);
			prevImage = drawer.getCachedImage();
			lastDrawer = drawer;
		}
		for (int i=0;i<cachedImages.length;i++) {
			Graphics2D graphics2D = cachedImages[i].createGraphics();
			int w = cachedImages[i].getWidth();
			int h = cachedImages[i].getHeight();
			if (lastDrawer != null) {
				graphics2D.drawImage(prevImage[i], 0, 0, w, h, null);
			}
			graphics2D.dispose();
		}
	}

	@Override
	public List<Drawer> getChildrenList() {
		return this.childrenList;
	}

	@Override
	public DrawerUtil getDrawerUtil() {
		return this.parentComponent.getDrawerUtil();
	}

	@Override
	public MdConfig getMdConfig() {
		return this.parentComponent.getMdConfig();
	}

	@Override
	public RootMdSetting getRootMdSetting() {
		return this.rootMdSetting;
	}

	@Override
	public MdSetting getMdSetting() {
		return this.rootMdSetting;
	}
	
	@Override
	public boolean isNeedUpdate() {
		boolean result;
		result = this.isNeedUpdate();
		if (!result) {
			for (Drawer drawer:this.getChildrenList()) {
				result = result || drawer.isNeedUpdate();
			}
			this.setNeedUpdate(result);
		}
		return result;
	}
}
