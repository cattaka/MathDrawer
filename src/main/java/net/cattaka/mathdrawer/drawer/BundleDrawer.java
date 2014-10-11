package net.cattaka.mathdrawer.drawer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.constraints.SelectConstraint;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.util.BufferedImageUtil;
import net.cattaka.util.OrderedMap;

public class BundleDrawer extends Drawer {
	private BundleMdSetting bundleMdSetting;
	private ArrayList<Drawer> childrenList;
	
	enum ValueName {
		SRC_BLEND,
		DST_BLEND,
		BLEND_OP
	};
	
	public static enum Blend {
		ZERO,
		ONE,
		SRCCOLOR,
		INVSRCCOLOR,
		SRCALPHA,
		INVSRCALPHA,
		DESTALPHA,
		INVDESTALPHA,
		DESTCOLOR,
		INVDESTCOLOR,
		SRCALPHASAT
	};
	public static enum BlendOp {
	    BLENDOP_ADD,
	    BLENDOP_SUBTRACT,
	    BLENDOP_REVSUBTRACT,
	    BLENDOP_MIN,
	    BLENDOP_MAX,
	};
	
	static class BundleMdSetting extends MdSetting {
		public BundleMdSetting() {
			SelectConstraint srcBlendConstraint = new SelectConstraint("Src Blend", "Src Blend", Blend.values(), false, Blend.SRCALPHA);
			SelectConstraint dstBlendConstraint = new SelectConstraint("Dst Blend", "Dst Blend", Blend.values(), false, Blend.INVSRCALPHA);
			SelectConstraint blendOpConstraint = new SelectConstraint("Blend Op", "Blend Op", BlendOp.values(), false, BlendOp.BLENDOP_ADD);
			
			OrderedMap<String,SettingConstraint<?>> settingConstantList = new OrderedMap<String, SettingConstraint<?>>(new HashMap<String, SettingConstraint<?>>());
			settingConstantList.put(ValueName.SRC_BLEND.name(),srcBlendConstraint);
			settingConstantList.put(ValueName.DST_BLEND.name(),dstBlendConstraint);
			settingConstantList.put(ValueName.BLEND_OP.name(),blendOpConstraint);

			MdSettingConstraints mdSettingConstraints = new MdSettingConstraints();
			mdSettingConstraints.setSettingConstantMap(settingConstantList);
			this.setMdSettingConstraints(mdSettingConstraints);
			
			// 初期値
			this.setSrcBlend(Blend.SRCALPHA);
			this.setDstBlend(Blend.INVSRCALPHA);
			this.setBlendOp(BlendOp.BLENDOP_ADD);
		}
		public Blend getSrcBlend() {
			return Blend.valueOf((String)this.getValues().get(ValueName.SRC_BLEND.name()));
		}
		public void setSrcBlend(Blend srcBlend) {
			this.getValues().put(ValueName.SRC_BLEND.name(), srcBlend.name());
		}
		public Blend getDstBlend() {
			return Blend.valueOf((String)this.getValues().get(ValueName.DST_BLEND.name()));
		}
		public void setDstBlend(Blend dstBlend) {
			this.getValues().put(ValueName.DST_BLEND.name(), dstBlend.name());
		}
		public BlendOp getBlendOp() {
			return BlendOp.valueOf((String)this.getValues().get(ValueName.BLEND_OP.name()));
		}
		public void setBlendOp(BlendOp blendOp) {
			this.getValues().put(ValueName.BLEND_OP.name(), blendOp.name());
		}
	}
	
	public BundleDrawer() {
		this.bundleMdSetting = new BundleMdSetting();
		this.childrenList = new ArrayList<Drawer>();
	}
	
	private void getBlendValues(Color4d out, Blend blend, Color4d src, Color4d dst) {
		switch(blend) {
		case ZERO:
			out.set(0, 0, 0, 0);
			break;
		case ONE:
			out.set(1, 1, 1, 1);
			break;
		case SRCCOLOR:
			out.set(src.a, src.b, src.c, src.d);
			break;
		case INVSRCCOLOR:
			out.set(1.0-src.a, 1.0-src.b, 1.0-src.c, 1.0-src.d);
			break;
		case SRCALPHA:
			out.set(src.a, src.a, src.a, src.a);
			break;
		case INVSRCALPHA:
			out.set(1.0-src.a, 1.0-src.a, 1.0-src.a, 1.0-src.a);
			break;
		case DESTALPHA:
			out.set(dst.a, dst.a, dst.a, dst.a);
			break;
		case INVDESTALPHA:
			out.set(1.0-dst.a, 1.0-dst.a, 1.0-dst.a, 1.0-dst.a);
			break;
		case DESTCOLOR:
			out.set(dst.a, dst.b, dst.c, dst.d);
			break;
		case INVDESTCOLOR:
			out.set(1.0-dst.a, 1.0-dst.b, 1.0-dst.c, 1.0-dst.d);
			break;
		case SRCALPHASAT:
			double f = Math.min(src.a, 1.0-src.a);
			out.set(1.0, f, f, f);
			break;
		}
	}
	
	private void calcBlendOp(Color4d out, BlendOp blendOp, Color4d src, Color4d dst) {
		switch(blendOp) {
		case BLENDOP_ADD:
			out.set(src);
			out.add(dst);
			break;
		case BLENDOP_SUBTRACT:
			out.set(src);
			out.sub(dst);
			break;
		case BLENDOP_REVSUBTRACT:
			out.set(dst);
			out.sub(src);
			break;
		case BLENDOP_MIN:
			out.a = Math.min(src.a, dst.a);
			out.b = Math.min(src.b, dst.b);
			out.c = Math.min(src.c, dst.c);
			out.d = Math.min(src.d, dst.d);
			break;
		case BLENDOP_MAX:
			out.a = Math.max(src.a, dst.a);
			out.b = Math.max(src.b, dst.b);
			out.c = Math.max(src.c, dst.c);
			out.d = Math.max(src.d, dst.d);
			break;
		}
	}
	// -- interface -----------------------------------
	
	@Override
	public String getLabel() {
		return "Bundle";
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
		if (lastDrawer != null) {
			Blend srcBlend = this.bundleMdSetting.getSrcBlend();
			Blend dstBlend = this.bundleMdSetting.getDstBlend();
			BlendOp blendOp = this.bundleMdSetting.getBlendOp();
			BufferedImage[] lastImages = lastDrawer.getCachedImage(); 
			for (int i=0;i<cachedImages.length;i++) {
				BufferedImage cachedImage = cachedImages[i];
				BufferedImageUtil.clearImage(cachedImage);
				PresetImage src = new PresetImage();
				PresetImage dst = new PresetImage();
				src.setBufferedImage(lastImages[i]);
				dst.setBufferedImage(baseImage[i]);
				Color4d srcColor = new Color4d();
				Color4d dstColor = new Color4d();
				Color4d outColor = new Color4d();
				Color4d srcBlendColor = new Color4d();
				Color4d dstBlendColor = new Color4d();
				for (int x=0;x<src.getBufferedImage().getWidth();x++) {
					for (int y=0;y<src.getBufferedImage().getWidth();y++) {
						src.getColor4d(srcColor, x, y);
						dst.getColor4d(dstColor, x, y);
						getBlendValues(srcBlendColor, srcBlend, srcColor, dstColor);
						getBlendValues(dstBlendColor, dstBlend, srcColor, dstColor);
						srcBlendColor.a *= srcColor.a;
						srcBlendColor.b *= srcColor.b;
						srcBlendColor.c *= srcColor.c;
						srcBlendColor.d *= srcColor.d;
						dstBlendColor.a *= dstColor.a;
						dstBlendColor.b *= dstColor.b;
						dstBlendColor.c *= dstColor.c;
						dstBlendColor.d *= dstColor.d;
						calcBlendOp(outColor, blendOp, srcBlendColor, dstBlendColor);
						cachedImage.setRGB(x, y, outColor.toRgb());
					}
				}
			}
		} else {
			BufferedImage[] cachedImage = this.getCachedImage(); 
			for (int i=0;i<cachedImage.length;i++) {
				BufferedImageUtil.clearImage(cachedImage[i]);
			}
		}
	}

	@Override
	public List<Drawer> getChildrenList() {
		return this.childrenList;
	}

	@Override
	public DrawerUtil getDrawerUtil() {
		return this.getParentDrawer().getDrawerUtil();
	}

	@Override
	public MdConfig getMdConfig() {
		return this.getParentDrawer().getMdConfig();
	}

	@Override
	public MdSetting getMdSetting() {
		return this.bundleMdSetting;
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
