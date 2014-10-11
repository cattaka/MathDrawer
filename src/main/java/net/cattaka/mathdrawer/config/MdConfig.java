package net.cattaka.mathdrawer.config;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.InvalidPropertiesFormatException;

import net.cattaka.jspf.ToolsJarBundle;
import net.cattaka.mathdrawer.MdConfigConstants;
import net.cattaka.swing.util.LookAndFeelBundle;
import net.cattaka.util.PropertiesEx;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.ExceptionHandler.Priority;

public class MdConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private PropertiesEx properties = new PropertiesEx();
	private Font fontForEditor;
	private int charactersPerTab = 4;
	private ToolsJarBundle toolsJarBundle = new ToolsJarBundle();
	private boolean useDefaultToolsJar;
	private File customDrawerRoot;
	private File customPixelShaderRoot;
	
	public MdConfig() {
	}
	
	//-- FontFotEditor --------------------------------------------------
	public Font getFontForEditor() {
		return fontForEditor;
	}

	public void setFontForEditor(Font fontForEditor) {
		this.fontForEditor = fontForEditor;
		String[] fttStrArray = new String[] {
			fontForEditor.getFamily(),
			String.valueOf(fontForEditor.getStyle()),
			String.valueOf(fontForEditor.getSize())
		};
		this.setPropertyArray(MdConfigConstants.FONT_FOR_EDITOR_KEY, fttStrArray);
	}
	
	// -- charactersPerTab ----------------------------------------------
	public int getCharactersPerTab() {
		return charactersPerTab;
	}

	public void setCharactersPerTab(int charactersPerTab) {
		this.charactersPerTab = charactersPerTab;
		this.setProperty(MdConfigConstants.CHARACTER_PER_TAB, String.valueOf(charactersPerTab));
	}

	// -- customDrawerRoot ---------------------------------------------------
	public File getCustomDrawerRoot() {
		return customDrawerRoot;
	}

	public boolean setCustomDrawerRoot(File customDrawerRoot) {
		if (customDrawerRoot != null && customDrawerRoot.isDirectory()) {
			this.customDrawerRoot = customDrawerRoot;
			this.setProperty(MdConfigConstants.CUSTOM_DRAWER_ROOT, customDrawerRoot.getPath());
			return true;
		} else {
			return false;
		}
	}
		
	// -- customDrawerRoot ---------------------------------------------------
	public File getCustomPixelShaderRoot() {
		return customPixelShaderRoot;
	}

	public boolean setCustomPixelShaderRoot(File customPixelShaderRoot) {
		if (customPixelShaderRoot != null && customPixelShaderRoot.isDirectory()) {
			this.customPixelShaderRoot = customPixelShaderRoot;
			this.setProperty(MdConfigConstants.CUSTOM_PIXEL_SHADER_ROOT, customPixelShaderRoot.getPath());
			return true;
		} else {
			return false;
		}
	}
	
	// -- useDefaultToolsJar ---------------------------------------------------
	public boolean isUseDefaultToolsJar() {
		return useDefaultToolsJar;
	}

	public void setUseDefaultToolsJar(boolean useDefaultToolsJar) {
		this.useDefaultToolsJar = useDefaultToolsJar;
		this.setProperty(MdConfigConstants.LIB_USE_DEFAULT_TOOLS, useDefaultToolsJar ? "1" : "0");
	}
	
	// -- ToolsJar ---------------------------------------------------
	public void setToolsJar(String jarName) {
		this.setProperty(MdConfigConstants.LIB_TOOLS, jarName);
		this.toolsJarBundle.reloadTools(this);
	}

	public String getToolsJar() {
		String result = this.getProperty(MdConfigConstants.LIB_TOOLS);
		return (result != null) ? result : ""; 
	}
	
	
	// -- logLevel ---------------------------------------------------
	public String getLogLevel() {
		return ExceptionHandler.getCurrentPriority().name();
	}

	public void setLogLevel(String logLevel) {
		logLevel = (logLevel != null) ? logLevel : "";
		try {
			Priority priority = ExceptionHandler.Priority.valueOf(logLevel);
			ExceptionHandler.setCurrentPriority(priority);
			this.setProperty(MdConfigConstants.LOG_LEVEL, logLevel);
		} catch (IllegalArgumentException e) {
			ExceptionHandler.error(e);
		}
	}
	// -- lookAndFeel ---------------------------------------------------
	public void setLookAndFeel(String lookAndFeel) {
		this.setProperty(MdConfigConstants.LOOK_AND_FEEL, lookAndFeel);
		LookAndFeelBundle.setLookAndFeelClassName(lookAndFeel);
	}
	
	public String getLookAndFeel() {
		return LookAndFeelBundle.getLookAndFeelClassName();
	}

	//-- 更新処理 --------------------------------------------------
	public void updateArtificialInfo() {
		// Configファイルのリビジョンを設定する
//		int configRevision = MdConfigConstants.CURRENT_CONFIGFILE_REVISION;
		{
			String revStr = this.getProperty(MdConfigConstants.CONFIGFILE_REVISION);
			try {
				if (revStr != null) {
//					configRevision = Integer.parseInt(revStr);
				}
			} catch (NumberFormatException e) {
				ExceptionHandler.error(e);
			}
		}
		
		// fontForEditor作成
		{
			String[] values = this.getPropertyArray(MdConfigConstants.FONT_FOR_EDITOR_KEY);
			if (values != null && values.length >= 3) {
				int style = Integer.parseInt(values[1]);
				int size = Integer.parseInt(values[2]);
				fontForEditor = new Font(values[0], style, size);
			}
			if (fontForEditor == null) {
				setFontForEditor(new Font(MdConfigConstants.DEFAULT_FONT_EDITOR, Font.PLAIN, 12));
			}
		}
		
		// charactersPerTab作成
		{
			String value = this.getProperty(MdConfigConstants.CHARACTER_PER_TAB);
			Integer cpt = null;
			if (value != null) {
				try {
					cpt = Integer.valueOf(value);
					this.charactersPerTab = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					ExceptionHandler.error(e);
				}
			}
			if (cpt == null) {
				cpt = 4;
			}
			this.charactersPerTab = cpt;
		}

		// customDrawerRoot
		{
			String value = this.getProperty(MdConfigConstants.CUSTOM_DRAWER_ROOT);
			File sqar = null;
			if (value != null && value.length() > 0) {
				sqar = new File(value);
				if (!sqar.exists() || !sqar.isDirectory()) {
					sqar = null;
				}
			}
			// 見つからない場合はnullのままとする(下手に走られると困る)
			this.customDrawerRoot = sqar;
		}

		// customPixelShaderRoot
		{
			String value = this.getProperty(MdConfigConstants.CUSTOM_PIXEL_SHADER_ROOT);
			File sqar = null;
			if (value != null && value.length() > 0) {
				sqar = new File(value);
				if (!sqar.exists() || !sqar.isDirectory()) {
					sqar = null;
				}
			}
			// 見つからない場合はnullのままとする(下手に走られると困る)
			this.customPixelShaderRoot = sqar;
		}

		// useDefaultToolsJar
		{
			String value = this.getProperty(MdConfigConstants.LIB_USE_DEFAULT_TOOLS);
			if (value != null) {
				this.useDefaultToolsJar = "1".equals(value);
			} else {
				this.useDefaultToolsJar = true;
			}
		}
		
		// toolsJar作成
		{
			this.toolsJarBundle.reloadTools(this);
		}
		
		// logLevelの設定
		{
			String value = this.getProperty(MdConfigConstants.LOG_LEVEL);
			value = (value != null) ? value : Priority.INFO.name();
			try {
				Priority priority = ExceptionHandler.Priority.valueOf(value);
				ExceptionHandler.setCurrentPriority(priority);
			} catch (IllegalArgumentException e) {
				ExceptionHandler.error(e);
			}
		}
		// LookAndFeelの設定
		{
			String value = this.getProperty(MdConfigConstants.LOOK_AND_FEEL);
			if (value != null) {
				LookAndFeelBundle.setLookAndFeelClassName(value);
			} else {
				value = LookAndFeelBundle.getLookAndFeelClassName();
			}
		}
	}

	// -- 文字列直接取得＆設定 -------------------------------------------------------------
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Object setProperty(String key, String value) {
		return properties.setProperty(key, value);
	}

	public String[] getPropertyArray(String key) {
		return properties.getPropertyArray(key);
	}

	public String[] setPropertyArray(String key, String[] valueArray) {
		return properties.setPropertyArray(key, valueArray);
	}

	// -- 入出力 -------------------------------------------------------------

	public ToolsJarBundle getToolsJarBundle() {
		return toolsJarBundle;
	}

	public void load(InputStream inStream) throws IOException {
		properties.load(inStream);
		updateArtificialInfo();
	}

	public void loadFromXML(InputStream in) throws IOException, InvalidPropertiesFormatException {
		properties.loadFromXML(in);
		updateArtificialInfo();
	}

	public void store(OutputStream out, String comments) throws IOException {
		setProperty(MdConfigConstants.CONFIGFILE_REVISION, String.valueOf(MdConfigConstants.CURRENT_CONFIGFILE_REVISION));
		properties.store(out, comments);
	}

	public void storeToXML(OutputStream os, String comment, String encoding)
			throws IOException {
		setProperty(MdConfigConstants.CONFIGFILE_REVISION, String.valueOf(MdConfigConstants.CURRENT_CONFIGFILE_REVISION));
		properties.storeToXML(os, comment, encoding);
	}

	public void storeToXML(OutputStream os, String comment) throws IOException {
		setProperty(MdConfigConstants.CONFIGFILE_REVISION, String.valueOf(MdConfigConstants.CURRENT_CONFIGFILE_REVISION));
		properties.storeToXML(os, comment);
	}
}
