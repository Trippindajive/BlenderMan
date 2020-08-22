package Entity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AIBase {
	
	public ScheduledExecutorService sed = Executors.newScheduledThreadPool(2);
	
	public AIBase() {
		
	}
	
	public void murderAI() {
		sed.shutdown();
	}
	
	public void reviveAI() {
		sed = Executors.newScheduledThreadPool(2);
	}
	
}
