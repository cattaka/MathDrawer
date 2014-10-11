package net.cattaka.mathdrawer;

import java.awt.Color;
import java.nio.charset.Charset;

public class MdConstants {
	public static String MD_CONFIG_FILE = "mathdrawer.cfg";
	public static Charset DEFAULT_CHARSET = Charset.forName("UTF8");
	public static Color DEFAULT_BACKGROUND = Color.BLACK;
	public static String DRAWER_SETTING_FILE = "drawerSetting.xml";
	public static String PRISET_IMAGE_LIST_FILE = "prisetImageList.txt";
	public static String PRISET_IMAGE_DIR = "prisetImage/";
	
	public static final String STRING_FOR_MESURE_WIDTH = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String MD_SCRIPT_ROOT = "script";
	
	public static final String CUSTOM_DRAWER_BASE = "CustomDrawerBase.txt";
	public static final String CUSTOM_PIXEL_SHADER_BASE = "CustomPixelShaderBase.txt";
	
	public static final String MD_DRAWER_EXT = ".dwr";
	public static final String MD_PIXELSHADER_EXT = ".ps";
}
