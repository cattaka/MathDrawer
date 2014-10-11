package net.cattaka.mathdrawer.drawer;

import java.awt.image.BufferedImage;
import java.util.List;

import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.exception.MdDrawerException;
import net.cattaka.mathdrawer.setting.entity.MdSetting;
import net.cattaka.mathdrawer.setting.entity.RootMdSetting;

abstract public class Drawer {
	private Drawer parentDrawer;
	private BufferedImage[] cachedImage;
	private boolean needUpdate;
	private String message;
	
	public void createCachedImage() {
		this.cachedImage = this.createBlankBufferedImage();
		
		List<Drawer> drawerList = this.getChildrenList();
		if (drawerList != null) {
			for (Drawer drawer:drawerList) {
				drawer.createCachedImage();
			}
		}
	}
	
	public void addChild(Drawer drawer) {
		if (this.getChildrenList() != null) {
			this.getChildrenList().add(drawer);
			drawer.setParentDrawer(this);
			drawer.setCachedImage(this.createBlankBufferedImage());
		}
	}

	public void addChild(int index, Drawer drawer) {
		if (this.getChildrenList() != null) {
			this.getChildrenList().add(index, drawer);
			drawer.setParentDrawer(this);
			drawer.setCachedImage(this.createBlankBufferedImage());
		}
	}

	public void removeChild(int index) {
		if (this.getChildrenList() != null) {
			Drawer drawer = this.getChildrenList().remove(index);
			drawer.setParentDrawer(null);
			drawer.setCachedImage(null);
		}
	}

	public Drawer getParentDrawer() {
		return parentDrawer;
	}

	public void setParentDrawer(Drawer parentDrawer) {
		this.parentDrawer = parentDrawer;
	}
	
	/**
	 * 表示用ラベルを取得します。
	 * @return
	 */
	public String getLabel() {
		return this.getClass().getName();
	}
	/**
	 * 子供のDrawerのListの実体を取得します。
	 * このDrawerが子供を持て無い場合はnullを返します。
	 * @return
	 */
	public List<Drawer> getChildrenList() {
		return null;
	}
	/**
	 * 現在のプロジェクト全体のセッティングを取得します。
	 * @return
	 */
	public RootMdSetting getRootMdSetting() {
		return parentDrawer.getRootMdSetting();
	}
	
	/**
	 * MathDrawer全体の設定を取得します。
	 * @return
	 */
	public MdConfig getMdConfig() {
		return parentDrawer.getMdConfig();
	}
	
	/**
	 * 現在のプロジェクト全体のDrawerUtilを取得します。
	 * @return
	 */
	public DrawerUtil getDrawerUtil() {
		return parentDrawer.getDrawerUtil();
	}
	
	/**
	 * 空の描画用イメージを作成します。
	 * フォーマットはプロジェクトで指定した物となります。
	 * 
	 * @return
	 */
	public BufferedImage[] createBlankBufferedImage() {
		return parentDrawer.createBlankBufferedImage();
	}
	/**
	 * 描画用のイメージを取得します。
	 * @return
	 */
	public BufferedImage[] getCachedImage() {
		return cachedImage;
	}
	/**
	 * 描画用のイメージを設定します。
	 * @return
	 */
	public void setCachedImage(BufferedImage[] cachedImage) {
		this.cachedImage = cachedImage;
	}
	/**
	 * 描画用イメージの更新の要否を返します。
	 * @return
	 */
	public boolean isNeedUpdate() {
		return needUpdate;
	}
	/**
	 * 描画用イメージの更新の要否を設定します。
	 * @param needUpdate
	 */
	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
		if (this.needUpdate) {
			this.createCachedImage();
		}
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * このDrawerのセッティングを取得します。
	 * @return
	 */
	abstract public MdSetting getMdSetting();
	
	/**
	 * このDrawerの描画を行います。
	 * @param baseImage 直前のDrawerの出力結果
	 * @throws MdDrawerException TODO
	 */
	public void draw(BufferedImage[] baseImage) throws MdDrawerException {
		
	}
}
