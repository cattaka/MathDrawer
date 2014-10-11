package net.cattaka.mathdrawer.setting.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.cattaka.mathdrawer.drawer.RootDrawer;
import net.cattaka.mathdrawer.entity.PresetImage;
import net.cattaka.mathdrawer.entity.PresetImage.ImageType;
import junit.framework.TestCase;

public class MdProjectTest extends TestCase {
	public void testReadWriteMdProject() throws Exception {
		MdProject mdProject = new MdProject();
		{
			RootDrawer rootDrawer = new RootDrawer();
			rootDrawer.getRootMdSetting().setWidth(1024);
			rootDrawer.getRootMdSetting().setHeight(512);
			
			PresetImageList presetImageList = new PresetImageList();

			BufferedImage image1 = new BufferedImage(200,300,BufferedImage.TYPE_INT_ARGB);
			BufferedImage image2 = new BufferedImage(200,300,BufferedImage.TYPE_INT_RGB);
			{
				Graphics2D g = image1.createGraphics();
				g.setColor(Color.BLACK);
				g.drawString("testImage1", 50, 50);
				g.dispose();
			}
			{
				Graphics2D g = image2.createGraphics();
				g.setColor(Color.BLACK);
				g.drawString("testImage1", 50, 50);
				g.dispose();
			}
			
			PresetImage presetImage1 = new PresetImage();
			PresetImage presetImage2 = new PresetImage();
			presetImage1.setName("testImage1");
			presetImage2.setName("testImage1");
			presetImage1.setBufferedImage(image1);
			presetImage2.setBufferedImage(image2);
			presetImage1.setImageType(ImageType.PNG);
			presetImage2.setImageType(ImageType.BMP);
			
			presetImageList.add(presetImage1);
			presetImageList.add(presetImage2);
			
			mdProject.setRootDrawer(rootDrawer);
			mdProject.setPresetImageList(presetImageList);
		}
		
//		File testFile = new File("test.zip");
		File testFile = File.createTempFile("test", "zip");
		FileOutputStream fout = new FileOutputStream(testFile);
		ZipOutputStream zout = new ZipOutputStream(fout);
		MdProject.writeMdProject(zout, mdProject);
		zout.finish();
		zout.flush();
		fout.close();
		
		FileInputStream fin = new FileInputStream(testFile);
		ZipInputStream zin = new ZipInputStream(fin);
		MdProject mdProject2 = MdProject.readMdProject(zin);
		
		{
			RootDrawer rootDrawer = mdProject.getRootDrawer();
			RootDrawer rootDrawer2 = mdProject2.getRootDrawer();
			Map<String,Object> values = rootDrawer.getMdSetting().getValues();
			Map<String,Object> values2 = rootDrawer2.getMdSetting().getValues();
			assertEquals(values.size(),values2.size());
			for (Entry<String,Object> entry:values.entrySet()) {
				assertEquals(entry.getValue(), values2.get(entry.getKey()));
			}
			
			PresetImageList presetImageList = mdProject.getPresetImageList();
			PresetImageList presetImageList2 = mdProject2.getPresetImageList();
			assertEquals(presetImageList.size(), presetImageList2.size());
			for (int i=0;i<presetImageList.size();i++) {
				PresetImage pi1 = presetImageList.get(i);
				PresetImage pi2 = presetImageList.get(i);
				assertEquals(pi1.getName(), pi2.getName());
				assertEquals(pi1.getImageType(), pi2.getImageType());
			}
		}
	}
}
