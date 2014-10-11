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
 * $Id: RdbAssistant.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.config.MdConfigUtil;
import net.cattaka.mathdrawer.gui.MathDrawerPanel;
import net.cattaka.util.MessageBundle;

public class MathDrawer {
	static class WindowAdapterEx extends WindowAdapter {
		MathDrawerPanel mathDrawerPanel;
		public WindowAdapterEx(MathDrawerPanel mathDrawerPanel) {
			this.mathDrawerPanel = mathDrawerPanel;
		}
		
		public void windowClosing(WindowEvent e) {
			this.mathDrawerPanel.doExit();
		}
	}
	
	static class MathDrawerFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		private MathDrawerPanel mathDrawerPanel;
		
		public MathDrawerFrame(MdConfig mdConfig) {
			this.mathDrawerPanel = new MathDrawerPanel(mdConfig) {
				private static final long serialVersionUID = 1L;
				@Override
				public void doExit() {
					MathDrawerFrame.this.doExit();
				}
			};
			this.setIconImage((Image)this.mathDrawerPanel.getMdSingletonBundle().getResource(MdResourceConstants.IMAGE_APP));
			
			String title = String.format("%s %s - build %s -", MessageBundle.getMessage("mathdrawer_title"), MessageBundle.getReleaseNumber(), MessageBundle.getBuildNumber());
			this.setTitle(title);
			this.setSize(800,700);
			this.getContentPane().add(mathDrawerPanel);
			this.setJMenuBar(mathDrawerPanel.getMenuBar());
			this.addWindowListener(new WindowAdapterEx(mathDrawerPanel));

			mathDrawerPanel.doGuiLayout();
			mathDrawerPanel.createWindowRelatedObject();
		}
		
		public MathDrawerPanel getMathDrawerPanel() {
			return mathDrawerPanel;
		}

		public void doExit() {
			this.mathDrawerPanel.saveMdConfig();
			this.setVisible(false);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		MdConfig mdConfig = MdConfigUtil.loadMdConfig();
		MathDrawerFrame f = new MathDrawerFrame(mdConfig);
		f.setVisible(true);
		for (String arg:args) {
			File file = new File(arg);
			if (file.exists() && file.isFile()) {
				f.getMathDrawerPanel().openDocument(file);
			}
		}
	}
}
