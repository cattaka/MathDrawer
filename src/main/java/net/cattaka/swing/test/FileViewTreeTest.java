package net.cattaka.swing.test;

import java.io.File;

import javax.swing.JFrame;

import net.cattaka.swing.fileview.FileViewTree;

public class FileViewTreeTest extends JFrame{
	private static final long serialVersionUID = 1L;

	public FileViewTreeTest() {
		setSize(500,300);
		
		FileViewTree fileViewTree = new FileViewTree(new File("/home/cattaka/other/rdba"));
		getContentPane().add(fileViewTree);
	}
	
	public static void main(String[] args) {
		FileViewTreeTest f = new FileViewTreeTest();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
