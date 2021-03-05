package Entity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AIBase {
	
	public ScheduledExecutorService sed = Executors.newScheduledThreadPool(2);
	
	public AIBase() {
		
	}
	
	public void murderAI() { // Shut the scheduler off when the player is not playing
		sed.shutdown();
	}
	
	public void reviveAI() { // Restart the scheduler when the player begins playing
		sed = Executors.newScheduledThreadPool(2);
	}
	
}
