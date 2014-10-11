package net.cattaka.mathdrawer.gui.pixelshader;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.HashMap;

import net.cattaka.mathdrawer.drawer.custom.CustomPixelShader;
import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.PresetImage;

public class CustomPixelShaderRunner {
	protected PrintWriter out;
	protected CustomPixelShader customPixelShader;
	private BufferedImage cachedImage;
	private boolean debug = true;
	
	public BufferedImage getCachedImage() {
		return cachedImage;
	}
	public void setCachedImage(BufferedImage cachedImage) {
		this.cachedImage = cachedImage;
	}
	public PrintWriter getOut() {
		return out;
	}
	public void setOut(PrintWriter out) {
		this.out = out;
	}
	
	public CustomPixelShader getCustomPixelShader() {
		return customPixelShader;
	}
	public void setCustomPixelShader(CustomPixelShader customPixelShader) {
		this.customPixelShader = customPixelShader;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public void draw(BufferedImage baseImage) {
		if (customPixelShader == null) {
			return;
		}
		if (customPixelShader.getSettingValues() == null) {
			customPixelShader.setSettingValues(new HashMap<String, Object>());
		}
		customPixelShader.setDebug(debug);
		customPixelShader.setOut(out);
		
		PresetImage presetImage = new PresetImage();
		presetImage.setBufferedImage(baseImage);
		customPixelShader.setBaseImage(presetImage);

		Color4d color4d = new Color4d();
		int w = cachedImage.getWidth();
		int h = cachedImage.getHeight();
		
		for (int x=0;x<w;x++) {
			for (int y=0;y<h;y++) {
				customPixelShader.shadePixel(color4d, x, y, w, h);
				cachedImage.setRGB(x, y, color4d.toRgb());
			}
		}
	}
}
