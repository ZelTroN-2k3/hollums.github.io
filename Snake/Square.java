import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Square extends JPanel
{
	private final int SIZE = 10;
	private String direction,up="up",down="down",left="left",right="right";
	private int x,y;
	
	public Square(int i,int j)
	{
		x = i;
		y = j;
		direction = right;
		setBackground(Color.green);
		setBorder(BorderFactory.createLineBorder(Color.red));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.fillRect(x,y,SIZE,SIZE);
	}
	
	public String getDirection()
	{
		return direction;
	}
	
	public void setDirection(String i)
	{
		direction = i;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int i)
	{
		x = i;
	}
	
	public void setY(int i)
	{
		y = i;
	}
	
	public String toString()
	{
		String square = "X: "+x+"\nY: "+y+"\nD: "+direction;
		return square;
	}
}
	
	