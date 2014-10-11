package net.cattaka.swing.util;

import java.util.Comparator;

public class KeyStringArrayComparator implements Comparator<String[]> {
	int[] indicesOfKeys;
	
	public KeyStringArrayComparator(int[] indicesOfKeys) {
		this.indicesOfKeys = new int[indicesOfKeys.length];
		System.arraycopy(indicesOfKeys, 0, this.indicesOfKeys, 0, this.indicesOfKeys.length);
	}
	
	public int compare(String[] ssc1, String[] ssc2) {
		for (int idx:indicesOfKeys) {
			String str1 = ssc1[idx];
			String str2 = ssc2[idx];
			if (str1 == null && str2 == null) {
				continue;
			} else if (str1 != null && str2 == null) {
				return -1;
			} else if (str1 == null && str2 != null) {
				return 1;
			} else {
				int r = str1.compareTo(str2);
				if (r == 0) {
					continue;
				} else {
					return r;
				}
			}
		}
		return 0;
	}

}
