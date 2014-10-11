package net.cattaka.jspf;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.List;

import net.cattaka.mathdrawer.MdConfigConstants;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.util.ExceptionHandler;

public class ToolsJarBundle {
	private Object javacMain;

	class MyURLClassLoader extends URLClassLoader {
		private List<Permission> extraPermission = new ArrayList<Permission>();
		public MyURLClassLoader(URL[] urls) {
			super(urls);
		}

		public MyURLClassLoader(URL[] urls, ClassLoader parent,
				URLStreamHandlerFactory factory) {
			super(urls, parent, factory);
		}

		public MyURLClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		public List<Permission> getExtraPermission() {
			return extraPermission;
		}

		@Override
		protected PermissionCollection getPermissions(CodeSource codesource) {
			PermissionCollection pc = super.getPermissions(codesource);
			for(Permission perm:extraPermission) {
				pc.add(perm);
			}
			return pc;
		}
	}
	
	public ToolsJarBundle() {
	}

	@SuppressWarnings("unchecked")
	public void reloadTools(MdConfig rdbaConfig) {
		this.javacMain = null;
		
		String toolsFileName = rdbaConfig.getProperty(MdConfigConstants.LIB_TOOLS);
		if (toolsFileName == null || toolsFileName.length() == 0) {
			return;
		}
		
		// JarのURLを取得
		URL toolsUrl = null;
		try {
			toolsUrl = new URL(toolsFileName);
		} catch (MalformedURLException e) {
			// URLでない場合はファイルとして試みる
			File toolsFile = new File(toolsFileName);
			if (toolsFile.exists()) {
				try {
					toolsUrl = toolsFile.toURL();
				} catch (MalformedURLException e2) {
					// 面倒見切れん
				}
			}
		}
		if (toolsUrl == null) {
			// URLが見つからないので諦め
			return;
		}
		
		// Jarファイルをロード
		MyURLClassLoader urlClassLoader = new MyURLClassLoader(new URL[]{toolsUrl}, ToolsJarBundle.class.getClassLoader());
		urlClassLoader.getExtraPermission().add(new AllPermission());
		try {
			Class<?> javacMainClass = urlClassLoader.loadClass("com.sun.tools.javac.Main");
			this.javacMain = javacMainClass.newInstance();
		} catch (ClassNotFoundException e) {
			// コンパイルにミスってる
			ExceptionHandler.error(e);
		} catch (InstantiationException e) {
			// ありえない
			ExceptionHandler.fatal(e);
		} catch (IllegalAccessException e) {
			// ありえない
			ExceptionHandler.fatal(e);
		}
	}

	public boolean isAvailable() {
		return (this.javacMain != null);
	}
	
	@SuppressWarnings("static-access")
	public void compile(String[] args, PrintWriter printWriter) throws JspfException {
		if (this.javacMain == null) {
			throw new JspfException("Java compiler is not set.");
		}
		try {
			Method method = this.javacMain.getClass().getMethod("compile", String[].class, PrintWriter.class);
			method.invoke(null, args, printWriter);
		} catch (NoSuchMethodException e) {
			ExceptionHandler.fatal(e);
			throw new JspfException(e);
		} catch (InvocationTargetException e) {
			ExceptionHandler.fatal(e);
			throw new JspfException(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.fatal(e);
			throw new JspfException(e);
		}
	}
}
