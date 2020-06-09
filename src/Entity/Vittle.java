package Entity;

import TileMap.TileMap;

/**
 * This is the superclass for all vittles.
 * @author Tim Riggins
 *
 */

public class Vittle extends MapObject{
	
	// Attributes of vittle
	protected int healPoints;
	protected int atkPoints;
	protected int defPoints;
	protected int boostPercentage;
	protected int scorePoints;
	protected String[] category = {"Fruit", "Vegetable", "Protein", "Liquid"};
	protected String type;
	protected int health = 1;
	protected int maxHealth = health;
	
	// Behavior of vittle
	protected boolean captured;
	protected boolean flinching;
	protected long flinchTimer;
	protected boolean fleeing;
	protected long fleeTimer;
	
	public Vittle(TileMap tm) {
		super(tm);
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
		if(health == 0) {
			captured = true;
			fleeing = false;
		}
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void setType(int element) {
		type = this.category[element];
	}
	
	public String getType() {
		return type;
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
}
