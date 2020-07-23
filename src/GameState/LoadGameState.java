package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import TileMap.Background;
import Entity.Player;
import java.util.Scanner;

/** The load game screen activated from the main menu
 * 
 * @author Tim Riggins
 *
 */

public class LoadGameState extends GameState {
	
	private Player player;
	private Background bg;
	private Font font;
	private String declaration = "Choose an adventure to resume from!";
	//private double[] gameSaveData = {player.getHealth(), player.getMaxHealth(), player.getAtkPower(), player.getMaxPower(),
			//player.getShield(), player.getMaxShield(), player.getScore()};
	public String[] gameSaves = new String[10];
	
	public LoadGameState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Backgrounds/retro sunshine 640x480.jpg", 1.0);
			bg.setVector(-0.5, 0.0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayer(Player p) {
		this.player = p;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void init() {
		
	}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(declaration, 125, 100);
		
		for(int i = 0; i < gameSaves.length; i++) {
			g.drawString(gameSaves[i], 280, 150 + i * 25);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) 
			gsm.setState(0);
	}
	
	public void keyReleased(int k) {
		
	}

}
