package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;
import TileMap.Background;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * A subclass of GameState, it defines the properties of the main menu.
 * @author Tim Riggins
 *
 */
public class MenuState extends GameState {
	
	private Background bg;
	private int currentChoice = 0;
	private String[] options = {"New Game", "Load Game", "Password(WIP)", "Leaderboards(WIP)", 
			"Controls", "Mute Music", "Quit"};
	private Color titleColor;
	private Font titleFont;
	private Font font;
	private HashMap<String, AudioPlayer> bgMusic;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg = new Background("/Backgrounds/menu placeholder.jpg", 1.0);
			bg.setVector(-0.5, 0);
			
			titleColor = new Color(0, 128, 0);
			titleFont = new Font("Century Gothic", Font.ITALIC, 72);
			
			font = new Font("Arial", Font.PLAIN, 24);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		bgMusic = new HashMap<String, AudioPlayer>();
		bgMusic.put("menu music", new AudioPlayer("/Music/Red-MarKer-DMC-12-Gauge.wav"));
	
		
	}
	
	public void init() {
		
	}
	
	public void update() {
		bg.update();
	}
	
	/*private void playMusic() {
		bgMusic.get("menu music").play();
	}*/
	
	public void draw(Graphics2D g) {
		// Draw background
		bg.draw(g);
		
		// Draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("BlenderMan", 240, 100); // In the future, write a function that automatically centers strings
		
		// Draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 380, 220 + i * 25);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			// Start
			//gsm.setState(STATE.LEVEL_ONE);
			gsm.setState(1);
		}
		if(currentChoice == 1) {
			//Log-Ins
			gsm.setState(2);
		}
		if(currentChoice == 2) {
			// Passwords
		}
		if(currentChoice == 3) {
			//Leaderboards
		}
		if(currentChoice == 4) {
			//Help
			gsm.setState(11);
		}
		if(currentChoice == 5) {
			// Mute Music
			gsm.musicOn = true;
		}
		if(currentChoice == 6) {
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
		if(k == KeyEvent.VK_M) {
			gsm.musicOn = true;
		}
	}
	
	public void keyReleased(int k) {
		
	}
}
