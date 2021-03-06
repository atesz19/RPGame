// The tile map class contains a loaded tile set
// and a 2d array of the map.
// Each index in the map corresponds to a specific tile.

package com.attila.rpgame.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.attila.rpgame.Main.GamePanel;

public class TileMap {
	
	// pozíciók
	private int x;
	private int y;
	private int xdest;
	private int ydest;
	private final int speed = 5;
	private boolean moving;
	
	// méretek
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	// pálya
	private int[][] map;
	private final int tileSize;
	private int numRows;
	private int numCols;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// rajzolás
	private int rowOffset;
	private int colOffset;
	private final int numRowsToDraw;
	private final int numColsToDraw;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.MAGAS / tileSize + 2;
		numColsToDraw = GamePanel.SZELESSEG / tileSize + 2;
	}
	
	public void loadTiles(String s) {
		try {
			BufferedImage tileset = ImageIO.read(
					Objects.requireNonNull(getClass().getResourceAsStream(s))
			);
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
							col * tileSize,
							0,
							tileSize,
							tileSize
						);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(
							col * tileSize,
							tileSize,
							tileSize,
							tileSize
						);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s) {
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			assert in != null;
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			int width = numCols * tileSize;
			int height = numRows * tileSize;

			xmin = -width;
			xmax = 0;
			ymin = -height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() { return tileSize; }
	public int getx() { return x; }
	public int gety() { return y; }

	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	public int getIndex(int row, int col) {
		return map[row][col];
	}
	public boolean isMoving() { return moving; }
	
	public void setTile(int row, int col, int index) {
		map[row][col] = index;
	}
	public void replace(int i1, int i2) {
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				if(map[row][col] == i1) map[row][col] = i2;
			}
		}
	}
	
	public void setPosition(int x, int y) {
		xdest = x;
		ydest = y;
	}
	public void setPositionImmediately(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void update() {
		if(x < xdest) {
			x += speed;
			if(x > xdest) {
				x = xdest;
			}
		}
		if(x > xdest) {
			x -= speed;
			if(x < xdest) {
				x = xdest;
			}
		}
		if(y < ydest) {
			y += speed;
			if(y > ydest) {
				y = ydest;
			}
		}
		if(y > ydest) {
			y -= speed;
			if(y < ydest) {
				y = ydest;
			}
		}
		
		fixBounds();
		
		colOffset = -this.x / tileSize;
		rowOffset = -this.y / tileSize;

		moving = x != xdest || y != ydest;
		
	}
	
	public void draw(Graphics2D g) {
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			if(row >= numRows) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(
					tiles[r][c].getImage(),
					x + col * tileSize,
					y + row * tileSize,
					null
				);
			}
		}
	}
	
}



















