package GameState;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import Audio.AudioPlayer;

/**
 * This handles the GameState properties within our game.
 * @author Tim Riggins
 *
 */
public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	public static final int NUMGAMESTATES = 13;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int LOADGAMESTATE = 2;
	public static final int HELPSCREEN = 11;
	//private STATE currentState = STATE.MENU; // Default screen state
	private HashMap<String, AudioPlayer> bgMusic = new HashMap<String, AudioPlayer>();
	
	private String previousSong = "";
	private boolean musicOnOrOff;
	
	public GameStateManager() {
		gameStates = new GameState[NUMGAMESTATES];
		//currentState = STATE.MENU;
		currentState = MENUSTATE;
		
		bgMusic.put("menu music", new AudioPlayer("/Music/Red-MarKer-DMC-12-Gauge.wav"));
		bgMusic.put("level1 music", new AudioPlayer("/Music/Miami-Beach-Force-Sudden-Impact.wav"));
		
		loadState(currentState);
	
	}
	
	private void playSong(String s) {
		FloatControl gainControl = (FloatControl) 
				bgMusic.get(s).clip.getControl(
				FloatControl.Type.MASTER_GAIN);
		
		if(s.equals(previousSong) || musicOnOrOff) {
			return;
		}
		if(!previousSong.equals("")) {
			bgMusic.get(previousSong).stop();
		}
		if(s.equals("level1 music")) {
			gainControl.setValue(-10.0f); // Lowers volume by 10 decibels
		}
		bgMusic.get(s).play();
		bgMusic.get(s).clip.loop(Clip.LOOP_CONTINUOUSLY);
		previousSong = s;
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE) {
			//playSong("menu music");
			gameStates[state] = new MenuState(this);
		}
		if(state == LEVEL1STATE) {
			//playSong("level1 music");
			gameStates[state] = new Level1State(this);
		}
		if(state == HELPSCREEN) {
			gameStates[state] = new HelpScreen(this);
		}
		if(state == LOADGAMESTATE) {
			gameStates[state] = new LoadGameState(this);
		}
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
			//b.printStackTrace(); //Currently getting nullPointerException when uncommented, not sure why
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
			//e.printStackTrace(); //Currently getting nullPointerException when uncommented, not sure why
		}
	}
	/**
	 * Signals key press.
	 * @param k The ASCII value of the key pressed.
	 */
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
		if(k == KeyEvent.VK_M) {
			//musicOnOrOff = true;
		}
	}
	/**
	 * Signals key release.
	 * @param k The ASCII value of the key released.
	 */
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
}
