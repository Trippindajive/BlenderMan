package Entity;

import TileMap.TileMap;
/**
 * This is the superclass for all vittles.
 * @author Tim Riggins
 *
 */

public class Vittle extends MapObject{
	
	protected Vittle Fruit;
	protected Vittle Veggie;
	protected Vittle Protein;
	protected Vittle Liquid;
	
	protected boolean isFruit;
	protected boolean isVeggie;
	protected boolean isProtein;
	protected boolean isLiquid;
	protected boolean[] types = {isFruit, isVeggie, isProtein, isLiquid};
	
	//protected boolean isVeggie;
	
	// Attributes of vittle
	protected int healPoints;
	protected int atkPoints;
	protected int defPoints;
	protected int boostPercentage;
	protected int scorePoints;
	protected Vittle[] category = {Fruit, Veggie, Protein, Liquid};
	protected int element;
	//protected boolean type;
	protected int health = 1;
	protected int maxHealth = health;
	
	// Behavior of vittle
	protected boolean captured;
	protected boolean flinching;
	protected long flinchTimer;
	//protected boolean fleeing;
	protected long fleeTimer;
	
	public Vittle(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		terminalSpeed = 10.0;
		
		width = 40;
		height = 40;
		cwidth = 30;
		cheight = 30;
	}
	
	public boolean isCaptured() {
		return captured;
	}
	
	public void hit(int damage) {
		if(captured) 
			return;
		health -= damage;
		if(health < 0) 
			health = 0;
		if(health == 0) 
			captured = true;
		//fleeing = false;
	}
	
	public void setFruitType(Vittle v) {
		if(v == category[0]) {
			isFruit = true;
		}
	}
	
	public boolean getFruitType() {
		return isFruit;
	}
	
	public void setVegType(Vittle v) {
		if(v == category[1]) {
			isVeggie = true;
		}
	}
	
	public boolean getVegType() {
		return isVeggie;
	}
	
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealPoints(int healPoints) {
		this.healPoints = healPoints;
	}
	
	public int getHealPoints() {
		return healPoints;
	}
	
	public void setAtkPoints(int atkPoints) {
		this.atkPoints = atkPoints;
	}
	
	public int getAtkPoints() {
		return atkPoints;
	}
	
	public void setDefPoints(int defPoints) {
		this.defPoints = defPoints;
	}
	
	public int getDefPoints() {
		return defPoints;
	}
	
	public void update() {
		
	}
	
	public void getNextPosition() {
		
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		if(falling) {
			dy += fallSpeed;
		}
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
