/*
 * Copyright (c) 2009, Takao Sumitomo
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the 
 *       above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce
 *       the above copyright notice, this list of
 *       conditions and the following disclaimer in the
 *       documentation and/or other materials provided
 *       with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should
 * not be interpreted as representing official policies,
 * either expressed or implied.
 */
/*
 * $Id: RdbaScriptJavaSourceConverter.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.gui.drawer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.cattaka.jspf.JspfBundle;
import net.cattaka.jspf.JspfEntry;
import net.cattaka.jspf.JspfException;
import net.cattaka.jspf.JspfJavaSourceConverter;
import net.cattaka.mathdrawer.MdConstants;
import net.cattaka.mathdrawer.drawer.custom.CustomDrawerProcess;
import net.cattaka.mathdrawer.jspf.MdJspfBundle;
import net.cattaka.mathdrawer.setting.entity.CustomMdSettingUtil;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringUtil;

public class MdDrawerJavaSourceConverter implements JspfJavaSourceConverter<CustomDrawerProcess> {
	static class BodyPiace {
		private boolean source;
		private String body;
		public BodyPiace(boolean source, String body) {
			super();
			this.source = source;
			this.body = body;
		}
		public boolean isSource() {
			return source;
		}
		public void setSource(boolean source) {
			this.source = source;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
	};
	static class SourceWriter {
		StringBuilder nameParamsOut = new StringBuilder();
		StringBuilder descriptionParamsOut = new StringBuilder();
		StringBuilder defineParamsOut = new StringBuilder();
		StringBuilder importOut = new StringBuilder();
		StringBuilder bodyOut = new StringBuilder();
		StringBuilder extractParamsOut = new StringBuilder();
	};
	
	enum ParseMode {
		BODY,
		CODE_START1,
		CODE,
		CODE_END1
	};
	
	public void convertSourceFile(JspfBundle<CustomDrawerProcess> jspfBundle, JspfEntry<CustomDrawerProcess> jspfEntry) throws JspfException {
		try {
			SourceWriter sourceWriter = new SourceWriter();
			
			// ソースを変換する
			processSourceText(sourceWriter, jspfBundle, jspfEntry.getSourceText());
			
			// ベースを読み込み
			StringBuilder sb = new StringBuilder();
			InputStream scriptBaseIn = this.getClass().getClassLoader().getResourceAsStream(MdConstants.CUSTOM_DRAWER_BASE);
			try {
				Reader reader = new InputStreamReader(scriptBaseIn);
				int r;
				while((r = reader.read()) != -1 ) {
					sb.append((char)r);
				}
				reader.close();
			} finally {
				scriptBaseIn.close();
			}
			
			// Bodyの置換
			StringUtil.replaceString(sb,"/*CLASS_NAME*/", jspfEntry.getClassName());
			StringUtil.replaceString(sb,"/*BODY*/", sourceWriter.bodyOut.toString());
			StringUtil.replaceString(sb,"/*IMPORT*/", sourceWriter.importOut.toString());
			StringUtil.replaceString(sb,"/*DEFINE_PARAMS*/", sourceWriter.nameParamsOut.toString() + sourceWriter.descriptionParamsOut.toString() + sourceWriter.defineParamsOut.toString());
			StringUtil.replaceString(sb,"/*EXTRACT_PARAMS*/", sourceWriter.extractParamsOut.toString());
			BufferedWriter javaWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jspfEntry.getJavaSourceFile()),"UTF-8"));
			try {
				javaWriter.write(sb.toString());
			} finally {
				javaWriter.close();
			}
		} catch (IOException e) {
			throw new JspfException(e);
		}
	}
	
	/**
	 * JSPFソースをスクリプトレットごとに分割する
	 * @param body
	 * @return
	 */
	private List<BodyPiace> splitSourceString(String body) throws JspfException {
		ArrayList<BodyPiace> result = new ArrayList<BodyPiace>();
		boolean sourceMode = false;
		boolean modeChange = false;
		StringBuilder sb = new StringBuilder();
		char lastCh=' ';
		for (int i=0;i<body.length();i++) {
			char ch = (char)body.charAt(i);
			if(sourceMode) {
				if (lastCh == '%') {
					if (ch == '>') {
						if (sb.length() > 0) {
							result.add(new BodyPiace(sourceMode, sb.toString()));
							sb.delete(0, sb.length());
						}
						modeChange = true;
					} else {
						sb.append(lastCh);
						sb.append(ch);
					}
				} else {
					if (ch == '%') {
						// 無し
					} else {
						sb.append(ch);
					}
				}
			} else {
				if (lastCh == '<') {
					if (ch == '%') {
//						if (sb.length() > 0 && sb.charAt(0) == '\n') {
//							sb.delete(0, 1);
//						}
						if (sb.length() > 0 && sb.charAt(sb.length()-1) == '\n') {
							sb.delete(sb.length()-1, sb.length());
						}
						if (sb.length() > 0) {
							result.add(new BodyPiace(sourceMode, sb.toString()));
							sb.delete(0, sb.length());
						}
						modeChange = true;
					} else {
						sb.append(lastCh);
						sb.append(ch);
					}
				} else {
					if (ch == '<') {
						// 無し
					} else {
						sb.append(ch);
					}
				}
			}
			if (modeChange) {
				modeChange = false;
				lastCh = ' ';
				sourceMode = !sourceMode;
			} else {
				lastCh = ch;
			}
		}
		if (sourceMode) {
			throw new JspfException(MessageBundle.getMessage("scriptlet_is_not_closed"));
		}
		if (sb.length() > 0) {
			result.add(new BodyPiace(sourceMode, sb.toString()));
		}
		
		return result;
	}
	
	private void processSourceText(SourceWriter sourceWriter, JspfBundle<CustomDrawerProcess> jspfBundle, String sourceText) throws JspfException {
		List<BodyPiace> bodyPieceList = splitSourceString(sourceText);
		for (BodyPiace bodyPiace:bodyPieceList) {
			processBodyPiace(sourceWriter, jspfBundle, bodyPiace);
		}
	}
	
	private void processBodyPiace(SourceWriter sourceWriter, JspfBundle<CustomDrawerProcess> jspfBundle, BodyPiace bodyPiace) throws JspfException {
		String body = bodyPiace.getBody();
		if (body == null) {
			return;
		}
		if (bodyPiace.isSource()) {
			if (body.length() > 0 && body.charAt(0) == '@') {
				processDirective(sourceWriter, jspfBundle, body.substring(1,body.length()));
			} else if (body.length() > 0 && body.charAt(0) == '=') {
				sourceWriter.bodyOut.append(processOutput(body.substring(1,body.length())));
				sourceWriter.bodyOut.append('\n');
			} else {
				sourceWriter.bodyOut.append(replaceQuotedReturn(body));
				sourceWriter.bodyOut.append('\n');
			}
		} else {
			sourceWriter.bodyOut.append("out.print(\"");
			sourceWriter.bodyOut.append(StringUtil.escapeJavaString(body));
			sourceWriter.bodyOut.append("\");\n");
		}
	}
	
	/**
	 * 与えられたソース内における、ダブルクオート内の改行を\nに置換する
	 * 
	 * @param source
	 * @return
	 */
	private String replaceQuotedReturn(String source) {
		if (source == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean doubleQuoted = false;
		boolean escaped = false;
		for (int i=0;i<source.length();i++) {
			char ch = (char)source.charAt(i);
			if (ch == '"') {
				if (!escaped) {
					doubleQuoted = !doubleQuoted;
				}
			}
			escaped = false;
			if (ch == '\\') {
				escaped = true;
			}
			if (doubleQuoted) {
				if (ch=='\n') {
					sb.append("\\n");
				} else {
					sb.append(ch);
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	/**
	 * ディレクティブを解釈します。
	 * 例：<%@ page import="java.util.ArrayList" %>などを
	 * Java用ソースコードに変換します。
	 * 
	 * @param source
	 * @return
	 */
	private void processDirective(SourceWriter sourceWriter, JspfBundle<CustomDrawerProcess> jspfBundle, String source) throws JspfException {
		List<String[]> attrs = StringUtil.parseAttributeString(source);
		if (attrs == null || attrs.size() == 0) {
			return;
		}
		String[] modeStr = attrs.get(0);
		if ("page".equals(modeStr[0])) {
			for (String[] attr : attrs) {
				if ("import".equals(attr[0])) {
					sourceWriter.importOut.append("import ");
					sourceWriter.importOut.append(attr[1]);
					sourceWriter.importOut.append(";\n");
				} else if ("name".equals(attr[0])) {
					sourceWriter.nameParamsOut.append(String.format("setName(\"%s\\n\");\n", StringUtil.escapeJavaString(attr[1])));
				} else if ("description".equals(attr[0])) {
					sourceWriter.descriptionParamsOut.append(String.format("descSb.append(\"%s\\n\");\n", StringUtil.escapeJavaString(attr[1])));
				}
			}
		} else if ("include".equals(modeStr[0])) {
			for (String[] attr : attrs) {
				if ("file".equals(attr[0])) {
					SourceWriter sw2 = new SourceWriter();
					sw2.defineParamsOut = sourceWriter.defineParamsOut;
					sw2.importOut = sourceWriter.importOut;
					sw2.bodyOut = sourceWriter.bodyOut;
					sw2.extractParamsOut = sourceWriter.extractParamsOut;
					
					String labelName = attr[1];
					String sourceText = getSourceText(jspfBundle, labelName);
					processSourceText(sw2, jspfBundle, sourceText);
				}
			}
		} else if ("param".equals(modeStr[0])) {
			String type = null;
			String var = null;
			String name = null;
			String description = "";
			String defaultValue = null;
			for (String[] attr : attrs) {
				if ("param".equals(attr[0])) {
					// 無視
				} else if ("type".equals(attr[0])) {
					type = attr[1];
				} else if ("var".equals(attr[0])) {
					var = attr[1];
				} else if ("name".equals(attr[0])) {
					name = attr[1];
				} else if ("description".equals(attr[0])) {
					description = attr[1];
				} else if ("default".equals(attr[0])) {
					defaultValue = attr[1];
				} else {
					String message = String.format(MessageBundle.getMessage("this_is_invalid_parameter_name"), "param", attr[0]);
					throw new JspfException(message);
				}
			}
			if (type == null) {
				String message = String.format(MessageBundle.getMessage("this_parameter_is_required"),"param", "type");
				throw new JspfException(message);
			}
			if (name == null) {
				String message = String.format(MessageBundle.getMessage("this_parameter_is_required"),"param", "name");
				throw new JspfException(message);
			}
			if (!Arrays.asList(CustomMdSettingUtil.AVAILABLE_TYPES).contains(type)) {
				String message = String.format(MessageBundle.getMessage("this_is_invalid_parameter_value"),"param", "type", type);
				throw new JspfException(message);
			}
			if (defaultValue != null) {
				sourceWriter.defineParamsOut.append(String.format("getCustomMdSettingUtil().addSettingConstraintsInfo(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\");\n", var, type, name, description, defaultValue));
				sourceWriter.extractParamsOut.append(String.format("%s %s = %s.valueOf(\"%s\");\n", type, var, type, defaultValue));
			} else {
				sourceWriter.defineParamsOut.append(String.format("getCustomMdSettingUtil().addSettingConstraintsInfo(\"%s\", \"%s\", \"%s\", \"%s\", null);\n", var, type, name, description));
				sourceWriter.extractParamsOut.append(String.format("%s %s = null;\n", type, var));
			}
			sourceWriter.extractParamsOut.append(String.format("%s = (get%s(\"%s\") != null) ? get%s(\"%s\") : %s;\n", var, type, var, type, var, var));
		}
	}
	private String getSourceText(JspfBundle<CustomDrawerProcess> jspfBundle, String labelName) throws JspfException {
		StringBuilder sb = new StringBuilder();
		MdJspfBundle<CustomDrawerProcess> mdJspfBundle = (MdJspfBundle<CustomDrawerProcess>)jspfBundle;
		String fileName = mdJspfBundle.getScriptDir().getAbsolutePath() + File.separatorChar + labelName; 
		File file = new File(fileName);
		Reader reader = null;
		try {
			reader = new FileReader(file);
			int r = 0;
			while((r=reader.read()) != -1) {
				sb.append((char)r);
			}
		} catch (IOException e) {
			throw new JspfException(String.format(MessageBundle.getMessage("could_not_open_file"), fileName));
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// ありえない
					ExceptionHandler.warn(e);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 出力スクリプトを解釈します。
	 * 例：<%=arg%>などをJava用ソースコードに変換します。
	 * 
	 * @param source
	 * @return
	 */
	private String processOutput(String source) {
		return "out.print(String.valueOf("+source+"));";
	}
}
