package net.cattaka.swing;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;

public class FontChooser extends JDialog {
	private static final long serialVersionUID = 1L;
	private JList fontNameList;
	private JList fontSizeList;
	private Font currentFont;
	private JTextField previewPlain;
	private JTextField previewBold;
	private JTextField previewItalic;
	private JTextField previewBoldItalic;
	
	class ButtonPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public ButtonPanel() {
			JButton okButton = new JButton();
			JButton cancelButton = new JButton();
			
			ActionListenerImpl al = new ActionListenerImpl();
			ButtonsBundle.applyButtonDifinition(okButton, "ok");
			ButtonsBundle.applyButtonDifinition(cancelButton, "cancel");
			okButton.setActionCommand("ok");
			cancelButton.setActionCommand("cancel");
			okButton.addActionListener(al);
			cancelButton.addActionListener(al);
			
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.add(okButton);
			this.add(cancelButton);
		}
	}
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("ok")) {
				currentFont = getSelectedFont();
				if (currentFont != null) {
					setVisible(false);
				}
			} else if (e.getActionCommand().equals("cancel")) {
				currentFont = null;
				setVisible(false);
			}
		}
	}
	
	class ListSelectionListenerImpl implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			updatePreview();
		}
	}
	
	public FontChooser() throws HeadlessException {
		super();
		makeLayout();
	}
	public FontChooser(Dialog owner) throws HeadlessException {
		super(owner);
		makeLayout();
	}

	public FontChooser(Frame owner) throws HeadlessException {
		super(owner);
		makeLayout();
	}

	public void makeLayout() {
		setTitle(MessageBundle.getMessage("font"));
		setSize(500,400);
		
		String sampleStringForFont = MessageBundle.getMessage("sample_string_for_font");
		previewPlain = new JTextField(sampleStringForFont);
		previewBold = new JTextField(sampleStringForFont);
		previewItalic = new JTextField(sampleStringForFont);
		previewBoldItalic = new JTextField(sampleStringForFont);
		
		ListSelectionListenerImpl lsl = new ListSelectionListenerImpl();
		fontNameList = new JList();
		fontSizeList = new JList();
		{
			String[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			fontNameList.setListData(fontList);
			fontNameList.addListSelectionListener(lsl);
		}
		{
			Integer fontSizes[] = { 8, 10, 11, 12, 14, 16, 18, 20, 24, 30, 36, 40, 48, 60, 72};
			fontSizeList.setListData(fontSizes);
			fontSizeList.addListSelectionListener(lsl);
		}
		StdScrollPane fontNameScrollPane = new StdScrollPane(fontNameList);
		StdScrollPane fontSizeScrollPane = new StdScrollPane(fontSizeList);
		JLabel fontNameLabel = new JLabel(MessageBundle.getMessage("font_name"));
		JLabel fontSizeLabel = new JLabel(MessageBundle.getMessage("font_size"));
		JLabel previewLabel = new JLabel(MessageBundle.getMessage("preview"));
		ButtonPanel buttonPanel = new ButtonPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(fontNameLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(fontNameScrollPane, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbl.setConstraints(fontSizeLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(fontSizeScrollPane, gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.weighty = 1;
		gbl.setConstraints(previewLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(previewPlain, gbc);
		gbc.gridy++;
		gbl.setConstraints(previewBold, gbc);
		gbc.gridy++;
		gbl.setConstraints(previewItalic, gbc);
		gbc.gridy++;
		gbl.setConstraints(previewBoldItalic, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy++;
		gbc.weighty = 0;
		gbl.setConstraints(buttonPanel, gbc);

		this.getContentPane().setLayout(gbl);
		this.getContentPane().add(fontNameLabel);
		this.getContentPane().add(fontNameScrollPane);
		this.getContentPane().add(fontSizeLabel);
		this.getContentPane().add(fontSizeScrollPane);
		this.getContentPane().add(buttonPanel);
		this.getContentPane().add(previewLabel);
		this.getContentPane().add(previewPlain);
		this.getContentPane().add(previewBold);
		this.getContentPane().add(previewItalic);
		this.getContentPane().add(previewBoldItalic);
	}

	public Font getSelectedFont() {
		String fontName = "";
		Integer fontSize = 12;
		Object obj1 = fontNameList.getSelectedValue();
		Object obj2 = fontSizeList.getSelectedValue();
		if (obj1 != null) {
			fontName = (String)obj1;
		}
		if (obj2 != null) {
			fontSize = (Integer)obj2;
		}
		Font result = new Font(fontName, Font.PLAIN, fontSize);
		return result;
	}

	public void setSelectedFont(Font font) {
		fontNameList.setSelectedValue(font.getName(), true);
		fontSizeList.setSelectedValue(font.getSize(), true);
	}
	
	private void updatePreview() {
		Font selectedFont = getSelectedFont();
		if (selectedFont != null) {
			previewPlain.setFont(selectedFont.deriveFont(Font.PLAIN));
			previewBold.setFont(selectedFont.deriveFont(Font.BOLD));
			previewItalic.setFont(selectedFont.deriveFont(Font.ITALIC));
			previewBoldItalic.setFont(selectedFont.deriveFont(Font.BOLD | Font.ITALIC));
		}
	}
	
	public Font showFontSelectDialog() {
		this.setModal(true);
		this.setVisible(true);
		return currentFont;
	}
}
