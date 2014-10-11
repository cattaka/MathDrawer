package net.cattaka.mathdrawer.setting.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.cattaka.mathdrawer.drawer.Drawer;
import net.cattaka.mathdrawer.entity.Color4d;
import net.cattaka.mathdrawer.entity.Vector2d;
import net.cattaka.mathdrawer.entity.Vector3d;
import net.cattaka.mathdrawer.entity.Vector4d;
import net.cattaka.util.ExceptionHandler;

/**
 * DrawerをXMLに変換するための一時オブジェクト用クラス。
 * @author cattaka
 */
public class DrawerSetting implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String className;
	private Map<String, Object> values;
	private List<DrawerSetting>  drawerSettingList;

	public DrawerSetting() {
		this.values = new HashMap<String, Object>();
		this.drawerSettingList = new ArrayList<DrawerSetting>();
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Map<String, Object> getValues() {
		return values;
	}
	public void setValues(Map<String, Object> values) {
		this.values = cloneMap(values);
	}
	public List<DrawerSetting> getDrawerSettingList() {
		return drawerSettingList;
	}
	public void setDrawerSettingList(List<DrawerSetting> drawerSettingList) {
		this.drawerSettingList = drawerSettingList;
	}

	public static List<Object> cloneList(List<Object> src) {
		List<Object> result = new ArrayList<Object>(src);
		for (int i=0;i<result.size();i++) {
			Object obj = result.get(i);
			if (obj != null) {
				if (obj instanceof Color4d) {
					obj = new Color4d((Color4d)obj);
				} else if (obj instanceof Vector2d) {
					obj = new Vector2d((Vector2d)obj);
				} else if (obj instanceof Vector3d) {
					obj = new Vector3d((Vector3d)obj);
				} else if (obj instanceof Vector4d) {
					obj = new Vector4d((Vector4d)obj);
				}
			}
			result.set(i,obj);
		}
		return result;
	}

	public static Map<String, Object> cloneMap(Map<String, Object> src) {
		Map<String, Object> result = new HashMap<String, Object>(src);
		for (Entry<String, Object> entry: src.entrySet()) {
			Object obj = entry.getValue();
			if (obj != null) {
				if (obj instanceof Color4d) {
					obj = new Color4d((Color4d)obj);
				} else if (obj instanceof Vector2d) {
					obj = new Vector2d((Vector2d)obj);
				} else if (obj instanceof Vector3d) {
					obj = new Vector3d((Vector3d)obj);
				} else if (obj instanceof Vector4d) {
					obj = new Vector4d((Vector4d)obj);
				}
			}
			result.put(entry.getKey(),obj);
		}
		return result;
	}

	/**
	 * XMLから生成した一時オブジェクトからDrawerを生成する。
	 * 
	 * @param drawerSetting
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassCastException
	 */
	public static Drawer createDrawer(DrawerSetting drawerSetting, Drawer parentDrawer)
		throws ClassNotFoundException,IllegalAccessException,InstantiationException,ClassCastException
	{
		Drawer result = null;
		Class<?> drawerClass = Class.forName(drawerSetting.getClassName());
		Object drawerObject = drawerClass.newInstance();
		Drawer drawer = (Drawer)drawerObject;
		drawer.setParentDrawer(parentDrawer);
		if (drawer.getMdSetting().setValues(drawerSetting.getValues())) {
			if (drawer.getChildrenList() != null) {
				for (DrawerSetting childSetting:drawerSetting.getDrawerSettingList()) {
					Drawer child;
					try {
						child = createDrawer(childSetting, drawer);
						if (child != null) {
							drawer.addChild(child);
						}
					} catch(ClassNotFoundException e) {
						ExceptionHandler.error(e);
					} catch(IllegalAccessException e) {
						ExceptionHandler.error(e);
					} catch(InstantiationException e) {
						ExceptionHandler.error(e);
					} catch(ClassCastException e) {
						ExceptionHandler.error(e);
					}
				}
			}
		}
		result = drawer;
		return result;
	}

	/**
	 * DrawerからXMLに出力するための一時オブジェクトを作成する。
	 * 
	 * @param drawer
	 * @return
	 */
	public static DrawerSetting createDrawerSetting(Drawer drawer) {
		DrawerSetting result = new DrawerSetting();
		result.setClassName(drawer.getClass().getName());
		result.setValues(cloneMap(drawer.getMdSetting().getValues()));
		
		if (drawer.getChildrenList() != null) {
			List<DrawerSetting> drawerSettingList = new ArrayList<DrawerSetting>();
			for (Drawer child:drawer.getChildrenList()) {
				drawerSettingList.add(createDrawerSetting(child));
			}
			result.setDrawerSettingList(drawerSettingList);
		}
		
		return result;
	}
}
