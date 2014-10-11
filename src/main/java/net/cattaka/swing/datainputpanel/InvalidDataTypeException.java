/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

public class InvalidDataTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidDataTypeException() {
		super();
	}

	public InvalidDataTypeException(String message) {
		super(message);
	}

	public InvalidDataTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDataTypeException(Throwable cause) {
		super(cause);
	}

}
