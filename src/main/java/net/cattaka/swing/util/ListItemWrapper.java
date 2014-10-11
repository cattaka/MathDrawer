package net.cattaka.swing.util;

public class ListItemWrapper<S> {
	private String name;
	private S data;
	public ListItemWrapper(String name, S data) {
		super();
		this.name = name;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}

	public S getData() {
		return data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setData(S data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return name;
	}
}
