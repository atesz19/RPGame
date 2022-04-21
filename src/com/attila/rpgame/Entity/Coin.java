// Coin class.
// May contain a list of tileChanges.
// These tileChanges are used to modify
// the tile map upon collection.

package com.attila.rpgame.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.attila.rpgame.Manager.Content;
import com.attila.rpgame.TileMap.TileMap;

public class Coin extends Entity {
	
	private ArrayList<int[]> tileChanges;
	
	public Coin(TileMap tm) {
		
		super(tm);
		
		width = 16;
		height = 16;
		cwidth = 16;
		cheight = 16;

		BufferedImage[] sprites = Content.COIN[0];
		animation.setFrames(sprites);
		animation.setDelay(8);
		
		tileChanges = new ArrayList<int[]>();
		
	}
	
	public void addChange(int[] i) {
		tileChanges.add(i);
	}

	public ArrayList<int[]> getChanges() {
		return tileChanges;
	}
	
	public void update() {
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}
