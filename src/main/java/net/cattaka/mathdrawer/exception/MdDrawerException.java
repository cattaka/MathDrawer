package net.cattaka.mathdrawer.exception;

public class MdDrawerException extends MdException {
	private static final long serialVersionUID = 1L;
	private String contents;
	
	public MdDrawerException() {
		super();
	}

	public MdDrawerException(String message, Throwable cause) {
		super(message, cause);
	}

	public MdDrawerException(String message) {
		super(message);
	}

	public MdDrawerException(Throwable cause) {
		super(cause);
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
