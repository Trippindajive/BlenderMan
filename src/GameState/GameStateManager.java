package GameState;

import Audio.AudioPlayer;

/**
 * This handles the GameState properties within our game.
 * @author Tim Riggins
 *
 */
public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	public static final int NUMGAMESTATES = 12;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LOADGAMESTATE = 2;
	public static final int HELPSCREEN = 11;
	//private STATE currentState = STATE.MENU; // Default screen state
	
	private AudioPlayer bgMusic;
	public GameStateManager() {
		gameStates = new GameState[NUMGAMESTATES];
		//currentState = STATE.MENU;
		currentState = MENUSTATE;
		
		loadState(currentState);
		
		bgMusic = new AudioPlayer("/Music/Red-MarKer-DMC-12-Gauge.wav");
		//bgMusic.play();
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
		if(state == HELPSCREEN)
			gameStates[state] = new HelpScreen(this);
		if(state == LOADGAMESTATE)
			gameStates[state] = new LoadGameState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	/**
	 * Sets the state of the game
	 * @param state
	 */
	/*public void setState(STATE state) { 
		currentState = state;
		gameStates[currentState].init();
	}*/
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}
	/**
	 * Updates the game's values.
	 */
	public void update() {
		try {
			
			gameStates[currentState].update();
			
		} catch (Exception b) {
			//b.printStackTrace();
		}
	}
	/**
	 * Renders graphics with the AWT Graphics2D library.
	 * @param g The AWT Graphics library.
	 */
	public void draw(java.awt.Graphics2D g) {
		try {
			
			gameStates[currentState].draw(g);
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	/**
	 * Signals key press.
	 * @param k The ASCII value of the key pressed.
	 */
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	/**
	 * Signals key release.
	 * @param k The ASCII value of the key released.
	 */
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
}
