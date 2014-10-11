package net.cattaka.swing.util;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.cattaka.util.ExceptionHandler;

public class LookAndFeelBundle {
	
	public static String getDefaultLookAndFeelName() {
		return UIManager.getSystemLookAndFeelClassName();
	}

	public static String[] getLookAndFeelClassNames() {
		UIManager.LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
		String[] result = new String[lafis.length];
		for (int i=0;i<lafis.length;i++) {
			result[i] = lafis[i].getClassName();
		}
		return result;
	}
	
	public static String[] getLookAndFeelNames() {
		UIManager.LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
		String[] result = new String[lafis.length];
		for (int i=0;i<lafis.length;i++) {
			result[i] = lafis[i].getName();
		}
		return result;
	}
	
	public static String getLookAndFeelClassName() {
		String result;
		LookAndFeel laf = UIManager.getLookAndFeel();
		if (laf != null) {
			result = laf.getClass().getName();
		} else {
			result = UIManager.getSystemLookAndFeelClassName();
		}
		return result;
	}

	public static void setLookAndFeelClassName(String lookAndFeelClassName) {
		try {
			UIManager.setLookAndFeel(lookAndFeelClassName);
		} catch (ClassNotFoundException e) {
			ExceptionHandler.error(e);
		} catch (UnsupportedLookAndFeelException e) {
			ExceptionHandler.error(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.error(e);
		} catch (InstantiationException e) {
			ExceptionHandler.error(e);
		}
		
	}
}
