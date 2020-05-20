package GameState;

import java.util.ArrayList;

/**
 * This handles the GameState properties within our game.
 * @author Tim Riggins
 *
 */
public class GameStateManager {
	// REMOVE LATER LOL
	private ArrayList<GameState> gameStates;
	private STATE currentState;
	
	public GameStateManager() {
		gameStates = new ArrayList<GameState>();
		currentState = STATE.MENU;
		gameStates.add(new MenuState(this));
		gameStates.add(new Level1State(this));
	}
	
	/**
	 * Sets the state of the game
	 * @param state
	 */
	public void setState(STATE state) { 
		currentState = state;
		gameStates.get(currentState.value()).init();
	}
	/**
	 * Updates the game's values.
	 */
	public void update() {
		gameStates.get(currentState.value()).update();
	}
	/**
	 * Renders graphics with the AWT Graphics2D library.
	 * @param g The AWT Graphics library.
	 */
	public void draw(java.awt.Graphics2D g) {
		gameStates.get(currentState.value()).draw(g);
	}
	/**
	 * Signals key press.
	 * @param k The ASCII value of the key pressed.
	 */
	public void keyPressed(int k) {
		gameStates.get(currentState.value()).keyPressed(k);
	}
	/**
	 * Signals key release.
	 * @param k The ASCII value of the key released.
	 */
	public void keyReleased(int k) {
		gameStates.get(currentState.value()).keyReleased(k);
	}
}
