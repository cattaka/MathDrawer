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
 * $Id: RdbaConfigEditorPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.gui.config;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import net.cattaka.mathdrawer.MdConfigConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.swing.datainputpanel.DIPInfo;
import net.cattaka.swing.datainputpanel.DIPInfoFile;
import net.cattaka.swing.datainputpanel.DIPInfoFont;
import net.cattaka.swing.datainputpanel.DIPInfoSelect;
import net.cattaka.swing.datainputpanel.DIPInfoSwitch;
import net.cattaka.swing.datainputpanel.DataInputPanel;
import net.cattaka.swing.util.LookAndFeelBundle;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class MdConfigEditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private DataInputPanel dataInputPanel;
	
	private DIPInfoFont fontForEditor;
	private DIPInfoFile customPixelShaderDir;
	private DIPInfoFile customDrawerDir;
	private DIPInfoSwitch useDefaultToolsJar;
	private DIPInfoFile toolsJar;
	private DIPInfoSelect lookAndFeel;
	private DIPInfoSelect logLevel;
	
	class ActionListenerEx implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("approve")) {
				if (dataInputPanel.isValidData()) {
					doApprove();
				} else {
					dataInputPanel.validateData();
				}
			} else if (e.getActionCommand().equals("cancel")) {
				doCancel();
			}
		}
	}
	
	public MdConfigEditorPanel() {
		makeLayout();
	}
	
	private void makeLayout() {
		fontForEditor = new DIPInfoFont(MessageBundle.getMessage("font_for_editor"), new Font(MdConfigConstants.DEFAULT_FONT_EDITOR, 0 , 12));
		customPixelShaderDir = new DIPInfoFile(MessageBundle.getMessage("custom_pixel_shader_dir"), "", DIPInfoFile.MODE_OPEN);
		customDrawerDir = new DIPInfoFile(MessageBundle.getMessage("custom_drawer_dir"), "", DIPInfoFile.MODE_OPEN);
		toolsJar = new DIPInfoFile(MessageBundle.getMessage("tools_jar"), "", DIPInfoFile.MODE_OPEN);
		useDefaultToolsJar = new DIPInfoSwitch(MessageBundle.getMessage("use_default_tools_jar"), false, new DIPInfo[0], new DIPInfo[]{toolsJar});
		lookAndFeel  = new DIPInfoSelect(MessageBundle.getMessage("look_and_feel"), LookAndFeelBundle.getLookAndFeelNames(), LookAndFeelBundle.getLookAndFeelClassNames(), LookAndFeelBundle.getDefaultLookAndFeelName());
		logLevel = new DIPInfoSelect(MessageBundle.getMessage("log_level"), ExceptionHandler.getPriorityNames(), ExceptionHandler.Priority.WARNING.name());
		
		customPixelShaderDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		customDrawerDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		DIPInfo[] dipis = new DIPInfo[] {
				fontForEditor,
				customPixelShaderDir,
				customDrawerDir,
				toolsJar,
				useDefaultToolsJar,
				lookAndFeel,
				logLevel
		};
		
		ActionListener al = new ActionListenerEx();
		dataInputPanel = new DataInputPanel(dipis);
		dataInputPanel.getButtonApprove().addActionListener(al);
		dataInputPanel.getButtonApprove().setActionCommand("approve");
		dataInputPanel.getButtonCancel().addActionListener(al);
		dataInputPanel.getButtonCancel().setActionCommand("cancel");

		this.setLayout(new GridLayout());
		this.add(dataInputPanel);
	}
	
	public void loadMdConfig(MdConfig mdConfig) {
		fontForEditor.setValue(mdConfig.getFontForEditor());
		customPixelShaderDir.setValue(mdConfig.getCustomPixelShaderRoot());
		customDrawerDir.setValue(mdConfig.getCustomDrawerRoot());
		useDefaultToolsJar.setValue(mdConfig.isUseDefaultToolsJar());
		toolsJar.setStringValue(mdConfig.getToolsJar());
		lookAndFeel.setObjectValue(mdConfig.getLookAndFeel());
		logLevel.setStringValue(mdConfig.getLogLevel());
	}

	public void saveMdConfig(MdConfig mdConfig) {
		mdConfig.setFontForEditor(fontForEditor.getFontValue());
		mdConfig.setCustomPixelShaderRoot(customPixelShaderDir.getFileValue());
		mdConfig.setCustomDrawerRoot(customDrawerDir.getFileValue());
		mdConfig.setUseDefaultToolsJar(useDefaultToolsJar.getBooleanValue());
		mdConfig.setToolsJar(toolsJar.getStringValue());
		mdConfig.setLookAndFeel(lookAndFeel.getObjectValue().toString());
		mdConfig.setLogLevel(logLevel.getStringValue());
	}

	public void doApprove() {
		
	}
	public void doCancel() {
		
	}
}
