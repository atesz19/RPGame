// Congratulations for finishing the game.
// Gives you a rank based on how long it took
// you to beat the game.

// Under two minutes = Speed Demon
// Under three minutes = Adventurer
// Under four minutes = Beginner
// Four minutes or greater = Bumbling Idiot

package com.attila.rpgame.States;

import java.awt.Color;
import java.awt.Graphics2D;

import com.attila.rpgame.Main.GamePanel;
import com.attila.rpgame.Manager.Content;
import com.attila.rpgame.Manager.Data;
import com.attila.rpgame.Manager.GameStateManager;
import com.attila.rpgame.Manager.JukeBox;
import com.attila.rpgame.Manager.Keys;

public class OverState extends GameState {

	private int rank;
	
	public OverState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		if(Data.getTime() < 3600) rank = 1;
		else if(Data.getTime() < 5400) rank = 2;
		else rank = 3;
	}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(164, 198, 222));
		g.fillRect(0, 0, GamePanel.SZELESSEG, GamePanel.MAGASSAG);
		
		Content.drawString(g, "jatekido", 30, 36);
		
		int minutes = (int) (Data.getTime() / 1800);
		int seconds = (int) ((Data.getTime() / 30) % 60);
		if(minutes < 10) {
			if(seconds < 10) Content.drawString(g, "0" + minutes + ":0" + seconds, 44, 48);
			else Content.drawString(g, "0" + minutes + ":" + seconds, 44, 48);
		}
		else {
			if(seconds < 10) Content.drawString(g, minutes + ":0" + seconds, 44, 48);
			else Content.drawString(g, minutes + ":" + seconds, 44, 48);
		}
		
		Content.drawString(g, "rang", 48, 66);
		if(rank == 1) Content.drawString(g, "profi", 44, 78);
		else if(rank == 2) Content.drawString(g, "halado", 41, 78);
		else if(rank == 3) Content.drawString(g, "kezdo", 44, 78);
		
		Content.drawString(g, "Nyomj meg egy", 12, 110);
		Content.drawString(g, "gombot a", 12, 120);
		Content.drawString(g, "kilepeshez", 12, 130);
	}
	
	public void handleInput() {
		if(Keys.anyKeyPress()) {
			System.exit(0);
		}
	}
	
}