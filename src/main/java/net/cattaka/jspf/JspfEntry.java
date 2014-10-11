package net.cattaka.jspf;

import java.io.File;

public class JspfEntry<T> {
	private String sourceText;
	private File javaSourceFile;
	private Class<T> jspfClass;
	private String labelName;
	private String className;

	public String getSourceText() {
		return sourceText;
	}
	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}
	public File getJavaSourceFile() {
		return javaSourceFile;
	}
	public void setJavaSourceFile(File javaSourceFile) {
		this.javaSourceFile = javaSourceFile;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Class<T> getJspfClass() {
		return jspfClass;
	}
	public void setJspfClass(Class<T> jspfClass) {
		this.jspfClass = jspfClass;
	}
}
