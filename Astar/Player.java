import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player
{
	private int SPEED=1;
	private int SIZE;
	private String direction = "";
	private int x,y;

	public Player(int pX, int pY, int pSize)
	{
		x = pX;
		y = pY;
		SIZE = pSize;
		SPEED = SIZE * SPEED;
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.green);
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

	public void moveLeft()
	{
		x-=SPEED;
	}
	
	public void moveRight()
	{
		x+=SPEED;
	}

	public void moveUp()
	{
		y-=SPEED;
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

	public void moveDown()
	{
		y+=SPEED;
	}

	public void setSpeed(int newSpeed)
	{
		SPEED = newSpeed;
	}

	public int getSpeed()
	{
		return SPEED;
	}

	public int[] getLocation()
	{
		int[] location = {x,y};
		return location;
	}

	public int getSquareSize()
	{
		return SIZE;
	}
}