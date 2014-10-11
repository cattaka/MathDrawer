package net.cattaka.mathdrawer.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;


public class PresetImage {
	public static enum ImageType {
		BMP,
		JPG,
		PNG,
		UNKNOWN
	};
	
	private String name;
	private BufferedImage bufferedImage;
	private ImageType imageType = ImageType.UNKNOWN;

	// Getter/Setter -----------------------------------------------
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
	public ImageType getImageType() {
		return imageType;
	}
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	// Method -----------------------------------------------
	/**
	 * 補間無しで指定した点の色を取得する
	 */
	public void getColor4d(Color4d out, int ix, int iy) {
		int x = ix % this.bufferedImage.getWidth(); 
		int y = iy % this.bufferedImage.getHeight();
		if (x<0) {
			x += this.bufferedImage.getWidth();
		}
		if (y<0) {
			y += this.bufferedImage.getHeight();
		}
		int rgba;
		try {
			rgba = this.bufferedImage.getRGB(x, y);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw e;
		}

		out.a = (double)((rgba & 0xFF000000) >>> 24 ) / 255d;
		out.b = (double)((rgba & 0x00FF0000) >>> 16) / 255d;
		out.c = (double)((rgba & 0x0000FF00) >>> 8) / 255d;
		out.d = (double)((rgba & 0x000000FF)) / 255d;
	}
	/**
	 * 指定した座標の色を取得します。
	 */
	public void getColor4d(Color4d out,double dx, double dy) {
		int x = (int)((float)(MdMath.cyclicDouble(dx) * this.bufferedImage.getWidth()));
		int y = (int)((float)(MdMath.cyclicDouble(dy) * this.bufferedImage.getHeight()));
		int rgba;
		try {
			rgba = this.bufferedImage.getRGB(x, y);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw e;
		}

		out.a = (double)((rgba & 0xFF000000) >>> 24 ) / 255d;
		out.b = (double)((rgba & 0x00FF0000) >>> 16) / 255d;
		out.c = (double)((rgba & 0x0000FF00) >>> 8) / 255d;
		out.d = (double)((rgba & 0x000000FF)) / 255d;
	}

	/**
	 * 指定した座標の色を線形補完して取得します。
	 */
	public void getColor4dLiner(Color4d out, double dx, double dy) {
		double nx = (float)(MdMath.cyclicDouble(dx) * this.bufferedImage.getWidth());
		double ny = (float)(MdMath.cyclicDouble(dy) * this.bufferedImage.getHeight());
		int x1 = Math.round((float)nx);
		int y1 = Math.round((float)ny);
		int x2;
		int y2;
		double ax = nx - (double)x1;
		double ay = ny - (double)y1;
		
		if (ax >= 0) {
			x2 = x1+1;
		} else {
			ax = ax + 1.0;
			x2 = x1;
			x1 = x2 - 1;
		}
		if (ay >= 0) {
			y2 = y1+1;
		} else {
			ay = ay + 1.0;
			y2 = y1;
			y1 = y2 - 1;
		}
		Color4d c11 = new Color4d();
		Color4d c12 = new Color4d();
		Color4d c21 = new Color4d();
		Color4d c22 = new Color4d();
		this.getColor4d(c11, x1, y1);
		this.getColor4d(c12, x1, y2);
		this.getColor4d(c21, x2, y1);
		this.getColor4d(c22, x2, y2);
		Color4d t1 = new Color4d();
		Color4d t2 = new Color4d();
		MdMath.interpolateColor4d(t1, ax, c11, c21);
		MdMath.interpolateColor4d(t2, ax, c12, c22);
		MdMath.interpolateColor4d(out, ay, t1, t2);
	}
	
	// Serialization -----------------------------------------------
	
	public static PresetImage readPresetImage(File file) throws IOException {
		String fileName = file.getName();
		ImageType imageType = ImageType.UNKNOWN;
		for (ImageType it:ImageType.values()) {
			if (fileName.toUpperCase().endsWith(it.name())) {
				imageType = it;
				break;
			}
		}
		
		FileInputStream fin = new FileInputStream(file);
		return readPresetImage(fin, fileName, imageType);
	}
	
	public static PresetImage readPresetImage(InputStream in, String name) throws IOException {
		ImageType imageType = ImageType.UNKNOWN;
		for (ImageType it:ImageType.values()) {
			if (name.toUpperCase().endsWith(it.name())) {
				imageType = it;
				break;
			}
		}
		
		return readPresetImage(in, name, imageType);
	}

	public static PresetImage readPresetImage(InputStream in, String name, ImageType imageType) throws IOException {
		PresetImage result = new PresetImage();
		result.setBufferedImage(	ImageIO.read(in));
		result.setName(name);
		if (imageType == ImageType.UNKNOWN) {
			result.setImageType(ImageType.PNG);
		} else {
			result.setImageType(imageType);
		}
		return result;
	}
	
	public static void writePresetImage(OutputStream out, PresetImage presetImage) throws IOException {
		ImageIO.write(presetImage.getBufferedImage(), presetImage.getImageType().name(), out);
	}
}
