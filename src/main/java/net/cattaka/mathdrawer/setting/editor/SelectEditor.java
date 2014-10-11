package net.cattaka.mathdrawer.setting.editor;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import net.cattaka.mathdrawer.exception.InvalidValueException;

public class SelectEditor extends MdSettingEditor {
	protected JPanel editorComponent;
	protected JComboBox valueComboBox;
	protected boolean nullable;
	protected Color succeedColor = Color.WHITE;
	protected Color errorColor = new Color(255,127,127);
	private SelectItem[] items;
	private MdSettingObserver mdSettingChangeListener;
	
//	class ItemListenerImpl implements ItemListener {
//		public void itemStateChanged(ItemEvent e) {
//			if (e.getStateChange() == ItemEvent.SELECTED && mdSettingChangeListener != null) {
//				mdSettingChangeListener.mdSettingChange(SelectEditor.this);
//			}
//		}
//	}
//	
	class PopupMenuListenerImpl implements PopupMenuListener {
		private Object lastValue;
		public void popupMenuCanceled(PopupMenuEvent e) {
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			if (mdSettingChangeListener != null && lastValue != valueComboBox.getSelectedItem()) {
				mdSettingChangeListener.mdSettingChange(SelectEditor.this);
				lastValue = valueComboBox.getSelectedItem();
			}
		}

		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			lastValue = valueComboBox.getSelectedItem();
		}
	}
	
	class SelectItem {
		private String label;
		private Object value;
		public SelectItem(String label, Object value) {
			super();
			this.label = label;
			this.value = value;
		}
		public String getLabel() {
			return label;
		}
		public Object getValue() {
			return value;
		}
		@Override
		public String toString() {
			return label;
		}
	}
	
	public SelectEditor(String[] items, boolean nullable) {
		this.nullable = nullable;
		if (nullable) {
			this.items = new SelectItem[items.length+1];
			this.items[0] = new SelectItem("",null);
			for (int i=0;i<items.length;i++) {
				this.items[i+1] = new SelectItem(items[i],items[i]);
			}
		} else {
			this.items = new SelectItem[items.length];
			for (int i=0;i<items.length;i++) {
				this.items[i] = new SelectItem(items[i],items[i]);
			}
		}
		makeLayout();
	}
	
	private void makeLayout() {
		editorComponent = new JPanel();
		valueComboBox = new JComboBox(this.items);
//		valueComboBox.addItemListener(new ItemListenerImpl());
		valueComboBox.addPopupMenuListener(new PopupMenuListenerImpl());
		
		editorComponent.setLayout(new BoxLayout(editorComponent, BoxLayout.LINE_AXIS));
		if (nullable) {
			editorComponent.add(valueComboBox);
		} else {
			editorComponent.add(valueComboBox);
		}
	}
	
	@Override
	public JComponent getEditorComponent() {
		return editorComponent;
	}

	@Override
	public Object getValue() throws InvalidValueException {
		Object obj = this.valueComboBox.getSelectedItem();
		if (obj instanceof SelectItem) {
			SelectItem selectItem = (SelectItem) obj;
			return selectItem.getValue();
		} else {
			return null;
		}
	}

	@Override
	public void setValue(Object value) throws InvalidValueException {
		SelectItem target = null;
		for (SelectItem selectItem : this.items) {
			if (value == null) {
				if (selectItem.getValue() == null) {
					target = selectItem;
					break;
				}
			} else {
				if (value.equals(selectItem.getValue())) {
					target = selectItem;
					break;
				}
			}
		}
		if (target != null) {
			this.valueComboBox.setSelectedItem(target);
			this.valueComboBox.setBackground(succeedColor);
		}
	}
	
	@Override
	public boolean setMdSettingObserver(MdSettingObserver mdSettingChangeListener) {
		this.mdSettingChangeListener = mdSettingChangeListener;
		return true;
	}

	public String getStringValue() throws InvalidValueException {
		return (String)this.getValue();
	}
}
