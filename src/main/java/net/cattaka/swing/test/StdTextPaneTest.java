package net.cattaka.swing.test;

import java.awt.FontMetrics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import net.cattaka.swing.text.AbstractStdStyledDocument;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.text.FindConditionDialog;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.swing.text.StdTextPane;
import net.cattaka.swing.text.TextLineInfo;

public class StdTextPaneTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private StdTextPane stdTextPane;
	private FindConditionDialog findConditionDialog;
	
	class TestStdStyledDocument extends AbstractStdStyledDocument {
		private static final long serialVersionUID = 1L;

		@Override
		protected void updateAttributes(int start, int tail) {
		}

		public void setFontMetrics(FontMetrics fontMetrics, int tabNum) {
		}
	}
	
	public StdTextPaneTest() {
		setSize(800,600);
		stdTextPane = new StdTextPane();
		stdTextPane.setStdStyledDocument(new TestStdStyledDocument());
		findConditionDialog = new FindConditionDialog(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void doAction(FindCondition findCondition) {
				super.doAction(findCondition);
				stdTextPane.doFindAction(findCondition);
			}
		};
		findConditionDialog.setModal(false);
		
		stdTextPane.setText("ab ab ab ab\nab ab ab ab\n");
		
		getContentPane().add(stdTextPane);
		
		/*
		 * StdStyledDocument.getLineのテスト
		 */
		stdTextPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F3) {
					int pos = stdTextPane.getCaretPosition();
					StdStyledDocument ssd = stdTextPane.getStdStyledDocument();
					TextLineInfo tli = ssd.getLine(pos);
					System.out.println("P:"+ssd.getPrevLine(tli));
					System.out.println("C:"+tli);
					System.out.println("N:"+ssd.getNextLine(tli));
				}
			}
		});
		
	}
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		//findConditionDialog.setVisible(b);
	}
	
	public static void main(String[] args) {
		StdTextPaneTest f = new StdTextPaneTest();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
