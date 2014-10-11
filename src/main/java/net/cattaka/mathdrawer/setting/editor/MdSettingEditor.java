package net.cattaka.mathdrawer.setting.editor;

import javax.swing.JComponent;

import net.cattaka.mathdrawer.exception.InvalidValueException;

abstract public class MdSettingEditor {
	abstract public void setValue(Object value) throws InvalidValueException;
	abstract public Object getValue() throws InvalidValueException;
	abstract public JComponent getEditorComponent();
	abstract public boolean setMdSettingObserver(MdSettingObserver mdSettingChangeListener);
}
