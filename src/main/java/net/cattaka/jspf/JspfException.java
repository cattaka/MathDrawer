package net.cattaka.jspf;

public class JspfException extends Exception {
	private static final long serialVersionUID = 1L;
	private String contents;

	public JspfException() {
		super();
	}

	public JspfException(String message, Throwable cause) {
		super(message, cause);
	}

	public JspfException(String message) {
		super(message);
	}

	public JspfException(Throwable cause) {
		super(cause);
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
}
