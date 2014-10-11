/*
 * Copyright (c) 2009, Takao Sumitomo
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the 
 *       above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce
 *       the above copyright notice, this list of
 *       conditions and the following disclaimer in the
 *       documentation and/or other materials provided
 *       with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should
 * not be interpreted as representing official policies,
 * either expressed or implied.
 */
/*
 * $Id: RdbaScriptAssistTabbedPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.gui.pixelshader;

import java.awt.Component;
import java.io.File;

import javax.swing.JTabbedPane;

import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.gui.MdGuiInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.mathdrawer.gui.common.FileAccessPanel;
import net.cattaka.util.MessageBundle;

public class MdPixelShaderAssistTabbedPanel extends JTabbedPane implements MdGuiInterface {
	private static final long serialVersionUID = 1L;
	private MdGuiInterface parentComponent;
	private FileAccessPanel mdPixelShaderAccessPanel;
	private MdPixelShaderEditorTabbedPanel mdPixelShaderEditorTabbedPanel;
	
	class FileAccessPanelEx extends FileAccessPanel {
		private static final long serialVersionUID = 1L;

		public FileAccessPanelEx(MdGuiInterface parentComponent) {
			super(parentComponent, FileAccessPanel.Target.TARGET_PIXELSHADER);
		}

		@Override
		public void doOpen(File file) {
			super.doOpen(file);
			mdPixelShaderEditorTabbedPanel.openPixelShader(file, MdConstants.DEFAULT_CHARSET);
		}
	}

	public MdPixelShaderAssistTabbedPanel(MdGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		this.mdPixelShaderAccessPanel = new FileAccessPanelEx(this);
		
		this.addTab(MessageBundle.getMessage("script_list"), this.mdPixelShaderAccessPanel);
	}

	public void setMdPixelShaderEditorTabbedPanel(
			MdPixelShaderEditorTabbedPanel mdPixelShaderEditorTabbedPanel) {
		this.mdPixelShaderEditorTabbedPanel = mdPixelShaderEditorTabbedPanel;
	}
	
	// -- Interface ----------------------------------------------

	/** {@link MdGuiInterface} */
	public void doGuiLayout() {
		// 無し
	}

	/** {@link MdGuiInterface} */
	public MdConfig getMdConfig() {
		return parentComponent.getMdConfig();
	}

	/** {@link MdGuiInterface} */
	public MdSingletonBundle getMdSingletonBundle() {
		return parentComponent.getMdSingletonBundle();
	}

	/** {@link MdGuiInterface} */
	public void relayMdMessage(MdMessage mdMessage) {
		Component comp = this.getSelectedComponent();
		if (comp != null && comp instanceof MdGuiInterface) {
			((MdGuiInterface)comp).relayMdMessage(mdMessage);
		}
	}

	/** {@link MdGuiInterface} */
	public void reloadMdConfig() {
		this.mdPixelShaderAccessPanel.reloadMdConfig();
	}

	/** {@link MdGuiInterface} */
	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}
}
