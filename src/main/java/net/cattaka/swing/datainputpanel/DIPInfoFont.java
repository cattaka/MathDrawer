/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.cattaka.swing.FontChooser;

public class DIPInfoFont implements DIPInfo, ActionListener {
	private String label;
	private Font defaultData;
	private Font selectedData;
	private JTextField field;
	private JComponent component;
	private FontChooser fontChooser;
	private JButton selectButton;
	
	public DIPInfoFont(String label, Font defaultData)
			throws InvalidDataTypeException {
		super();
		if (label == null || defaultData == null)
			throw new NullPointerException();

		this.label = label;
		this.defaultData = defaultData;
		this.selectedData = defaultData;
		this.field = new JTextField(defaultData.getName(), 0);
		this.field.setEditable(false);

		JPanel panel = new JPanel();
		selectButton = new JButton("Select");
		selectButton.setActionCommand("select");
		selectButton.addActionListener(this);
		GridBagLayout gl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gl.setConstraints(field, gc);
		gc.gridx++;
		gc.weightx = 0;
		gl.setConstraints(selectButton, gc);
		panel.setLayout(gl);
		panel.add(field);
		panel.add(selectButton);
		this.component = panel;
	}

	public Font getFontValue() {
		return this.selectedData;
	}
	
	public Object getValue() {
		return this.selectedData;
	}
	
	public void setValue(Font value) {
		this.selectedData = value;
		if (value != null) {
			field.setText(value.getFontName() + "(" + value.getSize() + ")");
		}
	}

	public void makeDefault() {
		this.setValue(defaultData);
	}

	public String getLabel() {
		return label;
	}

	public JComponent getComponent() {
		return component;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("select")) {
			if (fontChooser == null) {
				fontChooser = new FontChooser();
			}
			if (this.selectedData != null) {
				fontChooser.setSelectedFont(this.selectedData);
			}
			Font t = fontChooser.showFontSelectDialog();
			if (t != null) {
				this.setValue(t);
			}
		}
	}

	public boolean isEnable() {
		return field.isEnabled();
	}

	public void setEnable(boolean enable) {
		selectButton.setEnabled(enable);
		field.setEnabled(enable);
	}
}
