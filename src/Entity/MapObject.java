package Entity;

import TileMap.TileMap;
import java.awt.Rectangle;
import TileMap.Tile;
import Main.GamePanel;
/**
 * This is the superclass that helps define ALL objects in the game (it's gonna be a big 'un)
 * @author Tim Riggins
 *
 */
public abstract class MapObject {
	
	// Tile Properties
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// Position and Vector
	protected double x; // Global Positions
	protected double y;
	public double dx; // Local Positions
	public double dy;
	
	// Dimensions of sprite sheets
	protected int width;
	protected int height;
	
	// Collision Boxes of Entity
	protected int cwidth;
	protected int cheight;
	
	// Collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	public boolean facingRight;
	public boolean facingLeft;
	
	// Movement
	public boolean left;
	public boolean right;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// Actions
	protected boolean blending; // Blending action
	
	// Movement Attributes (Physics)
	public double moveSpeed;
	public double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed; // Gravity
	protected double terminalSpeed; // Terminal Velocity
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	// Setter and Getter Constructor for Tilemaps
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	/**
	 * Checks whether one map object has collided with another map object.
	 * @param o The entity
	 */
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	/**
	 * Helper method for intersects(MapObject o)
	 * @return Rectangle
	 */
	public Rectangle getRectangle() {
		return new Rectangle((int) x - cwidth, (int) y - cheight, cwidth, cheight);
	}
	/**
	 * Determines tile collision in the four corners
	 * @param x
	 * @param y
	 */
	public void calculateCorners(double x, double y) {
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
				leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	/**
	 * Checks whether a tile block has collided with a normal/blocked tile
	 */
	public void checkTileMapCollision() {
		currCol = (int)x / tileSize;
		currRow = (int)y/ tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2; 
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	public int getx() {
		return (int) x;
	}
	
	public double getCurrentXPosition() {
		return (double)x;
	}
	
	public int gety() {
		return (int) y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getCWidth() {
		return cwidth;
	}
	
	public int getCHeight() {
		return cheight;
	}
	
	public void setPosition(double x, double y) { // Entity Position
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * Determines where to draw the entity on the map.
	 * x position + dx positon (final position) compensates for the dislocation between the entity and the map when it moves
	 */
	public void setMapPosition() { // Map Position
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setLeft(boolean b) {
		left = b;
	}
	
	public void setRight(boolean b) {
		right = b;
	}
	
	public void setBlending(boolean b) {
		blending = b;
	}
	
	public void setDown(boolean b) {
		down = b;
	}
	
	public void setJumping(boolean b) {
		jumping = b;
	}
	/** 
	 * Checks whether the entity is actually on the screen.
	 * In other words, it determines whether the entity needs to be drawn or not.
	 */
	public boolean notOnScreen() {
		return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH ||
				y + ymap + height < 0 || y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), 
					(int)(y + ymap - height / 2), null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), 
					(int)(y + ymap - height / 2), -width, height, null);
		}
	}
}
