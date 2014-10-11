package net.cattaka.swing.test;

import java.awt.Font;

import net.cattaka.swing.FontChooser;

public class FontChooserTest {
	public static void main(String[] args) {
		FontChooser f = new FontChooser();
		Font font = f.showFontSelectDialog();
		System.out.println(font);
		System.exit(0);
	}
}
