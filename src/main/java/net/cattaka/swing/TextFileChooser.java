package net.cattaka.swing;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.nio.charset.Charset;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.util.MessageBundle;

public class TextFileChooser extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFileChooserEx fileChooser;
	private int option = JFileChooser.CANCEL_OPTION;
	private JComboBox charsetComboBox;
	
	class JFileChooserEx extends JFileChooser {
		private static final long serialVersionUID = 1L;

		public JFileChooserEx() {
		}
		@Override
		public void approveSelection() {
			super.approveSelection();
			option = JFileChooser.APPROVE_OPTION;
			TextFileChooser.this.setVisible(false);
		}
		@Override
		public void cancelSelection() {
			super.cancelSelection();
			option = JFileChooser.CANCEL_OPTION;
			TextFileChooser.this.setVisible(false);
		}
	}
	
	public TextFileChooser() {
		makeLayout();
	}
	
	public TextFileChooser(Dialog owner) throws HeadlessException {
		super(owner);
		makeLayout();
	}

	public TextFileChooser(Frame owner) throws HeadlessException {
		super(owner);
		makeLayout();
	}

	private void makeLayout() {
		this.fileChooser = new JFileChooserEx();
		this.charsetComboBox = new JComboBox();
		JLabel encodingLabel = new JLabel(MessageBundle.getMessage("encoding"));
		{
			Charset charset = MdConstants.DEFAULT_CHARSET;
			this.charsetComboBox.addItem(charset);
		}
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridwidth = 2;
		gbl.setConstraints(fileChooser, gc);
		gc.gridy++;
		
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridwidth = 1;
		gc.insets.set(5,5,5,5);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(encodingLabel, gc);
		gc.weightx = 1;
		gc.gridx++;
		gbl.setConstraints(charsetComboBox, gc);
		
		this.getContentPane().setLayout(gbl);
		this.getContentPane().add(fileChooser);
		this.getContentPane().add(encodingLabel);
		this.getContentPane().add(charsetComboBox);
	}
	
    public int showOpenDialog(Component parent) throws HeadlessException {
		this.setModal(true);
		this.setTitle(MessageBundle.getMessage("file_open"));
		this.pack();
		this.setVisible(true);
		return this.option;
    }

	public int showSaveDialog(Component parent) throws HeadlessException {
		this.setModal(true);
		this.setTitle(MessageBundle.getMessage("file_save"));
    	fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		this.pack();
		this.setVisible(true);
		return this.option;
	}
	
	public void setCharsetList(Charset defaultCharset, Charset[] charsetList) {
		charsetComboBox.removeAllItems();
		for (Charset enc : charsetList) {
			charsetComboBox.addItem(enc);
		}
	}
	
	public Charset getCharset() {
		return (Charset)this.charsetComboBox.getSelectedItem();
	}
	
	public FileFilter getFileFilter() {
		return fileChooser.getFileFilter();
	}

	public File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}

	public File[] getSelectedFiles() {
		return fileChooser.getSelectedFiles();
	}

	public void setFileFilter(FileFilter filter) {
		fileChooser.setFileFilter(filter);
	}

	public void setSelectedFile(File file) {
		fileChooser.setSelectedFile(file);
	}

	public void setSelectedFiles(File[] selectedFiles) {
		fileChooser.setSelectedFiles(selectedFiles);
	}

	public void setCurrentDirectory(File dir) {
		fileChooser.setCurrentDirectory(dir);
	}

    
    
}
