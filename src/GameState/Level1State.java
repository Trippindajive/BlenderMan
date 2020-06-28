package GameState;

import java.awt.*;
import java.util.ArrayList;
import Entity.*;
import TileMap.Background;
import TileMap.TileMap;
import Main.GamePanel;
import java.awt.event.KeyEvent;
import Entity.Enemies.*;
import Entity.Vittles.*;;

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
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Vittle> Fruits = new ArrayList<Vittle>();
	private ArrayList<Vittle> Veggies = new ArrayList<Vittle>();
	private ArrayList<Vittle> Proteins = new ArrayList<Vittle>();
	private ArrayList<DeathExplosion> deathExplosions;
	
	// Location arrays for objects
	Point[] p; // strawberries
	Point[] pp; // broccoli
	Point[] ppp; // cheese
	
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
			new Point(200, 200),
			new Point(300, 200),
			new Point(400, 100),// Enemy won't appear at this location. Tiles blocking it?
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
		
		Strawberry fs = new Strawberry(tileMap);
		Broccoli vb = new Broccoli(tileMap);
		Cheese pc = new Cheese(tileMap);
		
		p = new Point[] {
				new Point(250, 160),
				new Point(300, 160)
		};
		for(int i = 0; i < p.length; i++) {
			fs = new Strawberry(tileMap);
			fs.setPosition(p[i].x, p[i].y);
			Fruits.add(fs);
		}
		
		pp = new Point[] {
				new Point(100, 160)
		};
		for(int i = 0; i < pp.length; i++) {
			vb = new Broccoli(tileMap);
			vb.setPosition(pp[i].x, pp[i].y);
			Veggies.add(vb);
		}
		
		ppp = new Point[] {
				new Point(200, 130)
		};
		for(int i = 0; i < ppp.length; i++) {
			pc = new Cheese(tileMap);
			pc.setPosition(ppp[i].x, ppp[i].y);
			Proteins.add(pc);
		}
	}
		
	public void update() {
		Enemy e; // enemy entity
		Vittle f; // fruit entity
		Vittle v; // vegetable entity
		Vittle p; // protein entity
		
		
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
		player.checkCapturedFruit(Fruits);
		player.checkCapturedVeg(Veggies);
		player.checkCapturedProtein(Proteins);
		
		// Update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				deathExplosions.add(new DeathExplosion(
						e.getx(), e.gety()));
			}
		}
		
		// Update all fruit vittles
		for(int i = 0; i < Fruits.size(); i++) {
			f = Fruits.get(i);
			f.update();
			if(f.isCaptured()) {
				player.addToInventory(f);
				Fruits.remove(i);
				i--;
				deathExplosions.add(new DeathExplosion(
						f.getx(), f.gety()));
			}
		}
		
		// Update all veggie vittles
		for(int i = 0; i < Veggies.size(); i++) {
			 v = Veggies.get(i);
			v.update();
			if(v.isCaptured()) {
				player.addToInventory(v);
				Veggies.remove(i);
				i--;
				deathExplosions.add(new DeathExplosion(
						v.getx(), v.gety()));
			}
		}
		
		// Update all protein vittles
		for(int i = 0; i < Proteins.size(); i++) {
			p = Proteins.get(i);
			p.update();
			if(p.isCaptured()) {
				player.addToInventory(p);
				Proteins.remove(i);
				i--;
				deathExplosions.add(new DeathExplosion(
						p.getx(), p.gety()));
			}
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
		for(int i = 0; i < Fruits.size(); i++) {
			Fruits.get(i).draw(g);
		}
		
		// Draw vittles
		for(int i = 0; i < Veggies.size(); i++) {
			Veggies.get(i).draw(g);
		}
		
		for(int i = 0; i < Proteins.size(); i++) {
			Proteins.get(i).draw(g);
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
			if(k == KeyEvent.VK_UP) player.setBlending(true);
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
		if(k == KeyEvent.VK_A) {
			player.setLeft(false);
		}
		if(k == KeyEvent.VK_D) {
			player.setRight(false);
		}
		if(k == KeyEvent.VK_UP) {
			player.setBlending(false);
		}
		if(k == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		if(k == KeyEvent.VK_SPACE) {
			player.setJumping(false);
		}
		if(k == KeyEvent.VK_E) {
			player.setGliding(false);
		}
	}
}
