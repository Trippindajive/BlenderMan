package TileMap;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import Main.GamePanel;

/**
 * This defines the properties of the background images.
 * @author Tim Riggins
 *
 */
public class Background {
	
	private BufferedImage image;
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		 
		try {
			 image = ImageIO.read(getClass().getResourceAsStream(s));
			 moveScale = ms;
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	}
	
	// Ensures image doesn't get drawn off the screen
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	// Ensures screen is constantly drawn as player moves across screen
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
		if(x < 0) {
			g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
		}
		if(x > 0) {
			g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
		}
	}
}
