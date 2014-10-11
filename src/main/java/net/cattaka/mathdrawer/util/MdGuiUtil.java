/*
 * Copyright (c) 2009, Takao Sumitomo
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the 
 *       above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce
 *       the above copyright notice, this list of
 *       conditions and the following disclaimer in the
 *       documentation and/or other materials provided
 *       with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should
 * not be interpreted as representing official policies,
 * either expressed or implied.
 */
/*
 * $Id: RdbaGuiUtil.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.mathdrawer.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.SwingUtilities;

import net.cattaka.mathdrawer.gui.MdGuiInterface;

public class MdGuiUtil {
	static class DoLayoutRunner implements Runnable {
		private MdGuiInterface mdGuiInterface;
		public DoLayoutRunner(MdGuiInterface mdGuiInterface) {
			this.mdGuiInterface =  mdGuiInterface;
		}
		public void run() {
			this.mdGuiInterface.doGuiLayout();
		}
	}
	static class RepaintRunner implements Runnable {
		private Component component;
		
		public RepaintRunner(Component component) {
			this.component = component;
		}

		public void run() {
			this.component.repaint();
		}
	}
	
	public static void doLayout(MdGuiInterface mdGuiInterface) {
		SwingUtilities.invokeLater(new DoLayoutRunner(mdGuiInterface));
	}

	public static void doRepaint(Component component) {
		SwingUtilities.invokeLater(new RepaintRunner(component));
	}

	public static Frame getParentFrame(Container container) {
		Container parent = container.getParent();
		
		while((parent != null) &&!(parent instanceof Frame)) {
			parent = parent.getParent();
		}
		
		return (Frame) parent;
	}
}
