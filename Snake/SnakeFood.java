import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SnakeFood extends JPanel
{
	private final int SIZE = 10;
	private int xFood, yFood;
	private Timer timer;
	
	public SnakeFood(Board board)
	{
		xFood = (int)(Math.random()*(board.getBoardSize()/10))*10;
		yFood = (int)(Math.random()*((board.getBoardSize()-30)/10))*10;
		setBackground(Color.red);
		setBorder(BorderFactory.createLineBorder(Color.yellow));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.red);
		g.fillRect(xFood,yFood,SIZE,SIZE);
	}
	
	// ACCESSORS
	
	public int getFoodSize()
	{
		return SIZE;
	}
	
	public int getX()
	{
		return xFood;
	}
	
	public int getY()
	{
		return yFood;
	}
	
	public void setX(int x)
	{
		xFood = x;
	}
	
	public void setY(int y)
	{
		yFood = y;
	}
}
	