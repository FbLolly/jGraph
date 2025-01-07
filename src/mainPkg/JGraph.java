package mainPkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;

import cameraPkg.Camera;
import graphPkg.Graph;
import utilsPkg.DoublePoint;
import utilsPkg.KeyHandler;

public class JGraph extends JPanel implements Runnable{
	private double drawInterval;
	private double nextDrawTime;
	private double remainingTime;
	@SuppressWarnings("unused")
	private long timer;

	private Camera cam;

	private Graph graph;
	
	private Thread thread;
	private KeyHandler keyHandler;
	
	public JGraph() {
		this.keyHandler = new KeyHandler();
		this.cam = new Camera(20, new Point(50, 50));
		this.graph = new Graph();

    	refreshFont();

		this.timer = 0;

		this.setDoubleBuffered(true);
		this.setPreferredSize(new Dimension(Defines.width, Defines.height));
		this.setSize(new Dimension(Defines.width, Defines.height));
		this.setFocusable(true);
		this.setVisible(true);
		
		this.startGame();
	}
	
	public void startGame() {
		thread = new Thread(this);
		thread.start();
	}
	
	public long getNewTimeAndSleep() {
    	drawInterval = 1000000000/Defines.FPS; //recalculate drawInterval in case the fps changes
		
    	try {
			remainingTime = nextDrawTime - System.nanoTime();
			remainingTime /= 1000000;
    	
			if (remainingTime < 0)
				remainingTime = 0;
			
			Thread.sleep((long)remainingTime);
			nextDrawTime += drawInterval;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	return (long)remainingTime;
    }
	
	@Override
	public void run() {
		drawInterval = 1000000000/Defines.FPS;
    	nextDrawTime = System.nanoTime() + drawInterval;
    	remainingTime = 0;
    	timer = 0;
    	
    	while (thread != null) {
        	getNewTimeAndSleep();
        	
        	update();
    		repaint();
    		Toolkit.getDefaultToolkit().sync();
    		
    		timer += 1;
    	}
	}

	
	private void refreshFont(){
		try {
			InputStream is = this.getClass().getResourceAsStream("fonts/CaskaydiaCove-Bold.ttf"); //im forced to put it in the same pkg
			Font f = Font.createFont(Font.TRUETYPE_FONT, is);
			float size = Defines.maxZoom + 10f - (float)this.cam.getZoom();
			
			size *= 0.75;

			Font sizedFont = f.deriveFont(size);
			this.setFont(sizedFont);
		}catch (IOException | FontFormatException e) {
			this.setFont(new Font("serif", Font.BOLD, 30));
		}
	}

	private void updateKeyHandle(DoublePoint p){
		if (p != null){
			this.graph.insertPoint(p.x, p.y);
			keyHandler.instruction = 0;
		}

		cam.setOffSet(new Point((int)(cam.getOffSet().x + (this.keyHandler.direction.x*cam.getZoom())), (int)(cam.getOffSet().y + (this.keyHandler.direction.y*cam.getZoom()))));

		if (keyHandler.pressedEscape){
			System.exit(0);
		}

		if (this.keyHandler.zoom == 1 && this.cam.getZoom() < Defines.maxZoom){
			this.cam.setZoom(this.cam.getZoom()+0.5);

			this.refreshFont();
		}
		if (this.keyHandler.zoom == -1 && this.cam.getZoom() > Defines.minZoom){
			this.cam.setZoom(this.cam.getZoom()-0.5);

			this.refreshFont();
		}
		this.keyHandler.zoom = 0;

		switch (this.keyHandler.instruction) {
			case 1:
				graph.calculate();
				break;
			case 2:
				graph.flush();
				this.keyHandler.instruction = 0;
				break;
		}
	}
	
	public void update() {
		DoublePoint p = this.keyHandler.handleKeys(this);
		updateKeyHandle(p);
	}
	
	public void paintComponent(Graphics g) {
		//paint background
		g.setColor(Color.black);
		g.fillRect(0, 0, Defines.width, Defines.height);
		//-

		graph.paintComponent((Graphics2D) g, cam, this);
	}
}


