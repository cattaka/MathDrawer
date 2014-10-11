package net.cattaka.mathdrawer.entity;

import java.awt.Color;
import java.util.Scanner;


public class Color4d extends Vector4d {
	private static final long serialVersionUID = 1L;
	private Color cachedColor = Color.WHITE;
	private double alpha = 1;
	private double red = 1;
	private double green = 1;
	private double blue = 1;
	
	public Color4d() {
		super(1,1,1,1);
	}

	public Color4d(double a, double b, double c, double d) {
		super(a, b, c, d);
	}

	public Color4d(Vector4d v) {
		super(v);
	}

	public Color4d(Color4d v) {
		super(v);
	}

	public int toRgb() {
		int ia = Math.max(Math.min(Math.round((float)(this.a * 255)), 0xFF), 0);
		int ir = Math.max(Math.min(Math.round((float)(this.b * 255)), 0xFF), 0);
		int ig = Math.max(Math.min(Math.round((float)(this.c * 255)), 0xFF), 0);
		int ib = Math.max(Math.min(Math.round((float)(this.d * 255)), 0xFF), 0);
		return (ia << 24) | (ir << 16) | (ig << 8) | ib;
	}
	
	public Color toColor() {
		if (this.a == alpha
			&& this.b == red
			&& this.c == green
			&& this.d == blue
		) {
			return cachedColor;
		} else {
			alpha = this.a; 
			red = this.b;
			green = this.c;
			blue = this.a;
			this.cachedColor = new Color((float)b, (float)c, (float)d, (float)a); 
			return this.cachedColor;
		}
	}
	
	public void rgbToHsv(Color4d out) {
		double r = MdMath.limitDouble(this.b);
		double g = MdMath.limitDouble(this.c);
		double b = MdMath.limitDouble(this.d);
//		char minFlag;
		char maxFlag;
		double minValue;
		double maxValue;
		
		if (r >= g && r >= b) {
			maxFlag = 'R';
			maxValue = r;
			if (g <= b) {
//				minFlag = 'G';
				minValue = g;
			} else {
//				minFlag = 'B';
				minValue = b;
			}
		} else if (g >= r && g >= b) {
			maxFlag = 'G';
			maxValue = g;
			if (r <= b) {
//				minFlag = 'R';
				minValue = r;
			} else {
//				minFlag = 'B';
				minValue = b;
			}
//		} else if (b >= r && b >= g) {
		} else {
			maxFlag = 'B';
			maxValue = b;
			if (r <= g) {
//				minFlag = 'R';
				minValue = r;
			} else {
//				minFlag = 'G';
				minValue = g;
			}
		}
		// Alpha
		out.a = this.a;
		
		// H
		if (maxFlag == 'R') {
			out.b = (g-b) / (maxValue - minValue);
			out.b = out.b / 3;
		} else if (maxFlag == 'G') {
			out.b = (b-r) / (maxValue - minValue);
			out.b = (out.b + 1.0) / 3;
		} else {
			out.b = (r-g) / (maxValue - minValue);
			out.b = (out.b + 2.0) / 3;
		}
		if (Double.isNaN(out.b)) {
			out.b = 0;
		}
		
		// S
		out.c = maxValue - minValue;
		
		// V
		out.d = maxValue;
	}

	public void hsvToRgb(Color4d out) {
		double h = MdMath.cyclicDouble(this.b);
		double s = MdMath.limitDouble(this.c);
		double v = MdMath.limitDouble(this.d);
		
		int hi = (int)(h * 6);
		double f = (h*6) - hi;
		double p = v * (1 - s);
		double q = v * (1 - f*s);
		double t = v * (1 - (1-f)*s);
		
		out.a = this.a; 
		if (hi == 0) {
			out.b = v;
			out.c = t;
			out.d = p;
		} else if (hi == 1) {
			out.b = q;
			out.c = v;
			out.d = p;

		} else if (hi == 2) {
			out.b = p;
			out.c = v;
			out.d = t;
		} else if (hi == 3) {
			out.b = p;
			out.c = q;
			out.d = v;
		} else if (hi == 4) {
			out.b = t;
			out.c = p;
			out.d = v;
		} else if (hi == 5) {
			out.b = v;
			out.c = p;
			out.d = q;
		}
	}

	public static Color4d valueOf(String arg) {
		Color4d result = null;
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
				result = new Color4d(a,b,c,d);
			} catch (NumberFormatException e) {
			}
		}
		if (result == null) {
			throw new NumberFormatException(arg);
		}
		return result;
	}
}
