package net.cattaka.swing.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.security.AccessControlException;

public class ClipboardUtil {
	public static boolean copyBufferedImage(BufferedImage image) {
		boolean result = true;
		Clipboard cb = null;
		if (image == null) {
			result = false;
		}
		
		if (result) {
			try {
				cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			} catch (AccessControlException e) {
				e.printStackTrace();
				result = false;
			}
		}
		
		if (result) {
			try {
				BufferedImageTransferable it = new BufferedImageTransferable(image);
				cb.setContents(it, null);
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
		}
		
		return result;
	}
}
