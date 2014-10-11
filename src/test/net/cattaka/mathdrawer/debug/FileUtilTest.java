package net.cattaka.mathdrawer.debug;

import java.io.File;
import java.util.List;

import net.cattaka.util.FileUtil;

public class FileUtilTest {
	public static void main(String[] args) {
		File curDir = new File(".");
		List<String> fileList = FileUtil.getAllFileNameList(curDir, 5, ".ps");
		
		for (String file:fileList) {
			System.out.println(file);
		}
	}
}
