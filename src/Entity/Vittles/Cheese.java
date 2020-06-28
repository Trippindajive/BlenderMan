package Entity.Vittles;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
public class Cheese extends Vittle{
	
	private BufferedImage[] sprites;
	
	public Cheese(TileMap tm) {
		super(tm);
		
		healPoints = 2;
		defPoints = 5;
		scorePoints = 15;
		
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Vittles/spritesheet (5).png"));
					
			sprites = new BufferedImage[5];
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
		setDefPoints(defPoints);
		getDefPoints();
		setProteinType(Protein);
		getProteinType();
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
