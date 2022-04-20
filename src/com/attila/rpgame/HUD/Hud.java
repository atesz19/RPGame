// Contains a reference to the Player.
// Draws all relevant information at the
// bottom of the screen.

package com.attila.rpgame.HUD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.attila.rpgame.Entity.Coin;
import com.attila.rpgame.Entity.Player;
import com.attila.rpgame.Main.GamePanel;
import com.attila.rpgame.Manager.Content;

public class Hud {

	private Player player;
	private int numCoin;
	
	public Hud(Player p, ArrayList<Coin> d) {
		player = p;
		numCoin = d.size();
	}
	
	public void draw(Graphics2D g) {
		// alsó sáv kirajzolása
		g.drawImage(Content.BAR[0][0], 0, GamePanel.MAGAS, null);
		
		// érme jelzo sav kirajzolása
		g.setColor(Color.yellow);
		g.fillRect(8, GamePanel.MAGAS + 6, (int)(28.0 * player.numCoins() / numCoin), 4);
		
		// érmék számának kirajzolása
		String s = player.numCoins() + "/" + numCoin;
		Content.drawString(g, s, 60, GamePanel.MAGAS + 4);
		g.drawImage(Content.COIN[0][0], 44, GamePanel.MAGAS, null);
		
		// tárgyak kirajzolása
		if(player.hasBoat()) g.drawImage(Content.ITEMS[0][0], 100, GamePanel.MAGAS, null);
		if(player.hasAxe()) g.drawImage(Content.ITEMS[0][1], 112, GamePanel.MAGAS, null);
		
		// idő kirajzolása
		int minutes = (int) (player.getTicks() / 1800);
		int seconds = (int) ((player.getTicks() / 30) % 60);
		if(minutes < 10) {
			if(seconds < 10) Content.drawString(g, "0" + minutes + ":0" + seconds, 85, 3);
			else Content.drawString(g, "0" + minutes + ":" + seconds, 85, 3);
		}
		else {
			if(seconds < 10) Content.drawString(g, minutes + ":0" + seconds, 85, 3);
			else Content.drawString(g, minutes + ":" + seconds, 85, 3);
		}
	}
	
}
