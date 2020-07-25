package Entity;

import TileMap.TileMap;
import java.awt.Rectangle;
import Entity.Enemies.*;
import Entity.Player;
import java.util.ArrayList;

public class EnemyAI {
	
	private Player p;
	private ArrayList<Enemy> enemies;
	private Enemy e;
	private Enemy ej;
	
	private int i;
	private double enemyPlayerRange;
	private boolean chaseIsOn;
	private boolean playerWithinRange;
	private boolean imTouchingYou;
	private int collisionRange;
	
	public EnemyAI(Player p, ArrayList<Enemy> enemies) {
		this.p = p;
		this.enemies = enemies;
	}
	
	private void makeYouJump() {
		for(Enemy e : enemies) {
			if(e.dy <= 0) {
				e.setPosition(e.getCurrentXPosition(), 1);
			}
		}
	}
	
	public boolean checkIfPlayerWithinReach() {
		enemyPlayerRange = p.getCurrentXPosition() - e.getCurrentXPosition(); 
		
		if(enemyPlayerRange <= 100 && enemyPlayerRange >= -100) {
			chaseIsOn = true;
			playerWithinRange = true;
		}
		else {
			chaseIsOn = false;
			playerWithinRange = false;
		}
		
		return chaseIsOn;
	}
	
	public void chasePlayer() {
		for(i = 0; i < enemies.size(); i++) {
			e = enemies.get(i);
			if(checkIfPlayerWithinReach()) {
				//System.out.println("I'm following you.\n");
				if(e.right && e.getx() != p.getx() && p.getx() < e.getx() && playerWithinRange) {
					e.right = false;
					e.left = true;
				}
				else if(e.left && e.getx() != p.getx() && p.getx() > e.getx() && playerWithinRange) {
					e.left = false;
					e.right = true;
				}
			}
			else {
				//System.out.println("Player out of reach\n");
				chaseIsOn = false;
			}
		}
	}
	
	public void intersects() {
		for(i = 0; i < enemies.size(); i++) {
			e = enemies.get(i);
			for(int j = i + 1; j < enemies.size(); j++) {
				ej = enemies.get(j);
				collisionRange = e.getx() - ej.getx();
				//System.out.println(collisionRange);
				if(collisionRange >= -20 && collisionRange <= 20) {
					imTouchingYou = true;
				}
				else {
					imTouchingYou = false;
				}
			}
		}
	}
	
	public void doNotTouchMe(boolean b) {
		for(i = 0; i < enemies.size(); i++) {
			if(b)
				enemies.get(i).x = 1.0;
		}
	}
	
	
	public void update() {
		//makeYouJump();
		chasePlayer();
		intersects();
		doNotTouchMe(imTouchingYou);
			
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
