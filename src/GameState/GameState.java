package GameState;
/**
 * This is an abstract class defining what
 * methods we expect a GameState object to have.
 * @author Tim Riggins
 *
 */

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
	
}
