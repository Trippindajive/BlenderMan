package GameState;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Audio.AudioPlayer;
import Entity.*;

import TileMap.Background;
import TileMap.TileMap;
import Main.GamePanel;
import java.awt.event.KeyEvent;
import Entity.Enemies.*;
import Entity.Items.Ions;
import Entity.Items.StopWatch;
import Entity.Vittles.*;
import Entity.AI.ChairmawnAI;
import Entity.AI.VittleAI;

/**
 * A subclass of GameState, it defines the properties of Level 1, such as: graphics, functions, objects, etc.
 * @author Tim Riggins
 *
 */
public class Level1State extends GameState{
	
	/**
	  @param TileMap
	 */
	private TileMap tileMap;
	private Background bg;
	private GameOver gameOver;
	
	/**
	 *@param List of entities in this level
	 */
	public Player player;
	public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public ArrayList<Vittle> Fruits = new ArrayList<Vittle>();
	public ArrayList<Vittle> Veggies = new ArrayList<Vittle>();
	public  ArrayList<Vittle> Proteins = new ArrayList<Vittle>();
	private ArrayList<Vittle> Liquids = new ArrayList<Vittle>();
	
	/**
	  @param List of entities
	 */
	Enemy e;
	Vittle f; // fruit entity
	Vittle v; // vegetable entity
	Vittle p; // protein entity
	Vittle l; // liquid entity
	PowerUp pw; // powerup entity
	
	/**
	 * @param List of items/powerups in this level
	 */
	private ArrayList<PowerUp> PowerUps = new ArrayList<PowerUp>();
	
	/**
	  @param List of AI entities in this level
	 */
	private ChairmawnAI chairmawnAI;
	private VittleAI fruitAI;
	private VittleAI veggieAI;
	private VittleAI proteinAI;
	
	/**
	  @param List of fx in this level
	 */
	private ArrayList<DeathExplosion> deathExplosions = new ArrayList<DeathExplosion>();;
	
	/**
	  @param Location arrays for vittles
	 */
	Point[] F; // strawberries
	Point[] V; // broccoli
	Point[] P; // cheese
	Point[] L; // water
	Point[] FB; // banana
	Point[] FO; // orange
	
	public int killScheduler = 0;
	public ArrayList<AIBase> aibase = new ArrayList<AIBase>();
	
	private HUD hud;
	
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/TaylorDemo");
		tileMap.setPosition(75, 365);
		tileMap.setTween(0.2); //Corrects "twitching" behavior of moving entities, tutorial(1)
		
		bg = new Background("/Backgrounds/world 1-1 placeholder C.jpg", 0.2); // double value is a move scale

		player = new Player(tileMap);
		player.setPosition(75, 365);
		
		// Populate enemies & vittles
		populateEnemies();
		populateVittles();
		
		// Populate powerups
		populatePowerUps();
		
		// Populate AI entities
		chairmawnAI = new ChairmawnAI(player, enemies);
		fruitAI = new VittleAI(player, Fruits);
		veggieAI = new VittleAI(player, Veggies);
		proteinAI = new VittleAI(player, Proteins);
		
		hud = new HUD(player);
	}
	
	/** Random generator for enemy spawns
	 * 
	 */
	private void enemySpawner() {
		
	}
	
	private void populateEnemies() {
		
		Chairmawn e;
		
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
			e = new Chairmawn(tileMap);
			e.setPosition(points[i].x, points[i].y);
			enemies.add(e);
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
			new Point(400, 365)
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
				new Point(300, 365),
				new Point(1525, 160),
				new Point(750, 80),
				new Point(1200, 260),
				new Point(1500, 130),
				new Point(2830, 650)
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
	
	private void populatePowerUps() {
		int i;
		StopWatch stopwatch;
		Ions ions;
		
		Point[] stopwatchP = new Point[] {
				new Point(230, 270),
				new Point(780, 270)
		};
		for(i = 0; i < stopwatchP.length; i++) {
			stopwatch = new StopWatch(tileMap);
			stopwatch.setPosition(stopwatchP[i].x, stopwatchP[i].y);
			PowerUps.add(stopwatch);
		}
		
		Point[] ionsP = new Point[] {
				new Point(280, 270),
				new Point(940, 270)
		};
		for(i = 0; i < ionsP.length; i++) {
			ions = new Ions(tileMap);
			ions.setPosition(ionsP[i].x, ionsP[i].y);
			PowerUps.add(ions);
		}
	}
	
	private void checkForStopWatch() {
		StopWatch stopwatch = new StopWatch(tileMap);
		
		if(player.usingPowerUp) {
			for(int i = 0; i < PowerUps.size(); i++) {
				if(PowerUps.get(i).getName().equals("STOPWATCH")) {
					stopwatch = (StopWatch) PowerUps.get(i);
				}
			}
			stopwatch.stopWatchVittles(Fruits);
			stopwatch.stopWatchVittles(Veggies);
			stopwatch.stopWatchVittles(Proteins);
			player.usingPowerUp = false;
		}
	}
	
	public void killSchedule() {
		ArrayList<AIBase> r = makeAIArrayList();
		r.forEach(ai -> ai.murderAI());
	}
	
	private ArrayList<AIBase> makeAIArrayList() {
		aibase.add(chairmawnAI);
		aibase.add(fruitAI);
		aibase.add(veggieAI);
		aibase.add(proteinAI);
		return aibase;
	}
	
	private void checkPlayerDead() {
		if(player.getHealth() == 0) {
			player.getNextPosition();
		}
	}
	
	private void updateEnemies() {
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
	}
	
	private void updateFruits() {
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
	}
	
	private void updateVeggies() {
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
	}
	
	private void updateProteins() {
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
	}
	
	private void updateLiquids() {
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
	}
	
	private void updatePowerUps() {
		for(int i = 0; i < PowerUps.size(); i++) {
			pw = PowerUps.get(i);
			pw.update();
			if(pw.obtained) {
				PowerUps.remove(i);
			}
		}
	}
	
	private void updateDeathExplosions() {
		for(int i = 0; i < deathExplosions.size(); i++) {
			deathExplosions.get(i).update();
			if(deathExplosions.get(i).shouldRemove()) {
				deathExplosions.remove(i);
				i--;
			}
		}
	}
	
	private void updateAI() {
		chairmawnAI.update();
		fruitAI.update();
		veggieAI.update();
		proteinAI.update();
	}
	
	public void update() {
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(), // Sets how "camera" should move in-game horizontally
				GamePanel.HEIGHT / 2 - player.gety()); // Sets how "camera" should move in-game vertically
		
		// UPDATE BACKGROUND
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		player.update();
		checkPlayerDead();
		player.checkAttack(enemies);
		player.checkCapturedFruit(Fruits);
		player.checkCapturedVeg(Veggies);
		player.checkCapturedProtein(Proteins);
		player.checkCapturedLiquid(Liquids);
		player.checkObtainedPowerUps(PowerUps);
		
		updateEnemies();
		updateFruits();
		updateVeggies();
		updateProteins();
		updateLiquids();
		updatePowerUps();
		updateAI();
		
		checkForStopWatch();
		
		updateDeathExplosions();
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
		if(!player.isDead()) {
			player.draw(g);
		}
		
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
		
		// Draw powerups
		for(int i = 0; i < PowerUps.size(); i++) {
			PowerUps.get(i).draw(g);
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
			killSchedule();
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
			if(k == KeyEvent.VK_Q) player.setPowerUp();
			//if(k == KeyEvent.VK_P) paused = true;
		}
		else {
			if(k == KeyEvent.VK_ENTER) gsm.setState(0);
		}
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setBlending(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}
}
