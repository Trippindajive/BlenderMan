package Entity;

import TileMap.*;
import Audio.AudioPlayer;
import java.util.ArrayList;
import javax.imageio.ImageIO; // Package that contains classes which can read spritesheets
import javax.sound.sampled.FloatControl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import GameState.LoadGameState;
import Entity.Vittles.*;

/**
 * The subclass of MapObject which handles all Player attributes
 * @author Tim Riggins
 *
 *
 * WIP: Lines 331 saveGame() method
 * WIP: Lines 1110 bleedEnergy() method
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
	private double energy;
	private double maxEnergy;
	
	private int secondCounter = 0;
	private long energyDecayRate;
	private long previousTime = System.nanoTime() / 1000000000;
	
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
	
	// Use Powerups
	public boolean usingPowerUp;
	public String powerUpName;
	public ArrayList<PowerUp> powerups = new ArrayList<PowerUp>();
	
	// Save Game Data
	public String playerSave = "";
	
	// Vittles inventory
	private ArrayList<Vittle> fruits = new ArrayList<Vittle>();
	private ArrayList<Vittle> veggies = new ArrayList<Vittle>();
	private ArrayList<Vittle> proteins = new ArrayList<Vittle>();
	private ArrayList<Vittle> liquids = new ArrayList<Vittle>();
	private final int MAX_INVENTORY_SIZE = 3;
	private String[] fruitNames = new String[MAX_INVENTORY_SIZE ];
	private String[] veggieNames = new String[MAX_INVENTORY_SIZE ];
	private String[] proteinNames = new String[MAX_INVENTORY_SIZE ];
	private double bonusMultiplierFruit = 0.0;
	private double bonusMultiplierVeg = 0.0;
	private double bonusMultiplierPro= 0.0;
	private double malusModifier = 0.0;
	
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
	private static final int POWERUP = 8;
	
	/**
	 * @param Sound effects that the player can play
	 */
	private HashMap<String, AudioPlayer> sfx;
	private AudioPlayer BLENDING_SFX = new AudioPlayer("/SFX/blendersfx1.wav");
	private AudioPlayer FIRING_SFX = new AudioPlayer("/SFX/ranged_attack.wav");
	private AudioPlayer JUMPING_SFX = new AudioPlayer("/SFX/zapsplat_multimedia_game_sound_classic_jump_002_41725.wav");
	private AudioPlayer DAMAGED_SFX = new AudioPlayer("/SFX/player_hit.wav");
	private AudioPlayer POWERUP_OBTAINED_SFX = new AudioPlayer("/SFX/powerup_obtained.wav");
	private AudioPlayer SCRATCHING_SFX = new AudioPlayer("/SFX/zapsplat_household_band_aid_plaster_strip_rip_tear_002_11599.wav");
	private AudioPlayer ERROR_SFX = new AudioPlayer("/SFX/error_tone.wav");
	private AudioPlayer POWERUP_USED_SFX = new AudioPlayer("/SFX/powerup_used.wav");
	private AudioPlayer ION_OBTAINED_SFX = new AudioPlayer("/SFX/ion_obtained.wav");
	
	private BufferedImage spritesheet;
	
	private LoadGameState lsm;
	
	FloatControl gainControl;
	
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
		moveSpeed = 0.3; // 0.3
		maxSpeed = 1.6; // 1.6
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		terminalSpeed = 5.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = 10;
		maxHealth = 50;
		atkPower = 10;
		maxPower = 25;
		shield = 0;
		maxShield = 50;
		energy = maxEnergy = 10000.00;
		
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
	
	public void setScore(int scorePoints) {
		this.scorePoints += scorePoints;
	}
	
	public int getScore() {
		return scorePoints;
	}
	
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public void setMaxEnergy(double maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	public double getMaxEnergy() {
		return maxEnergy;
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
	
	public void setFiring() {
		firing = true;
	}
	
	public void setScratching() {
		scratching = true;
	}
	
	public void setGliding(boolean b) {
		gliding = b;
	}
	
	public void setPowerUp() {
		if(powerups.size() > 0) {
			usingPowerUp = true;
			powerups.get(0).usePowerUp(powerups.get(0).getName());
			sfx.put("powerupUsed", POWERUP_USED_SFX);
			sfx.get("powerupUsed").play();
			powerups.remove(0);
		} else {
			sfx.put("powerupError", ERROR_SFX);
			sfx.get("powerupError").play();
		}
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
	
	/**
	 * 
	 * @param Adds powerups to the player's inventory
	 */
	public void setPowerUps(PowerUp p) {
		if(this.powerups.size() == 0)
		powerups.add(p);
	}
	
	/**
	 * 
	 * @return Returns name of powerup in inventory to HUD
	 */
	public String getPowerUps() {
		if(powerups.size() == 0) {
			return "none";
		}
		else {
			return powerups.get(0).getName();
		}
	}
	
	/**
	 *  WIP: need to assign player name to game data,
	 *  load it to LoadGameState, and pause game while typing
	 */
	public void saveGame() {
		Scanner scnr = new Scanner(System.in);
		
		System.out.println("Enter your name to save the game...");
		playerSave = scnr.nextLine();
		System.out.println("The game is saved as: " + playerSave);
		
		lsm.gameSaves[0] = playerSave;
		
		scnr.close();
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
				hit(e.getDamage(), e, e.knockBackPlayer);
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
	
	public void checkObtainedPowerUps(ArrayList<PowerUp> powerups) {
		
		for(int i = 0; i < powerups.size(); i++) {
			PowerUp pu = powerups.get(i);
			int xRange = pu.getx() - getx();
			int yRange = pu.gety() - gety();
			
			if(xRange >= -25 && xRange <= 25 && yRange >= -30 && yRange <= 30) {
				pu.obtained = true;
				sfx.put("powerup obtained", POWERUP_OBTAINED_SFX);
				sfx.put("ion obtained", ION_OBTAINED_SFX);
				if(!pu.getName().equals("ION")) {
					sfx.get("powerup obtained").play();
					setPowerUps(pu);
				}
				else if(pu.getName().equals("ION")) {
					gainControl = (FloatControl) 
					sfx.get("ion obtained").clip.getControl(
					FloatControl.Type.MASTER_GAIN);
					
					gainControl.setValue(-10.0f);
					sfx.get("ion obtained").play();
					energy = energy * 1.33;
					if(energy > maxEnergy) {
						energy = maxEnergy;
					}
				}
			}
		}
	}
	
	public void grabItem(ArrayList<PowerUp> item) {
		
	}
	
	public void hit(int damage, Enemy e, int knockback) {
		if(flinching) {
			return;
		}
		sfx.put("playerHit", DAMAGED_SFX);
		health -= damage;
		if(e.right && !dead) {
			sfx.get("playerHit").play();
			if((x + e.knockBackPlayer < getx()) == !intersects(this)) {
				x += e.knockBackPlayer;
			}
		}
		if(e.left && !dead) {
			sfx.get("playerHit").play();
			if((x - e.knockBackPlayer > getx()) ==!intersects(this)) {
				x -= e.knockBackPlayer;
			}			
		}
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
		if(jumping && !falling && !blending) {
			if(currentAction != BLENDING) {
				sfx.put("jump", JUMPING_SFX);
				sfx.get("jump").play();
				
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
		if(currentAction == POWERUP) {
			if(animation.hasPlayedOnce()) usingPowerUp = false;
		}
	}
	
	public void fireBallAtk() {
		if(firing && currentAction != FIREBALL) {
			if(atkPower >= fireCost) {
				atkPower -= fireCost;
				animation.setDelay(400);
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y + 10);
				fireBalls.add(fb);
				sfx.put("firing", FIRING_SFX);
				sfx.get("firing").play();
			}
			else if(atkPower < fireCost) {
				sfx.put("no fire", ERROR_SFX);
				sfx.get("no fire").play();
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
			energy = 0.0;
		}
	}
	
	public void checkEnergy() {
		if(energy < 0.1) {
			dead = true;
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
			System.out.println("Boost detected!!!");
			System.out.println("TOTAL HEALTH RECOVERED: " + health);
			System.out.println("TOTAL ATTACK POWER RECOVERED: " + atkPower);
			System.out.println("TOTAL SHIELD POWER RECOVERED: " + shield);
			System.out.println();
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
			System.out.println("only fruits");
			return onlyHasFruits;
		}
		else if(veggies.size() > 0 && fruits.size() == 0 && proteins.size() == 0) {
			onlyHasVeggies = true;
			System.out.println("only veggies");
			return onlyHasVeggies;
		}
		else if(proteins.size() > 0 && fruits.size() == 0 && veggies.size() == 0) {
			onlyHasProteins = true;
			System.out.println("only proteins");
			return onlyHasProteins;
		}
		return false;
	}
	
	public int calcForCombo(ArrayList<Vittle> v) {
		for(int i = 0; i < v.size(); i++) {
			System.out.println("FINISH CALCFORCOMBO");
		}
		
		return -100;
	}
	
	public void checkForBonuses() {
		int i, j, numRepeatFruit = 0, numRepeatVeg = 0, numRepeatPro = 0;
		
		System.out.println("BEGIN CHECKING FOR BONUSES");
		// Populate vittles' String arrays
		createStringArrays();
		
		// Check through vittle String arrays for any duplicate vittles
		for(i = 0; i < fruits.size(); i++) {
			for(j = i + 1; j < fruits.size(); j++) {
				if(fruitNames[i].equals(fruitNames[j])) {
					numRepeatFruit++;
				}
			}
		}
		for(i = 0; i < veggies.size(); i++) {
			for(j = i + 1; j < veggies.size(); j++) {
				if(veggieNames[i].equals(veggieNames[j])) {
					numRepeatVeg++;
				}
			}
		}
		for(i = 0; i < proteins.size(); i++) {
			for(j = i + 1; j < proteins.size(); j++) {
				if(proteinNames[i].equals(proteinNames[j])) {
					numRepeatPro++;
				}
			}
		}
		
		
		if(numRepeatFruit == 0 && fruits.size() == 3) {
			bonusMultiplierFruit = 2.00;
			System.out.println("All fruit different: "  + bonusMultiplierFruit);
		}
		else if(numRepeatFruit == 0 && fruits.size() == 2) {
			bonusMultiplierFruit = 1.50;
			System.out.println("Both fruit different: " + bonusMultiplierFruit);
		}
		else if(numRepeatFruit == 1 && fruits.size() == 3) {
			bonusMultiplierFruit = 1.75;
			System.out.println("2 of the same fruit but three fruits total: "  + bonusMultiplierFruit);
		}
		else {
			bonusMultiplierFruit = 1.00;
			System.out.println("No fruit bonuses: "  + bonusMultiplierFruit);
		}
		if(numRepeatVeg == 0 && veggies.size() == 3) {
			bonusMultiplierVeg = 2.00;
			System.out.println("All veggies different: " + bonusMultiplierVeg);
		}
		else if(numRepeatVeg == 0 && veggies.size() == 2) {
			bonusMultiplierVeg = 1.50;
			System.out.println("Both veggies different: " + bonusMultiplierVeg);
		}
		else if(numRepeatVeg == 1 && veggies.size() == 3) {
			bonusMultiplierVeg = 1.75;
			System.out.println("2 of the same veggies but three veggies total: " + bonusMultiplierVeg);
		}
		else {
			bonusMultiplierVeg = 1.00;
			System.out.println("No veggie bonuses: " + bonusMultiplierVeg);
		}
		if(numRepeatPro == 0 && proteins.size() == 3) {
			bonusMultiplierPro = 2.00;
			System.out.println("All proteins different: " + bonusMultiplierPro);
		}
		else if(numRepeatPro == 0 && proteins.size() == 2) {
			bonusMultiplierPro = 1.50;
			System.out.println("Both proteins different: " + bonusMultiplierPro);
		}
		else if(numRepeatPro == 1 && proteins.size() == 3) {
			bonusMultiplierPro = 1.75;
			System.out.println("2 of the same proteins but three proteins total: " + bonusMultiplierPro);
		}
		else {
			bonusMultiplierPro = 1.00;
			System.out.println("No protein bonuses: " + bonusMultiplierPro);
		}
		
		healPoints = (int)(healPoints * bonusMultiplierFruit);
		atkPoints = (int)(atkPoints * bonusMultiplierVeg);
		shieldPoints = (int)(shieldPoints * bonusMultiplierPro);
		
		System.out.println("healpoints: " + healPoints + " atkPoints: " + atkPoints + " shieldPoints: " + shieldPoints);
		System.out.println("END CHECKING FOR BONUSES");
		
	}
	
	public void createStringArrays() {
		int i;
		
		for(i = 0; i < fruits.size(); i++) {
			fruitNames[i] = fruits.get(i).getName();
			System.out.println(fruitNames[i]);
		}
		
		for(i = 0; i < veggies.size(); i++) {
			veggieNames[i] = veggies.get(i).getName();
			System.out.println(veggieNames[i]);
		}
		
		for(i = 0; i < proteins.size(); i++) {
			proteinNames[i] = proteins.get(i).getName();
			System.out.println(proteinNames[i]);
		}
	}
	
	public void clearStringArrays() {
		int i;
		for(i = 0; i < fruitNames.length; i++) {
			fruitNames[i] = null;
		}
		for(i = 0; i < veggieNames.length; i++) {
			veggieNames[i] = null;
		}
		for(i = 0; i < proteinNames.length; i++) {
			proteinNames[i] = null;
		}
	}
	
	public void checkForMaluses() {
		
		System.out.println("BEGIN CHECKING FOR MALUSES");
		
		if((fruits.size() > 0 && veggies.size() > 0 && proteins.size() == 0) ||
				(fruits.size() > 0 && veggies.size() == 0 && proteins.size() > 0) ||
				(fruits.size() == 0 && veggies.size() > 0 && proteins.size() > 0)) {
			malusModifier = 0.75;
			System.out.println("Malus because of fruits with veggies, fruits with proteins, or veggies with proteins: " + malusModifier);
		}
		else if(fruits.size() > 0 && veggies.size() > 0 && proteins.size() > 0) {
			malusModifier = 0.50;
			System.out.println("Malus because of fruits with veggies and proteins: " + malusModifier);
		}
		else {
			System.out.println("No maluses detected");
		}
		
		healPoints = (int)(healPoints * malusModifier);
		atkPoints = (int)(atkPoints * malusModifier);
		shieldPoints = (int)(shieldPoints * malusModifier);
		
		System.out.println("END CHECKING FOR MALUSES");
		System.out.println("TOTAL HEAL POINTS RECOVERED: " + healPoints);
		System.out.println("TOTAL ATK POINTS RECOVERED: " + atkPoints);
		System.out.println("TOTAL SHIELD POINTS RECOVERED: " + shieldPoints);
		System.out.println();
		
	}
	
	public long setTimeDelay(int key) {
		
		long currentTime = System.nanoTime() / 1_000_000_000; //(System.nanoTime() - previousTime) / 1_000_000_000;
		long elapsedTime = currentTime - previousTime;
		energyDecayRate = elapsedTime;
		
		if(currentTime == previousTime + key) {
			secondCounter++;
			previousTime = System.nanoTime() / 1000000000;
			
		} return elapsedTime;
	}
	
	private void doBlendingStuff() {
		if(blending && hasBoost != true && left == false && right == false && jumping == false) {
			energy -= 20.0;
			System.out.println("CONSUMED 20 ENERGY");
			checkForOtherVittles();
			if(onlyHasFruits == true) {
				//healPoints += calcForCombo(fruits);
				health += healPoints;
				removeFromInventory(fruits);
			}
			else if(onlyHasVeggies == true) {
				//atkPoints += calcForCombo(veggies);
				atkPower += atkPoints;
				removeFromInventory(veggies);
			}
			else if(onlyHasProteins == true) {
				//shieldPoints += calcForCombo(proteins);
				shield += shieldPoints;
				removeFromInventory(proteins);
			}
			else {
				checkForBonuses();
				checkForMaluses();
				health += healPoints;
				removeFromInventory(fruits);
				atkPower += atkPoints;
				removeFromInventory(veggies);
				shield += shieldPoints;
				removeFromInventory(proteins);
				clearStringArrays();
				bonusMultiplierFruit = bonusMultiplierVeg = bonusMultiplierPro = 0;
			}
		}
		else if (blending && left == false && right == false && jumping == false) {
			energy -= 30.0;
			System.out.println("CONSUMED 30 ENERGY");
			if(fruits.size() > 0 || veggies.size() > 0 || proteins.size() > 0) {
			implementBoost(liquids.get(0).getName());
			removeFromInventory(fruits);
			removeFromInventory(veggies);
			removeFromInventory(proteins);
			liquids.remove(0);
			}
		}
	}
	
	public void checkBlending() {
		ScheduledExecutorService bud = Executors.newScheduledThreadPool(2);
		
		if(blending) {
		Runnable blendingTakesTime = () -> {
			doBlendingStuff();
		};
	
		bud.schedule(blendingTakesTime, 1, TimeUnit.SECONDS);
		
		}
		
		bud.shutdown();
		
	}
	
	public void setAnimation() {
		if(scratching) {
			if(currentAction != SCRATCHING) {
				currentAction = SCRATCHING;
				sfx.put("scratch", SCRATCHING_SFX);
				sfx.get("scratch").play();
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
		else if(blending && falling == false && jumping == false) {
			if(currentAction != BLENDING) {
				currentAction = BLENDING;
				sfx.put("blending", BLENDING_SFX);
				sfx.get("blending").play();
				checkBlending();
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(50);
				width = 100;
			}
		}
		else if(usingPowerUp) {
			if(currentAction != POWERUP) {
				currentAction = POWERUP;
				animation.setDelay(100);
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
	 * Subtracts 10.1 energy per every two in-game seconds
	 */
	public void bleedEnergy() {
		if(energyDecayRate == 2 && !dead) {
			energy -= 10.10;
		}
		if(gliding) {
			energy -= 1.00;
		}
	}
	
	/** 
	 * Probably the most important method of this class
	 */
	public void update() {
	
		getNextPosition();
		checkTileMapCollision(); // Perhaps the key to fixing enemy atk bug?
		setPosition(xtemp, ytemp);
		notOnScreen();
		checkIfAttacksStopped();
		fireBallAtk();
		updateFireBalls();
		checkFlinching();
		checkShield();
		checkDead();
		checkEnergy();
		setAnimation();
		setDirection();
		setTimeDelay(2);
		bleedEnergy();
	
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
