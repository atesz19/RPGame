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
	private ArrayList<Coin> coins = new ArrayList<Coin>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Sparkle> sparkles = new ArrayList<Sparkle>();
	private Hud hud;
	
	// kamera pizíció
	private int xsector;
	private int ysector;
	private int sectorSize;
	
	// események
	private boolean blockInput;
	private boolean eventStart;
	private int eventTick;

	// Játék elején lévő effekt
	private ArrayList<Rectangle> boxes;
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {

		// pálya betöltése
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/testtileset.gif");
		tileMap.loadMap("/Maps/testmap.map");
		
		// játékos létrehozása
		player = new Player(tileMap);
		
		// tárgyak és érmék elhelyezése
		populateCoins();
		populateItems();
		
		// játékos elhelyezése
		player.setTilePosition(17, 17);
		player.setTotalCoins(coins.size());
		
		// kamera pizíció
		sectorSize = GamePanel.SZELESSEG;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPositionImmediately(-xsector * sectorSize, -ysector * sectorSize);

		hud = new Hud(player, coins);
		
		// zene betöltése
		JukeBox.load("/Music/hatterzene.wav", "music1");
		JukeBox.setVolume("music1", -10);
		JukeBox.loop("music1", 1000, 1000, JukeBox.getFrames("music1") - 1000);
		JukeBox.load("/Music/befejez.wav", "finish");
		JukeBox.setVolume("finish", -10);
		
		// hangok betöltése
		JukeBox.load("/SFX/collect.wav", "collect");
		JukeBox.load("/SFX/mapmove.wav", "mapmove");
		JukeBox.load("/SFX/tilechange.wav", "tilechange");
		JukeBox.load("/SFX/splash.wav", "splash");
		
		// event elindítása
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
		
		// billentyűzet figyelése
		handleInput();
		
		// eventek figyelése
		if(player.numCoins() == player.getTotalCoins()) {
			eventFinish();
		}

		if(eventStart) eventStart();

		// kamera
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

		player.update();

		for(int i = 0; i < coins.size(); i++) {
			Coin d = coins.get(i);
			d.update();

			if(player.intersects(d)) {
				
				// felvett érme törlése a listából
				coins.remove(i);
				i--;

				// összegyűjtött érmék számának növelése
				player.collectedCoin();
				
				// új effekt hozzáadása
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(d.getx(), d.gety());
				sparkles.add(s);
				
				// pálya változások
				ArrayList<int[]> ali = d.getChanges();
				for(int[] j : ali) {
					tileMap.setTile(j[0], j[1], j[2]);
				}
				if(ali.size() != 0) {
					JukeBox.play("tilechange");
				} else {
					JukeBox.play("collect");
				}
				
			}
		}
		
		// effektek frissítése
		for(int i = 0; i < sparkles.size(); i++) {
			Sparkle s = sparkles.get(i);
			s.update();
			if(s.shouldRemove()) {
				sparkles.remove(i);
				i--;
			}
		}
		
		// tárgyak frissítése
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

		tileMap.draw(g);
		player.draw(g);
		for(Coin d : coins) {
			d.draw(g);
		}
		for(Sparkle s : sparkles) {
			s.draw(g);
		}
		for(Item i : items) {
			i.draw(g);
		}
		hud.draw(g);

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
		if(eventTick > 33) {
			if(!JukeBox.isPlaying("finish")) {
				JukeBox.stop("music1");
				Data.setTime(player.getTicks());
				blockInput=false;
				gsm.setState(GameStateManager.GAMEOVER);
			}
		}
	}
	
}
