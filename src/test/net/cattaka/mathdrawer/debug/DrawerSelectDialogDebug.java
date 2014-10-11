package net.cattaka.mathdrawer.debug;

import net.cattaka.mathdrawer.drawer.ColorDrawer;
import net.cattaka.mathdrawer.drawer.Drawer;
import net.cattaka.mathdrawer.drawer.RootDrawer;
import net.cattaka.mathdrawer.gui.project.DrawerSelectDialog;

public class DrawerSelectDialogDebug {
	public static void main(String[] args) {
		Drawer[] drawerSelection = new Drawer[] {
			new RootDrawer(),
			new ColorDrawer()
		};
		
		DrawerSelectDialog dsd = new DrawerSelectDialog();
		Drawer selectedDrawer = dsd.showSelectDrawerDialog(drawerSelection);
		System.out.println(selectedDrawer);
		
		System.exit(0);
	}
}
