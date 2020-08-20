package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.PowerUp;
import TileMap.TileMap;

public class StopWatch extends PowerUp{

	private BufferedImage[] sprites;
	
	public StopWatch(TileMap tm) {
		super(tm);
		name = "STOPWATCH";
		
		
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Items/stopwatch_spritesheet.png"));
			
			sprites = new BufferedImage[2];
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
			animation.setDelay(250);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update() {
		//getNextPosition();
		
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
