package net.cattaka.mathdrawer.setting.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.util.OrderedMap;

public class MdSettingEditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MdSettingConstraints mdSettingConstraints;
	private OrderedMap<String,MdSettingEditor> mdSettingEditors;
	private MdSettingChangeListener mdSettingChangeListener;
	private MdSettingObserverImpl mdSettingObserver;
	
	class MdSettingObserverImpl implements MdSettingObserver {
		private boolean enable = true;
		public void mdSettingChange(MdSettingEditor src) {
			if (enable && mdSettingChangeListener != null) {
				Object value;
				try {
					value = src.getValue();
					String key = null;
					for (Entry<String,MdSettingEditor> entry:mdSettingEditors.entrySet()) {
						if (entry.getValue() == src) {
							key = entry.getKey();
							break;
						}
					}
					if (key != null) {
						mdSettingChangeListener.mdSettingChange(key, value);
					}
				} catch (InvalidValueException e) {
					// 無視
				}
			}
		}
		public boolean isEnable() {
			return enable;
		}
		public void setEnable(boolean enable) {
			this.enable = enable;
		}
	}
	
	public MdSettingEditorPanel(MdSettingChangeListener mdSettingChangeListener) {
		this.mdSettingChangeListener = mdSettingChangeListener;
	}
	
	public void setMdSettingConstraints(MdSettingConstraints mdSettingConstraints, DrawerUtil drawerUtil) {
		this.mdSettingConstraints = mdSettingConstraints;
		makeLayout(drawerUtil);
	}
	
	private void makeLayout(DrawerUtil drawerUtil) {
		this.removeAll();
		
		if (this.mdSettingConstraints != null) {
			GridBagLayout gbl = new GridBagLayout();
			GridBagConstraints gbc = new GridBagConstraints();
	
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridx=0;
			gbc.gridy=0;
	
			this.mdSettingObserver = new MdSettingObserverImpl();
			OrderedMap<String, SettingConstraint<?>> mscl = mdSettingConstraints.getSettingConstantMap();
			mdSettingEditors = new OrderedMap<String, MdSettingEditor>(new HashMap<String, MdSettingEditor>());
			for (String key:mscl.getKeyList()) {
				SettingConstraint<?> sc = mscl.get(key);
				if (sc.isEnable()) {
					JLabel nameLabel = new JLabel(sc.getName());
					MdSettingEditor msd = sc.createMdSettingEditor(drawerUtil);
					mdSettingEditors.put(key,msd);
					JComponent editorComponent = msd.getEditorComponent();
					if (sc.getDescription() != null) {
						nameLabel.setToolTipText(sc.getDescription());
					}
					if (sc.isApplyImmediate()) {
						msd.setMdSettingObserver(mdSettingObserver);
					}
					
					gbc.gridx=0;
					gbc.weightx = 0;
					gbl.setConstraints(nameLabel, gbc);
					gbc.gridx=1;
					gbc.weightx = 1;
					gbl.setConstraints(editorComponent, gbc);
					
					gbc.gridy++;
					
					this.add(nameLabel);
					this.add(editorComponent);
				}
			}
			this.setLayout(gbl);
		}
	}
	
	public void setValues(Map<String, Object> values) throws InvalidValueException  {
		this.mdSettingObserver.setEnable(false);
		try {
			if (this.mdSettingConstraints != null) {
				for (Entry<String, Object> entry:values.entrySet()) {
					MdSettingEditor mse = this.mdSettingEditors.get(entry.getKey());
					if (mse != null) {
						mse.setValue(entry.getValue());
					}
				}
			}
		} finally {
			this.mdSettingObserver.setEnable(true);
		}
	}
	
	public Map<String,Object> getValues() throws InvalidValueException {
		Map<String, Object> result = new HashMap<String, Object>();
		if (this.mdSettingConstraints == null) {
			return new HashMap<String, Object>();
		}
		for (Entry<String,MdSettingEditor> entry:mdSettingEditors.entrySet()){
			try {
				result.put(entry.getKey(), entry.getValue().getValue());
			} catch(InvalidValueException e) {
				// 無視
			}
		}
		return result;
	}
	
	public List<String> getKeyList() {
		return this.mdSettingEditors.getKeyList();
	}
}
