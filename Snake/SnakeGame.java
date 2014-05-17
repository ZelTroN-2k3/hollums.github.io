import javax.swing.*;
import java.awt.*;

public class SnakeGame
{
	public static void main(String args[])
	{
		JFrame frame = new JFrame("Snake!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Board board = new Board();
		
		frame.getContentPane().add(board);
		
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
}