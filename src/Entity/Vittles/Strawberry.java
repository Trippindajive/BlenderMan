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
		
		// Strawberry Stats
		name = "Strawberry";
		healPoints = 5;
		atkPoints = 2;
		scorePoints = 10;
		
		// Load Sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Vittles/spritesheet.png"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
						i * width,
						0,
						width,
						height
						);
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
	
	@Override
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		checkWallCollision();
		setPosition(xtemp, ytemp);
		setHealPoints(healPoints);
		getHealPoints();
		setFruitType(Fruit);
		getFruitType();
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
