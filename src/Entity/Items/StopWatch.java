package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.PowerUp;
import Entity.Vittle;
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
	
	public void stopWatchVittles(ArrayList<Vittle> vittles) {
		int i;
		ScheduledExecutorService sed = Executors.newScheduledThreadPool(2);
		
		for(i = 0; i < vittles.size(); i++) {
			Vittle f = vittles.get(i);
			f.moveSpeed = 0.0;
			f.maxSpeed = 0.0;
			
			Runnable resumeMove = () -> {
				f.moveSpeed = 0.3;
				f.maxSpeed = 0.3;
			};
			sed.schedule(resumeMove, 5, TimeUnit.SECONDS);
		}
		sed.shutdown();
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
