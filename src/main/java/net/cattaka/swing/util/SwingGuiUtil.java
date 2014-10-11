package net.cattaka.swing.util;

import java.awt.Container;
import java.awt.Frame;

public class SwingGuiUtil {
//	static class DoLayoutRunner implements Runnable {
//		private RdbaGuiInterface rdbaGuiInterface;
//		public DoLayoutRunner(RdbaGuiInterface rdbaGuiInterface) {
//			this.rdbaGuiInterface =  rdbaGuiInterface;
//		}
//		public void run() {
//			this.rdbaGuiInterface.doGuiLayout();
//		}
//	}
//	
//	public static void doLayout(RdbaGuiInterface rdbaGuiInterface) {
//		SwingUtilities.invokeLater(new DoLayoutRunner(rdbaGuiInterface));
//	}
	
	public static Frame getParentFrame(Container container) {
		Container parent = container.getParent();
		
		while((parent != null) &&!(parent instanceof Frame)) {
			parent = parent.getParent();
		}
		
		return (Frame) parent;
	}
}
