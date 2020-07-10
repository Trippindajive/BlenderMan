package Entity.Vittles;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class Water extends Vittle {
	
	private BufferedImage[] sprites;
	
	public Water(TileMap tm) {
		super(tm);
		
		name = "Water";
		boostPercentage = 1.25;
	
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Vittles/spritesheet(water).png"));
			
			sprites = new BufferedImage[7];
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
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		checkWallCollision();
		setPosition(xtemp, ytemp);
		setLiquidType(Liquid);
		getLiquidType();
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
