package net.cattaka.swing;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class StdScrollPane extends JScrollPane {
	private static final long serialVersionUID = 1L;

	class MouseWheelListenerImpl implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			StdScrollPane scrollpane = StdScrollPane.this;
			if (scrollpane.isWheelScrollingEnabled()
					&& e.getScrollAmount() != 0) {
				JScrollBar toScroll;
				int direction = 0;
				// find which scrollbar to scroll, or return if none
				if ((e.getModifiersEx() & MouseWheelEvent.SHIFT_DOWN_MASK) == 0) {
					toScroll = scrollpane.getVerticalScrollBar();
				} else {
					toScroll = scrollpane.getHorizontalScrollBar();
				}
				if (toScroll == null || !toScroll.isVisible()) {
					return;
				}
				direction = e.getWheelRotation() < 0 ? -1 : 1;
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					scrollByUnits(toScroll, direction, e.getScrollAmount());
				} else if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
					scrollByBlock(toScroll, direction);
				}
			}
		}

		void scrollByUnits(JScrollBar scrollbar, int direction, int units) {
			// This method is called from BasicScrollPaneUI to implement wheel
			// scrolling, as well as from scrollByUnit().
			int delta;

			for (int i = 0; i < units; i++) {
				if (direction > 0) {
					delta = scrollbar.getUnitIncrement(direction);
				} else {
					delta = -scrollbar.getUnitIncrement(direction);
				}

				int oldValue = scrollbar.getValue();
				int newValue = oldValue + delta;

				// Check for overflow.
				if (delta > 0 && newValue < oldValue) {
					newValue = scrollbar.getMaximum();
				} else if (delta < 0 && newValue > oldValue) {
					newValue = scrollbar.getMinimum();
				}
				if (oldValue == newValue) {
					break;
				}
				scrollbar.setValue(newValue);
			}
		}

	    void scrollByBlock(JScrollBar scrollbar, int direction) {
			// This method is called from BasicScrollPaneUI to implement wheel
			// scrolling, and also from scrollByBlock().
			int oldValue = scrollbar.getValue();
			int blockIncrement = scrollbar.getBlockIncrement(direction);
			int delta = blockIncrement * ((direction > 0) ? +1 : -1);
			int newValue = oldValue + delta;

			// Check for overflow.
			if (delta > 0 && newValue < oldValue) {
				newValue = scrollbar.getMaximum();
			} else if (delta < 0 && newValue > oldValue) {
				newValue = scrollbar.getMinimum();
			}

			scrollbar.setValue(newValue);
		}
	}
	
	public StdScrollPane() {
		super();
		initialize();
	}

	public StdScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		initialize();
	}

	public StdScrollPane(Component view) {
		super(view);
		initialize();
	}

	public StdScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		initialize();
	}

	private void initialize() {
		// CTRL+PgUpとCTRL+PgDnを奪うので削除
		InputMap inputs = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		KeyStroke ctrlPgUpKey = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_DOWN_MASK);
		inputs.put(ctrlPgUpKey, "None");
		KeyStroke ctrlPgDownKey = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_DOWN_MASK);
		inputs.put(ctrlPgDownKey, "None");
		
		// スクロールの修正
		{
			MouseWheelListener[] mwls = this.getMouseWheelListeners();
			for (MouseWheelListener mwl : mwls) {
				this.removeMouseWheelListener(mwl);
			}
			this.addMouseWheelListener(new MouseWheelListenerImpl());
		}
	}
}
