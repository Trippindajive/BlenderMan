package GameState;

import java.awt.*;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.*;
import TileMap.Background;
import TileMap.TileMap;
import Main.GamePanel;
import java.awt.event.KeyEvent;
import Entity.Enemies.*;
import Entity.Vittles.*;
import Entity.BasicEnemyAI;

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
	
	public Player player;
	
	//private LoadGameState lsm;
	
	//private boolean paused;
	
	public ArrayList<Enemy> enemies;
	private ArrayList<Vittle> Fruits = new ArrayList<Vittle>();
	private ArrayList<Vittle> Veggies = new ArrayList<Vittle>();
	private ArrayList<Vittle> Proteins = new ArrayList<Vittle>();
	private ArrayList<Vittle> Liquids = new ArrayList<Vittle>();
	private ArrayList<DeathExplosion> deathExplosions;
	
	
	
	// Location arrays for objects
	Point[] F; // strawberries
	Point[] V; // broccoli
	Point[] P; // cheese
	Point[] L; // water
	Point[] FB; // banana
	Point[] FO; // orange
	
	private HUD hud;
	
	private BasicEnemyAI basicAI;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/TaylorDemo");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1); //Corrects "twitching" behavior of moving entities
		
		bg = new Background("/Backgrounds/retro arcade.gif", 0.1); // double value is a move scale

		player = new Player(tileMap);
		player.setPosition(80.0, 365);
		
		// Populate enemies & vittles
		populateEnemies();
		populateVittles();
		
		deathExplosions = new ArrayList<DeathExplosion>();
		
		hud = new HUD(player);

		basicAI = new BasicEnemyAI(player, enemies);
		
	}
	/**
	 * This method creates an array of enemies that will be created onto the screen 
	 * according to their individual positions.
	 */
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>(); // Slugger Enemies
		Chairmawn s;
		Point[] points = new Point[] {
			//new Point(200, 365),
			new Point(300, 365),
			new Point(400, 365),
			//new Point(100, 365),
			//new Point(150, 365),
			//new Point(180, 365),
			new Point(1680, 200),
			new Point(1730, 370),
			new Point(1800, 370),
			new Point(2270, 170),
			new Point(2420, 200),
			new Point(2750, 350),
			new Point(2800, 350),
			new Point(2900, 350),
			new Point(2970, 350),
			new Point(2670, 650)
			
		};
		for(int i = 0; i < points.length; i++) {
			s = new Chairmawn(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}
	
	private void populateVittles() {
		int i;
		Strawberry fs = new Strawberry(tileMap);
		Broccoli vb = new Broccoli(tileMap);
		Cheese pc = new Cheese(tileMap);
		Water lw = new Water(tileMap);
		Banana fb = new Banana(tileMap);
		Orange fo = new Orange(tileMap);
		
		F = new Point[] {
				//new Point(250, 160),
				//new Point(300, 160),
				//new Point(270, 160),
				new Point(860, 130),
				new Point(1300, 160),
				new Point(1800, 160),
				new Point(2850, 650),
				new Point(3000, 650),
				new Point(2660, 690)
		};
		for(i = 0; i < F.length; i++) {
			fs = new Strawberry(tileMap);
			fs.setPosition(F[i].x, F[i].y);
			Fruits.add(fs);	
		}
		
		FB = new Point[] {
				//new Point(350, 130),
				new Point(1680, 160)
		};
		for(i = 0; i < FB.length; i++) {
			fb = new Banana(tileMap);
			fb.setPosition(FB[i].x, FB[i].y);
			Fruits.add(fb);
		}
		
		FO = new Point[] {
				//new Point(150, 130),
			new Point(400, 100)
		};
		for(i = 0; i < FO.length; i++) {
			fo = new Orange(tileMap);
			fo.setPosition(FO[i].x, FO[i].y);
			Fruits.add(fo);
		}
		
		V = new Point[] {
				//new Point(100, 160),
				//new Point(150, 160),
				//new Point(270, 160),
				//new Point(300, 160),
				new Point(1525, 160),
				new Point(750, 80)
		};
		for(i = 0; i < V.length; i++) {
			vb = new Broccoli(tileMap);
			vb.setPosition(V[i].x, V[i].y);
			Veggies.add(vb);
		}
		
		P = new Point[] {
				//new Point(860, 130),
				new Point(1000, 130),
				new Point(1820, 130)
		};
		for(i = 0; i < P.length; i++) {
			pc = new Cheese(tileMap);
			pc.setPosition(P[i].x, P[i].y);
			Proteins.add(pc);
		}
		
		L = new Point[] {
				//new Point(200, 130),
				//new Point(400, 130),
				new Point(900, 140)
		};
		for(i = 0; i < L.length; i++) {
			lw = new Water(tileMap);
			lw.setPosition(L[i].x, L[i].y);
			Liquids.add(lw);
		}
	}

	
	public void update() {
		Enemy e; // enemy entity
		Vittle f; // fruit entity
		Vittle v; // vegetable entity
		Vittle p; // protein entity
		Vittle l; // liquid entity
		
		/*if(paused) {
			sleep();
			System.out.println("Game Paused");
		}
		*/
		
		// Update player
		player.update();
		
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(),
				GamePanel.HEIGHT / 2 - player.gety());
		
		// Checks if player died
		if(player.getHealth() == 0) {
			player.getNextPosition();
		}
		
		// UPDATE BACKGROUND
		//bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// Check if player is attacking
		player.checkAttack(enemies);
		player.checkCapturedFruit(Fruits);
		player.checkCapturedVeg(Veggies);
		player.checkCapturedProtein(Proteins);
		player.checkCapturedLiquid(Liquids);
		
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
		
		// Update all liquid vittles
		for(int i = 0; i < Liquids.size(); i++) {
			l = Liquids.get(i);
			l.update();
			if(l.isCaptured()) {
				player.addToInventory(l);
				Liquids.remove(i);
				i--;
				deathExplosions.add(new DeathExplosion(
						l.getx(), l.gety()));
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
		
		// Update Artificial Intelligence
		basicAI.update();
		
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
		
		for(int i = 0; i < Veggies.size(); i++) {
			Veggies.get(i).draw(g);
		}
		
		for(int i = 0; i < Proteins.size(); i++) {
			Proteins.get(i).draw(g);
		}
		
		for(int i = 0; i < Liquids.size(); i++) {
			Liquids.get(i).draw(g);
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
			//if(k == KeyEvent.VK_P) paused = true;
		}
		else {
			if(k == KeyEvent.VK_ENTER) gsm.setState(0);
		}
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) 
			player.setLeft(false);
		if(k == KeyEvent.VK_D) 
			player.setRight(false);
		if(k == KeyEvent.VK_UP) 
			player.setBlending(false);
		if(k == KeyEvent.VK_DOWN) 
			player.setDown(false);
		if(k == KeyEvent.VK_SPACE) 
			player.setJumping(false);
		if(k == KeyEvent.VK_E) 
			player.setGliding(false);
		
	}
}
