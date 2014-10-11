/*
 * Copyright 2009 Takao Sumitomo. All rights reserved.
 * 
 * Redistribution and use in source and binary forms,
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the 
 *      above copyright notice, this list of conditions
 *      and the following disclaimer.
 *   2. Redistributions in binary form must reproduce 
 *      the above copyright notice, this list of conditions
 *      and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should not be
 * interpreted as representing official policies,
 * either expressed or implied.
 * 
 * @Author: Takao Sumitomo
 */

/*
 * 作成日: 2005/01/14
 */
package net.cattaka.mathdrawer.entity;


/**
 * @author cattaka ベクトルの各種演算を行うためのクラス。
 */
public class MdMath {
	
	public static String NUMBER_FORMAT_REGEX = "[\\+-]?((\\.[^\\(,\\)]+)|([^\\(,\\)]+(\\.[^\\(,\\)]+)?))";
	
	/**
	 * 入力値を[0.0, 1.0)の範囲に変換します。
	 * @param d
	 * @return
	 */
	public static double cyclicDouble(double d) {
		double result = d % 1.0;
		if (result < 0) {
			result +=1.0;
		}
		return result;
	}
	/**
	 * 入力値を[0.0, 1.0]の範囲に変換します。
	 * @param d
	 * @return
	 */
	public static double limitDouble(double d) {
		double result = d;
		if (result < 0) {
			result = 0;
		} else if (result > 1.0) {
			result = 1.0;
		}
		return result;
	}

	/**
	 * a1とa2の線分の内部にbが存在するか調べる。 a1==bまたはa2==bならばfalseを返す。
	 * 
	 * @return a1とa2の線分の内部にbが存在するならばtrue、そうでなければfalse。
	 */
	public static boolean isOnLineInner(Vector2d a1, Vector2d a2, Vector2d b) {
		return (a1.x - b.x) * (a2.x - b.x) + (a1.y - b.y) * (a2.y - b.y) < 0;
	}

	/**
	 * ２つのColor4dを補完して新しい色を作成します。
	 * 
	 * @param out
	 * @param rate
	 * @param c1
	 * @param c2
	 */
	public static void interpolateColor4d(Color4d out, double rate, Color4d c1, Color4d c2) {
		double a1 = c1.a;
		double r1 = c1.b;
		double g1 = c1.c;
		double b1 = c1.d;
		double a2 = c2.a;
		double r2 = c2.b;
		double g2 = c2.c;
		double b2 = c2.d;
		
		double d2 = limitDouble(rate);
		double d1 = 1.0 - d2;
		out.a = d1 * a1 + d2 * a2;
		out.b = d1 * r1 + d2 * r2;
		out.c = d1 * g1 + d2 * g2;
		out.d = d1 * b1 + d2 * b2;
	}
	
	/**
	 * a1とa2の線分上にbが存在するか調べる。 a1==bまたはa2==bならばtrueを返す。
	 * 
	 * @return a1とa2の線分上にbが存在するならばtrue、そうでなければfalse。
	 */
	public static boolean isOnLine(Vector2d a1, Vector2d a2, Vector2d b) {
		return (a1.x - b.x) * (a2.x - b.x) + (a1.y - b.y) * (a2.y - b.y) <= 0;
	}

	/**
	 * a1とa2の線分の内部にbが存在するか調べる。 a1==bまたはa2==bならばfalseを返す。
	 * 
	 * @return a1とa2の線分の内部にbが存在するならばtrue、そうでなければfalse。
	 */
	public static boolean isOnLineInner(Vector3d a1, Vector3d a2, Vector3d b) {
		return (a1.x - b.x) * (a2.x - b.x) + (a1.y - b.y) * (a2.y - b.y)
				+ (a1.z - b.z) * (a2.z - b.z) < 0;
	}

	/**
	 * a1とa2の間にbが存在するか調べる。 a1==bまたはa2==bならばtrueを返す。
	 * 
	 * @return a1より外側なら-1,a2より外側なら1,それ以外は0を返す。
	 */
	public static int checkOnBlock(Vector2d a1, Vector2d a2, Vector2d b) {
		if ((b.x - a1.x) * (a2.x - a1.x) + (b.y - a1.y) * (a2.y - a1.y) < 0) {
			return -1;
		} else if ((b.x - a2.x) * (a1.x - a2.x) + (b.y - a2.y) * (a1.y - a2.y) < 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * a1とa2の間の内部にbが存在するか調べる。 a1==bまたはa2==bならばfalseを返す。
	 * 
	 * @return a1より外側なら-1,a2より外側なら1,それ以外は0を返す。
	 */
	public static int checkOnBlockInner(Vector3d a1, Vector3d a2, Vector3d b) {
		if ((b.x - a1.x) * (a2.x - a1.x) + (b.y - a1.y) * (a2.y - a1.y) <= 0) {
			return -1;
		} else if ((b.x - a2.x) * (a1.x - a2.x) + (b.y - a2.y) * (a1.y - a2.y) <= 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * a1とa2の線分上にbが存在するか調べる。 a1==bまたはa2==bならばtrueを返す。
	 * 
	 * @return a1とa2の線分上にbが存在するならばtrue、そうでなければfalse。
	 */
	public static boolean isOnLine(Vector3d a1, Vector3d a2, Vector3d b) {
		return (a1.x - b.x) * (a2.x - b.x) + (a1.y - b.y) * (a2.y - b.y)
				+ (a1.z - b.z) * (a2.z - b.z) <= 0;
	}

	/**
	 * 線分p11-p12の内部と線分p21-p22の内部（両端を含まない）の共有点を求める。
	 * 
	 * @param dest
	 *            交点の出力先
	 * @param p11
	 * @param p12
	 * @param p21
	 * @param p22
	 * @return 共有点が存在するならばtrue。そうでなければfalse。
	 */
	public static boolean isIntersectInner(Vector2d dest, Vector2d p11,
			Vector2d p12, Vector2d p21, Vector2d p22) {
		double a1, a2, b1, b2, f;
		f = p11.x * p12.y - p12.x * p11.y;
		if (f == 0)
			return false;
		a1 = -(p12.y - p11.y) / f;
		b1 = (p12.x - p11.x) / f;

		f = p21.x * p22.y - p22.x * p21.y;
		if (f == 0)
			return false;
		a2 = -(p22.y - p21.y) / f;
		b2 = (p22.x - p21.x) / f;

		f = a1 * b2 - a2 * b1;
		if (f == 0)
			return false;
		dest.x = -(b2 - b1) / f;
		dest.y = (a2 - a1) / f;

		return (isOnLineInner(p11, p12, dest) && isOnLineInner(p21, p22, dest));
	}

	/**
	 * 線分p11-p12と線分p21-p22（両端を含む）の共有点を求める。
	 * 
	 * @param dest
	 *            交点の出力先
	 * @param p11
	 * @param p12
	 * @param p21
	 * @param p22
	 * @return 共有点が存在するならばtrue。そうでなければfalse。
	 */
	public static boolean isIntersect(Vector2d dest, Vector2d p11,
			Vector2d p12, Vector2d p21, Vector2d p22) {
		double a1, a2, b1, b2, f;
		f = p11.x * p12.y - p12.x * p11.y;
		if (f == 0)
			return false;
		a1 = -(p12.y - p11.y) / f;
		b1 = (p12.x - p11.x) / f;

		f = p21.x * p22.y - p22.x * p21.y;
		if (f == 0)
			return false;
		a2 = -(p22.y - p21.y) / f;
		b2 = (p22.x - p21.x) / f;

		f = a1 * b2 - a2 * b1;
		if (f == 0)
			return false;
		dest.x = -(b2 - b1) / f;
		dest.y = (a2 - a1) / f;

		return (isOnLine(p11, p12, dest) && isOnLine(p21, p22, dest));
	}

	/**
	 * 0 = args.x * x^2 + args.y * y + args.zの二次関数の解2つを引数ansに出力する。
	 * 
	 * @param ans
	 *            長さ2の配列で解の出力先
	 * @param args
	 *            二次関数を表すベクトル
	 * @return 実数解を持つならばtrue、そうでないならfalse。
	 */
	public static boolean solveQuadEquation(double[] ans, Vector3d args) {
		double f = args.y * args.y - 4 * args.x * args.z;
		if (f < 0)
			return false;
		f = (double) Math.sqrt(f);
		ans[0] = (-args.y + f) / (2 * args.x);
		ans[1] = (-args.y - f) / (2 * args.x);
		return true;
	}

	/**
	 * 線分p11-p12と線分p21-p22（両端を含む）の共有点を求める。
	 * 
	 * @param dest
	 *            交点の出力先
	 * @param p11
	 * @param p12
	 * @param p21
	 * @param p22
	 * @return 共有点が存在するならばtrue。そうでなければfalse。
	 */
	public static boolean getIntersection(Vector2d dest, Vector2d p11,
			Vector2d p12, Vector2d p21, Vector2d p22) {
		double a1 = -(p12.y - p11.y);
		double a2 = -(p22.y - p21.y);
		double b1 = p12.x - p11.x;
		double b2 = p22.x - p21.x;
		double c1 = p11.x * p12.y - p12.x * p11.y;
		double c2 = p21.x * p22.y - p22.x * p21.y;
		double c = a1 * b2 - a2 * b1;
		if (c == 0) {
			return false;
		} else {
			Vector2d v = new Vector2d();
			v.x = (b1 * c2 - b2 * c1) / c;
			v.y = (c1 * a2 - c2 * a1) / c;
			// if (isInInner(v,p11,p12) && isInInner(v,p21,p22))
			if (isOnLine(p11, p12, v) && isOnLine(p21, p22, v)) {
				dest.set(v);
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * pos1とpos2を通る一次関数を得る。 dest.x * x + dest.y * y + dest.z = 0
	 * 
	 * @param dest
	 *            一次関数の出力先
	 * @param pos1
	 * @param pos2
	 */
	public static void getLineFunction(Vector3d dest, Vector2d pos1,
			Vector2d pos2) {
		dest.x = pos2.y - pos1.y;
		dest.y = -(pos2.x - pos1.x);
		dest.z = pos2.x * pos1.y - pos1.x * pos2.y;
	}
	
	/**
	 * ２つの直線が交差する点を計算します。
	 * 
	 * @param dest
	 * @param f1
	 * @param f2
	 * @return
	 */
	public static boolean getIntersection(Vector2d dest, Vector3d f1, Vector3d f2) {
		double d = f1.x * f2.y - f2.x * f1.y; 
		if (d == 0) {
			return false;
		}
		dest.x = - (f2.y*f1.z - f1.y*f2.z) / d;
		dest.y = + (f2.x*f1.z - f1.x*f2.z) / d;
		return true;
	}
	/**
	 * l1,l2を通る直線からpまでの距離を返します。
	 * 
	 * @param p
	 * @param l1
	 * @param l2
	 */
	public static double calcFromLine(Vector2d p, Vector2d l1, Vector2d l2) {
		double d = Vector2d.getDistance(l1, l2);
		if (d == 0) {
			return Vector2d.getDistance(l1, p);
		} else {
			double s = (l2.x-l1.x)*(p.y-l1.y) - (l2.y-l1.y)*(p.x-l1.x);
			return s / d;
		}
	}
}
