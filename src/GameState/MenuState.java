package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import TileMap.Background;

public class MenuState extends GameState {
	
	private Background bg;
	private int currentChoice = 0;
	private String[] options = {"Start", "Help", "Quit"};
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(0, 128, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			font = new Font("Arial", Font.PLAIN, 12);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		
	}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		// Draw background
		bg.draw(g);
		
		// Draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("BlenderMaaaaan", 40, 70); // In the future, write a function that automatically centers strings
		
		// Draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			// Start
			gsm.setState(STATE.LEVEL_ONE);
		}
		if(currentChoice == 1) {
			// Help
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice < 0) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice > options.length - 1) {
				currentChoice = 0;
			}
		}
	}
	
	public void keyReleased(int k) {
		
	}
}