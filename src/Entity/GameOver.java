package Entity;

import java.awt.*;

public class GameOver {
	
	private Font font;
	private String[] message = {"            GAME OVER", "PRESS ENTER TO RETURN"};
	
	public GameOver() {
		try {
		
			font = new Font("Arial", Font.PLAIN, 24);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		g.setFont(font);
		for(int i = 0; i < message.length; i++) {
			g.setColor(Color.RED);
			g.drawString(message[i], 180, 180 + i * 25);
		}
		
	}
}
