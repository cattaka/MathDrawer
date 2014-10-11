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

import java.io.Serializable;
import java.util.Scanner;


/**
 * @author cattaka ２次元ベクトルを表すクラス
 */
public class Vector2d implements Serializable {
	private static final long serialVersionUID = 1L;
	public double x = 0, y = 0;

	/**
	 * (0,0)で初期化する。
	 */
	public Vector2d() {
	}

	/**
	 * 与えられたx,yでベクトルを初期化する。
	 * 
	 * @param x
	 *            x値
	 * @param y
	 *            y値
	 */
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 与えられたベクトルと同じ値で初期化する。
	 * 
	 * @param v
	 *            元となるベクトル
	 */
	public Vector2d(Vector2d v) {
		this.set(v);
	}

	/**
	 * ベクトルの値を設定する
	 * 
	 * @param x
	 *            x値
	 * @param y
	 *            y値
	 */
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 与えられたベクトルの値をこのインスタンスに設定する。
	 * 
	 * @param v
	 *            元となるベクトル
	 */
	public void set(Vector2d v) {
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * 引数vにこのインスタンスの内容をコピーする。
	 * 
	 * @param v
	 *            コピー先
	 */
	public void get(Vector2d v) {
		v.x = this.x;
		v.y = this.y;
	}

	/**
	 * このベクトルと引数のスカラー倍をとる。
	 * 
	 * @param scale
	 *            スカラー値
	 */
	public void scale(double scale) {
		x *= scale;
		y *= scale;
	}

	/**
	 * 引数vをこのインスタンスに加算する。
	 * 
	 * @param v
	 *            加算するベクトル
	 */
	public void add(Vector2d v) {
		x += v.x;
		y += v.y;
	}

	/**
	 * 引数dと引数vのスカラー倍をこのインスタンスに加算する。
	 * 
	 * @param d
	 *            スカラー値
	 * @param v
	 *            ベクトル
	 */
	public void scaleAdd(double d, Vector2d v) {
		x += v.x * d;
		y += v.y * d;
	}

	/**
	 * 引数vをこのインスタンスに減算する。
	 * 
	 * @param v
	 *            減算するベクトル
	 */
	public void sub(Vector2d v) {
		x -= v.x;
		y -= v.y;
	}

	/**
	 * 2つのベクトルの内積を返す。
	 * 
	 * @param a
	 * @param b
	 * @return 内積の値。
	 */
	public static double getDot(Vector2d a, Vector2d b) {
		return a.x * b.x + a.y * b.y;
	}

	/**
	 * 2つのベクトルの外積を返す。
	 * 
	 * @param a
	 * @param b
	 * @return 外積の値。
	 */
	public static double getCross(Vector2d a, Vector2d b) {
		return a.x * b.y - a.y * b.x;
	}

	/**
	 * 2つのベクトルの距離を返す。
	 * 
	 * @param a
	 * @param b
	 * @return 2つのベクトルの距離。
	 */
	public static double getDistance(Vector2d a, Vector2d b) {
		return  Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y)
				* (a.y - b.y));
	}

	/**
	 * 2つのベクトルの角度(ラジアン値)を返す。ベクトルaからベクトルbへの反時計回りの角度となる。
	 * 
	 * @param a
	 * @param b
	 * @return 2つのベクトルの角度(ラジアン値)
	 */
	public static double getAngle(Vector2d a, Vector2d b) {
		double s = a.getLength() * b.getLength();
		double rad = Math.acos(Vector2d.getDot(a, b) / s);
		double cross = Vector2d.getCross(a, b);
		if (cross >= 0)
			return rad;
		else
			return -rad;
	}

	/**
	 * このベクトルの長さを返す。
	 * 
	 * @return ベクトルの長さ
	 */
	public double getLength() {
		double d =  Math.sqrt(x * x + y * y);
		return d;
	}

	/**
	 * このベクトルの長さの二乗を返す。
	 * 
	 * @return ベクトルの長さの二乗
	 */
	public double getLengthSquare() {
		return (x * x + y * y);
	}

	/**
	 * ゼロベクトルかどうか調べる。
	 * 
	 * @return ゼロベクトルならtrue、そうでなければfales。
	 */
	public boolean isZeroVector() {
		return x == 0 && y == 0;
	}

	/**
	 * このベクトルを正規化する。 長さが0のときは(NaN,NaN)になるので注意すること。
	 */
	public void normalize() {
		double d =  Math.sqrt(x * x + y * y);
		if (d > 0) {
			x = x / d;
			y = y / d;
		} else {
			x = Double.NaN;
			y = Double.NaN;
		}
	}

	/**
	 * このベクトルを反時計回りに回転させる。
	 * 
	 * @param rad
	 *            回転させる角度(ラジアン値)
	 */
	public void rotate(double rad) {
		double xd =  (x * Math.cos(rad) - y * Math.sin(rad));
		double yd =  (y * Math.cos(rad) + x * Math.sin(rad));
		x = xd;
		y = yd;
	}

	public static Vector2d valueOf(String arg) {
		Vector2d result = null;
		boolean goFlag = true;
		String tmp = "";
		String strX = "";
		String strY = "";
		Scanner s1 = new Scanner(arg);
		
		if (goFlag) {
			tmp = s1.findInLine("\\s*\\(\\s*");
			goFlag = (tmp != null);
		}
		if (goFlag) {
			strX = s1.findInLine(MdMath.NUMBER_FORMAT_REGEX);
			goFlag = (strX != null);
		}
		if (goFlag) {
			tmp = s1.findInLine("\\s*,\\s*");
			goFlag = (tmp != null);
		}
		if (goFlag) {
			strY = s1.findInLine(MdMath.NUMBER_FORMAT_REGEX);
			goFlag = (strY != null);
		}
		if (goFlag) {
			tmp = s1.findInLine("\\s*\\)\\s*");
			goFlag = (tmp != null);
		}
		
		if (goFlag) {
			try {
				double x = Double.parseDouble(strX);
				double y = Double.parseDouble(strY);
				result = new Vector2d(x,y);
			} catch (NumberFormatException e) {
			}
		}
		if (result == null) {
			throw new NumberFormatException(arg);
		}
		return result;
	}
	
	public String toString() {
		return String.format("(%f,%f)", x, y);
	}

	/**
	 * ベクトルの比較して一致するならtrueを返す。そうでなければfalseを返す。
	 */
	public boolean equals(Object obj) {
		Vector2d v = (Vector2d) obj;
		return (this.x == v.x && this.y == v.y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}
