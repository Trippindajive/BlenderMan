package Main;

import java.awt.*; // Contains all the classes for creating user interfaces and painting graphics and images
import java.awt.image.BufferedImage; // A subclass of the image class, it handles and manipulates image data
import java.awt.event.*;
import javax.swing.JPanel; // A class from the swing package that is a container which stores groups of components

import GameState.GameStateManager;

/**
 * This defines the properties of the game window.
 * @author Tim Riggins
 *
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	// Dimensions of the GUI
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	// Game thread
	private Thread thread; // A branch of execution in the program
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// Image
	private BufferedImage image;
	private Graphics2D g;
	
	// Game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE)); // Specifies the size of the game panel
		setFocusable(true); // Indicates whether a component will be focused on if requested to do so
		requestFocus(); // Makes a request for the given component to be set in a focused state,
	}
	
	// The game panel is done loading and is ready for a new thread
	@Override
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	// Initializes contents
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();;
		running = true;
		
		gsm = new GameStateManager();
	}
	
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// Game loop
		while(running) {
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) {
				wait = 1;
			}
			
			try {
				Thread.sleep(wait);
			} catch(Exception e) {
				e.printStackTrace(); // When catching exceptions, always call printStackTrace to ensure I can see any problems
			}
		}
	}
	
	private void update() {
		gsm.update();
	}
	
	private void draw() {
		gsm.draw(g);
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics(); // Game panel graphics object
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}
	
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}
}
