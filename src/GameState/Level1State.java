package GameState;

import java.awt.*;
import TileMap.Background;
import TileMap.TileMap;
import Main.GamePanel;

/**
 * A subclass of GameState, it defines the properties of Level 1, such as: graphics, functions, etc.
 * @author Tim Riggins
 *
 */
public class Level1State extends GameState{
	
	// TileMap
	private TileMap tileMap;
	private Background bg;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1); // double value is a move scale
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics2D g) {
		// Clear screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		//Draw background
		bg.draw(g);
		
		//Draw tilemap
		tileMap.draw(g);
	}
	
	public void keyPressed(int k) {
		
	}
	
	public void keyReleased(int k) {
		
	}
}
