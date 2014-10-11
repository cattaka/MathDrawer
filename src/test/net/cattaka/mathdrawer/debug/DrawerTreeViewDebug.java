package net.cattaka.mathdrawer.debug;

import javax.swing.JFrame;

import net.cattaka.mathdrawer.drawer.ColorDrawer;
import net.cattaka.mathdrawer.drawer.RootDrawer;
import net.cattaka.mathdrawer.gui.DrawerTreeView;

public class DrawerTreeViewDebug {
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		RootDrawer rootDrawer = new RootDrawer();
		ColorDrawer colorDrawer1 = new ColorDrawer();
		ColorDrawer colorDrawer2 = new ColorDrawer();
		rootDrawer.addChild(colorDrawer1);
		rootDrawer.addChild(colorDrawer2);
		
		JFrame frame = new JFrame();
		frame.setSize(400,400);
		DrawerTreeView drawerTreeView = new DrawerTreeView();
		drawerTreeView.setRootDrawer(rootDrawer);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(drawerTreeView);
		
		frame.setVisible(true);
	}
}
