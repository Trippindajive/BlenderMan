package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;


public class Chairmawn extends Enemy {
	
	private BufferedImage[] sprites;
	
	public Chairmawn(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		terminalSpeed = 10.0;
		
		width = 40; // Width of actual spritesheet
		height = 40;
		cwidth = 20; // Width of actual entity
		cheight = 20;
		
		health = maxHealth = 10;
		damage = 1;
		scorePoints = 5;
		xpPoints = 2;
		
		rangeModifier = 100;
		kickBack = 5;
		timeFlinching = 1500;
		
		// Load Sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Enemies/chairmander.png"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
						i * width,
						0,
						width,
						height);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(300);
			
			right = true;
			facingRight = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	private void checkIfStill() {
		if(dx == 0) {
			animation.setFrame(0);
		}
	}
	
	public void randomizeXDirection() {
		
	}
	
	public void update() {
		
		getNextPosition();
		checkTileMapCollision();
		checkWallCollision();
		setPosition(xtemp, ytemp);
		setXPPoints(xpPoints);
		checkIfStill();
		checkFlinching();
		// Update animation
		animation.update();
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		if(flinching && !dead) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
	}
}
