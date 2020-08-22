package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.PowerUp;
import TileMap.TileMap;

public class Ions extends PowerUp{
	
	private BufferedImage[] sprites;
	
	public Ions(TileMap tm) {
		super(tm);
		name = "ION";
		
		try {
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
					"/Sprites/Items/ion_spritesheet.png"));
			
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
			animation.setDelay(250);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update() {
		animation.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
