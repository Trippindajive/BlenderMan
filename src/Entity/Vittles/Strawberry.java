package Entity.Vittles;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class Strawberry extends Vittle{
	
	private BufferedImage[] sprites;
	
	public Strawberry(TileMap tm) {
		super(tm);
		
		// Strawberry Physical Attributes
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		terminalSpeed = 10.0;
		
		width = 100;
		height = 100;
		cwidth = 50;
		cheight = 100;
		
		type = category[0];
		
		// Load Sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Vittles/spritesheet.png"));
			
			sprites = new BufferedImage[6];
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
	
	private void getNextPosition() {
		
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
	
	public void update() {
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
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
		
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}
}
