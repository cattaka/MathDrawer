package net.cattaka.swing.util;

import java.awt.Insets;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.PropertiesEx;


public class ButtonsBundle {
	private static HashMap<String, ButtonDefinition> buttonDifinitionMap;
	static {
		try {
			buttonDifinitionMap = new HashMap<String, ButtonDefinition>();
			PropertiesEx properties = ButtonDifinitionBundleLoader.getProperties();
			Enumeration<Object> keys = properties.keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				String[] strArray = properties.getPropertyArray(key.toString());
				ButtonDefinition buttonDefinition = new ButtonDefinition();
				if (buttonDefinition.restoreStringArray(strArray)) {
					buttonDifinitionMap.put(key.toString(), buttonDefinition);
				}
			}
		} catch(Exception e) {
			// 起こりえない
			ExceptionHandler.error(e);
		}
	}

	public static void applyMenuDifinition(JMenuItem button, String key) {
		ButtonDefinition buttonDefinition = buttonDifinitionMap.get(key);
		if (buttonDefinition != null) {
			Character mnemonic = buttonDefinition.getMnemonic();
			String buttonText = buttonDefinition.getButtonText();
			KeyStroke keyStroke = buttonDefinition.createKeyStroke();
//			String toolTip = buttonDefinition.getButtonToolTip();
			if (buttonText == null && buttonText.length() > 0) {
				buttonText = key;
			}
			if (mnemonic != null) {
				buttonText += "(" + mnemonic + ")";
				button.setMnemonic(mnemonic);
			}
			if (keyStroke != null) {
				button.setAccelerator(keyStroke);
			}
			button.setText(buttonText);
		} else {
			button.setText(key);
		}
	}
	
	public static void applyButtonDifinition(AbstractButton button, String key) {
		ButtonDefinition buttonDefinition = buttonDifinitionMap.get(key);
		if (buttonDefinition != null) {
			Character mnemonic = buttonDefinition.getMnemonic();
			String buttonText = buttonDefinition.getButtonText();
			String toolTip = buttonDefinition.getButtonToolTip();
			if (buttonText == null && buttonText.length() > 0) {
				buttonText = key;
			}
			if (mnemonic != null) {
				buttonText += "(" + mnemonic + ")";
				button.setMnemonic(mnemonic);
			}
			button.setText(buttonText);
			if (toolTip != null && toolTip.length() > 0) {
				button.setToolTipText(toolTip);
			}
		} else {
			button.setText(key);
		}
	}
	public static void applyButtonDifinition(AbstractButton button, Icon icon, String key, boolean withoutText) {
		ButtonDefinition buttonDefinition = buttonDifinitionMap.get(key);
		if (buttonDefinition != null) {
			Character mnemonic = buttonDefinition.getMnemonic();
			String buttonText = buttonDefinition.getButtonText();
			String toolTip = buttonDefinition.getButtonToolTip();
			button.setIcon(icon);
			button.setMargin(new Insets(0,0,0,0));
			if (buttonText == null && buttonText.length() > 0) {
				buttonText = key;
			}
			if (mnemonic != null) {
				buttonText += "(" + mnemonic + ")";
				button.setMnemonic(mnemonic);
			}
			if (!withoutText) {
				button.setText(buttonText);
			}
			if (toolTip != null && toolTip.length() > 0) {
				button.setToolTipText(toolTip);
			}
		} else {
			button.setText(key);
		}
	}
}

class ButtonDifinitionBundleLoader {
	public static PropertiesEx getProperties() {
		PropertiesEx properties = new PropertiesEx();
		try {
			InputStream in = MessageBundle.class.getClassLoader().getResourceAsStream("buttons_ja_JP.properties");
			properties.loadFromXML(in);
		} catch(Exception e) {
			ExceptionHandler.fatal(e);
		}
		return properties;
	}
}
