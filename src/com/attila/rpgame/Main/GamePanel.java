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

	public static final int SZELESSEG = 128;
	public static final int MAGAS = 128;
	public static final int MAGASSAG = MAGAS + 16;
	public static final int MERETEZES = 5;

	private Thread thread;
	private final int FPS = 35;
	private final int IDOEGYSEG = 1000 / FPS;

	private BufferedImage rajzfelulet;
	private Graphics2D g;

	private GameStateManager gsm;

	public GamePanel() {
		setPreferredSize(new Dimension(SZELESSEG * MERETEZES, MAGASSAG * MERETEZES));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			addKeyListener(this);
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {

		rajzfelulet = new BufferedImage(SZELESSEG, MAGASSAG, 1);
		g = (Graphics2D) rajzfelulet.getGraphics();
		gsm = new GameStateManager();
		
		long start;
		long elapsed;
		long wait;
		
		// játék loop
		while(true) {
			
			start = System.nanoTime();
			// játék frissítése
			gsm.update();
			Keys.update();
			// játék rajzolása
			gsm.draw(g);
			// megjelenítás a képernyőn
			Graphics g2 = getGraphics();
			g2.drawImage(rajzfelulet, 0, 0, SZELESSEG * MERETEZES, MAGASSAG * MERETEZES, null);
			g2.dispose();
			
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

	
	// Billentyűzet eventek
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
	
}
