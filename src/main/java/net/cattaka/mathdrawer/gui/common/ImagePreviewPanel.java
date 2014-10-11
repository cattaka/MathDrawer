package net.cattaka.mathdrawer.gui.common;

import java.awt.Color;
import java.awt.Graphics;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.gui.MdGuiInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.util.ImageViewPort;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.clipboard.ClipboardUtil;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.swing.util.ListItemWrapper;
import net.cattaka.util.BufferedImageUtil;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class ImagePreviewPanel extends JPanel implements MdGuiInterface {
	public static final Color DEFAULT_COLOR = Color.WHITE;
	public static final String DEFAULT_COLOR_STRING = "(1,1,1,1)";
	
	private static final long serialVersionUID = 1L;
	private MdGuiInterface parentComponent;
	private ImageViewPort imagePanel;
	private BufferedImage baseImage;
	private BufferedImage cachedImage;
	private JTextField widthField;
	private JTextField heightField;
	private JTextField colorField;
	private JComboBox backgroundList;
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("resize")) {
				int width;
				int height;
				Color4d color;
				try {
					width = Integer.parseInt(widthField.getText());
					height = Integer.parseInt(heightField.getText());
					color = Color4d.valueOf(colorField.getText());
					baseImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
					cachedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
					{
						BufferedImageUtil.clearImage(baseImage, color.toColor());
						BufferedImageUtil.clearImage(cachedImage);
					}
					
					imagePanel.setImage(cachedImage);
					imagePanel.repaint();
				} catch (NumberFormatException exc) {
					widthField.setText(String.valueOf(cachedImage.getWidth()));
					heightField.setText(String.valueOf(cachedImage.getHeight()));
				}
			} else if (e.getActionCommand().equals("save_image")) {
				JFileChooser fileChooser = getMdSingletonBundle().getImageFileChooser();
				if (fileChooser.showSaveDialog(ImagePreviewPanel.this) == JFileChooser.APPROVE_OPTION) {
					try {
						File file = fileChooser.getSelectedFile();
						ImageIO.write(cachedImage, PresetImage.ImageType.PNG.name(), file);
					} catch (IOException exc) {
						ExceptionHandler.error(exc);
					}
				}
			} else if (e.getActionCommand().equals("copy_image")) {
				ClipboardUtil.copyBufferedImage(cachedImage);
			}
		}
	}
	
	class ItemListenerImpl implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			Object obj = backgroundList.getSelectedItem();
			if (obj != null && obj instanceof ListItemWrapper<?>) {
				ListItemWrapper<?> liw = (ListItemWrapper<?>)obj;
				Color color = (Color)liw.getData();
				imagePanel.setPreviewBackground(color);
			}
		}
	}
	
	public ImagePreviewPanel(MdGuiInterface parentComponent) {
		super();
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		cachedImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		{
			int width = baseImage.getWidth();
			int height = baseImage.getHeight();
			{
				BufferedImageUtil.clearImage(baseImage);
				Graphics g = baseImage.getGraphics();
				g.setColor(DEFAULT_COLOR);
				g.fillRect(0, 0, width, height);
				g.dispose();
			}
			{
				BufferedImageUtil.clearImage(cachedImage);
			}
		}
		this.widthField = new JTextField(String.valueOf(cachedImage.getWidth()), 5);
		this.heightField = new JTextField(String.valueOf(cachedImage.getHeight()), 5);
		this.colorField = new JTextField(DEFAULT_COLOR_STRING, 20);
		
		this.imagePanel = new ImageViewPort();
		this.imagePanel.setPreviewBackground(Color.BLACK);
		this.imagePanel.setImage(cachedImage);
		StdScrollPane imagePanelPane = new StdScrollPane(this.imagePanel); 
		
		ActionListenerImpl al = new ActionListenerImpl();

		JLabel widthLabel = new JLabel(MessageBundle.getMessage("width"));
		JLabel heightLabel = new JLabel(MessageBundle.getMessage("height"));
		JLabel colorLabel = new JLabel(MessageBundle.getMessage("base_image"));
		JLabel bgColorLabel = new JLabel(MessageBundle.getMessage("bg_color"));
		JButton resizeButton = new JButton();
		JButton saveImageButton = new JButton();
		JButton copyImageButton = new JButton();
		ButtonsBundle.applyButtonDifinition(resizeButton, "resize");
		ButtonsBundle.applyButtonDifinition(saveImageButton, "save_image");
		ButtonsBundle.applyButtonDifinition(copyImageButton, "copy_image");
		resizeButton.setActionCommand("resize");
		resizeButton.addActionListener(al);
		saveImageButton.setActionCommand("save_image");
		saveImageButton.addActionListener(al);
		copyImageButton.setActionCommand("copy_image");
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
			backgroundList.addItemListener(new ItemListenerImpl());
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(widthLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(heightLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(colorLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(bgColorLabel, gbc);
		gbc.gridy++;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbl.setConstraints(widthField, gbc);
		gbc.gridy++;
		gbl.setConstraints(heightField, gbc);
		gbc.gridy++;
		gbl.setConstraints(colorField, gbc);
		gbc.gridy++;
		gbl.setConstraints(backgroundList, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbl.setConstraints(resizeButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(saveImageButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(copyImageButton, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 8;
		gbl.setConstraints(imagePanelPane, gbc);
		
		this.setLayout(gbl);
		this.add(widthLabel);
		this.add(heightLabel);
		this.add(colorLabel);
		this.add(bgColorLabel);
		this.add(widthField);
		this.add(heightField);
		this.add(colorField);
		this.add(backgroundList);
		this.add(resizeButton);
		this.add(saveImageButton);
		this.add(copyImageButton);
		this.add(imagePanelPane);
		
		resetCachedImage();
	}
	
	// Getter/Setter -----------------------------------------
	
	public BufferedImage getBaseImage() {
		return baseImage;
	}

	public BufferedImage getCachedImage() {
		return cachedImage;
	}

	public void resetCachedImage() {
		BufferedImageUtil.clearImage(this.cachedImage);
	}
	
	// Interface -----------------------------------------

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
