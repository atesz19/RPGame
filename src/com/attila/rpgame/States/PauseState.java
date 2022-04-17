// The pause GameState can only be activated
// by calling GameStateManager#setPaused(true).

package com.attila.rpgame.States;

import java.awt.*;

import com.attila.rpgame.Manager.Content;
import com.attila.rpgame.Manager.GameStateManager;
import com.attila.rpgame.Manager.JukeBox;
import com.attila.rpgame.Manager.Keys;

public class PauseState extends GameState {
	
	public PauseState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {

		g.setColor(Color.gray);
		g.fillRect(5,5, 119, 119);
		g.setColor(Color.darkGray);
		g.drawRect(5,5,119,119);

		Content.drawString(g, "megallitva", 25, 30);
		
		Content.drawString(g, "nyilak", 7, 80);
		Content.drawString(g, ":mozgas", 58, 80);
		
		Content.drawString(g, "szokoz", 7, 96);
		Content.drawString(g, ":hasznal", 58, 96);
		
	}
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			gsm.setPaused(false);
			JukeBox.resumeLoop("music1");
		}
	}
	
}
