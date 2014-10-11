package net.cattaka.mathdrawer.util;

import javax.swing.JViewport;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class ImageViewPort extends JViewport {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private Color previewBackground = Color.BLACK;

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(this.previewBackground);
		if (image != null) {
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
			g.drawImage(image, 0, 0, this);
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		if (image != null) {
			Dimension dim = new Dimension();
			dim.height = image.getHeight();
			dim.width = image.getWidth();
			setPreferredSize(dim);
			setSize(dim);
		}
	}

	public BufferedImage getImage() {
		return image;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	public Color getPreviewBackground() {
		return previewBackground;
	}

	public void setPreviewBackground(Color previewBackground) throws NullPointerException {
		if (previewBackground == null) {
			throw new NullPointerException();
		}
		this.previewBackground = previewBackground;
		this.repaint();
	}
}
