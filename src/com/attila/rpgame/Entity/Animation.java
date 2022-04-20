// This class takes in an array of images.
// Calling getImage() gives you the appropriate
// image in the animation cycle.

package com.attila.rpgame.Entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame = 0;
	
	private int count = 0;
	private int delay = 2;
	
	private boolean playedOnce = false;
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
	}
	
	public void setDelay(int i) { delay = i; }

	public BufferedImage getImage() { return frames[currentFrame]; }

	public boolean hasPlayedOnce() { return playedOnce; }

	public void update() {
		
		if(delay == -1) return;
		count++;
		if(count == delay) {
			currentFrame++;
			count = 0;
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
	}


	
}