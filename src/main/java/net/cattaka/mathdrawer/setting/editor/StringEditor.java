package net.cattaka.mathdrawer.setting.editor;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.cattaka.mathdrawer.exception.InvalidValueException;

public class StringEditor extends MdSettingEditor {
	protected JPanel editorComponent;
	protected JCheckBox nullCheck;
	protected JTextField valueField;
	protected boolean nullable;
	protected Color succeedColor = Color.WHITE;
	protected Color errorColor = new Color(255,127,127);
	
	class ItemListenerImpl implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateNullCheck();
		}
	}
	
	public StringEditor(boolean nullable) {
		this.nullable = nullable;
		makeLayout();
	}
	
	private void makeLayout() {
		editorComponent = new JPanel();
		nullCheck = new JCheckBox();
		valueField = new JTextField();
		
		nullCheck.addItemListener(new ItemListenerImpl());
		
		editorComponent.setLayout(new BoxLayout(editorComponent, BoxLayout.LINE_AXIS));
		if (nullable) {
			nullCheck.setSelected(false);
			editorComponent.add(valueField);
			editorComponent.add(nullCheck);
		} else {
			nullCheck.setSelected(true);
			editorComponent.add(valueField);
		}
		
		updateNullCheck();
	}
	
	@Override
	public JComponent getEditorComponent() {
		return editorComponent;
	}

	@Override
	public Object getValue() throws InvalidValueException {
		if (nullCheck.isSelected()) {
			return this.valueField.getText();
		} else {
			return null;
		}
	}

	@Override
	public void setValue(Object value) throws InvalidValueException {
		if (nullable) {
			if (value == null) {
				this.nullCheck.setSelected(false);
				this.valueField.setText("");
			} else {
				this.nullCheck.setSelected(true);
				this.valueField.setText(value.toString());
			}
		} else {
			this.nullCheck.setSelected(true);
			if (value == null) {
				this.valueField.setText("");
			} else {
				this.valueField.setText(value.toString());
			}
		}
		this.valueField.setBackground(succeedColor);
		updateNullCheck();
	}

	@Override
	public boolean setMdSettingObserver(MdSettingObserver mdSettingChangeListener) {
		return false;
	}

	public String getStringValue() throws InvalidValueException {
		return (String)this.getValue();
	}

	private void updateNullCheck() {
		this.valueField.setEnabled(this.nullCheck.isSelected());
	}
}
