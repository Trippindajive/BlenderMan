package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage; // contact damage
	protected boolean flinching;
	protected long flinchTimer;
	protected int scorePoints;
	protected int xpPoints;
	
	public int rangeModifier; // How close enemy must be close to player for it to follow
	protected int kickBack; // Amount of x distance removed after taking damage
	protected int timeFlinching; // How long flinching will occur
	
	protected int zeroSpeed = 0;
	
	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setXPPoints(int xpPoints) {
		this.xpPoints = xpPoints;
	}
	
	public int getXPPoints() {
		return xpPoints;
	}
	
	public void hit(int damage) {
		if(dead || flinching) 
			return;
		health -= damage;
		if(right) {
			x -= kickBack;
		}
		else if(left) {
			x += kickBack;
		}
		if(health < 0)
			health = 0;
		if(health == 0) {
			dead = true;
		}
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update() {
		
	}
	
	public void checkWallCollision() {
		// If hits wall, move other way
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
	}
	
	protected void checkFlinching() {
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > timeFlinching) { // Used to be 400
				flinching = false;
			}
		}
	}
}
