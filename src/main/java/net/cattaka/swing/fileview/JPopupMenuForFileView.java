package net.cattaka.swing.fileview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

import net.cattaka.swing.fileview.FileViewTree.FileTreeNode;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.swing.util.SwingGuiUtil;
import net.cattaka.util.FileUtil;
import net.cattaka.util.MessageBundle;

public class JPopupMenuForFileView extends JPopupMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	private FileViewTree fileViewTree;

	private JMenuItem openItem;
	private JMenuItem renameItem;
	private JMenuItem deleteItem;
	private JMenuItem createDirItem;
	private JMenuItem createFileItem;
	private JMenuItem refleshItem;

	class MouseListenerImpl extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 2) {
					TreePath[] treePaths = fileViewTree.getSelectionPaths();
					if (treePaths != null && treePaths.length == 1) {
						TreePath treePath = treePaths[0];
						Object obj = treePath.getPath()[treePath.getPathCount()-1];
						if (obj instanceof FileViewTree.FileTreeNode) {
							FileViewTree.FileTreeNode fileTreeNode = (FileViewTree.FileTreeNode)obj;
							File file = fileTreeNode.getFile();
							if (file.exists() && file.isFile()) {
								fileViewTree.doOpen(file);
							}
						}
					}
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				TreePath[] treePaths = fileViewTree.getSelectionPaths();
				if (treePaths != null) {
					if (treePaths.length == 1) {
						openItem.setEnabled(true);
						refleshItem.setEnabled(true);
					} else {
						openItem.setEnabled(false);
						refleshItem.setEnabled(false);
					}
					show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}

	public JPopupMenuForFileView(boolean createMenuItems) {
		if (createMenuItems) {
			createMenuItems();
		}
	}
	
	public void createMenuItems() {
		ActionListener ac = this;

		openItem = new JMenuItem();
		renameItem = new JMenuItem();
		deleteItem = new JMenuItem();
		createDirItem = new JMenuItem();
		createFileItem = new JMenuItem();
		refleshItem = new JMenuItem();
		ButtonsBundle.applyMenuDifinition(openItem, "file_open");
		ButtonsBundle.applyMenuDifinition(renameItem, "file_rename");
		ButtonsBundle.applyMenuDifinition(deleteItem, "file_delete");
		ButtonsBundle.applyMenuDifinition(createDirItem, "file_create_dir");
		ButtonsBundle.applyMenuDifinition(createFileItem, "file_create_file");
		ButtonsBundle.applyMenuDifinition(refleshItem, "reflesh");
		
		openItem.setActionCommand("open");
		renameItem.setActionCommand("rename");
		deleteItem.setActionCommand("delete");
		createDirItem.setActionCommand("create_dir");
		createFileItem.setActionCommand("create_file");
		refleshItem.setActionCommand("reflesh");

		openItem.addActionListener(ac);
		renameItem.addActionListener(ac);
		deleteItem.addActionListener(ac);
		createDirItem.addActionListener(ac);
		createFileItem.addActionListener(ac);
		refleshItem.addActionListener(ac);
		
		this.add(openItem);
		this.addSeparator();
		this.add(renameItem);
		this.add(deleteItem);
		this.addSeparator();
		this.add(createDirItem);
		this.add(createFileItem);
		this.addSeparator();
		this.add(refleshItem);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("open")) {
			TreePath[] treePaths = fileViewTree.getSelectionPaths();
			if (treePaths != null && treePaths.length == 1) {
				TreePath treePath = treePaths[0];
				Object obj = treePath.getPath()[treePath.getPathCount()-1];
				if (obj instanceof FileViewTree.FileTreeNode) {
					FileViewTree.FileTreeNode fileTreeNode = (FileViewTree.FileTreeNode)obj;
					File file = fileTreeNode.getFile();
					if (file.exists() && file.isFile()) {
						fileViewTree.doOpen(file);
					}
				}
			}
		} else if (e.getActionCommand().equals("rename")) {
			TreePath treePath = fileViewTree.getSelectionPath();
			if (treePath != null && treePath.getPathCount() > 1) {
				int pc = treePath.getPathCount();
				FileTreeNode parentNode = (FileTreeNode)treePath.getPath()[pc-2];
				FileTreeNode node = (FileTreeNode)treePath.getPath()[pc-1];
				File targetFile = node.getFile();
				File parentFile = targetFile.getParentFile();
				String newName = "";
				if ((newName = JOptionPane.showInputDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("input_file_name"), targetFile.getName())) != null) {
					File newFile = new File(parentFile.getAbsolutePath() + File.separator + newName);
					if (targetFile.renameTo(newFile)) {
						// 成功
						parentNode.reflesh();
					} else {
						// 失敗
						JOptionPane.showMessageDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("rename_file_failed"), MessageBundle.getMessage("error"), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if (e.getActionCommand().equals("delete")) {
			TreePath treePath = fileViewTree.getSelectionPath();
			if (treePath != null && treePath.getPathCount() > 1) {
				int pc = treePath.getPathCount();
				FileTreeNode parentNode = (FileTreeNode)treePath.getPath()[pc-2];
				FileTreeNode node = (FileTreeNode)treePath.getPath()[pc-1];
				File targetFile = node.getFile();
				if (targetFile != null) {
					// 念のためダイアログを表示して確認
					if (JOptionPane.showConfirmDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("delete_file_really"), MessageBundle.getMessage("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
						if (FileUtil.deleteFile(targetFile)) {
							// 成功
							parentNode.reflesh();
						} else {
							// 失敗
							JOptionPane.showMessageDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("delete_file_failed"), MessageBundle.getMessage("error"), JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		} else if (e.getActionCommand().equals("create_dir")) {
			TreePath treePath = fileViewTree.getSelectionPath();
			if (treePath != null) {
				int pc = treePath.getPathCount();
				FileTreeNode node = (FileTreeNode)treePath.getPath()[pc-1];
				File targetFile = node.getFile();
				if (targetFile != null && targetFile.isDirectory()) {
					String newName = "";
					if ((newName = JOptionPane.showInputDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("input_dir_name"))) != null) {
						File newFileDir = new File(targetFile.getAbsolutePath() + File.separator + newName);
						if (newFileDir.mkdir()) {
							// 成功
							node.reflesh();
						} else {
							// 失敗
							JOptionPane.showMessageDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("create_dir_failed"), MessageBundle.getMessage("error"), JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		} else if (e.getActionCommand().equals("create_file")) {
			TreePath treePath = fileViewTree.getSelectionPath();
			if (treePath != null) {
				int pc = treePath.getPathCount();
				FileTreeNode node = (FileTreeNode)treePath.getPath()[pc-1];
				File targetFile = node.getFile();
				if (targetFile != null && targetFile.isDirectory()) {
					String newName = "";
					if ((newName = JOptionPane.showInputDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("input_file_name"))) != null) {
						File newFileDir = new File(targetFile.getAbsolutePath() + File.separator + newName);
						try {
							if (newFileDir.createNewFile()) {
								// 成功
								node.reflesh();
							} else {
								// 失敗
								JOptionPane.showMessageDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("create_file_failed"), MessageBundle.getMessage("error"), JOptionPane.ERROR_MESSAGE);
							}
						} catch(IOException exc) {
							// 起こりえないと思うのだけど・・・
							JOptionPane.showMessageDialog(SwingGuiUtil.getParentFrame(this), MessageBundle.getMessage("create_file_failed") + exc.getMessage(), MessageBundle.getMessage("error"), JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		} else if (e.getActionCommand().equals("reflesh")) {
			TreePath[] treePaths = fileViewTree.getSelectionPaths();
			if (treePaths != null && treePaths.length == 1) {
				TreePath treePath = treePaths[0];
				Object obj = treePath.getPath()[treePath.getPathCount()-1];
				if (obj instanceof FileViewTree.FileTreeNode) {
					FileViewTree.FileTreeNode fileTreeNode = (FileViewTree.FileTreeNode)obj;
					fileTreeNode.reflesh();
				}
			}
		}
	}

	public void install(FileViewTree fileViewTree) {
		this.fileViewTree = fileViewTree;
		MouseListenerImpl ml = new MouseListenerImpl();
		fileViewTree.addMouseListener(ml);
	}
}
