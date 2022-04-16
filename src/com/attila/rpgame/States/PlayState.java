// The main playing GameState.
// Contains everything you need for gameplay:
// Player, TileMap, Diamonds, etc.
// Updates and draws all game objects.

package com.attila.rpgame.States;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.attila.rpgame.Entity.Coin;
import com.attila.rpgame.Entity.Item;
import com.attila.rpgame.Entity.Player;
import com.attila.rpgame.Entity.Sparkle;
import com.attila.rpgame.HUD.Hud;
import com.attila.rpgame.Main.GamePanel;
import com.attila.rpgame.Manager.Data;
import com.attila.rpgame.Manager.GameStateManager;
import com.attila.rpgame.Manager.JukeBox;
import com.attila.rpgame.Manager.Keys;
import com.attila.rpgame.TileMap.TileMap;

public class PlayState extends GameState {

	private Player player;

	private TileMap tileMap;

	private ArrayList<Coin> coins;

	private ArrayList<Item> items;

	private ArrayList<Sparkle> sparkles;
	
	// kamera pizíció
	private int xsector;
	private int ysector;
	private int sectorSize; 

	private Hud hud;
	
	// események
	private boolean blockInput;
	private boolean eventStart;
	private boolean eventFinish;
	private int eventTick;
	
	// transition box
	private ArrayList<Rectangle> boxes;
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		
		// create lists
		coins = new ArrayList<Coin>();
		sparkles = new ArrayList<Sparkle>();
		items = new ArrayList<Item>();
		
		// load map
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/testtileset.gif");
		tileMap.loadMap("/Maps/testmap.map");
		
		// create player
		player = new Player(tileMap);
		
		// fill lists
		populateCoins();
		populateItems();
		
		// initialize player
		player.setTilePosition(17, 17);
		player.setTotalDiamonds(coins.size());
		
		// set up camera position
		sectorSize = GamePanel.SZELESSEG;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPositionImmediately(-xsector * sectorSize, -ysector * sectorSize);
		
		// load hud
		hud = new Hud(player, coins);
		
		// load music
		JukeBox.load("/Music/hatterzene.wav", "music1");
		JukeBox.setVolume("music1", -10);
		JukeBox.loop("music1", 1000, 1000, JukeBox.getFrames("music1") - 1000);
		JukeBox.load("/Music/befejez.wav", "finish");
		JukeBox.setVolume("finish", -10);
		
		// load sfx
		JukeBox.load("/SFX/collect.wav", "collect");
		JukeBox.load("/SFX/mapmove.wav", "mapmove");
		JukeBox.load("/SFX/tilechange.wav", "tilechange");
		JukeBox.load("/SFX/splash.wav", "splash");
		
		// start event
		boxes = new ArrayList<Rectangle>();
		eventStart = true;
		eventStart();
			
	}
	
	private void populateCoins() {
		
		Coin coin;
		
		coin = new Coin(tileMap);
		coin.setTilePosition(20, 20);
		coin.addChange(new int[] { 23, 19, 1 });
		coin.addChange(new int[] { 23, 20, 1 });
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(12, 36);
		coin.addChange(new int[] { 31, 17, 1 });
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(28, 4);
		coin.addChange(new int[] {27, 7, 1});
		coin.addChange(new int[] {28, 7, 1});
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(4, 34);
		coin.addChange(new int[] { 31, 21, 1 });
		coins.add(coin);
		
		coin = new Coin(tileMap);
		coin.setTilePosition(28, 19);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(35, 26);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(38, 36);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(27, 28);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(20, 30);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(14, 25);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(4, 21);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(9, 14);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(4, 3);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(20, 14);
		coins.add(coin);
		coin = new Coin(tileMap);
		coin.setTilePosition(13, 20);
		coins.add(coin);
		
	}
	
	private void populateItems() {
		
		Item item;
		
		item = new Item(tileMap);
		item.setType(Item.AXE);
		item.setTilePosition(26, 37);
		items.add(item);
		
		item = new Item(tileMap);
		item.setType(Item.BOAT);
		item.setTilePosition(12, 4);
		items.add(item);
		
	}
	
	public void update() {
		
		// check keys
		handleInput();
		
		// check events
		if(eventStart) eventStart();
		if(eventFinish) eventFinish();
		
		if(player.numDiamonds() == player.getTotalDiamonds()) {
			eventFinish = blockInput = true;
		}
		
		// update camera
		int oldxs = xsector;
		int oldys = ysector;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPosition(-xsector * sectorSize, -ysector * sectorSize);
		tileMap.update();
		
		if(oldxs != xsector || oldys != ysector) {
			JukeBox.play("mapmove");
		}
		
		if(tileMap.isMoving()) return;
		
		// update player
		player.update();
		
		// update coins
		for(int i = 0; i < coins.size(); i++) {
			
			Coin d = coins.get(i);
			d.update();
			
			// player collects diamond
			if(player.intersects(d)) {
				
				// remove from list
				coins.remove(i);
				i--;
				
				// increment amount of collected coins
				player.collectedDiamond();
				
				// play collect sound
				JukeBox.play("collect");
				
				// add new sparkle
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(d.getx(), d.gety());
				sparkles.add(s);
				
				// make any changes to tile map
				ArrayList<int[]> ali = d.getChanges();
				for(int[] j : ali) {
					tileMap.setTile(j[0], j[1], j[2]);
				}
				if(ali.size() != 0) {
					JukeBox.play("tilechange");
				}
				
			}
		}
		
		// update sparkles
		for(int i = 0; i < sparkles.size(); i++) {
			Sparkle s = sparkles.get(i);
			s.update();
			if(s.shouldRemove()) {
				sparkles.remove(i);
				i--;
			}
		}
		
		// update items
		for(int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(player.intersects(item)) {
				items.remove(i);
				i--;
				item.collected(player);
				JukeBox.play("collect");
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(item.getx(), item.gety());
				sparkles.add(s);
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw coins
		for(Coin d : coins) {
			d.draw(g);
		}
		
		// draw sparkles
		for(Sparkle s : sparkles) {
			s.draw(g);
		}
		
		// draw items
		for(Item i : items) {
			i.draw(g);
		}
		
		// draw hud
		hud.draw(g);
		
		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < boxes.size(); i++) {
			g.fill(boxes.get(i));
		}
		
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			JukeBox.stop("music1");
			gsm.setPaused(true);
		}
		if(blockInput) return;
		if(Keys.isDown(Keys.LEFT)) player.setLeft();
		if(Keys.isDown(Keys.RIGHT)) player.setRight();
		if(Keys.isDown(Keys.UP)) player.setUp();
		if(Keys.isDown(Keys.DOWN)) player.setDown();
		if(Keys.isPressed(Keys.SPACE)) player.setAction();
	}
	
	//===============================================
	
	private void eventStart() {
		eventTick++;
		if(eventTick == 1) {
			boxes.clear();
			for(int i = 0; i < 9; i++) {
				boxes.add(new Rectangle(0, i * 16, GamePanel.SZELESSEG, 16));
			}
		}
		if(eventTick > 1 && eventTick < 32) {
			for(int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if(i % 2 == 0) {
					r.x -= 4;
				}
				else {
					r.x += 4;
				}
			}
		}
		if(eventTick == 33) {
			boxes.clear();
			eventStart = false;
			eventTick = 0;
		}
	}
	
	private void eventFinish() {
		eventTick++;
		if(eventTick == 1) {
			boxes.clear();
			for(int i = 0; i < 9; i++) {
				if(i % 2 == 0) boxes.add(new Rectangle(-128, i * 16, GamePanel.SZELESSEG, 16));
				else boxes.add(new Rectangle(128, i * 16, GamePanel.SZELESSEG, 16));
			}
			JukeBox.stop("music1");
			JukeBox.play("finish");
		}
		if(eventTick > 1) {
			for(int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if(i % 2 == 0) {
					if(r.x < 0) r.x += 4;
				}
				else {
					if(r.x > 0) r.x -= 4;
				}
			}
		}
		if(eventTick > 33) {
			if(!JukeBox.isPlaying("finish")) {
				Data.setTime(player.getTicks());
				gsm.setState(GameStateManager.GAMEOVER);
			}
		}
	}
	
}
