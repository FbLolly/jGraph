package mainPkg;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("JGraph");
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		
		frame.setUndecorated(true);
		device.setFullScreenWindow(frame);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JGraph());
		frame.setVisible(true);
		frame.pack();
	}
}
//Copyright 2024 FbLolly
