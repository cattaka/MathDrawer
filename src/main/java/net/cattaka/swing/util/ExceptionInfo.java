package net.cattaka.swing.util;

import java.util.Date;

public class ExceptionInfo {
	private java.util.Date timestamp;
	private String type;
	private String message;
	private String stackTrace;

	public ExceptionInfo(Date timestamp, String type, String message, String stackTrace) {
		super();
		this.timestamp = timestamp;
		this.type = type;
		this.message = message;
		this.stackTrace = stackTrace;
	}

	public java.util.Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(java.util.Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
}
