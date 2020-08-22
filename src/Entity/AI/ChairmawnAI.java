package Entity.AI;

import Entity.AIBase;
import Entity.Enemy;
import Entity.Player;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ChairmawnAI extends AIBase{
	
	private Player p;
	private ArrayList<Enemy> enemies;
	private Enemy e;
	private Enemy ej;
	
	private int i;
	private double enemyPlayerRange;
	private boolean chaseIsOn;
	private boolean playerWithinRange;
	private int collisionRange;
	
	//ScheduledExecutorService sed = Executors.newScheduledThreadPool(2);
	
	public ChairmawnAI(Player p, ArrayList<Enemy> enemies) {
		this.p = p;
		this.enemies = enemies;
		
		Runnable randMove = () -> {
			Random r = new Random();
			randomizeXMovement(r);
		};
		
		sed.scheduleAtFixedRate(randMove, 2, 4, TimeUnit.SECONDS);
		
	}
	
	private boolean checkIfPlayerWithinReach(int rangeModifier) {
		enemyPlayerRange = p.getCurrentXPosition() - e.getCurrentXPosition(); 
		
		if(enemyPlayerRange <= rangeModifier && enemyPlayerRange >= -rangeModifier) {
			chaseIsOn = true;
			playerWithinRange = true;
		}
		else {
			chaseIsOn = false;
			playerWithinRange = false;
		}
		
		return chaseIsOn;
	}

	private void chasePlayer() {
		for(i = 0; i < enemies.size(); i++) {
			e = enemies.get(i);
			if(checkIfPlayerWithinReach(e.rangeModifier) && !p.isDead()) {
				if(e.right && e.getx() != p.getx() && p.getx() < e.getx() && playerWithinRange) {
					e.right = false;
					e.left = true;
					e.maxSpeed = 1.3;
					e.moveSpeed = 1.3;
				}
				else if(e.left && e.getx() != p.getx() && p.getx() > e.getx() && playerWithinRange) {
					e.left = false;
					e.right = true;
					e.maxSpeed = 1.3;
					e.moveSpeed = 1.3;
				}
			}
			else {
				chaseIsOn = false;
				e.maxSpeed = 0.4;
				e.moveSpeed = 0.4;
			}
		}
	}
	
	// Want a better enemy/enemy collision system but this will work for now
	private void intersects() {
		for(i = 0; i < enemies.size(); i++) {
			e = enemies.get(i);
			for(int j = i + 1; j < enemies.size(); j++) {
				ej = enemies.get(j);
				collisionRange = e.getx() - ej.getx();
				if(collisionRange >= -20 && collisionRange <= 20) {
					if(e.right && ej.right) {
						e.dx = -e.moveSpeed;
					}
					else if(e.left && ej.left) {
						e.dx = -e.moveSpeed;
					}
				}
			}
		}
	}
	
	private void randomizeXMovement(Random r) {
		for(i = 0; i < enemies.size(); i++) {
			e = enemies.get(i);
			int randomDir = r.nextInt(2) - 1;
			if(randomDir == -1) {
				e.dx = e.moveSpeed;
			}
			else if(randomDir == 0) {
				e.dx = -e.moveSpeed;
			}
		}
	}
	
	/*public void doNotTouchMe(boolean b) {
		for(i = 0; i < enemies.size(); i++) {
			if(b)
				enemies.get(i).x = 1.0;
		}
	}*/
	
	
	public void update() {
		chasePlayer();
		intersects();
	}
	
	// WIP: Trying to return boolean when a block is hit
	public boolean isThereATileBlocking() {
		if((e.right && e.dx == 0)) {
			//System.out.println("hit a wall.");
			return true;
		}
		return false;
	}
}
