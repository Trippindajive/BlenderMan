package Entity.Vittles;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class Broccoli extends Vittle {
	
	private BufferedImage[] sprites;
	
	public Broccoli(TileMap tm) {
		super(tm);
		
		// Broccoli Stats
		name = "Broccoli";
		atkPoints = 7;
		shieldPoints = 4;
		scorePoints = 14;
		
		// Load Sprites
		try {
					
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Vittles/spritesheet (3).png"));
					
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
		setAtkPoints(atkPoints);
		getAtkPoints();
		setVegType(Veggie);
		getVegType();
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
