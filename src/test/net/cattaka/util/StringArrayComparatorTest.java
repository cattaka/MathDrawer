package net.cattaka.util;

import java.util.TreeSet;

import junit.framework.TestCase;

public class StringArrayComparatorTest extends TestCase {
	public void testCompare() {
		TreeSet<String[]> ts = new TreeSet<String[]>(new StringArrayComparator());
		ts.add(new String[]{"2"});
		ts.add(new String[]{"a","b","c"});
		ts.add(new String[]{"a","b"});
		ts.add(new String[]{"1"});
		ts.add(new String[]{"a","b","c"});
		ts.add(new String[]{"a","b"});
		ts.add(new String[]{null,"b","c"});
		for (String[] sa : ts) {
			for (String s:sa) {
				System.out.print(s);
				System.out.print(",");
			}
			System.out.println();
		}
	}
}
