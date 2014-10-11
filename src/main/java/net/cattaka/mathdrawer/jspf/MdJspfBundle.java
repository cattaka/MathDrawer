package net.cattaka.mathdrawer.jspf;

import java.io.File;
import java.util.List;

import net.cattaka.jspf.JspfBundle;
import net.cattaka.jspf.JspfJavaSourceConverter;
import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.util.FileUtil;

public class MdJspfBundle<T> extends JspfBundle<T> {
	public MdJspfBundle(JspfJavaSourceConverter<T> jspfJavaSourceConverter,
			Class<T> targetClass) {
		super(jspfJavaSourceConverter, targetClass);
	}
	
	public File getScriptDir() {
		File result = null;
		File workDir = this.getWorkDir();
		
		if (workDir != null) {
			result = new File(workDir.getPath() + File.separator + MdConstants.MD_SCRIPT_ROOT);
		}
		return result;
	}
	
	public String[] createJspfFileNameArray(int limit, String ext) {
		String[] result;
		File scriptDir = getScriptDir();
		if (scriptDir != null) {
			List<String> filenames = FileUtil.getAllFileNameList(scriptDir, limit, ext);
			result = (String[]) filenames.toArray(new String[filenames.size()]);
		} else {
			result = new String[0];
		}
		return result;
	}
	
	/**
	 * 編集中のスクリプトのラベル名を表示する。
	 * 
	 * @param targetFile
	 * @return
	 */
	public String getLabelName(File targetFile) {
		String result = "";
		if ( targetFile != null) {
			File scriptDir = getScriptDir();
			if (scriptDir != null) {
				result = FileUtil.cutSubPath(scriptDir, targetFile);
			}
		}
		return result;
	}
}
