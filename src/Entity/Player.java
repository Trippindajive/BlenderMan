package Entity;

import TileMap.*;
import Audio.AudioPlayer;

import java.util.ArrayList;
import javax.imageio.ImageIO; // Package that contains classes which can read spritesheets
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * The subclass of MapObject which handles all Player attributes
 * @author Tim Riggins
 *
 */
public class Player extends MapObject {
	
	// Player Variables
	private int health;
	private int maxHealth;
	private int fire;
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
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
	
	// Vittles inventory
	private ArrayList<Vittle> fruits = new ArrayList<Vittle>();
	//private ArrayList<Vittle> veggies = new ArrayList<Veggies>();
	//private ArrayList<Vittle> proteins = new ArrayList<Proteins>();
	//private ArrayList<Vittle> liquids = new ArrayList<Liquids>();
	
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
		
		health = 10;
		maxHealth = 50;
		maxFire = 2500;
		fire = 0;
		
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 8;
		scratchRange = 40; // In pixels
		
		// Loads sprites
		try {
			
			spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/Player/BMSpriteIdleWalk.gif"
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
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getFire() {
		return fire;
	}
	
	public int getMaxFire() {
		return maxFire;
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
	
	public boolean isDead() {
		return dead;
	}
	
	public void setFruits(Vittle v) {
		fruits.add(v);
	}
	
	public int getFruits() {
		return fruits.size();
	}
	
	public void addToInventory(Vittle v) {
		if(v.isCaptured() == true) {
			setFruits(v);
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
					}
							
				}
			
			else {
					if(
							e.getx() < x &&
							e.getx() > x - scratchRange && // Checks enemy position against player position from the left
							e.gety() > y - height / 2 &&
							e.gety() < y + height / 2) {
						e.hit(scratchDamage);
					}
				}
			}
		
		// Check fireball attack
		for(int j = 0; j < fireBalls.size(); j++) {
			if(fireBalls.get(j).intersects(e)) {
				e.hit(fireBallDamage);
				fireBalls.get(j).setHit();
				break;
			}
		}
			
			// Check for enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
		}
	}
	
	public void checkCaptured(ArrayList<Vittle> vittles) {
		
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
					}
				}
			}
		}
	}
	
	public void hit(int damage) {
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
	
	/**
	 * Determines the next position the player must be at
	 */
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
		(currentAction == SCRATCHING || currentAction == FIREBALL) &&
		!(jumping || falling)) {
			dx = 0;
		}
		
		// Jumping
		if(jumping && !falling) {
			sfx.put("jump", new AudioPlayer("/SFX/zapsplat_multimedia_game_sound_classic_jump_002_41725.wav"));
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
		}
	}
	/**
	 * Updates the positioning of the player
	 */
	public void update() {
		
		// Update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// Checks if attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false;
		}
	
		
		// Fireball attack
		//fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) {
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y + 10);
				fireBalls.add(fb);
			}
		}
		
		// Update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		// Check for flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// Check if dead
		if(dead) {
			moveSpeed = 0.0;
			maxSpeed = 0.0;
			health = maxHealth = 0;
			fire = maxFire = 0;
			
			
			
			/*sfx.put("jump", null);
			sfx.put("scratch", null);
			sfx.put("blend", null);
			*/
		}
		
		// Blending action
		if(up) {
			if(currentAction != BLENDING) {
				long elapsed = (System.nanoTime() - 100) / 1000000;
				if(elapsed < 1000000000) {
					up = false;
					AudioPlayer blendsfx = new AudioPlayer("/SFX/blendersfx1.wav");
					blendsfx.play();
				}
				
				animation.setDelay(1000);
			}
		}
		
		// Set animation
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
		
		// Set direction of player after attacking
		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
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
