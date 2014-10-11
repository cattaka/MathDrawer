package net.cattaka.mathdrawer.drawer.custom;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.Map;

import net.cattaka.mathdrawer.drawer.DrawerUtil;
import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.MdMath;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.entity.Vector2d;
import net.cattaka.mathdrawer.entity.Vector3d;
import net.cattaka.mathdrawer.entity.Vector4d;
import net.cattaka.mathdrawer.setting.entity.CustomMdSettingUtil;

abstract public class CustomDrawerProcess {
	private BufferedImage cachedImage;
	private boolean debug = false;
	private CustomMdSettingUtil customMdSettingUtil;
	private Map<String, Object> settingValues;
	private DrawerUtil drawerUtil;
	private String name;
	private String description;

	/**
	 * デバッグ用のメッセージの出力先です。
	 */
	protected PrintWriter out;
	/**
	 * 直前のDrawerによって描画されたイメージです。
	 */
	protected Graphics2D g;
	/**
	 * 現在のフレーム番号です。
	 * 0以上、maxFrameCount未満の値を取ります。
	 */
	protected int frameCount = 0;
	/**
	 * フレームの個数です。
	 */
	protected int maxFrameCount = 1;

	/**
	 * コンストラクタ
	 */
	public CustomDrawerProcess() {
		this.customMdSettingUtil = new CustomMdSettingUtil();
	}
	
	public void draw(BufferedImage baseImage) {
		if (this.g != null) {
			g.dispose();
		}
		g = this.cachedImage.createGraphics();
		int w = this.cachedImage.getWidth();
		int h = this.cachedImage.getHeight();
		
		this.drawCustom(baseImage, w, h);
		
		g.dispose();
		g = null;
	}
	
	abstract public void drawCustom(BufferedImage baseImage, int w, int h);
	
	// -- Interface -------------------------------------
	
	public BufferedImage getCachedImage() {
		return cachedImage;
	}
	public void setCachedImage(BufferedImage cachedImage) {
		this.cachedImage = cachedImage;
	}
	public PrintWriter getOut() {
		return out;
	}
	public void setOut(PrintWriter out) {
		this.out = out;
	}
	public DrawerUtil getDrawerUtil() {
		return drawerUtil;
	}

	public void setDrawerUtil(DrawerUtil drawerUtil) {
		this.drawerUtil = drawerUtil;
	}

	public Map<String, Object> getSettingValues() {
		return settingValues;
	}

	public void setSettingValues(Map<String, Object> settingValues) {
		this.settingValues = settingValues;
	}

	// -- ユーティリティ -------------------------------------------------------

	/**
	 * 画像上の指定されたピクセルの座標を
	 * 0.0〜1.0の範囲に正規化した形式に変換します。
	 * 例：(x=200,y=100,w=800,h=500) → (0.25, 0.2)
	 * 
	 * @param vec 出力先
	 * @param x 画像上のx座標
	 * @param y 画像上のy座標
	 * @param w 画像の幅
	 * @param h 画像の高さ
	 */
	public void posToVector2d(Vector2d out, int x, int y, int w, int h) {
		out.x = (double)x/(double)w;
		out.y = (double)y/(double)h;
	}
	
	/**
	 * 直交座標を極座標に変換します。
	 * 
	 * @param out (l,rad)の極座標
	 * @param in (x,y)の直交座標
	 */
	public void toPolar(Vector2d out, Vector2d in) {
		double l = in.getLength();
		if (l == 0) {
			out.x = l;
			out.y = 0;
		} else {
			double rad = Math.acos(in.x / l);
			if (in.y < 0) {
				rad = -rad;
			}
			out.x = l;
			out.y = rad;
		}
	}

	/**
	 * 極座標を直交座標に変換します。
	 * 
	 * @param out (x,y)の直交座標
	 * @param in (l,rad)の極座標
	 */
	public void toRectangular(Vector2d out, Vector2d in) {
		out.x = in.x * Math.cos(in.y);
		out.y = in.x * Math.sin(in.y);
	}
	
	/**
	 * {@link MdMath#limitDouble(double)}と同じ。
	 * 
	 * @param d
	 * @return
	 */
	public double limitDouble(double d) {
		return MdMath.limitDouble(d);
	}

	/**
	 * {@link MdMath#cyclicDouble(double)}と同じ。
	 * 
	 * @param d
	 * @return
	 */
	public double cyclicDouble(double d) {
		return MdMath.cyclicDouble(d);
	}

	/**
	 * {@link MdMath#interpolateColor4d(Color4d, double, Color4d, Color4d)}と同じ。
	 * 
	 * @param out
	 * @param rate
	 * @param c1
	 * @param c2
	 */
	public void interpolateColor4d(Color4d out, double rate, Color4d c1, Color4d c2) {
		MdMath.interpolateColor4d(out, rate, c1, c2);
	}

	// -- Getter/Setter ---------------------------------
	/**
	 * デバッグモードの正否を返します。
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * デバッグモードを設定します。
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CustomMdSettingUtil getCustomMdSettingUtil() {
		return customMdSettingUtil;
	}

	public void setCustomMdSettingUtil(CustomMdSettingUtil customMdSettingUtil) {
		this.customMdSettingUtil = customMdSettingUtil;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public int getMaxFrameCount() {
		return maxFrameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	public void setMaxFrameCount(int maxFrameCount) {
		this.maxFrameCount = maxFrameCount;
	}

	// -- 引数取得用 --------------------------------------------
	/**
	 * プロジェクトで指定されたパラメータを取得します。
	 * @return パラメータ
	 */
	public Integer getInteger(String key) {
		Object obj = settingValues.get(key);
		if (obj != null && obj instanceof Integer) {
			return (Integer)obj;
		} else {
			return null;
		}
	}
	
	/**
	 * プロジェクトで指定されたパラメータを取得します。
	 * @return パラメータ
	 */
	public Double getDouble(String key) {
		Object obj = settingValues.get(key);
		if (obj != null && obj instanceof Double) {
			return (Double)obj;
		} else {
			return null;
		}
	}
	
	/**
	 * プロジェクトで指定されたパラメータを取得します。
	 * @return パラメータ
	 */
	public Vector2d getVector2d(String key) {
		Object obj = settingValues.get(key);
		if (obj != null && obj instanceof Vector2d) {
			return (Vector2d)obj;
		} else {
			return null;
		}
	}
	
	/**
	 * プロジェクトで指定されたパラメータを取得します。
	 * @return パラメータ
	 */
	public Vector3d getVector3d(String key) {
		Object obj = settingValues.get(key);
		if (obj != null && obj instanceof Vector3d) {
			return (Vector3d)obj;
		} else {
			return null;
		}
	}
	
	/**
	 * プロジェクトで指定されたパラメータを取得します。
	 * @return パラメータ
	 */
	public Vector4d getVector4d(String key) {
		Object obj = settingValues.get(key);
		if (obj != null && obj instanceof Vector4d) {
			return (Vector4d)obj;
		} else {
			return null;
		}
	}
	
	/**
	 * プロジェクトで指定されたパラメータを取得します。
	 * @return パラメータ
	 */
	public Color4d getColor4d(String key) {
		Object obj = settingValues.get(key);
		if (obj != null && obj instanceof Color4d) {
			return (Color4d)obj;
		} else {
			return null;
		}
	}

	/**
	 * プロジェクトで指定されたパラメータを取得します。
	 * 指定された画像が存在しない場合はnullを返します。
	 * @return パラメータ
	 */
	public PresetImage getPresetImage(String key) {
		PresetImage result = null;
		Object obj = settingValues.get(key);
		if (obj != null) {
			result = this.drawerUtil.getPresetImage(obj.toString());
		}
		return result;
	}
}
