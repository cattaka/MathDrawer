package net.cattaka.mathdrawer.gui.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.exception.InvalidValueException;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.setting.constraints.MdSettingConstraints;
import net.cattaka.mathdrawer.setting.editor.MdSettingChangeListener;
import net.cattaka.mathdrawer.setting.editor.MdSettingEditorPanel;
import net.cattaka.mathdrawer.setting.entity.MdProject;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.ExceptionHandler;

public class DrawerMdSettingEditorPanel extends JPanel implements MdEditorInterface {
	private static final long serialVersionUID = 1L;
	private MdEditorInterface parentComponent;
	private MdSettingEditorPanel mdSettingEditorPanel;
	private MdSettingChangeListener mdSettingChangeListener;
	private StdScrollPane messageAreaPane;
	private JTextArea messageArea;
	private JButton okButton;
	private JButton cancelButton;

	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("ok")) {
				doOk();
			} else if (e.getActionCommand().equals("cancel")) {
				doCancel();
			}
		}
	}

	public DrawerMdSettingEditorPanel(MdEditorInterface parentComponent, MdSettingChangeListener mdSettingChangeListener) {
		this.parentComponent = parentComponent;
		this.mdSettingChangeListener = mdSettingChangeListener;
		makeLayout();
	}

	private void makeLayout() {
		this.mdSettingEditorPanel = new MdSettingEditorPanel(this.mdSettingChangeListener);
		this.okButton = new JButton();
		this.cancelButton = new JButton();

		this.messageArea = new JTextArea();
		this.messageArea.setEditable(false);
		this.messageArea.setBackground(this.getBackground());
		this.messageAreaPane = new StdScrollPane(messageArea);

		ActionListenerImpl al = new ActionListenerImpl();
		
		ButtonsBundle.applyButtonDifinition(okButton, "ok");
		ButtonsBundle.applyButtonDifinition(cancelButton, "cancel");
		okButton.setActionCommand("ok");
		okButton.addActionListener(al);
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(al);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx= 1;
		gbc.weighty= 0;
		gbl.setConstraints(mdSettingEditorPanel, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx= 1;
		gbc.weighty= 0;
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(okButton, gbc);
		gbc.gridx++;
		gbl.setConstraints(cancelButton, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx--;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weighty= 1;
		gbl.setConstraints(messageAreaPane, gbc);

		this.setLayout(gbl);
		this.add(mdSettingEditorPanel);
		this.add(messageAreaPane);
		this.add(okButton);
		this.add(cancelButton);
	}
	
	public void doOk() {
	}

	public void doCancel() {
	}

	// Delegate --------------------------------------------
	
	public Map<String,Object> getValues() throws InvalidValueException {
		return mdSettingEditorPanel.getValues();
	}

	public void setMdSettingConstraints(MdSettingConstraints mdSettingConstraints, DrawerUtil drawerUtil) {
		this.mdSettingEditorPanel.setMdSettingConstraints(mdSettingConstraints, drawerUtil);
		this.okButton.setVisible(mdSettingConstraints != null);
		this.cancelButton.setVisible(mdSettingConstraints != null);
	}

	public void setValues(Map<String,Object> values) throws InvalidValueException {
		mdSettingEditorPanel.setValues(values);
	}
	
	public void setMessage(String message) {
		if (message == null || message.length() == 0) {
			this.messageArea.setText("");
		} else {
			this.messageArea.setText(message);
			this.messageArea.setCaretPosition(0);
		}
	}
	/**
	 * オーバーライド用
	 * @param src
	 */
	public void mdSettingChange(String key, Object value) {
		ExceptionHandler.debug(this.getClass().getName() + ".mdSettingChange : " + key + " : " + value);
	}

	// Interface ---------------------------------------
	public DrawerUtil getDrawerUtil() {
		return parentComponent.getDrawerUtil();
	}

	public MdProject getMdProject() {
		return parentComponent.getMdProject();
	}

	public void reloadMdProject() {
		// TODO Auto-generated method stub
	}

	public void doGuiLayout() {
		// TODO Auto-generated method stub
	}

	public MdConfig getMdConfig() {
		return parentComponent.getMdConfig();
	}

	public MdSingletonBundle getMdSingletonBundle() {
		return parentComponent.getMdSingletonBundle();
	}

	public void relayMdMessage(MdMessage mdMessage) {
		// TODO Auto-generated method stub
	}

	public void reloadMdConfig() {
		// TODO Auto-generated method stub
	}

	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}
}
