package net.cattaka.mathdrawer.gui.project;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import net.cattaka.mathdrawer.MdMessageConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.Drawer;
import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.setting.entity.MdProject;
import net.cattaka.mathdrawer.util.ImageViewPort;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.clipboard.ClipboardUtil;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.swing.util.ListItemWrapper;
import net.cattaka.util.BufferedImageUtil;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class DrawerPreviewPanel extends JTabbedPane implements MdEditorInterface {
	private static final long serialVersionUID = 1L;
	private MdEditorInterface parentComponent;
	private ImageViewPort imagePanel;
	private Drawer currentDrawer;
	private JComboBox backgroundList;
	private JComboBox frameCountList;
	private JTextArea messageArea;
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("draw_image")) {
				MdMessage mdMessage = new MdMessage(MdMessageConstants.MD_PROJECT_DRAW_IMAGE, null, DrawerPreviewPanel.this, currentDrawer);
				sendMdMessage(mdMessage);
			} else if (e.getActionCommand().equals("draw_layer")) {
				MdMessage mdMessage = new MdMessage(MdMessageConstants.MD_PROJECT_DRAW_LAYER, null, DrawerPreviewPanel.this, currentDrawer);
				sendMdMessage(mdMessage);
			} else if (e.getActionCommand().equals("save_image")) {
				JFileChooser fileChooser = getMdSingletonBundle().getImageFileChooser();
				if (fileChooser.showSaveDialog(DrawerPreviewPanel.this) == JFileChooser.APPROVE_OPTION) {
					try {
						Integer frameCount = getCurrentFrameCount();
						File file = fileChooser.getSelectedFile();
						ImageIO.write(currentDrawer.getCachedImage()[frameCount], PresetImage.ImageType.PNG.name(), file);
					} catch (IOException exc) {
						ExceptionHandler.error(exc);
					}
				}
			} else if (e.getActionCommand().equals("save_image_horizontal")) {
				JFileChooser fileChooser = getMdSingletonBundle().getImageFileChooser();
				if (fileChooser.showSaveDialog(DrawerPreviewPanel.this) == JFileChooser.APPROVE_OPTION) {
					try {
						File file = fileChooser.getSelectedFile();
						BufferedImage image = BufferedImageUtil.glueHorizontal(currentDrawer.getCachedImage());
						ImageIO.write(image, PresetImage.ImageType.PNG.name(), file);
					} catch (IOException exc) {
						ExceptionHandler.error(exc);
					}
				}
			} else if (e.getActionCommand().equals("save_image_vertical")) {
				JFileChooser fileChooser = getMdSingletonBundle().getImageFileChooser();
				if (fileChooser.showSaveDialog(DrawerPreviewPanel.this) == JFileChooser.APPROVE_OPTION) {
					try {
						File file = fileChooser.getSelectedFile();
						BufferedImage image = BufferedImageUtil.glueVertical(currentDrawer.getCachedImage());
						ImageIO.write(image, PresetImage.ImageType.PNG.name(), file);
					} catch (IOException exc) {
						ExceptionHandler.error(exc);
					}
				}
			} else if (e.getActionCommand().equals("copy_image")) {
				Integer frameCount = getCurrentFrameCount();
				if (currentDrawer != null && currentDrawer.getCachedImage() != null && frameCount < currentDrawer.getCachedImage().length) {
					ClipboardUtil.copyBufferedImage(currentDrawer.getCachedImage()[frameCount]);
				}
			}
		}
	}
	
	class BackgroundListItemListenerImpl implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			Object obj = backgroundList.getSelectedItem();
			if (obj != null && obj instanceof ListItemWrapper<?>) {
				ListItemWrapper<?> liw = (ListItemWrapper<?>)obj;
				Color color = (Color)liw.getData();
				imagePanel.setPreviewBackground(color);
			}
		}
	}

	class FrameCountListItemListenerImpl implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			Integer frameCount = getCurrentFrameCount();
			if (0 <= frameCount && frameCount < currentDrawer.getCachedImage().length) {
				imagePanel.setImage(currentDrawer.getCachedImage()[frameCount]);
			}
			imagePanel.repaint();
		}
	}

	public DrawerPreviewPanel(MdEditorInterface parentComponent) {
		super();
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		JPanel previewPanel = new JPanel();
		{
			this.imagePanel = new ImageViewPort();
			this.imagePanel.setPreviewBackground(Color.BLACK);
			StdScrollPane imagePanelPane = new StdScrollPane(this.imagePanel); 
			
			JLabel bgColorLabel = new JLabel(MessageBundle.getMessage("bg_color"));
			JLabel frameNumberLabel = new JLabel(MessageBundle.getMessage("frame_number"));
			
			ActionListenerImpl al = new ActionListenerImpl();
			JButton drawLayerButton = new JButton();
			JButton drawImageButton = new JButton();
			JButton saveImageButton = new JButton();
			JButton saveImageHorizontalButton = new JButton();
			JButton saveImageVerticalButton = new JButton();
			JButton copyImageButton = new JButton();
			ButtonsBundle.applyButtonDifinition(drawLayerButton, "draw_layer");
			ButtonsBundle.applyButtonDifinition(drawImageButton, "draw_image");
			ButtonsBundle.applyButtonDifinition(saveImageButton, "save_image");
			ButtonsBundle.applyButtonDifinition(saveImageHorizontalButton, "save_image_horizontal");
			ButtonsBundle.applyButtonDifinition(saveImageVerticalButton, "save_image_vertical");
			ButtonsBundle.applyButtonDifinition(copyImageButton, "copy_image");
			drawLayerButton.setActionCommand("draw_layer");
			drawImageButton.setActionCommand("draw_image");
			saveImageButton.setActionCommand("save_image");
			saveImageHorizontalButton.setActionCommand("save_image_horizontal");
			saveImageVerticalButton.setActionCommand("save_image_vertical");
			copyImageButton.setActionCommand("copy_image");
			drawLayerButton.addActionListener(al);
			drawImageButton.addActionListener(al);
			saveImageButton.addActionListener(al);
			saveImageHorizontalButton.addActionListener(al);
			saveImageVerticalButton.addActionListener(al);
			copyImageButton.addActionListener(al);
			
			{
				ListItemWrapper<?>[] colorArray = new ListItemWrapper<?>[] {
					new ListItemWrapper<Color>("Black",Color.BLACK),
					new ListItemWrapper<Color>("Gray",Color.GRAY),
					new ListItemWrapper<Color>("LightGray",Color.LIGHT_GRAY),
					new ListItemWrapper<Color>("White",Color.WHITE),
					new ListItemWrapper<Color>("Red",Color.RED),
					new ListItemWrapper<Color>("Yellow",Color.YELLOW),
					new ListItemWrapper<Color>("Green",Color.GREEN),
					new ListItemWrapper<Color>("Cyan",Color.CYAN),
					new ListItemWrapper<Color>("Blue",Color.BLUE),
					new ListItemWrapper<Color>("Magenta",Color.MAGENTA)
				};
				backgroundList = new JComboBox(colorArray);
				backgroundList.addItemListener(new BackgroundListItemListenerImpl());
			}
			{
				Integer[] frameCountModel = new Integer[]{0};
				frameCountList = new JComboBox(frameCountModel);
				frameCountList.addItemListener(new FrameCountListItemListenerImpl());
			}
			
			GridBagConstraints gbc = new GridBagConstraints();
			GridBagLayout gbl = new GridBagLayout();
			
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
			gbl.setConstraints(drawLayerButton, gbc);
			gbc.gridy++;
			gbl.setConstraints(drawImageButton, gbc);
			gbc.gridy++;
			gbl.setConstraints(saveImageButton, gbc);
			gbc.gridy++;
			gbl.setConstraints(saveImageHorizontalButton, gbc);
			gbc.gridy++;
			gbl.setConstraints(saveImageVerticalButton, gbc);
			gbc.gridy++;
			gbl.setConstraints(copyImageButton, gbc);
			
			gbc.gridwidth = 1;
			gbc.gridy++;
			gbl.setConstraints(bgColorLabel, gbc);
			gbc.gridy++;
			gbl.setConstraints(frameNumberLabel, gbc);
			gbc.gridx++;
			gbc.gridy-=2;
			gbc.gridy++;
			gbl.setConstraints(backgroundList, gbc);
			gbc.gridy++;
			gbl.setConstraints(frameCountList, gbc);
			
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 9;
			gbl.setConstraints(imagePanelPane, gbc);
			
			previewPanel.setLayout(gbl);
			previewPanel.add(drawLayerButton);
			previewPanel.add(drawImageButton);
			previewPanel.add(saveImageButton);
			previewPanel.add(saveImageHorizontalButton);
			previewPanel.add(saveImageVerticalButton);
			previewPanel.add(bgColorLabel);
			previewPanel.add(copyImageButton);
			previewPanel.add(frameNumberLabel);
			previewPanel.add(backgroundList);
			previewPanel.add(frameCountList);
			previewPanel.add(imagePanelPane);
		}
		this.messageArea = new JTextArea();
		this.messageArea.setEditable(false);
		StdScrollPane messagePane = new StdScrollPane(this.messageArea);
		
		this.addTab(MessageBundle.getMessage("preview"), previewPanel);
		this.addTab(MessageBundle.getMessage("message"), messagePane);
	}
	
	private Integer getCurrentFrameCount() {
		Integer frameCount = 0;
		{
			Object obj = frameCountList.getSelectedItem();
			if (obj != null && obj instanceof Integer) {
				frameCount = (Integer)obj;
			}
		}
		return frameCount;
	}
	
	// Getter/Setter -----------------------------------------
	
	public void displayCurrentDrawer(Drawer currentDrawer) {
		this.currentDrawer = currentDrawer;

		Integer frameCount = getCurrentFrameCount();
		if (0 <= frameCount && frameCount < currentDrawer.getCachedImage().length) {
			this.imagePanel.setImage(this.currentDrawer.getCachedImage()[frameCount]);
		}
		this.imagePanel.repaint();
		
		// コンボボックスの内容の再作成
		Integer[] frameCountListItem = new Integer[this.currentDrawer.getCachedImage().length];
		for (int i=0;i<frameCountListItem.length;i++) {
			frameCountListItem[i] = new Integer(i);
		}
		this.frameCountList.setModel(new DefaultComboBoxModel(frameCountListItem));
		this.frameCountList.setSelectedItem(frameCount);
		
		this.setMessage(this.currentDrawer.getMessage());
	}
	
	public void setMessage(String message) {
		if (message == null || message.length() == 0) {
			this.messageArea.setText("");
		} else {
			this.messageArea.setText(message);
			this.messageArea.setCaretPosition(0);
		}
	}
	
	// Interface -----------------------------------------

	public DrawerUtil getDrawerUtil() {
		return this.parentComponent.getDrawerUtil();
	}

	public MdProject getMdProject() {
		return this.parentComponent.getMdProject();
	}

	public void reloadMdProject() {
		// TODO Auto-generated method stub
	}

	public void doGuiLayout() {
		// TODO Auto-generated method stub
	}

	public MdConfig getMdConfig() {
		return this.parentComponent.getMdConfig();
	}

	public MdSingletonBundle getMdSingletonBundle() {
		return this.parentComponent.getMdSingletonBundle();
	}

	public void relayMdMessage(MdMessage mdMessage) {
		// TODO Auto-generated method stub
	}

	public void reloadMdConfig() {
		// TODO Auto-generated method stub
	}

	public void sendMdMessage(MdMessage mdMessage) {
		this.parentComponent.sendMdMessage(mdMessage);
	}

}
