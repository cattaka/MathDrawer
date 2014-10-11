package net.cattaka.mathdrawer.exception;

public class MdException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String contents;
	
	public MdException() {
		super();
	}

	public MdException(String message, Throwable cause) {
		super(message, cause);
	}

	public MdException(String message) {
		super(message);
	}

	public MdException(Throwable cause) {
		super(cause);
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
