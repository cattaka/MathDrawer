package net.cattaka.mathdrawer.util;

import javax.swing.JViewport;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class ImageArrayViewPort extends JViewport {
	private static final long serialVersionUID = 1L;
	private BufferedImage[] images;
	private Color previewBackground = Color.BLACK;

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(this.previewBackground);
		if (images != null) {
			g.fillRect(0, 0, images[0].getWidth(), images[0].getHeight());
			for (BufferedImage image:images) {
				g.drawImage(image, 0, 0, this);
			}
		}
	}

	public void setImage(BufferedImage[] images) {
		this.images = images;
		if (images != null) {
			Dimension dim = new Dimension(0,0);
			for (BufferedImage image:images) {
				dim.height = Math.max(dim.height, image.getHeight());
				dim.width = Math.max(dim.width, image.getWidth());
			}
			setPreferredSize(dim);
			setSize(dim);
		}
	}

	public BufferedImage[] getImages() {
		return images;
	}

	@Override
	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	public Color getPreviewBackground() {
		return previewBackground;
	}

	public void setPreviewBackground(Color previewBackground) {
		if (previewBackground == null) {
			throw new NullPointerException();
		}
		this.previewBackground = previewBackground;
		this.repaint();
	}
}
