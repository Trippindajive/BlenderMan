package Entity.AI;

import Entity.Vittle;
import Entity.AIBase;
import Entity.Player;
import Entity.PowerUp;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VittleAI extends AIBase{
	
	private Player p;
	private ArrayList<Vittle> vittles;
	private Vittle v;
	
	private int i;
	private double vittlePlayerRange;
	private boolean fleeing;
	private boolean playerWithinRange;
	
	//private double tempSpeed;
	
	
	
	public VittleAI(Player p, ArrayList<Vittle> vittles) {
		this.p = p;
		this.vittles = vittles;
		
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
					jumpFromFright();
					fleeRight();
				}
				else if(v.getx() != p.getx() && p.getx() < v.getx() && playerWithinRange) {
					jumpFromFright();
					fleeLeft();
				}
			}
			else {
				fleeing = false;
				v.maxSpeed = v.origMaxSpeed;
			}
		}
	}
	
	private void jumpFromFright() {
		v.y -= 2;
	}
	
	private void fleeRight() {
		v.left = true;
		v.right = false;
		v.facingLeft = true;
		v.maxSpeed = v.fleeSpeed;
	}
	
	private void fleeLeft() {
		v.right = true;
		v.left = false;
		v.facingRight = true;
		v.maxSpeed = v.fleeSpeed;
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
