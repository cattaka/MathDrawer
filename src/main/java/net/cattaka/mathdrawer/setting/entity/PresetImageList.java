package net.cattaka.mathdrawer.setting.entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.util.StringUtil;

public class PresetImageList extends ArrayList<PresetImage> {
	private static final long serialVersionUID = 1L;
	
	public static PresetImageList readPresetImageList(ZipInputStream zin) throws IOException {
		PresetImageList result = new PresetImageList();
		ArrayList<String> fileNameList = new ArrayList<String>();
		
		// ファイルリストを読み込み
		zin.getNextEntry();
		BufferedReader reader = new BufferedReader(new InputStreamReader(zin,"UTF-8"));
		String str;
		while((str = reader.readLine()) != null) {
			fileNameList.add(str);
		}
		zin.closeEntry();
		
		// ファイル本体を読み込み
		for (Object fileNameObject:fileNameList) {
			String[] strs = StringUtil.decodeStringArray(String.valueOf(fileNameObject));
			String fileName = (strs.length > 0) ? strs[0] : "";
			String name = (strs.length > 1) ? strs[1] : "";
			if (fileName.length() > MdConstants.PRISET_IMAGE_DIR.length()) {
				fileName = fileName.substring(MdConstants.PRISET_IMAGE_DIR.length(), fileName.length());
			}
			zin.getNextEntry();
			result.add(PresetImage.readPresetImage(zin, name));
			zin.closeEntry();
		}
		return result;
	}
	
	public static void writePresetImageList(ZipOutputStream zout, PresetImageList presetImageList) throws IOException {
		// ファイルリストを作成
		ArrayList<String> fileNameList = new ArrayList<String>();
		for (int i=0;i<presetImageList.size();i++) {
			PresetImage presetImage = presetImageList.get(i);
			String fileName = MdConstants.PRISET_IMAGE_DIR + String.valueOf(i) + "." + presetImage.getImageType().name();
			String name = presetImage.getName();
			fileNameList.add(StringUtil.encodeStringArray(new String[]{fileName, name}));
		}

		// ファイルリストを出力
		zout.putNextEntry(new ZipEntry(MdConstants.PRISET_IMAGE_LIST_FILE));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zout,"UTF-8"));
		for (int i=0;i<fileNameList.size();i++) {
			if (i>0) {
				writer.append('\n');
			}
			writer.append(fileNameList.get(i));
		}
		writer.flush();
		zout.closeEntry();

		// ファイル本体を出力
		assert(presetImageList.size() == fileNameList.size());
		for (int i=0;i<presetImageList.size();i++) {
			PresetImage presetImage = presetImageList.get(i);
			String fileName = fileNameList.get(i);
			zout.putNextEntry(new ZipEntry(fileName));
			PresetImage.writePresetImage(zout, presetImage);
			zout.closeEntry();
		}
	}
}
