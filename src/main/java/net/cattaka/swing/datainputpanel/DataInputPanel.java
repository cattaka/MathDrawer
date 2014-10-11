/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.cattaka.swing.util.ButtonsBundle;

public class DataInputPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DIPInfo[] dipis;
	private int[] cols;
	private JPanel panelButtons;
	private JButton buttonApprove;
	private JButton buttonCancel;

	public DataInputPanel(DIPInfo[] dipis) {
		this(dipis, new int[] { dipis.length });
	}

	public DataInputPanel(DIPInfo[] dipis, int[] cols) {
		super();
		if (dipis == null || cols == null)
			throw new NullPointerException();
		for (int i = 0; i < dipis.length; i++) {
			if (dipis[i] == null)
				throw new NullPointerException();
		}
		int num = 0;
		for (int i = 0; i < cols.length; i++)
			num += cols[i];
		if (num != dipis.length)
			throw new ArrayIndexOutOfBoundsException();

		this.dipis = dipis;
		this.cols = cols;
		makeLayout();
	}

	private void makeLayout() {
		JLabel[] labels = new JLabel[dipis.length];
		JComponent[] components = new JComponent[dipis.length];
		for (int i = 0; i < dipis.length; i++) {
			String label = dipis[i].getLabel();
			if (label != null)
				labels[i] = new JLabel(dipis[i].getLabel());
			components[i] = dipis[i].getComponent();
		}

		panelButtons = new JPanel();
		{
			buttonApprove = new JButton();
			buttonCancel = new JButton();
			ButtonsBundle.applyButtonDifinition(buttonApprove, "ok");
			ButtonsBundle.applyButtonDifinition(buttonCancel, "cancel");
			panelButtons.setLayout(new GridLayout(1, 2));
			panelButtons.add(buttonApprove);
			panelButtons.add(buttonCancel);
		}

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);
//		int gridXStart = 0;
		int gridYStart = 0;
		int p = 0;
		gbc.insets = new Insets(0, 10, 0, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		int buttonPosY = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		for (int i = 0; i < cols.length; i++) {
			if (buttonPosY < cols[i]) {
				buttonPosY = cols[i];
			}
			gbc.gridy = gridYStart;
			for (int j = 0; j < cols[i]; j++) {
				if (labels[p] != null) {
					gbc.gridwidth = 1;
					gbc.weightx = 0;
					gbl.setConstraints(labels[p], gbc);
					gbc.gridx++;
					;
					gbc.weightx = 1;
					gbl.setConstraints(components[p], gbc);
					gbc.gridx--;
					gbc.gridy++;
					add(labels[p]);
					add(components[p]);
				} else {
					gbc.gridwidth = 2;
					gbc.weightx = 0;
					gbl.setConstraints(components[p], gbc);
					gbc.gridy++;
					add(components[p]);
				}
				p++;
			}
			gbc.gridx += 2;
		}

		gbc.gridx = 0;
		gbc.gridy = buttonPosY + 1;
		gbc.gridwidth = cols.length * 2;
		gbl.setConstraints(panelButtons, gbc);
		add(panelButtons);
	}

	private boolean isValidData(int i) {
		return (dipis[i].getValue() != null);
	}

	public boolean isValidData() {
		for (int i = 0; i < dipis.length; i++) {
			if (!isValidData(i)) {
				return false;
			}
		}
		return true;
	}

	private void validateData(int i) {
		if (dipis[i].getValue() == null) {
			dipis[i].makeDefault();
		}
	}

	public void validateData() {
		for (int i = 0; i < dipis.length; i++) {
			validateData(i);
		}
	}

	public boolean isShowButtons() {
		return panelButtons.isVisible();
	}

	public void setShowButtons(boolean flag) {
		panelButtons.setVisible(flag);
	}

	public Object[] getValues() {
		Object[] objs = new Object[dipis.length];
		for (int i = 0; i < dipis.length; i++) {
			if (dipis[i].isEnable()) {
				Object ob = dipis[i].getValue();
				if (ob == null)
					return null;
				objs[i] = ob;
			} else {
				objs[i] = null;
			}
		}
		return objs;
	}

	public JButton getButtonApprove() {
		return buttonApprove;
	}

	public JButton getButtonCancel() {
		return buttonCancel;
	}
}
