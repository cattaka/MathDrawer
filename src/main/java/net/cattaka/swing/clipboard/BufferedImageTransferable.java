package net.cattaka.swing.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.cattaka.util.BufferedImageUtil;

public class BufferedImageTransferable implements Transferable {
	private BufferedImage image;
	private DataFlavor[] flavors;

	public BufferedImageTransferable(BufferedImage image) throws ClassNotFoundException {
		this.image = BufferedImageUtil.cloneBufferedImage(image);
		flavors = new DataFlavor[] {DataFlavor.imageFlavor};
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavors[0].equals(flavor);
	}

	public Object getTransferData(DataFlavor flavor) throws IOException, UnsupportedFlavorException {
		if (flavors[0].equals(flavor)) {
			return image;
		}
		throw new UnsupportedFlavorException(flavor);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
}
