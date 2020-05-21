package GameState;

/**
 * Scalable state representation.
 * @author Ashaun
 *
 */
public enum STATE {
	MENU(0),
	LEVEL_ONE(1);
	
	// The value of a given state (corresponds to GameState)
	private int value;
	
	// Constructor
	STATE(int v) { this.value = v; }
	
	// Returns value for a given state.
	public int value() { return this.value; }
	
}