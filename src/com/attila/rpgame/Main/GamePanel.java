// The GamePanel is the drawing canvas.
// It contains the game loop which
// keeps the game moving forward.
// This class is also the one that grabs key events.

package com.attila.rpgame.Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.attila.rpgame.Manager.GameStateManager;
import com.attila.rpgame.Manager.Keys;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	//Ablak méretének megadása
	public static final int SZELESSEG = 128;
	public static final int MAGAS = 128;
	public static final int MAGASSAG = MAGAS + 16;
	public static final int MERETEZES = 5;

	private Thread thread;
	private final int FPS = 35;
	private final int IDOEGYSEG = 1000 / FPS;

	// Rajzoláshoz szükséges elemek
	private BufferedImage rajzfelulet;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	// constructor
	public GamePanel() {
		setPreferredSize(new Dimension(SZELESSEG * MERETEZES, MAGASSAG * MERETEZES));
		setFocusable(true);
		requestFocus();
	}
	
	// ready to display
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			addKeyListener(this);
			thread = new Thread(this);
			thread.start();
		}
	}
	
	// run new thread
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(true) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;

			wait = IDOEGYSEG - elapsed / 1000000;
			if(wait < 0) wait = IDOEGYSEG;
			
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	// initializes fields
	private void init() {
		rajzfelulet = new BufferedImage(SZELESSEG, MAGASSAG, 1);
		g = (Graphics2D) rajzfelulet.getGraphics();
		gsm = new GameStateManager();
	}
	
	// updates game
	private void update() {
		gsm.update();
		Keys.update();
	}
	
	// draws game
	private void draw() {
		gsm.draw(g);
	}
	
	// copy buffer to screen
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(rajzfelulet, 0, 0, SZELESSEG * MERETEZES, MAGASSAG * MERETEZES, null);
		g2.dispose();
	}
	
	// key event
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
	
}
