// The only subclass the fully utilizes the
// Entity superclass (no other class requires
// movement in a tile based map).
// Contains all the gameplay associated with
// the Player.

package com.attila.rpgame.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.attila.rpgame.Manager.Content;
import com.attila.rpgame.Manager.JukeBox;
import com.attila.rpgame.TileMap.TileMap;

public class Player extends Entity {
	
	// képkockák
	private final BufferedImage[] downSprites = Content.PLAYER[0];
	private final BufferedImage[] leftSprites = Content.PLAYER[1];
	private final BufferedImage[] rightSprites = Content.PLAYER[2];
	private final BufferedImage[] upSprites = Content.PLAYER[3];
	private final BufferedImage[] downBoatSprites = Content.PLAYER[4];
	private final BufferedImage[] leftBoatSprites = Content.PLAYER[5];
	private final BufferedImage[] rightBoatSprites = Content.PLAYER[6];
	private final BufferedImage[] upBoatSprites = Content.PLAYER[7];
	
	// animáció
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;

	// játékmenet
	private int numCoins = 0;
	private int totalCoins;
	private boolean hasBoat;
	private boolean hasAxe;
	private boolean onWater;
	private long ticks;
	
	public Player(TileMap tm) {
		super(tm);
		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;
		moveSpeed = 2;

		animation.setFrames(downSprites);
		animation.setDelay(10);
	}
	
	private void setAnimation(int i, BufferedImage[] bi) {
		currentAnimation = i;
		animation.setFrames(bi);
	}
	
	public void collectedCoin() { numCoins++; }
	public int numCoins() { return numCoins; }
	public int getTotalCoins() { return totalCoins; }
	public void setTotalCoins(int i) { totalCoins = i; }
	
	public void gotBoat() { hasBoat = true; tileMap.replace(22, 4); }
	public void gotAxe() { hasAxe = true; }
	public boolean hasBoat() { return hasBoat; }
	public boolean hasAxe() { return hasAxe; }
	
	// idő frissítése
	public long getTicks() { return ticks; }
	
	// Billentyűzet bemenet. Mozgatja a játékost.
	public void setDown() {
		super.setDown();
	}
	public void setLeft() {
		super.setLeft();
	}
	public void setRight() {
		super.setRight();
	}
	public void setUp() {
		super.setUp();
	}

	// Billentyűzetbevitel.
    // Ha a játékosnak fejszéje van, a kidőlt
    // farönköket ki tudja vágni
	public void setAction() {
		if(hasAxe) {
			if(currentAnimation == UP && tileMap.getIndex(rowTile - 1, colTile) == 21) {
				tileMap.setTile(rowTile - 1, colTile, 1);
				JukeBox.play("tilechange");
			}
			if(currentAnimation == DOWN && tileMap.getIndex(rowTile + 1, colTile) == 21) {
				tileMap.setTile(rowTile + 1, colTile, 1);
				JukeBox.play("tilechange");
			}
			if(currentAnimation == LEFT && tileMap.getIndex(rowTile, colTile - 1) == 21) {
				tileMap.setTile(rowTile, colTile - 1, 1);
				JukeBox.play("tilechange");
			}
			if(currentAnimation == RIGHT && tileMap.getIndex(rowTile, colTile + 1) == 21) {
				tileMap.setTile(rowTile, colTile + 1, 1);
				JukeBox.play("tilechange");
			}
		}
	}
	
	public void update() {
		
		ticks++;
		
		// ellenőrzi, hogy vízben van-e
		boolean current = onWater;
		onWater = tileMap.getIndex(ydest / tileSize, xdest / tileSize) == 4;
		// szárazföldről megy vízbe
		if(!current && onWater) {
			JukeBox.play("splash");
		}
		
		// animáció beállítása
		if(down) {
			int DOWNBOAT = 4;
			if(onWater && currentAnimation != DOWNBOAT) {
				setAnimation(DOWNBOAT, downBoatSprites);
			}
			else if(!onWater && currentAnimation != DOWN) {
				setAnimation(DOWN, downSprites);
			}
		}
		if(left) {
			int LEFTBOAT = 5;
			if(onWater && currentAnimation != LEFTBOAT) {
				setAnimation(LEFTBOAT, leftBoatSprites);
			}
			else if(!onWater && currentAnimation != LEFT) {
				setAnimation(LEFT, leftSprites);
			}
		}
		if(right) {
			int RIGHTBOAT = 6;
			if(onWater && currentAnimation != RIGHTBOAT) {
				setAnimation(RIGHTBOAT, rightBoatSprites);
			}
			else if(!onWater && currentAnimation != RIGHT) {
				setAnimation(RIGHT, rightSprites);
			}
		}
		if(up) {
			int UPBOAT = 7;
			if(onWater && currentAnimation != UPBOAT) {
				setAnimation(UPBOAT, upBoatSprites);
			}
			else if(!onWater && currentAnimation != UP) {
				setAnimation(UP, upSprites);
			}
		}
		
		// pozíció frissítése
		super.update();
		
	}
	
	// Játékos kirajzolása
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}