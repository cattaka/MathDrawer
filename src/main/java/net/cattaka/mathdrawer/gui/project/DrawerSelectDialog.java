package net.cattaka.mathdrawer.gui.project;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;

import net.cattaka.mathdrawer.drawer.Drawer;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;

public class DrawerSelectDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JList drawerList;
	private Drawer selectedDrawer;
	
	class DrawerItem {
		private Drawer drawer;
		public DrawerItem(Drawer drawer) {
			if (drawer == null) {
				throw new NullPointerException();
			}
			this.drawer = drawer;
		}
		public Drawer getDrawer() {
			return drawer;
		}
		
		@Override
		public String toString() {
			return drawer.getLabel();
		}
	}
	
	class ActionListenerImpl implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("ok")) {
				doOk();
			} else if (e.getActionCommand().equals("cancel")) {
				doCancel();
			}
		}
	}
	
	class KeyListenerImpl extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == '\n') {
				doOk();
			}
		}
	}
	
	class MouseListenerImpl extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				doOk();
			}
		}
	}
	
	public DrawerSelectDialog() throws HeadlessException {
		super();
		setSize(200,300);
		setTitle(MessageBundle.getMessage("select_drawer"));
		setModal(true);
		makeLayout();
	}
	
	public DrawerSelectDialog(Dialog owner) throws HeadlessException {
		super(owner);
		setSize(400,300);
		setTitle(MessageBundle.getMessage("select_drawer"));
		setModal(true);
		makeLayout();
	}

	public DrawerSelectDialog(Frame owner) throws HeadlessException {
		super(owner);
		setSize(400,300);
		setTitle(MessageBundle.getMessage("select_drawer"));
		setModal(true);
		makeLayout();
	}

	private void makeLayout() {
		this.drawerList = new JList();
		this.drawerList.addMouseListener(new MouseListenerImpl());
		this.drawerList.addKeyListener(new KeyListenerImpl());
		
		ActionListenerImpl al = new ActionListenerImpl();
		
		JButton okButton = new JButton();
		JButton cancelButton = new JButton();
		ButtonsBundle.applyButtonDifinition(okButton, "ok");
		ButtonsBundle.applyButtonDifinition(cancelButton, "cancel");
		okButton.setActionCommand("ok");
		okButton.addActionListener(al);
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(al);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx= 1;
		gbc.weighty= 1;
		gbl.setConstraints(drawerList, gbc);

		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx= 0;
		gbc.weighty= 0;
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(okButton, gbc);
		gbc.gridx++;
		gbl.setConstraints(cancelButton, gbc);
		
		this.getContentPane().setLayout(gbl);
		this.getContentPane().add(drawerList);
		this.getContentPane().add(okButton);
		this.getContentPane().add(cancelButton);
	}
	
	private void doOk() {
		Object obj = drawerList.getSelectedValue();
		if (obj instanceof DrawerItem) {
			this.selectedDrawer = ((DrawerItem)obj).getDrawer();
			setVisible(false);
		}
	}

	private void doCancel() {
		this.selectedDrawer = null;
		setVisible(false);
	}
	
	private void setDrawerSelection(Drawer[] drawerSelection) {
		DrawerItem[] listData = new DrawerItem[drawerSelection.length];
		for (int i=0;i<listData.length;i++) {
			listData[i] = new DrawerItem(drawerSelection[i]);
		}
		drawerList.setListData(listData);
	}
	
	public Drawer showSelectDrawerDialog(Drawer[] drawerSelection) {
		this.setDrawerSelection(drawerSelection);
		this.setModal(true);
		this.setVisible(true);
		
		return this.selectedDrawer;
	}
}
