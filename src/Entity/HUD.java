package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	
	private BufferedImage image;
	private Font font;
	
	public HUD(Player p) {
		player = p;
		
		try {
			
			image = ImageIO.read(getClass().getResourceAsStream(
					"/HUD/hud.gif"));
			
			font = new Font("Arial", Font.PLAIN, 14);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		
		DecimalFormat df = new DecimalFormat(".##");
		
		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(),
				30, 25);
		g.drawString(player.getAtkPower() + "/" + player.getMaxPower(),
				30, 45);
		g.setColor(Color.YELLOW);
		g.drawString("Shield: " + player.getShield() + "/" + player.getMaxShield(),
				0, 65);
		g.setColor(Color.GREEN);
		g.drawString("Energy: " + df.format(player.getEnergy()) + "/" + player.getMaxEnergy(),
				420, 25);
		g.setColor(Color.RED);
		g.drawString("Fruits: " + player.getFruits(), 80, 25);
		g.drawString("Veggies: " + player.getVeggies(), 150, 25);
		g.drawString("Proteins: " + player.getProteins(), 230, 25);
		g.drawString("Liquid: ", 310, 25);
		g.setColor(Color.WHITE);
		g.drawString("SCORE: " + player.getScore(), 0, 85);
		g.drawString("EXP: " + player.getXP() + "/" + player.getMaxXP(), 0, 105);
		g.setColor(Color.YELLOW);
		g.drawString(player.getLiquid(), 360, 25);
		
	}
}
