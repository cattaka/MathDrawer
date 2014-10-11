package net.cattaka.mathdrawer.gui.preset;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.setting.entity.MdProject;
import net.cattaka.mathdrawer.util.ImageViewPort;
import net.cattaka.swing.StdScrollPane;

public class MdPresetModePanel extends JPanel implements MdEditorInterface {
	private static final long serialVersionUID = 1L;
	private MdEditorInterface parentComponent;
	private ImageListPanelEx imageListPanel;
	private ImageViewPort imageViewPort;
	
	class ImageListPanelEx extends ImageListPanel {
		private static final long serialVersionUID = 1L;
		public ImageListPanelEx(MdEditorInterface parentComponent) {
			super(parentComponent);
		}
		@Override
		public void selectedImageChanged(PresetImage presetImage) {
			imageViewPort.setImage(presetImage.getBufferedImage());
		}
	}
	
	public MdPresetModePanel(MdEditorInterface parentComponent) {
		this.parentComponent = parentComponent;
		
		makeLayout();
	}
	
	private void makeLayout() {
		this.imageListPanel = new ImageListPanelEx(this);
		this.imageViewPort = new ImageViewPort();
		StdScrollPane imageViewPortpane = new StdScrollPane(this.imageViewPort);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.imageListPanel, imageViewPortpane);
		
		this.setLayout(new GridLayout());
		this.add(splitPane);
	}
	
	// Interface ---------------------------------------------------
	
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
		this.imageListPanel.relayMdMessage(mdMessage);
	}

	public void reloadMdConfig() {
		this.imageListPanel.reloadMdConfig();
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
		this.imageListPanel.reloadMdProject();
	}
}
