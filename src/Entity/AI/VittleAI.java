package Entity.AI;

import Entity.Vittle;
import Entity.Player;
import Entity.PowerUp;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VittleAI {
	
	private Player p;
	private ArrayList<Vittle> vittles;
	private Vittle v;
	private PowerUp powerup;
	
	private int i;
	private double vittlePlayerRange;
	private boolean fleeing;
	private boolean playerWithinRange;
	
	ScheduledExecutorService sed = Executors.newScheduledThreadPool(2);
	
	public VittleAI(Player p, ArrayList<Vittle> vittles) {
		this.p = p;
		this.vittles = vittles;
		if(p.powerups.size() > 0) {
			this.powerup = p.powerups.get(0);
		}
		
		Runnable randMove = () -> {
			Random r = new Random();
			randomizeXMovement(r);
		};
		
		sed.scheduleAtFixedRate(randMove, 2, 4, TimeUnit.SECONDS);
	}
	
	private boolean checkIfPlayerInRange(int rangeModifier) {
		vittlePlayerRange = p.getCurrentXPosition() - v.getCurrentXPosition();
		
		if(vittlePlayerRange <= rangeModifier && vittlePlayerRange >= -rangeModifier) {
			fleeing = true;
			playerWithinRange = true;
		}
		else {
			fleeing = false;
			playerWithinRange = false;
		}
		
		return fleeing;
		
	}
	
	private void fleeFromPlayer() {	
		for(i = 0; i < vittles.size(); i++) {
			v = vittles.get(i);
			if(checkIfPlayerInRange(v.rangeModifier) && !p.isDead()) {
				if(v.getx() != p.getx() && p.getx() > v.getx() && playerWithinRange) {
					v.left = true;
					v.right = false;
					v.facingLeft = true;
					v.maxSpeed = 1.0;
				}
				else if(v.getx() != p.getx() && p.getx() < v.getx() && playerWithinRange) {
					v.right = true;
					v.left = false;
					v.facingRight = true;
					v.maxSpeed = 1.0;
				}
			}
			else {
				fleeing = false;
				v.maxSpeed = 0.3;
			}
		}
	}
	
	private void randomizeXMovement(Random r) {
		for(i = 0; i < vittles.size(); i++) {
			v = vittles.get(i);
			int randomDir = r.nextInt(2) - 1;
			if(randomDir == -1) {
				v.dx = v.moveSpeed;
			}
			else if(randomDir == 0) {
				v.dx = -v.moveSpeed;
			}
		}
	}
	
	public void update() {
		fleeFromPlayer();
	}
	
}
