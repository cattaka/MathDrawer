package net.cattaka.mathdrawer.gui.preset;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.cattaka.mathdrawer.MdResourceConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.setting.entity.MdProject;
import net.cattaka.mathdrawer.setting.entity.PresetImageList;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringUtil;

public class ImageListPanel extends JPanel implements MdEditorInterface {
	private static final long serialVersionUID = 1L;
	private MdEditorInterface parentComponent;
	private JList imageList;
	public static DataFlavor urlListDataFlavor = null;
	static {
		try {
			// GNOME対応
			urlListDataFlavor = new DataFlavor("text/uri-list; class=java.lang.String; charset=Unicode");
		} catch (ClassNotFoundException e) {
			// あり得ない
			ExceptionHandler.error(e);
		}
	}

	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("add_image")) {
				int index = imageList.getSelectedIndex();
				if (index < 0) {
					index = 0;
				}
				addImage(index);
			} else if(e.getActionCommand().equals("remove_image")) {
				int index = imageList.getSelectedIndex();
				if (index != -1) {
					removeImage(index);
				}
			} else if(e.getActionCommand().equals("rename_image")) {
				int index = imageList.getSelectedIndex();
				if (index != -1) {
					renameImage(index);
				}
			} else if(e.getActionCommand().equals("move_up_image")) {
				int index = imageList.getSelectedIndex();
				if (index != -1) {
					moveUpImage(index);
				}
			} else if(e.getActionCommand().equals("move_down_image")) {
				int index = imageList.getSelectedIndex();
				if (index != -1) {
					moveDownImage(index);
				}
			}
		}
	}
	
	class ListSelectionListenerImpl implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			PresetImageWrapper piw = (PresetImageWrapper)imageList.getSelectedValue();
			if (piw != null) {
				selectedImageChanged(piw.getPresetImage());
			}
		}
	}
	
	class TransferHandlerImpl extends TransferHandler {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			return canImportTransferable(transferFlavors);
		}

		@Override
		public boolean importData(JComponent comp, Transferable transferable) {
			return importTransferable(transferable);
		}
	}
	
	static class PresetImageWrapper {
		private PresetImage presetImage;
		public PresetImageWrapper(PresetImage presetImage) {
			super();
			if (presetImage == null) {
				throw new NullPointerException();
			}
			this.presetImage = presetImage;
		}
		public PresetImage getPresetImage() {
			return presetImage;
		}
		@Override
		public String toString() {
			return this.presetImage.getName();
		}
	}
	
	public ImageListPanel(MdEditorInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}

	private void makeLayout() {
		this.imageList = new JList();
		this.imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.imageList.addListSelectionListener(new ListSelectionListenerImpl());
		this.imageList.setTransferHandler(new TransferHandlerImpl());
		StdScrollPane imageListPane = new StdScrollPane(imageList);
		
		ActionListenerImpl al = new ActionListenerImpl();
		JToolBar toolBar = new JToolBar();
		{
			JButton addItemButton = new JButton();
			JButton removeButton = new JButton();
			JButton renameButton = new JButton();
			JButton moveUpButton = new JButton();
			JButton moveDownButton = new JButton();
		
			MdSingletonBundle mdSingletonBundle = getMdSingletonBundle();
			Icon iconAddItem = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_ADD_ITEM);
			Icon iconRemove = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_REMOVE);
			Icon iconRename = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_COMPILE);
			Icon iconMoveUp = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_ARROW_UP);
			Icon iconMoveDown = (Icon)mdSingletonBundle.getResource(MdResourceConstants.ICON_ARROW_DOWN);
			ButtonsBundle.applyButtonDifinition(addItemButton, iconAddItem, "add_image", true);
			ButtonsBundle.applyButtonDifinition(removeButton, iconRemove, "remove_image", true);
			ButtonsBundle.applyButtonDifinition(renameButton, iconRename, "rename_image", true);
			ButtonsBundle.applyButtonDifinition(moveUpButton, iconMoveUp, "move_up_image", true);
			ButtonsBundle.applyButtonDifinition(moveDownButton, iconMoveDown, "move_down_image", true);
	
			addItemButton.addActionListener(al);
			addItemButton.setActionCommand("add_image");
			removeButton.addActionListener(al);
			removeButton.setActionCommand("remove_image");
			renameButton.addActionListener(al);
			renameButton.setActionCommand("rename_image");
			moveUpButton.addActionListener(al);
			moveUpButton.setActionCommand("move_up_image");
			moveDownButton.addActionListener(al);
			moveDownButton.setActionCommand("move_down_image");
	
			toolBar.setFloatable(false);
			toolBar.add(addItemButton);
			toolBar.add(removeButton);
			toolBar.add(renameButton);
			toolBar.add(moveUpButton);
			toolBar.add(moveDownButton);
		}
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbl.setConstraints(toolBar, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbl.setConstraints(imageListPane, gbc);
		
		this.setLayout(gbl);
		this.add(toolBar);
		this.add(imageListPane);
	}
	
	public void addImage(BufferedImage image) {
		PresetImage pi = new PresetImage();
		pi.setName("unnamed.png");
		pi.setImageType(PresetImage.ImageType.PNG);
		pi.setBufferedImage(image);
		this.getMdProject().getPresetImageList().add(pi);
		this.reloadMdProject();
		this.imageList.setSelectedIndex(this.imageList.getVisibleRowCount()-1);
	}
	public void addImage(File file) {
		try {
			PresetImage pi = PresetImage.readPresetImage(file);
			this.getMdProject().getPresetImageList().add(pi);
			this.reloadMdProject();
			this.imageList.setSelectedIndex(this.imageList.getVisibleRowCount()-1);
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
	}
	public void addImage(int index) {
		JFileChooser fileChooser = this.getMdSingletonBundle().getImageFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				PresetImage pi = PresetImage.readPresetImage(file);
				this.getMdProject().getPresetImageList().add(index, pi);
				this.reloadMdProject();
				this.imageList.setSelectedIndex(index);
			} catch (IOException e) {
				ExceptionHandler.error(e);
			}
		}
	}
	public void removeImage(int index) {
		this.getMdProject().getPresetImageList().remove(index);
		this.reloadMdProject();
	}
	
	public void renameImage(int index) {
		PresetImage presetImage = this.getMdProject().getPresetImageList().get(index);
		String newName = JOptionPane.showInputDialog(MessageBundle.getMessage("msg_input_new_name"), presetImage.getName());
		if (newName != null) {
			presetImage.setName(newName);
			this.reloadMdProject();
		}
	}
	

	
	public void moveUpImage(int index) {
		PresetImageList pil = this.getMdProject().getPresetImageList();
		if (index > 0) {
			PresetImage pi = pil.remove(index);
			pil.add(index - 1, pi);
			this.reloadMdProject();
			this.imageList.setSelectedIndex(index-1);
		}
	}
	public void moveDownImage(int index) {
		PresetImageList pil = this.getMdProject().getPresetImageList();
		if (index < pil.size() - 1) {
			PresetImage pi = pil.remove(index);
			pil.add(index + 1, pi);
			this.reloadMdProject();
			this.imageList.setSelectedIndex(index+1);
		}
	}
	
	public boolean canImportTransferable(DataFlavor[] transferFlavors) {
		boolean result = false;
		for (DataFlavor df:transferFlavors) {
			if (DataFlavor.javaFileListFlavor.equals(df)
				|| DataFlavor.imageFlavor.equals(df)
				|| (urlListDataFlavor != null && urlListDataFlavor.equals(df))
			) {
				result = true;
				break;
			} else if (urlListDataFlavor != null && urlListDataFlavor.equals(df)) {
				result = true;
				break;
			}
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	public boolean importTransferable(Transferable transferable) {
//		for (DataFlavor df:transferable.getTransferDataFlavors()) {
//			System.out.println(df.getMimeType());
//		}
		boolean result = false;
		try {
			if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				Object obj = transferable.getTransferData(DataFlavor.javaFileListFlavor);
				List<File> fileList = (List<File>)obj;
				for (File file:fileList) {
					this.addImage(file);
				}
			} else if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
				Object obj = transferable.getTransferData(DataFlavor.imageFlavor);
				BufferedImage image = (BufferedImage)obj;
				addImage(image);
			} else if (urlListDataFlavor != null && transferable.isDataFlavorSupported(urlListDataFlavor)) {
				Object obj = transferable.getTransferData(urlListDataFlavor);
				String fileNames = StringUtil.removeCarriageReturn(obj.toString());
				String[] fileNameList = fileNames.split("\n");
				for (String fileName:fileNameList) {
					URI fileUri = new URI(fileName);
					File file = new File(fileUri);
					this.addImage(file);
				}
			}
		} catch (URISyntaxException e) {
			ExceptionHandler.error(e);
		} catch (ClassCastException e) {
			ExceptionHandler.error(e);
		} catch (UnsupportedFlavorException e) {
			ExceptionHandler.error(e);
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		
		return result;
	}
	
	/**
	 * オーバーライド用
	 * @param presetImage
	 */
	public void selectedImageChanged(PresetImage presetImage) {
	}
	
	// Interface ------------------------------------
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

	public DrawerUtil getDrawerUtil() {
		return parentComponent.getDrawerUtil();
	}

	public MdProject getMdProject() {
		return parentComponent.getMdProject();
	}

	public void reloadMdProject() {
		PresetImageList pil = getMdProject().getPresetImageList();
		PresetImageWrapper[] piws = new PresetImageWrapper[pil.size()];
		for (int i=0;i<pil.size();i++) {
			piws[i] = new PresetImageWrapper(pil.get(i));
		}
		this.imageList.setListData(piws);
	}
}
