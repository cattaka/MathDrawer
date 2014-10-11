package net.cattaka.swing.table;

import java.util.Comparator;


public class StdStyledCellArrayComparator implements Comparator<StdStyledCell[]> {
	private int[] indicesOfKeys;
	private boolean descending;
	
	public StdStyledCellArrayComparator(int[] indicesOfKeys, boolean descending) {
		this(indicesOfKeys);
		this.descending = descending;
	}
	
	public StdStyledCellArrayComparator(int[] indicesOfKeys) {
		this.indicesOfKeys = new int[indicesOfKeys.length];
		System.arraycopy(indicesOfKeys, 0, this.indicesOfKeys, 0, this.indicesOfKeys.length);
	}
	
	public int compare(StdStyledCell[] ssc1, StdStyledCell[] ssc2) {
		int result = 0;
		if (ssc1 != null && ssc2 == null) {
			result = -1;
		} else if (ssc1 == null && ssc2 != null) {
			result = 1;
		} else {
			for (int idx:indicesOfKeys) {
				StdStyledCell cm1 = ssc1[idx];
				StdStyledCell cm2 = ssc2[idx];
				String str1 = (cm1 != null) ? cm1.getValue() : null;
				String str2 = (cm2 != null) ? cm2.getValue() : null;
				if (str1 == null && str2 == null) {
					continue;
				} else if (str1 != null && str2 == null) {
					result = -1;
					break;
				} else if (str1 == null && str2 != null) {
					result = 1;
					break;
				} else {
					int r = str1.compareTo(str2);
					if (r == 0) {
						continue;
					} else {
						result = r;
						break;
					}
				}
			}
		}
		if (descending) {
			result = -result;
		}
		return result;
	}

}
