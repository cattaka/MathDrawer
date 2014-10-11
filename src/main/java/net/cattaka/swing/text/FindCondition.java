package net.cattaka.swing.text;

public class FindCondition {
	private String search="";
	private String replace="";
	private boolean downward = true;
	private boolean senseCaseSearch = false;
	private boolean loopSearch = false;
	private boolean wordUnitSearch = false;
	private boolean regexSearch = false;
	private ACTION action = ACTION.FIND;
	
	public enum ACTION {
		FIND,
		REPLACE,
		REPLACE_FIND,
		REPLACE_ALL
	};
	
	public FindCondition() {
	}

	public FindCondition(FindCondition orig) {
		this.search = orig.getSearch();
		this.replace = orig.getReplace();
		this.downward = orig.isDownward();
		this.senseCaseSearch = orig.isSenseCaseSearch();
		this.loopSearch = orig.loopSearch;
		this.wordUnitSearch = orig.isWordUnitSearch();
		this.regexSearch = orig.regexSearch;
		this.action = orig.getAction();
	}

	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public boolean isDownward() {
		return downward;
	}
	public void setDownward(boolean downward) {
		this.downward = downward;
	}
	public boolean isSenseCaseSearch() {
		return senseCaseSearch;
	}
	public void setSenseCaseSearch(boolean senseCaseSearch) {
		this.senseCaseSearch = senseCaseSearch;
	}
	public boolean isLoopSearch() {
		return loopSearch;
	}
	public void setLoopSearch(boolean loopSearch) {
		this.loopSearch = loopSearch;
	}
	public boolean isWordUnitSearch() {
		return wordUnitSearch;
	}
	public void setWordUnitSearch(boolean wordUnitSearch) {
		this.wordUnitSearch = wordUnitSearch;
	}
	public boolean isRegexSearch() {
		return regexSearch;
	}
	public void setRegexSearch(boolean regexSearch) {
		this.regexSearch = regexSearch;
	}
	public ACTION getAction() {
		return action;
	}
	public void setAction(ACTION action) {
		this.action = action;
	}
}
