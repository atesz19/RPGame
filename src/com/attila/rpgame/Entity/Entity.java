// The game object superclass.
// This class has all the logic required
// to move around a tile based map.

package com.attila.rpgame.Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.attila.rpgame.TileMap.Tile;
import com.attila.rpgame.TileMap.TileMap;

public abstract class Entity {
	
	// mérete
	protected int width;
	protected int height;
	//hitbox
	protected int cwidth;
	protected int cheight;
	
	// pozíciója
	protected int x;
	protected int y;
	protected int xdest;
	protected int ydest;
	protected int rowTile;
	protected int colTile;
	
	// mozgás
	protected boolean moving;
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	
	// mozgás sebessége
	protected int moveSpeed;
	
	// pálya
	protected TileMap tileMap;
	protected int tileSize;
	protected int xmap;
	protected int ymap;
	
	// animáció
	protected Animation animation;
	protected int currentAnimation;
	
	public Entity(TileMap tm) {
		tileMap = tm;
		tileSize = tileMap.getTileSize();
		animation = new Animation();
	}
	
	public int getx() { return x; }
	public int gety() { return y; }
	
	public void setPosition(int i1, int i2) {
		x = i1;
		y = i2;
		xdest = x;
		ydest = y;
	}
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	public void setTilePosition(int i1, int i2) {
		y = i1 * tileSize + tileSize / 2;
		x = i2 * tileSize + tileSize / 2;
		xdest = x;
		ydest = y;
	}
	
	public void setLeft() {
		if(moving) return;
		left = true;
		moving = validateNextPosition();
	}
	public void setRight() {
		if(moving) return;
		right = true;
		moving = validateNextPosition();
	}
	public void setUp() {
		if(moving) return;
		up = true;
		moving = validateNextPosition();
	}
	public void setDown() {
		if(moving) return;
		down = true;
		moving = validateNextPosition();
	}
	
	public boolean intersects(Entity o) {
		return getRectangle().intersects(o.getRectangle());
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x, y, cwidth, cheight);
	}
	
	// Visszaadja, hogy az entitás mozoghat-e
	// a következő pozícióba vagy nem.
	public boolean validateNextPosition() {
		
		if(moving) return true;
		
		rowTile = y / tileSize;
		colTile = x / tileSize;
		
		if(left) {
			if(colTile == 0 || tileMap.getType(rowTile, colTile - 1) == Tile.BLOCKED) {
				return false;
			}
			else {
				xdest = x - tileSize;
			}
		}
		if(right) {
			if(colTile == tileMap.getNumCols() || tileMap.getType(rowTile, colTile + 1) == Tile.BLOCKED) {
				return false;
			}
			else {
				xdest = x + tileSize;
			}
		}
		if(up) {
			if(rowTile == 0 || tileMap.getType(rowTile - 1, colTile) == Tile.BLOCKED) {
				return false;
			}
			else {
				ydest = y - tileSize;
			}
		}
		if(down) {
			if(rowTile == tileMap.getNumRows() - 1 || tileMap.getType(rowTile + 1, colTile) == Tile.BLOCKED) {
				return false;
			}
			else {
				ydest = y + tileSize;
			}
		}
		
		return true;
		
	}
	
	// Kiszámítja a cél koordinátáit.
	public void getNextPosition() {
		
		if(left && x > xdest) x -= moveSpeed;
		else left = false;
		if(left && x < xdest) x = xdest;
		
		if(right && x < xdest) x += moveSpeed;
		else right = false;
		if(right && x > xdest) x = xdest;
		
		if(up && y > ydest) y -= moveSpeed;
		else up = false;
		if(up && y < ydest) y = ydest;
		
		if(down && y < ydest) y += moveSpeed;
		else down = false;
		if(down && y > ydest) y = ydest;
	}
	
	public void update() {
		
		// következő pozíció
		if(moving) getNextPosition();
		
		// check stop moving
		if(x == xdest && y == ydest) {
			left = right = up = down = moving = false;
			rowTile = y / tileSize;
			colTile = x / tileSize;
		}
		
		// animációk frisítése
		animation.update();
		
	}
	
	// pálya kirajzolása
	public void draw(Graphics2D g) {
		setMapPosition();
		g.drawImage(
			animation.getImage(),
			x + xmap - width / 2,
			y + ymap - height / 2,
			null
		);
	}
	
}
