package com.JavaIndexer.gui.generics;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class IndeterminateProgressPopup {
	private Popup popup;

	
	public static void displayPopup(Component owner, String message){
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		JPanel panel = new JPanel();
		panel.add(new JLabel(message));
		panel.add(bar);
		panel.add(new JButton("TEMP"));
		//panel.add(bar);
		panel.setPreferredSize(new Dimension(100,100));
		System.out.println((int) (owner.getHeight()/2+owner.getLocationOnScreen().getY()));
		System.out.println((int) (owner.getWidth()/2+owner.getLocationOnScreen().getX()));
		PopupFactory f = PopupFactory.getSharedInstance();
		final Popup popup = f.getPopup(owner, new JButton("TEMP"), 10, 10);
		popup.show();
		//this.popup.show();
	}
	
	public void removePopup(){
		this.popup.hide();
	}
}
