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
	
	protected boolean isWater;
	protected boolean isOil;
	protected boolean isMilk;
	protected boolean isStock;
	protected boolean isWine;
	protected boolean isJuice;
	protected boolean[] liquidTypes = {isWater, isOil, isMilk, isStock, isWine, isJuice};
	
	// Attributes of vittle
	protected int healPoints;
	protected int atkPoints;
	protected int shieldPoints;
	protected double boostPercentage;
	protected int scorePoints;
	protected Vittle[] category = {Fruit, Veggie, Protein, Liquid};
	protected int element;
	protected int health = 1;
	protected int maxHealth = health;
	protected String name;
	
	// Behavior of vittle
	public boolean captured;
	protected boolean flinching;
	protected long flinchTimer;
	protected long fleeTimer;
	public int rangeModifier;
	
	public Vittle(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		terminalSpeed = 10.0;
		
		width = 42;
		height = 42;
		cwidth = 30;
		cheight = 30;
		rangeModifier = 150;
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
	
	public void setProteinType(Vittle v) {
		if(v == category[2]) {
			isProtein = true;
		}
	}
	
	public boolean getProteinType() {
		return isProtein;
	}
	
	public void setLiquidType(Vittle v) {
		if(v == category[3]) {
			isLiquid = true;
		}
	}
	
	public boolean getLiquidType() {
		return isLiquid;
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
	
	public double getHealPoints() {
		return healPoints;
	}
	
	public void setAtkPoints(int atkPoints) {
		this.atkPoints = atkPoints;
	}
	
	public int getAtkPoints() {
		return atkPoints;
	}
	
	public void setShieldPoints(int defPoints) {
		this.shieldPoints = defPoints;
	}
	
	public int getShieldPoints() {
		return shieldPoints;
	}
	
	public void setBoostPercentage(double boostPercentage, boolean b) {
		for(int i = 0; i < liquidTypes.length; i++) {
			if(!liquidTypes[i] == false) {
				return;
			}
		}
		this.boostPercentage = boostPercentage;
	}
	
	public double getBoostPercentage() {
		return boostPercentage;
	}
	
	public int getScore() {
		return scorePoints;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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
