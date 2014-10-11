/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DIPInfoSlider implements DIPInfo, ChangeListener {
	private String label;
	private JSlider slider;
	private JTextField field;
	private JComponent component;
	//private int min,max;
	private int defaultData;

	public DIPInfoSlider(String label, int min, int max, int defaultData)
			throws InvalidDataTypeException {
		super();
		if (label == null)
			throw new NullPointerException();
		if (defaultData < min || max < defaultData)
			throw new InvalidDataTypeException();

		this.label = label;
		//        this.min = min;
		//        this.max = max;
		this.defaultData = defaultData;

		this.slider = new JSlider(min, max);
		this.field = new JTextField(4);
		this.slider.addChangeListener(this);
		this.field.setEditable(false);

		JPanel panel = new JPanel();
		GridBagLayout gl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gl.setConstraints(slider, gc);
		gc.gridx++;
		gc.weightx = 0;
		gl.setConstraints(field, gc);
		panel.setLayout(gl);
		panel.add(slider);
		panel.add(field);
		this.component = panel;

		makeDefault();
	}

	public Object getValue() {
		return new Integer(slider.getValue());
	}

	public void setValue(int value) {
		slider.setValue(value);
	}

	public void makeDefault() {
		slider.setValue(defaultData);
		field.setText(String.valueOf(defaultData));
	}

	public String getLabel() {
		return label;
	}

	public JComponent getComponent() {
		return component;
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == slider) {
			field.setText(String.valueOf(slider.getValue()));
		}
	}

	public boolean isEnable() {
		return slider.isEnabled();
	}

	public void setEnable(boolean enable) {
		field.setEnabled(enable);
		slider.setEnabled(enable);
	}
}
