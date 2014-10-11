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
 * @author cattaka 3次元ベクトルを表すクラス。
 */
public class Vector3d implements Serializable {
	private static final long serialVersionUID = 1L;
	public double x = 0, y = 0, z = 0;

	public Vector3d() {
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(Vector3d v) {
		this.set(v);
	}

	public void set(Vector3d v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public void get(Vector3d v) {
		v.x = this.x;
		v.y = this.y;
		v.z = this.z;
	}

	public static double dot(Vector3d v1, Vector3d v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}

	public static void cross(Vector3d dist, Vector3d v1, Vector3d v2) {
		dist.x = v1.y * v2.z - v1.z * v2.y;
		dist.y = v1.z * v2.x - v1.x * v2.z;
		dist.z = v1.x * v2.y - v1.y * v2.x;
	}

	public static Vector3d valueOf(String arg) {
		Vector3d result = null;
		boolean goFlag = true;
		String tmp = "";
		String strX = "";
		String strY = "";
		String strZ = "";
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
			tmp = s1.findInLine("\\s*,\\s*");
			goFlag = (tmp != null);
		}
		if (goFlag) {
			strZ = s1.findInLine(MdMath.NUMBER_FORMAT_REGEX);
			goFlag = (strZ != null);
		}
		if (goFlag) {
			tmp = s1.findInLine("\\s*\\)\\s*");
			goFlag = (tmp != null);
		}
		
		if (goFlag) {
			try {
				double x = Double.parseDouble(strX);
				double y = Double.parseDouble(strY);
				double z = Double.parseDouble(strZ);
				result = new Vector3d(x,y,z);
			} catch (NumberFormatException e) {
			}
		}
		if (result == null) {
			throw new NumberFormatException(arg);
		}
		return result;
	}
	
	public String toString() {
		return String.format("(%f,%f,%f)", x,y,z);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}
}
