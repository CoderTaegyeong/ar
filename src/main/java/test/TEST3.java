package test;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.Gui;

public class TEST3 {
	public static void main(String[] args) {
		JFrame f = Gui.createFrame(new JPanel());
		f.setLocation(540, 440);
		JFrame f2 = Gui.createFrame(new JPanel());
		f.setSize(500, 400);
		f2.setSize(300,200);
		Gui.placeSubWindow(f, f2, 4);
	}
}
