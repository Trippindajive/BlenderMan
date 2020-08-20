package Entity;

import TileMap.TileMap;

/**
 * Superclass of all powerups
 * @author Tim Riggins
 *
 */
public class PowerUp extends MapObject{
	
	protected String name;
	public boolean obtained; 
	public boolean stopWatchPowerOn;
	
	public PowerUp(TileMap tm) {
		super(tm);
		
		width = 42;
		height = 40;
		cwidth = 30;
		cheight = 30;
	}
	
	public String getName() {
		return name;
	}
	
	public void usePowerUp(String name) {
		switch(name) {
		
		case "STOPWATCH":
			stopWatchPowerOn = true;
			break;
			
		default:
			break;
		}
	}
	
	public void update() {
		
	}
	
}
