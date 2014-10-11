package net.cattaka.jspf;

public interface JspfJavaSourceConverter<T> {
	public void convertSourceFile(JspfBundle<T> jspfBundle, JspfEntry<T> jspfEntry) throws JspfException;
}
