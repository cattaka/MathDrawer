import java.util.HashSet;

import net.cattaka.util.OrderedSet;


public class TestOrderedSet {
	public static void main(String[] args) {
		OrderedSet<String> os = new OrderedSet<String>(new HashSet<String>());
		os.add("a");
		os.add("b");
		os.add("a");
		os.add("c");
		os.add("d");
		os.add("c");
		os.remove("b");
		System.out.println("-----");
		for(String str:os) {
			System.out.println(str);
		}
		System.out.println("-----");
		for(String str:os.getValueList()) {
			System.out.println(str);
		}
	}
}
