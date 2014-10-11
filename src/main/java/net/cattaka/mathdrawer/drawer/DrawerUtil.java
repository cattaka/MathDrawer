package net.cattaka.mathdrawer.drawer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.cattaka.jspf.JspfException;
import net.cattaka.jspf.ToolsJarBundle;
import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.mathdrawer.drawer.custom.CustomDrawerProcess;
import net.cattaka.mathdrawer.drawer.custom.CustomPixelShader;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.exception.MdException;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.mathdrawer.jspf.MdJspfBundle;
import net.cattaka.mathdrawer.setting.entity.PresetImageList;
import net.cattaka.util.ExceptionUtil;
import net.cattaka.util.PrintWriterEx;
import net.cattaka.util.StringUtil;

public class DrawerUtil {
	private MdEditorInterface mdEditorInterface;
	
	public void drawLayer(Drawer drawer) {
		List<Drawer> drawerList = new ArrayList<Drawer>();
		{
			Drawer td = drawer;
			while(td != null) {
				if (drawerList.contains(td)) {
					throw new MdException("This drawer's parent contains loop.");
				} else {
					drawerList.add(td);
				}
				td = td.getParentDrawer();
			}
			Collections.reverse(drawerList);
		}
		
		Drawer rootDrawer = drawerList.get(0);
		try {
			rootDrawer.draw(rootDrawer.getCachedImage());
			drawer.setMessage(null);
		} catch (MdDrawerException e) {
			drawer.setMessage(e.getContents());
		}
	}

	public void drawImage(Drawer drawer) {
		List<Drawer> drawerList = new ArrayList<Drawer>();
		{
			Drawer td = drawer;
			while(td != null) {
				if (drawerList.contains(td)) {
					throw new MdException("This drawer's parent contains loop.");
				} else {
					drawerList.add(td);
				}
				td = td.getParentDrawer();
			}
			Collections.reverse(drawerList);
		}
		
		Drawer rootDrawer = drawerList.get(0);
		try {
			rootDrawer.draw(rootDrawer.getCachedImage());
			drawer.setMessage(null);
		} catch (MdDrawerException e) {
			drawer.setMessage(e.getContents());
		}

	}
	
//	public List<String> createPresetImageNameList() {
//		PresetImageList pil = mdEditorInterface.getMdProject().getPresetImageList();
//		ArrayList<String> result = new ArrayList<String>();
//		for (PresetImage pi:pil) {
//			result.add(pi.getName());
//		}
//		return result;
//	}

	public DrawerUtil(MdEditorInterface mdEditorInterface) {
		super();
		this.mdEditorInterface = mdEditorInterface;
	}

	public String[] createPresetImageNameArray() {
		PresetImageList pil = mdEditorInterface.getMdProject().getPresetImageList();
		String[] result = new String[pil.size()];
		for (int i=0;i<pil.size();i++) {
			result[i] = pil.get(i).getName();
		}
		return result;
	}

	public PresetImage getPresetImage(String name) {
		PresetImageList pil = mdEditorInterface.getMdProject().getPresetImageList();
		PresetImage result = null;
		for (PresetImage pi:pil) {
			if (pi.getName().equals(name)) {
				result = pi;
				break;
			}
		}
		return result;
	}

	public String[] createCustomDrawerProcessNameArray() {
		String[] result;
		MdJspfBundle<CustomDrawerProcess> jspfBundle = this.mdEditorInterface.getMdSingletonBundle().getCustomDrawerBundle();
		result = jspfBundle.createJspfFileNameArray(5, MdConstants.MD_DRAWER_EXT);
		return result;
	}

	public String[] createCustomPixelShaderNameArray() {
		String[] result;
		MdJspfBundle<CustomPixelShader> jspfBundle = this.mdEditorInterface.getMdSingletonBundle().getCustomPixelShaderBundle();
		result = jspfBundle.createJspfFileNameArray(5, MdConstants.MD_PIXELSHADER_EXT);
		return result;
	}
	
	public CustomDrawerProcess getCustomDrawerProcess(String labelName) throws JspfException {
		CustomDrawerProcess result;
		MdJspfBundle<CustomDrawerProcess> jspfBundle = this.mdEditorInterface.getMdSingletonBundle().getCustomDrawerBundle();
		result = jspfBundle.getNewInstance(labelName);
		if (result == null) {
			compile(jspfBundle, labelName);
			result = jspfBundle.getNewInstance(labelName);
		}
		return result;
	}

	public CustomPixelShader getCustomPixelShader(String labelName) throws JspfException {
		CustomPixelShader result;
		MdJspfBundle<CustomPixelShader> jspfBundle = this.mdEditorInterface.getMdSingletonBundle().getCustomPixelShaderBundle();
		result = jspfBundle.getNewInstance(labelName);
		if (result == null) {
			compile(jspfBundle, labelName);
			result = jspfBundle.getNewInstance(labelName);
		}
		return result;
	}
	
	public boolean compile(MdJspfBundle<?> jspfBundle, String labelName) throws JspfException {
		boolean result = true;
		
		File scriptDir = jspfBundle.getScriptDir();
		if (scriptDir == null) {
			result = false;
		}
		
		File scriptFile = null; 
		if (result) {
			scriptFile = new File(scriptDir.getAbsoluteFile()+File.separator+labelName);
			if (!scriptFile.exists() || !scriptFile.isFile()) {
				result = false;
			}
		}

		if (result) {
			StringWriter outSw = new StringWriter();
			PrintWriterEx outPw = new PrintWriterEx(outSw);
			result = false;
			try {
				String sourceText = StringUtil.readFile(scriptFile, MdConstants.DEFAULT_CHARSET.name());
				ToolsJarBundle toolsJarBundle = this.mdEditorInterface.getMdConfig().getToolsJarBundle();
				jspfBundle.compile(toolsJarBundle, labelName, sourceText, outPw);
				result = true;
			} catch(ClassNotFoundException e) {
				outPw.flush();
				JspfException e2 = new JspfException(e);
				e2.setContents(outSw.getBuffer().toString());
				throw e2;
			} catch (IOException e) {
				JspfException e2 = new JspfException(e);
				e2.setContents(ExceptionUtil.toString(e));
				throw e2;
			}
		}
		return result;
	}
}
