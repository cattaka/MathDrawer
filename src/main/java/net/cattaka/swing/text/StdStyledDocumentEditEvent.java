package net.cattaka.swing.text;

public class StdStyledDocumentEditEvent {
	private int offset;
	private String before;
	private String after;
	private EVENT_TYPE eventType;
	
	public enum EVENT_TYPE {
		INSERT,
		REMOVE,
		REPLACE,
		SEPALATOR
	};
	
	public StdStyledDocumentEditEvent() {
		this.eventType = EVENT_TYPE.SEPALATOR;
	}
	
	public StdStyledDocumentEditEvent(int offset, String before, String after) {
		super();
		this.offset = offset;
		this.before = before;
		this.after = after;
		
		if (before == null || before.length() == 0) {
			if (after.length() == 0) {
				this.eventType = EVENT_TYPE.SEPALATOR;
			} else {
				this.eventType = EVENT_TYPE.INSERT;
			}
		} else {
			if (after == null || after.length() == 0) {
				this.eventType = EVENT_TYPE.REMOVE;
			} else {
				this.eventType = EVENT_TYPE.REPLACE;
			}
		}
	}
	public int getOffset() {
		return offset;
	}
	public String getBefore() {
		return before;
	}
	public String getAfter() {
		return after;
	}
	public EVENT_TYPE getEventType() {
		return eventType;
	}
	@Override
	public String toString() {
		return "[" +
				offset + ", " +
				before + ", " +
				after + "]";
	}
}
