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
	
	public Enemy(TileMap tm) {
		super(tm);
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void hit(int damage) {
		if(dead || flinching) 
			return;
		health -= damage;
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
}
