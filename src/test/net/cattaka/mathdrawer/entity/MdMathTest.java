package net.cattaka.mathdrawer.entity;

import junit.framework.TestCase;

public class MdMathTest extends TestCase {
	public void testOnBlock() {
		Vector2d l1 = new Vector2d(2,0);
		Vector2d l2 = new Vector2d(0,2);
		assertEquals(MdMath.checkOnBlock(l1, l2, new Vector2d(2.1,0.0)), -1);
		assertEquals(MdMath.checkOnBlock(l1, l2, new Vector2d(2.0,2.0)), 0);
		assertEquals(MdMath.checkOnBlock(l1, l2, new Vector2d(0.0,2.1)), 1);
	}
	public void testCalcDistance() {
		{
			Vector3d f1 = new Vector3d(1,-2, 2);
			Vector3d f2 = new Vector3d(2,-1,-2);
			Vector2d dest = new Vector2d();
			MdMath.getIntersection(dest, f1, f2);
			// result must be (2,2)
			System.out.println(dest);
		}
		{
			Vector3d f1 = new Vector3d(-1,-2, 2);
			Vector3d f2 = new Vector3d(-2,-1,-2);
			Vector2d dest = new Vector2d();
			MdMath.getIntersection(dest, f1, f2);
			// result must be (-2,2)
			System.out.println(dest);
		}
		{
			Vector2d l1 = new Vector2d(2,0);
			Vector2d l2 = new Vector2d(0,2);
			double dist1 = MdMath.calcFromLine(new Vector2d(2,2), l1, l2);
			// result must be sqrt(2)
			System.out.println(dist1);

			double dist2 = MdMath.calcFromLine(new Vector2d(0,0), l1, l2);
			// result must be sqrt(2)
			System.out.println(dist2);
		}
	}

}
