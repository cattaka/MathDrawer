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
 * $Id: RdbaScriptAccessPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.gui.common;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.mathdrawer.MdMessageConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.core.MdSingletonBundle;
import net.cattaka.mathdrawer.gui.MdGuiInterface;
import net.cattaka.mathdrawer.gui.MdMessage;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.fileview.FileViewTree;
import net.cattaka.util.ExceptionHandler;

public class FileAccessPanel extends JPanel implements MdGuiInterface {
	private static final long serialVersionUID = 1L;
	private MdGuiInterface parentComponent;
	private FileViewTreeEx fileViewTree;
	private Target target;
	
	public enum Target {
		TARGET_DRAWER,
		TARGET_PIXELSHADER,
	};
	
	class FileViewTreeEx extends FileViewTree {
		private static final long serialVersionUID = 1L;

		public FileViewTreeEx() {
			super();
		}

		@Override
		public void doOpen(File file) {
			boolean goFlag = false;
			goFlag = true;
			if (goFlag) {
				FileAccessPanel.this.doOpen(file);
			}
		}
		
		public void refleshTarget(File target) {
			FileTreeNode rootNode = (FileTreeNode)getModel().getRoot();
			refleshTarget(target, rootNode);
		}
		private void refleshTarget(File target, FileTreeNode node) {
			File nodeFile = node.getFile();
			try {
				String targetCanonicalPath = target.getCanonicalPath();
				String nodeCanonicalPath = nodeFile.getCanonicalPath();
				if (targetCanonicalPath.indexOf(nodeCanonicalPath) == 0) {
					if (targetCanonicalPath.length() == nodeCanonicalPath.length()) {
						// 更新対象を特定した
						node.reflesh();
					} else {
						// 子供を探しに行く
				 		FileTreeNode[] children = node.getChildren();
				 		for (FileTreeNode child : children) {
				 			refleshTarget(target, child);
				 		}
				 	}
				}
			} catch(IOException e) {
				ExceptionHandler.error(e);
			}
		}
	}
	
	public FileAccessPanel(MdGuiInterface parentComponent, Target target) {
		this.parentComponent = parentComponent;
		this.target = target;
		makeLayout();
	}
	
	private void makeLayout() {
		this.fileViewTree = new FileViewTreeEx();
		StdScrollPane fileViewTreePane;
		fileViewTreePane = new StdScrollPane(this.fileViewTree);
		
		this.setLayout(new GridLayout());
		this.add(fileViewTreePane);
	}

	public void doOpen(File file) {
	}
	
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
		// ファイルが更新された場合はそれに関係するディレクトリをリフレッシュする
		if (mdMessage.getMessage().equals(MdMessageConstants.MDDRAWERACCESSPANEL_REFLESH)) {
			Object obj = mdMessage.getData();
			if (obj != null && obj instanceof File) {
				this.fileViewTree.refleshTarget((File)obj);
			}
		}
	}

	/** {@link MdGuiInterface} */
	public void reloadMdConfig() {
		if (getMdSingletonBundle().getCustomDrawerBundle().isAvailable()) {
			File workDir;
			if (this.target == Target.TARGET_DRAWER) {
				workDir = getMdSingletonBundle().getCustomDrawerBundle().getWorkDir();
			} else {
				workDir = getMdSingletonBundle().getCustomPixelShaderBundle().getWorkDir();
			}
			File sourceDir = new File(workDir.getAbsolutePath() + File.separatorChar + MdConstants.MD_SCRIPT_ROOT);
			if (sourceDir.exists()) {
				if (!sourceDir.isDirectory()) {
					sourceDir.delete();
				}
			} else {
				sourceDir.mkdir();
			}
			this.fileViewTree.setRootDirectory(sourceDir);
		} else {
			this.fileViewTree.setRootDirectory(null);
		}
	}

	/** {@link MdGuiInterface} */
	public void sendMdMessage(MdMessage mdMessage) {
		parentComponent.sendMdMessage(mdMessage);
	}
}
