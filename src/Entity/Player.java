package Entity;

import TileMap.*;
import Audio.AudioPlayer;
import java.util.ArrayList;
import javax.imageio.ImageIO; // Package that contains classes which can read spritesheets
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import Entity.Vittles.*;
import java.util.Arrays;

/**
 * The subclass of MapObject which handles all Player attributes
 * @author Tim Riggins
 *
 */
public class Player extends MapObject {
	
	// Player Variables
	private int health;
	private int maxHealth;
	private int atkPower;
	private int maxPower;
	private int atkPoints;
	private int shield;
	private int maxShield;
	private int shieldPoints;
	private double boostPercent;
	private boolean hasBoost;
	private boolean hasShield;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	private int healPoints;
	private int scorePoints;
	
	
	// Fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// Scratch Attack
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// Gliding
	private boolean gliding;
	
	// Blending
	private final int FRUITS_SIZE_MAX = 3;
	private final int VEGGIES_SIZE_MAX = 3;
	private final int PROTEINS_SIZE_MAX = 3;
	private final int LIQUIDS_SIZE_MAX = 0;
	private boolean onlyHasFruits;
	private boolean onlyHasVeggies;
	private boolean onlyHasProteins;
	
	// Vittles inventory
	public ArrayList<Vittle> fruits = new ArrayList<Vittle>();
	public ArrayList<Vittle> veggies = new ArrayList<Vittle>();
	private ArrayList<Vittle> proteins = new ArrayList<Vittle>();
	public ArrayList<Vittle> liquids = new ArrayList<Vittle>();
	public String[] fruitNames = fruits.toArray(new String[fruits.size()]);
	
	// Animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {8, 11}; // An array of the number of frames for each animation action
	
	// (ENUMS) Animation actions (describes index of array of animation sprites)
	private static final int IDLE = 0;
	private static final int WALKING  = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private static final int BLENDING = 7;
	
	private HashMap<String, AudioPlayer> sfx;
	
	private BufferedImage spritesheet;
	
	/**
	 * Constructor for making player object with its appropriate tilemap
	 * @param tm Tilemap
	 */
	public Player(TileMap tm) {
		super(tm); // relates to MapObject line 71
		
		width = 100; // Dimensions for spritesheet
		height = 100;
		cwidth = 30; // Dimensions for in-game
		cheight = 50;
		
		// These variables define the player physics which had been tested by the instructor as being well-balanced
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		terminalSpeed = 5.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = 5;
		maxHealth = 50;
		atkPower = 0;
		maxPower = 25;
		shield = 0;
		maxShield = 50;
		
		fireCost = 5;
		fireBallDamage = 10;
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 5;
		scratchRange = 40; // In pixels
		
		// Loads sprites
		try {
			
			spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/BMIdleWalkingSpriteNew.gif"
							)
					);
			
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i = 0; i < 2; i++) { // It is less than 2 because that is the # of animation actions
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != SCRATCHING) {
					bi[j] = spritesheet.getSubimage(
							j * width,
							i * height,
							width,
							height
							);
					}
					else { // The attack animation has a larger width
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
								);
					}
				}
				
				sprites.add(bi);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		
	}
	
	// Actual Health
	public int getHealth() {
		return health;
	}
	
	// Actual Max Health
	public int getMaxHealth() {
		return maxHealth;
	}
	
	// Actual Attack Power
	public int getAtkPower() {
		return atkPower;
	}
	
	// Actual Max Attack Power
	public int getMaxPower() {
		return maxPower;
	}
	
	// Actual Shield Power
	public int getShield() {
		return shield;
	}
	
	// Actual Max Shield Power
	public int getMaxShield() {
		return maxShield;
	}
	
	// Potential Heal Points in Inventory
	public int getHealPoints() {
		return healPoints;
	}
	
	// Potential Atk Points in Inventory
	public int getAtkPoints() {
		return atkPoints;
	}
	
	// Potential Shield points in inventory
	public int getShieldPoints() {
		return shieldPoints;
	}
	
	/*public void setHealPoints() {
		for(int i = 0; i < fruits.size(); i++) {
			healPoints += fruits.get(i).getHealPoints();
		}
	}
	*/
	
	/*public void setAtkPoints() {
		for(int i = 0; i < veggies.size(); i++) {
			atkPoints += veggies.get(i).getAtkPoints();
		}
	}
	*/
	
	/*public void setShieldPoints(int defPoints) {
		for(int i = 0; i < proteins.size(); i++) {
			shieldPoints += proteins.get(i).getShieldPoints();
		}
	}
	*/
	
	public void setFiring() {
		firing = true;
	}
	
	public void setScratching() {
		scratching = true;
	}
	
	public void setGliding(boolean b) {
		gliding = b;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setFruits(Vittle v) {
		fruits.add(v);
	}
	
	public int getFruits() {
		return fruits.size();
	}
	
	public void setVeggies(Vittle v) {
		veggies.add(v);
	}
	
	public int getVeggies() {
		return veggies.size();
	}
	
	public void setProteins(Vittle v) {
		proteins.add(v);
	}
	
	public int getProteins() {
		return proteins.size();
	}
	
	public void setLiquid(Vittle v) {
		liquids.add(v);
	}
	
	public String getLiquid() {
		if(liquids.size() < 1) {
			return "none";
		}
		else if(liquids.get(0) instanceof Water) {
			return "WATER";
		}
		return "-1";
	}
	
	public void setScore(int scorePoints) {
		this.scorePoints += scorePoints;
	}
	
	public int getScore() {
		return scorePoints;
	}
	
	public void addToInventory(Vittle v) {
		if(v.isCaptured() == true && v.getFruitType() && fruits.size() < FRUITS_SIZE_MAX) {
			setFruits(v);
		}
		if(v.isCaptured() == true && v.getVegType() && veggies.size() < VEGGIES_SIZE_MAX) {
			setVeggies(v);
		}
		if(v.isCaptured() == true && v.getProteinType() && proteins.size() <= PROTEINS_SIZE_MAX) {
			setProteins(v);
		}
		if(v.isCaptured() == true && v.getLiquidType() && liquids.size() <= LIQUIDS_SIZE_MAX) {
			setLiquid(v);
		}
	}
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// LOOPS THROUGH ENEMY ARRAY
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
		
		// Check scratch attack
		if(scratching) {
			if(facingRight) {
					if(
						e.getx() > x &&
						e.getx() < x + scratchRange && // Checks enemy position against player position from the right
						e.gety() > y - height / 2 &&
						e.gety() < y + height / 2
						) {
						e.hit(scratchDamage);
						if(e.isDead() == true) {
							setScore(e.scorePoints);
						}
					}
							
				}
			
			else {
					if(
							e.getx() < x &&
							e.getx() > x - scratchRange && // Checks enemy position against player position from the left
							e.gety() > y - height / 2 &&
							e.gety() < y + height / 2) {
						e.hit(scratchDamage);
						if(e.isDead() == true) {
							setScore(e.scorePoints);
						}
					}
				}
			}
		
		// Check fireball attack
		for(int j = 0; j < fireBalls.size(); j++) {
			if(fireBalls.get(j).intersects(e)) {
				e.hit(fireBallDamage);
				fireBalls.get(j).setHit();
				if(e.isDead() == true) {
					setScore(e.scorePoints);
				}
				break;
			}
		}
			
		// Check for enemy collision
		if(intersects(e)) {
			if(hasShield) {
				hitShield(e.getDamage(), e);
			} 
			else {
				hit(e.getDamage(), e);
				}
			}
		}
	}
	
	public void checkCapturedFruit(ArrayList<Vittle> vittles) {
		
		// LOOPS THROUGH VITTLE ARRAY
		for(int i = 0; i < vittles.size(); i++) {
			Vittle v = vittles.get(i);
			
			// Check scratch attack
				
			if(scratching) {
				if(facingRight) {
					if(
							v.getx() > x &&
							v.getx() < x + scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							healPoints += v.getHealPoints();
							setScore(v.scorePoints);
						}
					}
				}
				else {
					if(
							v.getx() < x &&
							v.getx() > x - scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							healPoints += v.getHealPoints();
							setScore(v.scorePoints);
						}
					}
				}	
			}
		}	
	}
	
	public void checkCapturedVeg(ArrayList<Vittle> vittles) {
		for(int i = 0; i < vittles.size(); i++) {
			Vittle v = vittles.get(i);
			
			if(scratching) {
				if(facingRight) {	
					if(
							v.getx() > x &&
							v.getx() < x + scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							atkPoints += v.getAtkPoints();
							setScore(v.scorePoints);
						}
					}
				}
				else {
					if(
							v.getx() < x &&
							v.getx() > x - scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							atkPoints += v.getAtkPoints();
							setScore(v.scorePoints);
						}
					}
				}
			}
			
		}
	}
	
	public void checkCapturedProtein(ArrayList<Vittle> vittles) {
		for(int i = 0; i < vittles.size(); i++) {
			Vittle v = vittles.get(i);
			
			if(scratching) {
				if(facingRight) {	
					if(
							v.getx() > x &&
							v.getx() < x + scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							shieldPoints += v.getShieldPoints();
							setScore(v.scorePoints);
						}
					}
				}
				else {
					if(
							v.getx() < x &&
							v.getx() > x - scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							shieldPoints += v.getShieldPoints();
							setScore(v.scorePoints);
						}
					}
				}
			}
		}
	}
	
	public void checkCapturedLiquid(ArrayList<Vittle> vittles) {
		for(int i = 0; i < vittles.size(); i++) {
			Vittle v = vittles.get(i);
			
			if(scratching) {
				if(facingRight) {	
					if(
							v.getx() > x &&
							v.getx() < x + scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							boostPercent += v.getBoostPercentage();
							hasBoost = true;
							}
						}
				}
				else {
					if(
							v.getx() < x &&
							v.getx() > x - scratchRange &&
							v.gety() > y - height / 2 &&
							v.gety() < y + height / 2
							) {
						v.hit(scratchDamage);
						if(v.getHealth() == 0) {
							boostPercent += v.getBoostPercentage();
							hasBoost = true;
							
						}
					}
				}
			}
		}
	}
	
	public void hit(int damage, Enemy e) {
		if(flinching) {
			return;
		}
		health -= damage;
		if(health < 0) {
			health = 0;
		}
		if(health == 0) {
			dead = true;
		}
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void hitShield(int damage, Enemy e) {
		shield -= damage;
		if(shield < 0) {
			shield = 0;
		}
		if(shield == 0) {
			hasShield = false;
		}
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void getNextPosition() {
		
		// Movement
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
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// Cannot move whilst attacking unless in the air
		if(
		(currentAction == SCRATCHING || currentAction == FIREBALL || currentAction == BLENDING) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// Jumping
		if(jumping && !falling) {
			if(currentAction != BLENDING) {
				sfx.put("jump", new AudioPlayer("/SFX/zapsplat_multimedia_game_sound_classic_jump_002_41725.wav"));
			}
			dy = jumpStart;
			falling = true;
		}
		
		// Falling
		if(falling) {
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
			if(dy > terminalSpeed) dy = terminalSpeed;
			if(notOnScreen()) dead = true;
		}
	}
	
	public void checkIfAttacksStopped() {
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
	}
	
	public void fireBallAtk() {
		if(firing && currentAction != FIREBALL) {
			if(atkPower > fireCost) {
				atkPower -= fireCost;
				animation.setDelay(400);
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y + 10);
				fireBalls.add(fb);
			}
		}
	}
	
	public void updateFireBalls() {
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
	}
	
	public void checkFlinching() {
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
	}
	
	public void checkShield() {
		if(shield > 0) {
			hasShield = true;
		}
	}
	
	public void checkDead() {
		if(dead) {
			moveSpeed = 0.0;
			maxSpeed = 0.0;
			health = 0;
		}
	}
	
	public void implementBoost(String name) {
		
		switch (name) {
		
		case "Milk":
			health += (int)(healPoints * boostPercent);
			atkPower += atkPoints;
			shield += 0;
			break;
			
		case "Water":
			health  += (int)(healPoints * boostPercent);
			atkPower += (int)(atkPoints * boostPercent);
			shield += (int)(shieldPoints * boostPercent);
			break;
			
		case "Stock":
			health += healPoints;
			atkPower += (int)(atkPoints * boostPercent);
			shield += (int)(shieldPoints * 1.75);
			break;
			
		case "Wine":
			health += (int)(healPoints * boostPercent);
			atkPower += atkPoints;
			shield += (int)(healPoints * boostPercent);
			break;
			
		case "Oil":
			health += 0;
			atkPower += (int)(atkPoints * 1.5);
			shield += (int)(shieldPoints * boostPercent);
			break;
			
		default:
			break;
		}
		
		hasBoost = false;
		boostPercent = 0.0;
	}
	
	public void removeFromInventory(ArrayList<Vittle> vittle) {
		if(vittle == fruits) {
			for(int j = 0; j < fruits.size(); j++) {
				fruits.remove(j);
				j--;
			}
			if(health > maxHealth) {
				health = maxHealth;
			}
			healPoints = 0;
			onlyHasFruits = false;
		}
		if(vittle == veggies) {
			for(int i = 0; i < veggies.size(); i++) {
				veggies.remove(i);
				i--;
			}
			if(atkPower > maxPower) {
				atkPower = maxPower;
			}
			atkPoints = 0;
			onlyHasVeggies = false;
		}
		if(vittle == proteins) {
			for(int i = 0; i < proteins.size(); i++) {
				proteins.remove(i);
				i--;
			}
			if(shield > maxShield) {
				shield = maxShield;
			}
			shieldPoints = 0;
			onlyHasProteins = false;
		}
	}
	
	public boolean checkForOtherVittles() {
		if(fruits.size() > 0 && veggies.size() == 0 && proteins.size() == 0) {
			onlyHasFruits = true;
			return onlyHasFruits;
		}
		else if(veggies.size() > 0 && fruits.size() == 0 && proteins.size() == 0) {
			onlyHasVeggies = true;
			return onlyHasVeggies;
		}
		else if(proteins.size() > 0 && fruits.size() == 0 && veggies.size() == 0) {
			onlyHasProteins = true;
			return onlyHasProteins;
		}
		return false;
	}
	
	public double calcForCombo(ArrayList<Vittle> v) {
		for(int i = 0; i < v.size(); i++) {
			
		}
		return -1.0;
	}
	
	public double checkForBonuses() {
		if(onlyHasFruits == true || onlyHasVeggies == true || onlyHasProteins == true) {
			return 0.0;
		}
		
		System.out.print(sortVittleArray(fruitNames));
		sortVittleArray(veggies.toArray(new String[veggies.size()]));
		sortVittleArray(proteins.toArray(new String[proteins.size()]));
		System.out.println("afd");
		return -1.0;
	}
	
	public double checkForMaluses() {
		return -1.0;
	}
	
	public boolean sortVittleArray(String[] names) {
		Arrays.sort(names);
		
		for(int i = 0; i < names.length; i++) {
			System.out.println(names[i] + " ");
		}
		return false;
	}
	
	public void checkBlending() {
		if(blending && hasBoost != true) {
			if(currentAction != BLENDING) {
				checkForOtherVittles();
				if(onlyHasFruits == true) {
					healPoints += calcForCombo(fruits);
				}
				if(onlyHasVeggies == true) {
					atkPoints += calcForCombo(veggies);
				}
				if(onlyHasProteins == true) {
					shieldPoints += calcForCombo(proteins);
				}
				checkForBonuses();
				checkForMaluses();
				health += healPoints;
				removeFromInventory(fruits);
				atkPower += atkPoints;
				removeFromInventory(veggies);
				shield += shieldPoints;
				removeFromInventory(proteins);
			}
		}
		else if (blending) {
			if(currentAction != BLENDING) {
				if(fruits.size() > 0 || veggies.size() > 0 || proteins.size() > 0) {
				implementBoost(liquids.get(0).getName());
				removeFromInventory(fruits);
				removeFromInventory(veggies);
				removeFromInventory(proteins);
				liquids.remove(0);
				}
			
			}
		}
	}
	
	public void setAnimation() {
		if(scratching) {
			if(currentAction != SCRATCHING) {
				sfx.put("scratch", new AudioPlayer("/SFX/zapsplat_household_band_aid_plaster_strip_rip_tear_002_11599.wav"));
				currentAction = SCRATCHING;
				/*scratching animation does not exist for now
				animation.setFrames(sprites.get(SCRATCHING));
				*/
				animation.setDelay(50);
				width = 100;
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				currentAction = FIREBALL;
				/*firing animation does not exist for now
				animation.setFrames(sprites.get(0));
				*/
				animation.setDelay(100);
				width = 100;
			}
		}
		else if(blending) {
			if(currentAction != BLENDING) {
				currentAction = BLENDING;
				//Thread.sleep(4000);
				AudioPlayer blendsfx = new AudioPlayer("/SFX/blendersfx1.wav");
				blendsfx.play();
				animation.setDelay(100);
				width = 100;
			}	
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 100;
				}
			}
			else if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 100;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1); // There's only one frame for jumping
				width = 100;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
					currentAction = WALKING;
					animation.setFrames(sprites.get(WALKING));
					animation.setDelay(40);
					width = 100;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 100;
			}
		}
		
		animation.update();
	}
	
	public void setDirection() {
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	/** 
	 * Probably the most important method of this class
	 */
	public void update() {
	
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		notOnScreen();
		checkIfAttacksStopped();
		fireBallAtk();
		updateFireBalls();
		checkFlinching();
		checkShield();
		checkBlending();
		checkDead();
		setAnimation();
		setDirection();
	
	}
	/**
	 * Actually draws the player
	 * @param g
	 */
	public void draw(Graphics2D g) {
		
		setMapPosition(); // THIS METHOD SHOULD FIRST BE CALLED FOR EACH ENTITY
		
		// Draw Fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		// Draw player
		if(flinching && !dead) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
	}
}
