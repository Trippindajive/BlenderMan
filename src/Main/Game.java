package Main;

import javax.swing.JFrame; // A class that essentially enables the creation of a GUI (graphic user interface)

/**
 * This is where the game contains the main method, running it as an executable.
 * @author Tim Riggins
 *
 */
public class Game {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("BlenderMan"); // A constructor using a string title with initially an invisible window
		window.setContentPane(new GamePanel()); // Sets the contentPane property
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tells how the 'x' button will function when clicked (by completely closing)
		window.setResizable(false); // Disables the GUI from being resized by the user
		window.pack(); // Sizes the frame so all content are at its preferred size. The frame layout manager would be in charge of the frame size, and layout managers are good at adjusting to platform dependencies and other factors that affect component size.
		window.setVisible(true); // Controls whether content would be displayed on-screen or not 
	}

}
