package net.cattaka.mathdrawer.debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.setting.constraints.DoubleConstraint;
import net.cattaka.mathdrawer.setting.constraints.IntegerConstraint;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.constraints.SelectConstraint;
import net.cattaka.mathdrawer.setting.constraints.SettingConstraint;
import net.cattaka.mathdrawer.setting.constraints.StringConstraint;
import net.cattaka.mathdrawer.setting.constraints.Vector2dConstraint;
import net.cattaka.mathdrawer.setting.constraints.Vector3dConstraint;
import net.cattaka.mathdrawer.setting.constraints.Vector4dConstraint;
import net.cattaka.mathdrawer.setting.editor.MdSettingChangeListener;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditorPanel;
import net.cattaka.util.OrderedMap;

public class MdSettingEditorPanelDebug {
	static class MdSettingChangeListenerImpl implements MdSettingChangeListener {
		public void mdSettingChange(String key, Object value) {
			// TODO Auto-generated method stub
			System.out.println(this.getClass().getName() + " : " + key + " : " + value);
		}
	}

	static class ActionListenerImple implements ActionListener {
		private MdSettingEditorPanel msep;
		public ActionListenerImple(MdSettingEditorPanel msep) {
			this.msep = msep;
		}
		
		public void actionPerformed(ActionEvent e) {
			try {
				Map<String, Object> values = msep.getValues();
				for (String key: msep.getKeyList()) {
					Object value = values.get(key);
					if (value != null) {
						System.out.print(String.format("%s : %s : %s", key, value.getClass().getName(), value));
					} else {
						System.out.print(String.format("%s : %s", key, "null"));
					}
					System.out.println();
				}
			} catch (InvalidValueException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			test1();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	public static void test1() throws Exception {
		JDialog frame = new JDialog();
		frame.setSize(300,400);
		frame.setModal(true);
		
		OrderedMap<String,SettingConstraint<?>> settingConstantMap = new OrderedMap<String,SettingConstraint<?>>(new HashMap<String,SettingConstraint<?>>());
		settingConstantMap.put("SC01",new StringConstraint("SC01", "S-desc01", true, null));
		settingConstantMap.put("SC02",new StringConstraint("SC02", "S-desc02", false, null));
		settingConstantMap.put("IC01",new IntegerConstraint("IC01", "I-desc01", true, null));
		settingConstantMap.put("IC02",new IntegerConstraint("IC02", "I-desc02", false, null));
		settingConstantMap.put("DC01",new DoubleConstraint("DC01", "D-desc01", true, null));
		settingConstantMap.put("DC02",new DoubleConstraint("DC02", "D-desc02", false, null));
		settingConstantMap.put("V2D01",new Vector2dConstraint("V2D01", "V2D-desc01", true, null));
		settingConstantMap.put("V2D02",new Vector2dConstraint("V2D02", "V2D-desc02", false, null));
		settingConstantMap.put("V3D01",new Vector3dConstraint("V3D01", "V3D-desc01", true, null));
		settingConstantMap.put("V3D02",new Vector3dConstraint("V3D02", "V3D-desc02", false, null));
		settingConstantMap.put("V4D01",new Vector4dConstraint("V4D01", "V4D-desc01", true, null));
		settingConstantMap.put("V4D02",new Vector4dConstraint("V4D02", "V4D-desc02", false, null));
		settingConstantMap.put("SL01",new SelectConstraint("SL01", "SL-desc01", new String[]{"a","b"}, true, null));
		settingConstantMap.put("SL02",new SelectConstraint("SL02", "SL-desc02", new String[]{"a","b"}, false, null));
		MdSettingConstraints msc = new MdSettingConstraints();
		msc.setSettingConstantMap(settingConstantMap);
		
		MdSettingEditorPanel msep = new MdSettingEditorPanel(new MdSettingChangeListenerImpl());
		msep.setMdSettingConstraints(msc, null);
		JButton okButton = new JButton("ok");
		okButton.addActionListener(new ActionListenerImple(msep));
		
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		frame.getContentPane().add(msep);
		frame.getContentPane().add(okButton);
		
		frame.setVisible(true);
	}
}
