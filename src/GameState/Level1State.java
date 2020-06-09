package GameState;

import java.awt.*;
import java.util.ArrayList;
import Entity.*;
import TileMap.Background;
import TileMap.TileMap;
import Main.GamePanel;
import java.awt.event.KeyEvent;
import Entity.Enemies.*;
import Entity.GameOver;
import Entity.Vittles.Strawberry;

/**
 * A subclass of GameState, it defines the properties of Level 1, such as: graphics, functions, objects, etc.
 * @author Tim Riggins
 *
 */
public class Level1State extends GameState{
	
	// TileMap
	private TileMap tileMap;
	private Background bg;
	private GameOver gameOver;
	
	private Player player;
	private Strawberry test;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<DeathExplosion> deathExplosions;
	private ArrayList<Vittle> vittles;
	
	private HUD hud;
	
	//private AudioPlayer bgMusic;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1); //Corrects "twitching" behavior of moving entities
		
		bg = new Background("/Backgrounds/retro arcade.gif", 0.1); // double value is a move scale
		
		player = new Player(tileMap);
		player.setPosition(50.0, 180.0);
		
		// Populate enemies & vittles
		populateEnemies();
		populateVittles();
		
		//test = new Strawberry(tileMap);
		//test.setPosition(0, 0);
		
		deathExplosions = new ArrayList<DeathExplosion>();
		
		hud = new HUD(player);
		
	}
	/**
	 * This method creates an array of enemies that will be created onto the screen 
	 * according to their individual positions.
	 */
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		Slugger s;
		Point[] points = new Point[] {
			//new Point(200, 200),
			//new Point(300, 200),
			//new Point(600, 200), Enemy won't appear at this location. Tiles blocking it?
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}
	
	private void populateVittles() {
		
		vittles = new ArrayList<Vittle>();
		Strawberry ss;
		Point[] points = new Point[] {
				new Point(200, 160),
				new Point(300, 160)
		};
		for(int i = 0; i < points.length; i++) {
			ss = new Strawberry(tileMap);
			ss.setPosition(points[i].x, points[i].y);
			vittles.add(ss);
		}
	}
	
	public void update() {
		
		// Update player
		player.update();
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(),
				GamePanel.HEIGHT / 2 - player.gety());
		
		// Checks if player died
		if(player.getHealth() == 0) {
			player.getNextPosition();
		}
		
		
		// Update background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// Check if player is attacking
		player.checkAttack(enemies);
		
		// Update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				deathExplosions.add(new DeathExplosion(
						e.getx(), e.gety()));
			}
		}
		
		// Update all vittles
		for(int i = 0; i < vittles.size(); i++) {
			vittles.get(i).update();
		}
		
		// Update death explosions
		for(int i = 0; i < deathExplosions.size(); i++) {
			deathExplosions.get(i).update();
			if(deathExplosions.get(i).shouldRemove()) {
				deathExplosions.remove(i);
				i--;
			}
		}
	}
	
	public void draw(Graphics2D g) {
		// Clear screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// Draw background
		bg.draw(g);
		
		// Draw tilemap
		tileMap.draw(g);
		
		// Draw player
		player.draw(g);
		
		// Draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// Draw vittles
		for(int i = 0; i < vittles.size(); i++) {
			vittles.get(i).draw(g);
		}
		
		// Draw death explosions
		for(int i = 0; i < deathExplosions.size(); i++) {
			deathExplosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
			deathExplosions.get(i).draw(g);
		}
		
		// Draw HUD
		hud.draw(g);
		
		// Draw Game Over
		if(player.isDead() == true) {
			gameOver = new GameOver();
			gameOver.draw(g);
			
		}
	}
	
	public void keyPressed(int k) {
		if(player.isDead() != true) {
			if(k == KeyEvent.VK_A) player.setLeft(true);
			if(k == KeyEvent.VK_D) player.setRight(true);
			if(k == KeyEvent.VK_UP) player.setUp(true);
			if(k == KeyEvent.VK_DOWN) player.setDown(true);
			if(k == KeyEvent.VK_SPACE) player.setJumping(true);
			if(k == KeyEvent.VK_E) player.setGliding(true);			
			if(k == KeyEvent.VK_R) player.setScratching();
			if(k == KeyEvent.VK_F) player.setFiring();
		}
		else {
			if(k == KeyEvent.VK_ENTER) gsm.setState(0);
		}
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}
}
