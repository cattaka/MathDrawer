package net.cattaka.mathdrawer.debug;

import net.cattaka.mathdrawer.entity.Vector2d;
import net.cattaka.mathdrawer.entity.Vector3d;
import net.cattaka.mathdrawer.entity.Vector4d;

public class MdMathDebug {
	public static void main(String[] args) {
		System.out.println(Vector2d.valueOf("(12,56)"));
		System.out.println(Vector2d.valueOf("(12.34,56.78)"));
		System.out.println(Vector2d.valueOf("(0.34,0.78)"));
		System.out.println(Vector3d.valueOf("(0.34,0.78, -8)"));
		System.out.println(Vector4d.valueOf("(0.34,0.78, 8, 9)"));
	}
}
