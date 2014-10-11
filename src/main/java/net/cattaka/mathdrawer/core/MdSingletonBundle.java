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
 * $Id: RdbaSingletonBundle.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import net.cattaka.mathdrawer.MdResourceConstants;
import net.cattaka.mathdrawer.drawer.custom.CustomDrawerProcess;
import net.cattaka.mathdrawer.drawer.custom.CustomPixelShader;
import net.cattaka.mathdrawer.jspf.MdJspfBundle;
import net.cattaka.swing.TextFileChooser;

public class MdSingletonBundle {
	private HashMap<String,Object> resourceMap = new HashMap<String, Object>();
	private JFileChooser imageFileChooser;
	private JFileChooser mdProjectFileChooser;
	private TextFileChooser customPixcelShaderChooser;
	private TextFileChooser customDrawerFileChooser;
//	private JspfBundle<RdbaScript> scriptBundle;
	private MdJspfBundle<CustomDrawerProcess> customDrawerBundle;
	private MdJspfBundle<CustomPixelShader> customPixelShaderBundle;
	
	public MdSingletonBundle() {
		createResource();
	}
	
	// -- getter/setter -------------------------------------------------
	public TextFileChooser getCustomPixelShaderFileChooser() {
		return customPixcelShaderChooser;
	}

	public void setCustomPixcelShaderFileChooser(TextFileChooser customPixcelShaderFileChooser) {
		this.customPixcelShaderChooser = customPixcelShaderFileChooser;
	}

	public TextFileChooser getCustomDrawerFileChooser() {
		return customDrawerFileChooser;
	}

	public void setCustomDrawerFileChooser(TextFileChooser customPixcelShaderFileChooser) {
		this.customDrawerFileChooser = customPixcelShaderFileChooser;
	}

	public Object getResource(String name) {
		return resourceMap.get(name);
	}

	public MdJspfBundle<CustomDrawerProcess> getCustomDrawerBundle() {
		return customDrawerBundle;
	}

	public void setCustomDrawerBundle(MdJspfBundle<CustomDrawerProcess> customDrawerBundle) {
		this.customDrawerBundle = customDrawerBundle;
	}

	public MdJspfBundle<CustomPixelShader> getCustomPixelShaderBundle() {
		return customPixelShaderBundle;
	}

	public void setCustomPixelShaderBundle(
			MdJspfBundle<CustomPixelShader> customPixelShaderBundle) {
		this.customPixelShaderBundle = customPixelShaderBundle;
	}

	public JFileChooser getImageFileChooser() {
		return imageFileChooser;
	}

	public void setImageFileChooser(JFileChooser imageFileChooser) {
		this.imageFileChooser = imageFileChooser;
	}

	public JFileChooser getMdProjectFileChooser() {
		return mdProjectFileChooser;
	}

	public void setMdProjectFileChooser(JFileChooser mdProjectFileChooser) {
		this.mdProjectFileChooser = mdProjectFileChooser;
	}

	// -- other -------------------------------------------------
	// リソースの作成
	private void createResource() {
		// アイコン
		{
			URL imageAppUrl = this.getClass().getClassLoader().getResource("icon/m_app.png");

			URL iconNewUrl = this.getClass().getClassLoader().getResource("icon/m_new.png");
			URL iconOpenUrl = this.getClass().getClassLoader().getResource("icon/m_open.png");
			URL iconSaveUrl = this.getClass().getClassLoader().getResource("icon/m_save.png");
			URL iconSaveasUrl = this.getClass().getClassLoader().getResource("icon/m_saveas.png");
			URL iconRunUrl = this.getClass().getClassLoader().getResource("icon/m_run.png");
			URL iconCompileUrl = this.getClass().getClassLoader().getResource("icon/m_compile.png");
			URL iconFindUrl = this.getClass().getClassLoader().getResource("icon/m_find.png");
			URL iconGcUrl = this.getClass().getClassLoader().getResource("icon/m_gc.png");
			URL iconTransposeUrl = this.getClass().getClassLoader().getResource("icon/m_transpose.png");
			URL iconAddItemUrl = this.getClass().getClassLoader().getResource("icon/m_add_item.png");
			URL iconAddBundleUrl = this.getClass().getClassLoader().getResource("icon/m_add_bundle.png");
			URL iconRemoveUrl = this.getClass().getClassLoader().getResource("icon/m_remove.png");
			URL iconArrowUpUrl = this.getClass().getClassLoader().getResource("icon/m_arrow_up.png");
			URL iconArrowDownUrl = this.getClass().getClassLoader().getResource("icon/m_arrow_down.png");

			Image imageApp = null;
			try {
				imageApp = ImageIO.read(imageAppUrl);
			} catch(IOException e) {
				imageApp = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
			}
			ImageIcon iconNew = new ImageIcon(iconNewUrl);
			ImageIcon iconOpen = new ImageIcon(iconOpenUrl);
			ImageIcon iconSave = new ImageIcon(iconSaveUrl);
			ImageIcon iconSaveas = new ImageIcon(iconSaveasUrl);
			ImageIcon iconRun = new ImageIcon(iconRunUrl);
			ImageIcon iconCompile = new ImageIcon(iconCompileUrl);
			ImageIcon iconFind = new ImageIcon(iconFindUrl);
			ImageIcon iconGc = new ImageIcon(iconGcUrl);
			ImageIcon iconTranspose = new ImageIcon(iconTransposeUrl);
			ImageIcon iconAddItem = new ImageIcon(iconAddItemUrl);
			ImageIcon iconAddBundle = new ImageIcon(iconAddBundleUrl);
			ImageIcon iconRemove = new ImageIcon(iconRemoveUrl);
			ImageIcon iconArrowUp = new ImageIcon(iconArrowUpUrl);
			ImageIcon iconArrowDown = new ImageIcon(iconArrowDownUrl);
			
			resourceMap.put(MdResourceConstants.IMAGE_APP, imageApp);
			resourceMap.put(MdResourceConstants.ICON_NEW, iconNew);
			resourceMap.put(MdResourceConstants.ICON_OPEN, iconOpen);
			resourceMap.put(MdResourceConstants.ICON_SAVE, iconSave);
			resourceMap.put(MdResourceConstants.ICON_SAVEAS, iconSaveas);
			resourceMap.put(MdResourceConstants.ICON_RUN, iconRun);
			resourceMap.put(MdResourceConstants.ICON_COMPILE, iconCompile);
			resourceMap.put(MdResourceConstants.ICON_FIND, iconFind);
			resourceMap.put(MdResourceConstants.ICON_GC, iconGc);
			resourceMap.put(MdResourceConstants.ICON_TRANSPOSE, iconTranspose);
			resourceMap.put(MdResourceConstants.ICON_ADD_ITEM, iconAddItem);
			resourceMap.put(MdResourceConstants.ICON_ADD_BUNDLE, iconAddBundle);
			resourceMap.put(MdResourceConstants.ICON_REMOVE, iconRemove);
			resourceMap.put(MdResourceConstants.ICON_ARROW_UP, iconArrowUp);
			resourceMap.put(MdResourceConstants.ICON_ARROW_DOWN, iconArrowDown);
		}
	}
}
