package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import TileMap.Background;

/**
 * The help screen from the main menu which displays controls.
 * @author Tim Riggins
 *
 */

public class HelpScreen extends GameState {
	
	private Background bg;
	private Font font;
	private String[] controls = {"A - Move Left", "D - Move Right", "Space - Jump", 
			"E - Float", "R - Melee", "F - Fire"};
	
	public HelpScreen(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Backgrounds/retro sunshine 640x480.jpg", 1.0);
			bg.setVector(-0.5, 0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		
	}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		// Draw controls
		g.setFont(font);
		for(int i = 0; i < controls.length; i++) {
			g.setColor(Color.WHITE);
			g.drawString(controls[i], 280, 180 + i * 25);
		}
		g.setColor(Color.RED);
		g.drawString("PRESS ENTER TO RETURN TO MENU", 130, 400);
		 
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER)
			gsm.setState(0);
	}
	
	public void keyReleased(int k) {
		
	}
	
}
