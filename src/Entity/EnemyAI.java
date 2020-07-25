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
	
	private int i;
	private double enemyPlayerRange;
	private boolean chaseIsOn;
	private boolean playerWithinRange;
	
	public EnemyAI(Player p, ArrayList<Enemy> enemies) {
		this.p = p;
		this.enemies = enemies;
	}
	
	private void makeYouJump() {
		for(i = 0; i < enemies.size(); i++) {
			e = enemies.get(i);
	
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
				System.out.println("I'm following you.\n");
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
				System.out.println("Player out of reach\n");
			}
		}
	}
	
	
	// WIP: Trying to return boolean when a block is hit
	public boolean isThereATileBlocking() {
		if((e.right && e.dx == 0)) {
			System.out.println("hit a wall.");
			return true;
		}
		return false;
	}
	
	public void update() {
		//makeYouJump();
		chasePlayer();
	}
}
