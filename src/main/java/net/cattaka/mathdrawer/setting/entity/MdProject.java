package net.cattaka.mathdrawer.setting.entity;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.mathdrawer.drawer.RootDrawer;
import net.cattaka.mathdrawer.gui.MdEditorInterface;
import net.cattaka.util.UncloseableInputStream;

public class MdProject {
	private RootDrawer rootDrawer;
	private PresetImageList presetImageList;
	
	public MdProject() {
		this.rootDrawer = new RootDrawer();
		this.rootDrawer.createCachedImage();
		this.presetImageList = new PresetImageList();
	}
	
	public void initialize(MdEditorInterface parentComponent) {
		this.rootDrawer.initialize(parentComponent);
	}
	
	public RootDrawer getRootDrawer() {
		return rootDrawer;
	}
	public void setRootDrawer(RootDrawer rootDrawer) {
		this.rootDrawer = rootDrawer;
	}
	public PresetImageList getPresetImageList() {
		return presetImageList;
	}
	public void setPresetImageList(PresetImageList presetImageList) {
		this.presetImageList = presetImageList;
	}
	
	public static MdProject readMdProject(ZipInputStream zin) throws IOException {
		MdProject result = new MdProject();
		// PresetImageListの読み込み
		{
			PresetImageList presetImageList = PresetImageList.readPresetImageList(zin);
			result.setPresetImageList(presetImageList);
		}

		// DrawerSettingの読み込み
		{
			zin.getNextEntry();
			XMLDecoder decoder = new XMLDecoder(new UncloseableInputStream(zin));
			Object drawerSettingObject = decoder.readObject();
			if (!(drawerSettingObject instanceof DrawerSetting)) {
				throw new IOException("DrawerSetting is not valid.");
			}
			DrawerSetting drawerSetting = (DrawerSetting)drawerSettingObject; 
			zin.closeEntry();
			
			// RootDrawerの作成
			RootDrawer rootDrawer;
			try {
				rootDrawer = (RootDrawer)DrawerSetting.createDrawer(drawerSetting, null);
				rootDrawer.createCachedImage();
			} catch (ClassNotFoundException e) {
				throw new IOException(e.getMessage());
			} catch (IllegalAccessException e) {
				throw new IOException(e.getMessage());
			} catch (InstantiationException e) {
				throw new IOException(e.getMessage());
			} catch (ClassCastException e) {
				throw new IOException(e.getMessage());
			}
			result.setRootDrawer(rootDrawer);
		}
		
		return result;
	}
	public static void writeMdProject(ZipOutputStream zout, MdProject mdProject) throws IOException {
		PresetImageList.writePresetImageList(zout, mdProject.getPresetImageList());
		
		DrawerSetting drawerSetting = DrawerSetting.createDrawerSetting(mdProject.getRootDrawer());
		zout.putNextEntry(new ZipEntry(MdConstants.DRAWER_SETTING_FILE));
		XMLEncoder encoder = new XMLEncoder(zout);
		encoder.writeObject(drawerSetting);
		encoder.flush();
		zout.closeEntry();
	}
}
