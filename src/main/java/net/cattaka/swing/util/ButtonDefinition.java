package net.cattaka.swing.util;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class ButtonDefinition {
	private String buttonText;
	private String buttonToolTip;
	private Character mnemonic;
	private boolean acceleratorCtrlMask;
	private boolean acceleratorShiftMask;
	private boolean acceleratorAltMask;
	private String acceleratorKey;
	
	public ButtonDefinition() {
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public String getButtonToolTip() {
		return buttonToolTip;
	}

	public void setButtonToolTip(String buttonToolTip) {
		this.buttonToolTip = buttonToolTip;
	}

	public Character getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(Character mnemonic) {
		this.mnemonic = mnemonic;
	}
	
	public boolean isAcceleratorCtrlMask() {
		return acceleratorCtrlMask;
	}

	public void setAcceleratorCtrlMask(boolean acceleratorCtrlMask) {
		this.acceleratorCtrlMask = acceleratorCtrlMask;
	}

	public boolean isAcceleratorShiftMask() {
		return acceleratorShiftMask;
	}

	public void setAcceleratorShiftMask(boolean acceleratorShiftMask) {
		this.acceleratorShiftMask = acceleratorShiftMask;
	}

	public boolean isAcceleratorAltMask() {
		return acceleratorAltMask;
	}

	public void setAcceleratorAltMask(boolean acceleratorAltMask) {
		this.acceleratorAltMask = acceleratorAltMask;
	}

	public String getAcceleratorKey() {
		return acceleratorKey;
	}

	public void setAcceleratorKey(String acceleratorKey) {
		this.acceleratorKey = acceleratorKey;
	}
	
	public KeyStroke createKeyStroke() {
		KeyStroke result = null;
		int modifiers = 0;
		if (isAcceleratorCtrlMask()) {
			modifiers = modifiers | InputEvent.CTRL_MASK;
		}
		if (isAcceleratorShiftMask()) {
			modifiers = modifiers | InputEvent.SHIFT_MASK;
		}
		if (isAcceleratorAltMask()) {
			modifiers = modifiers | InputEvent.ALT_MASK;
		}
		if (acceleratorKey != null && acceleratorKey.length() > 0) {
			if (acceleratorKey.equals("PgUp")) {
				result = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, modifiers);
			} else if (acceleratorKey.equals("PgDn")) {
				result = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, modifiers);
			} else if (acceleratorKey.equals("Tab")) {
				result = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, modifiers);
			} else {
				result = KeyStroke.getKeyStroke(acceleratorKey.charAt(0), modifiers);
			}
		}
		return result;
	}

	public String[] toStringArray() {
		return new String[] {
				buttonText,
				buttonToolTip,
				((mnemonic != null) ? mnemonic.toString() : ""),
				((acceleratorKey != null) ? acceleratorKey.toString() : ""),
				(acceleratorCtrlMask) ? "1" : "",
				(acceleratorShiftMask) ? "1" : "",
				(acceleratorAltMask) ? "1" : ""
		};
	}
	
	public boolean restoreStringArray(String[] stringArray) {
		if (stringArray.length < 6) {
			return false;
		}
		buttonText = stringArray[0];
		buttonToolTip = stringArray[1];
		if (stringArray[2] != null && stringArray[2].length() > 0) {
			mnemonic = stringArray[2].charAt(0);
		} else {
			mnemonic = null;
		}
		if (stringArray[3] != null && stringArray[3].length() > 0) {
			acceleratorKey = stringArray[3];
		} else {
			acceleratorKey = null;
		}
		acceleratorCtrlMask = (stringArray[4] != null && stringArray[4].equals("1"));
		acceleratorShiftMask = (stringArray[5] != null && stringArray[5].equals("1"));
		acceleratorAltMask = (stringArray[4] != null && stringArray[6].equals("1"));
		return true;
	}
}
