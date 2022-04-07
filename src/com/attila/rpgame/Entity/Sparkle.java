// Simple class that plays animation
// once and is removed.

package com.attila.rpgame.Entity;

import java.awt.Graphics2D;

import com.attila.rpgame.Manager.Content;
import com.attila.rpgame.TileMap.TileMap;

public class Sparkle extends Entity {
	
	private boolean remove;
	
	public Sparkle(TileMap tm) {
		super(tm);
		animation.setFrames(Content.SPARKLE[0]);
		animation.setDelay(5);
		width = height = 16;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) remove = true;
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}
