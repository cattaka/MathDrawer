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
 * @author cattaka 4次元ベクトルを表すクラス。
 */
public class Vector4d implements Serializable {
	private static final long serialVersionUID = 1L;
	public double a = 0, b = 0, c = 0, d = 0;

	public Vector4d() {
	}

	public Vector4d(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public Vector4d(Vector4d v) {
		this.set(v);
	}

	public void set(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public void set(Vector4d v) {
		this.a = v.a;
		this.b = v.b;
		this.c = v.c;
		this.d = v.d;
	}

	public void get(Vector4d v) {
		v.a = this.a;
		v.b = this.b;
		v.c = this.c;
		v.d = this.d;
	}

	public void add(Vector4d v) {
		this.a = this.a + v.a;
		this.b = this.b + v.b;
		this.c = this.c + v.c;
		this.d = this.d + v.d;
	}

	public void sub(Vector4d v) {
		this.a = this.a - v.a;
		this.b = this.b - v.b;
		this.c = this.c - v.c;
		this.d = this.d - v.d;
	}

	public static Vector4d valueOf(String arg) {
		Vector4d result = null;
		boolean goFlag = true;
		String tmp = "";
		String strA = "";
		String strB = "";
		String strC = "";
		String strD = "";
		Scanner s1 = new Scanner(arg);
		
		if (goFlag) {
			tmp = s1.findInLine("\\s*\\(\\s*");
			goFlag = (tmp != null);
		}
		if (goFlag) {
			strA = s1.findInLine(MdMath.NUMBER_FORMAT_REGEX);
			goFlag = (strA != null);
		}
		if (goFlag) {
			tmp = s1.findInLine("\\s*,\\s*");
			goFlag = (tmp != null);
		}
		if (goFlag) {
			strB = s1.findInLine(MdMath.NUMBER_FORMAT_REGEX);
			goFlag = (strB != null);
		}
		if (goFlag) {
			tmp = s1.findInLine("\\s*,\\s*");
			goFlag = (tmp != null);
		}
		if (goFlag) {
			strC = s1.findInLine(MdMath.NUMBER_FORMAT_REGEX);
			goFlag = (strC != null);
		}
		if (goFlag) {
			tmp = s1.findInLine("\\s*,\\s*");
			goFlag = (tmp != null);
		}
		if (goFlag) {
			strD = s1.findInLine(MdMath.NUMBER_FORMAT_REGEX);
			goFlag = (strD != null);
		}
		if (goFlag) {
			tmp = s1.findInLine("\\s*\\)\\s*");
			goFlag = (tmp != null);
		}
		
		if (goFlag) {
			try {
				double a = Double.parseDouble(strA);
				double b = Double.parseDouble(strB);
				double c = Double.parseDouble(strC);
				double d = Double.parseDouble(strD);
				result = new Vector4d(a,b,c,d);
			} catch (NumberFormatException e) {
			}
		}
		if (result == null) {
			throw new NumberFormatException(arg);
		}
		return result;
	}
	
	public String toString() {
		return String.format("(%f,%f,%f,%f)", a,b,c,d);
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public double getC() {
		return c;
	}

	public double getD() {
		return d;
	}

	public void setA(double a) {
		this.a = a;
	}

	public void setB(double b) {
		this.b = b;
	}

	public void setC(double c) {
		this.c = c;
	}

	public void setD(double d) {
		this.d = d;
	}
}
