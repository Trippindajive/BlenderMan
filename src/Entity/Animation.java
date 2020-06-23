package Entity;

import java.awt.image.BufferedImage;

/**
 * This class handles all the sprite animations.
 * @author Tim Riggins
 *
 */
public class Animation {
	
	// An array of buffered images to store all the frames
	private BufferedImage[] frames;
	private int currentFrame;
	
	// Describes timer between frames
	private long startTime;
	private long delay;
	
	// Describes whether an animation has been played already if it has been looped (useful for attack)
	private boolean playedOnce;
	
	public Animation() {
		playedOnce = false;
	}
	/**
	 * Sets the animation frames
	 * @param frames An array storing the animation image frames
	 */
	public void setFrames(BufferedImage[] frames) {
		this.frames= frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void setDelay(long d) {
		delay = d;
	}
	/**
	 * Useful if needing to manually adding the frame number
	 * @param i Frame #
	 */
	public void setFrame(int i) {
		currentFrame = i;
	}
	/**
	 * Handles the logic that determines if the next frame needs to played
	 */
	public void update() {
		if(delay == -1)
			return;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		 // How long it has been since animation was played in milliseconds
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime(); // Resets timer
		}
		if(currentFrame == frames.length) { // Ensures amount of frames is within bounds
			currentFrame = 0;
			playedOnce = true;
		}
	}
	/**
	 * Gets the current frame number
	 * @return Current frame #
	 */
	public int getFrame() {
		return currentFrame;
	}
	/** 
	 * VERY IMPORTANT: Gets the image that needs to be drawn
	 * @return
	 */
	public BufferedImage getImage() {
		return frames[currentFrame];
				
	}
	/**
	 * Returns whether the animation had been played once or otherwise
	 * @return
	 */
	public boolean hasPlayedOnce() {
		return playedOnce;
	}
}
