package net.cattaka.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import net.cattaka.swing.util.ButtonsBundle;

public class StdStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel messageLabel;
	private JProgressBar memoryBar;
	private Timer timer;

	public StdStatusBar(Icon gcIcon) {
		makeLayout(gcIcon);
	}
	private void makeLayout(Icon gcIcon) {
		JButton gcButton = new JButton();
		ButtonsBundle.applyButtonDifinition(gcButton, gcIcon, "exec_gc", true);
		
		this.messageLabel = new JLabel();
		this.memoryBar = new JProgressBar(0,1);
		this.memoryBar.setStringPainted(true);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx=0;
		gbc.gridy=0;
		gbl.setConstraints(this.messageLabel, gbc);
		gbc.weightx = 0;
		gbc.gridx++;
		gbl.setConstraints(this.memoryBar, gbc);
		gbc.gridx++;
		gbl.setConstraints(gcButton, gbc);
		
		this.setLayout(gbl);
		this.add(this.messageLabel);
		this.add(this.memoryBar);
		this.add(gcButton);
		
		// memory残量の初期表示
		updateMemoryBar();

		// GCボタンにアクションを設定
		gcButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.gc();
			}
		});
		// タイマーの作成
		this.timer = new Timer(500, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				updateMemoryBar();
			}
		});
	}
	private void updateMemoryBar() {
		Runtime runtime = Runtime.getRuntime();
		int maxMemory;
		int totalMemory;

		// メガバイト単位に直す
		maxMemory = (int)(runtime.maxMemory() / (2<<10));
		totalMemory = (int)((runtime.totalMemory() - runtime.freeMemory()) / (2<<10));
		
		this.memoryBar.setMaximum((int)maxMemory);
		this.memoryBar.setValue((int)totalMemory);
		
		String caption = String.format("%dKB / %dKB", totalMemory, maxMemory);
		this.memoryBar.setString(caption);
	}
	public void startMemoryUpdate() {
		timer.start();
	}
	public void stopMemoryUpdate() {
		timer.stop();
	}
	public void setMessage(String message) {
		this.messageLabel.setText(message);
	}
}
