package net.cattaka.mathdrawer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.cattaka.jspf.ToolsJarBundle;
import net.cattaka.mathdrawer.config.MdConfig;
import net.cattaka.mathdrawer.config.MdConfigUtil;
import net.cattaka.mathdrawer.drawer.custom.CustomPixelShader;
import net.cattaka.mathdrawer.gui.MathDrawerPanel;
import net.cattaka.mathdrawer.gui.pixelshader.CustomPixelShaderRunner;
import net.cattaka.mathdrawer.jspf.MdJspfBundle;
import net.cattaka.util.BufferedImageUtil;
import net.cattaka.util.PrintWriterEx;
import net.cattaka.util.StringUtil;

public class DummyRunner {
	public static void main(String[] args) throws Exception {
//		doFlush01();
		doFlush02();
//		doFlush03();
//		doFlower01();
//		doFlower02();
//		doHull01();
//		doLump01();
//		doRadiate01();
		
		System.exit(0);
	}
	public static void doFlower01() throws Exception {
		String dirName = "dummy/flower01";
		String labelName = "flower/flower01.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] waveTypes = new int[]{0,1,2};
			int[] numOfWaves = new int[]{4,8,16,32};
			int[] steps = new int[]{1,2,4,8};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int waveType:waveTypes) {
				for (int numOfWave:numOfWaves) {
					for (int step:steps) {
						settingValues.put("n", numOfWave);
						settingValues.put("step", step);
						settingValues.put("waveType", waveType);
						customPixelShader.setSettingValues(settingValues);
						BufferedImageUtil.clearImage(runner.getCachedImage());
						runner.draw(baseImage);
						
						String fileName = String.format("flower01_t%dn%02ds%02d.png", waveType, numOfWave, step);
						File outFile = new File(dirName+"/"+fileName);
						ImageIO.write(runner.getCachedImage(), "PNG", outFile);
						System.out.println("Finished : " + outFile.getAbsolutePath());
					}
				}
			}
		}
	}
	public static void doFlower02() throws Exception {
		String dirName = "dummy/flower02";
		String labelName = "flower/flower02.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] waveTypes = new int[]{0,1,2};
			int[] numOfWaves = new int[]{2,4,8,16};
			int[] steps = new int[]{1,2,4,8};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int waveType:waveTypes) {
				for (int numOfWave:numOfWaves) {
					for (int step:steps) {
						settingValues.put("n", numOfWave);
						settingValues.put("step", step);
						settingValues.put("waveType", waveType);
						customPixelShader.setSettingValues(settingValues);
						BufferedImageUtil.clearImage(runner.getCachedImage());
						runner.draw(baseImage);
						
						String fileName = String.format("flower02_t%dn%02ds%02d.png", waveType, numOfWave, step);
						File outFile = new File(dirName+"/"+fileName);
						ImageIO.write(runner.getCachedImage(), "PNG", outFile);
						System.out.println("Finished : " + outFile.getAbsolutePath());
					}
				}
			}
		}
	}

	public static void doFlush01() throws Exception {
		String dirName = "dummy/flush01";
		String labelName = "flush/flush01.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] numOfWaves = new int[]{2,4,8,16};
			int[] waveTypes = new int[]{0,1,2};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int numOfWave:numOfWaves) {
				for (int waveType:waveTypes) {
					settingValues.put("n", numOfWave);
					settingValues.put("waveType", waveType);
					customPixelShader.setSettingValues(settingValues);
					BufferedImageUtil.clearImage(runner.getCachedImage());
					runner.draw(baseImage);
					
					String fileName = String.format("flush01_t%dn%02d.png", waveType, numOfWave);
					File outFile = new File(dirName+"/"+fileName);
					ImageIO.write(runner.getCachedImage(), "PNG", outFile);
					System.out.println("Finished : " + outFile.getAbsolutePath());
				}
			}
		}
	}

	public static void doFlush02() throws Exception {
		String dirName = "dummy/flush02";
		String labelName = "flush/flush02.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] numOfWaves = new int[]{2,4,8,16};
			double[] pows = new double[]{1,2,3,4,5,6,7,8};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int numOfWave:numOfWaves) {
				for (double pow:pows) {
					settingValues.put("wave", 0.3d);
					settingValues.put("width", 0.1d);
					settingValues.put("n", numOfWave);
					settingValues.put("pow", pow);
					customPixelShader.setSettingValues(settingValues);
					BufferedImageUtil.clearImage(runner.getCachedImage());
					runner.draw(baseImage);
					
					String fileName = String.format("flush02_n%02dp%d.png", numOfWave, (int)pow);
					File outFile = new File(dirName+"/"+fileName);
					ImageIO.write(runner.getCachedImage(), "PNG", outFile);
					System.out.println("Finished : " + outFile.getAbsolutePath());
				}
			}
		}
	}

	public static void doFlush03() throws Exception {
		String dirName = "dummy/flush03";
		String labelName = "flush/flush03.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] waveTypes = new int[]{0,1,2};
			int[] counts = new int[]{1,2,4,8,16};
			int[] mods = new int[]{8,16,32,64};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int waveType:waveTypes) {
				for (int count:counts) {
					for (int mod:mods) {
						settingValues.put("waveType", waveType);
						settingValues.put("count", count);
						settingValues.put("mod", mod);
						customPixelShader.setSettingValues(settingValues);
						BufferedImageUtil.clearImage(runner.getCachedImage());
						runner.draw(baseImage);
						
						String fileName = String.format("flush03_t%dc%02dm%02d.png", waveType, count, mod);
						File outFile = new File(dirName+"/"+fileName);
						ImageIO.write(runner.getCachedImage(), "PNG", outFile);
						System.out.println("Finished : " + outFile.getAbsolutePath());
					}
				}
			}
		}
	}

	public static void doLump01() throws Exception {
		String dirName = "dummy/lump01";
		String labelName = "lump/lump01.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] waveTypes = new int[]{0,1,2};
			int[] numOfWaves = new int[]{2,4,8,16};
			double[] widths = new double[]{0.1, 0.2, 0.3};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int waveType:waveTypes) {
				for (int numOfWave:numOfWaves) {
					for (double width:widths) {
						settingValues.put("waveType", waveType);
						settingValues.put("n", numOfWave);
						settingValues.put("radius", 0.4d - width);
						settingValues.put("wave", width);
						settingValues.put("width", 0.1d);
						customPixelShader.setSettingValues(settingValues);
						BufferedImageUtil.clearImage(runner.getCachedImage());
						runner.draw(baseImage);
						
						String fileName = String.format("lump01_t%dw%02dn%02d.png", waveType, (int)(width*10), numOfWave);
						File outFile = new File(dirName+"/"+fileName);
						ImageIO.write(runner.getCachedImage(), "PNG", outFile);
						System.out.println("Finished : " + outFile.getAbsolutePath());
					}
				}
			}
		}
	}
	public static void doHull01() throws Exception {
		String dirName = "dummy/hull01";
		String labelName = "hull/hull01.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] waveTypes = new int[]{0,1,2};
			int[] numOfWaves = new int[]{2,4,8,16};
			double[] waves = new double[]{0.1, 0.2, 0.3};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int waveType:waveTypes) {
				for (int numOfWave:numOfWaves) {
					for (double wave:waves) {
						settingValues.put("waveType", waveType);
						settingValues.put("n", numOfWave);
						settingValues.put("radius", 0.4d - wave);
						settingValues.put("wave", wave);
						settingValues.put("outerWidth", 0.05d);
						customPixelShader.setSettingValues(settingValues);
						BufferedImageUtil.clearImage(runner.getCachedImage());
						runner.draw(baseImage);
						
						String fileName = String.format("impulse02_t%dw%02dn%02d.png", waveType, (int)(wave*10), numOfWave);
						File outFile = new File(dirName+"/"+fileName);
						ImageIO.write(runner.getCachedImage(), "PNG", outFile);
						System.out.println("Finished : " + outFile.getAbsolutePath());
					}
				}
			}
		}
	}
	public static void doRadiate01() throws Exception {
		String dirName = "dummy/radiate01";
		String labelName = "radiate/radiate01.ps";
		CustomPixelShader customPixelShader = createCustomPixelShader(labelName);
		CustomPixelShaderRunner runner = createCustomPixelShaderRunner(customPixelShader);
		BufferedImage baseImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
		BufferedImageUtil.fillImage(baseImage, Color.WHITE);

		File outDir = new File(dirName);
		outDir.mkdir();
		// ここから個別処理
		{
			int[] waveTypes = new int[]{0,1,2,3,4,5,6};
			int[] numOfWaves = new int[]{8,16,32};
			double[] startRadiuses = new double[]{0.0, 0.20};
			double[] scales = new double[]{1.0, 2.0};
			
			HashMap<String,Object> settingValues = new HashMap<String, Object>();
			for (int waveType:waveTypes) {
				for (int numOfWave:numOfWaves) {
					for (double startRadius:startRadiuses) {
						for (double scale:scales) {
							settingValues.put("waveType", waveType);
							settingValues.put("n", numOfWave);
							settingValues.put("startRadius", startRadius);
							settingValues.put("scale", scale);
							customPixelShader.setSettingValues(settingValues);
							BufferedImageUtil.clearImage(runner.getCachedImage());
							runner.draw(baseImage);
							
							String fileName = String.format("radiate02_t%dn%02db%02ds%02d.png", waveType, numOfWave, (int)(startRadius*10), (int)scale);
							File outFile = new File(dirName+"/"+fileName);
							ImageIO.write(runner.getCachedImage(), "PNG", outFile);
							System.out.println("Finished : " + outFile.getAbsolutePath());
						}
					}
				}
			}
		}
	}

	private static CustomPixelShader createCustomPixelShader(String labelName) throws Exception {
		MdConfig mdConfig = MdConfigUtil.loadMdConfig();
		MathDrawerPanel mathDrawerPanel = new MathDrawerPanel(mdConfig);
		
		CustomPixelShader customPixelShader = null;
		StringWriter sw = new StringWriter();
		PrintWriterEx out = new PrintWriterEx(sw);
		{
			ToolsJarBundle toolsJarBundle = mathDrawerPanel.getMdConfig().getToolsJarBundle();
			MdJspfBundle<CustomPixelShader> customPixelShaderBundle = mathDrawerPanel.getMdSingletonBundle().getCustomPixelShaderBundle();
			
			File targetFile = new File(customPixelShaderBundle.getScriptDir().getAbsoluteFile() + File.separator + labelName);
			String sourceText = StringUtil.readFile(targetFile, "UTF8");
			customPixelShaderBundle.compile(toolsJarBundle, labelName, sourceText, out);
			customPixelShader = customPixelShaderBundle.getNewInstance(labelName); 
		}
		
		return customPixelShader;
	}
	
	private static CustomPixelShaderRunner createCustomPixelShaderRunner(CustomPixelShader customPixelShader) throws IOException {
		BufferedImage cachedImage = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);

		StringWriter sw = new StringWriter();
		PrintWriterEx out = new PrintWriterEx(sw);

		CustomPixelShaderRunner runner = new CustomPixelShaderRunner();
		runner.setCustomPixelShader(customPixelShader);
		runner.setOut(out);
		runner.setCachedImage(cachedImage);
		runner.setDebug(false);
		
		return runner;
	}
}
