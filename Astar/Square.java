import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Square
{
	private int SIZE = 10;
	private int x,y;
	private boolean isCollision = false;
	private boolean isTarget = false;

	private Color borderColor = Color.darkGray;
	private Color fontColor = Color.yellow;
	private Color pathColor = Color.red;
	private int fontSize = 12;
	private double fontRatio = .20;

	private boolean tileSearched = false;
	private int fVal = 0;
	private int gVal = 0;
	private int hVal = 0;
	private int[] fOffset = new int[2];
	private int[] gOffset = new int[2];
	private int[] hOffset = new int[2];
	private Square parent = null;
	private int borderOffset = 2;
	private Font font;
	private boolean optimalTile = false;
	private int circleRadius = 10;
	private int steps = 0;

	public Square(int i,int j, int pSize)
	{
		x = i;
		y = j;
		SIZE = pSize;
		fontSize = (int)(SIZE*fontRatio);
		font = new Font("Dialog", Font.PLAIN, fontSize);
		getOffsets();
	}

	public Square(int i,int j, int pSize, boolean collision)
	{
		x = i;
		y = j;
		SIZE = pSize;
		isCollision = collision;
		fontSize = (int)(SIZE*fontRatio);
		font = new Font("Dialog", Font.PLAIN, fontSize);
	}
	
	public void paintComponent(Graphics g)
	{
		// Draw a collision tile
		if(isCollision)
		{
			g.setColor(Color.blue);
			g.fillRect(x,y,SIZE,SIZE);
		}
		// Draw a target square
		else if(isTarget)
		{
			g.setColor(Color.red);
			g.fillRect(x,y,SIZE,SIZE);
			if(steps>0)
			{
				g.setFont(new Font("TimesRoman",Font.PLAIN,(int)(fontSize*fontRatio*15)));
				g.setColor(fontColor);
				g.drawString(Integer.toString(steps),x+SIZE/2,y+SIZE/2);
			}
		}
		// Draw a grid tile
		else
		{
			if(tileSearched)
			{
				g.setFont(font);
				g.setColor(fontColor);
				g.drawString(Integer.toString(fVal),x+fOffset[0],y+fOffset[1]);
				g.drawString(Integer.toString(gVal),x+gOffset[0],y+gOffset[1]);
				g.drawString(Integer.toString(hVal),x+hOffset[0],y+hOffset[1]);
				g.setColor(borderColor);
				g.drawRect(x+borderOffset,y+borderOffset,SIZE-2*borderOffset,SIZE-2*borderOffset);
				if(optimalTile)
				{
					g.setColor(pathColor);
					g.fillOval(x+SIZE/2-circleRadius/2,y+SIZE/2-circleRadius/2,circleRadius,circleRadius);
				}
				if(optimalTile && steps!=0)
				{
					g.drawString(Integer.toString(steps),x+SIZE/2,y+SIZE/2);
				}
			}
			else
			{
				g.setColor(borderColor);
				g.drawRect(x,y,SIZE,SIZE);
			}
		}
	}

	public void setSearched(boolean searched)
	{
		tileSearched = searched;
		if(!searched)
		{
			resetTile();
		}
	}

	private void getOffsets()
	{
		fOffset[0]=(int)SIZE/7;
		fOffset[1]=(int)2*SIZE/7;
		gOffset[0]=(int)SIZE/8;
		gOffset[1]=(int)7*SIZE/8;
		hOffset[0]=(int)3*SIZE/5;
		hOffset[1]=(int)7*SIZE/8;
	}	

	public boolean isTarget()
	{
		return isTarget;
	}

	public void setTarget()
	{
		isTarget = true;
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

	public void setF(int f)
	{
		fVal = f;
	}

	public void setG(int g)
	{
		gVal = g;
	}

	public void setH(int h)
	{
		hVal = h;
	}

	public int getF()
	{
		return fVal;
	}

	public int getG()
	{
		return gVal;
	}

	public int getH()
	{
		return hVal;
	}

	public void setBorderColor(Color newColor)
	{
		borderColor = newColor;
	}

	public void resetTile()
	{
		fVal=0;
		gVal=0;
		hVal=0;
		tileSearched=false;
		parent = null;
		optimalTile = false;
		borderColor = Color.darkGray;
		steps = 0;
	}

	public String toString()
	{
		String square = "X: "+x+"\nY: "+y;
		return square;
	}

	public void setCollision(boolean collision)
	{
		isCollision = collision;
	}

	public boolean isCollision()
	{
		return isCollision;
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

	public Square getParent()
	{
		return parent;
	}

	public void setParent(Square parentSquare)
	{
		parent = parentSquare;
	}

	public void setOptimalTile()
	{
		optimalTile = true;
	}

	public void setSteps(int pSteps)
	{
		steps = pSteps;
	}

	public int getSteps()
	{
		return steps;
	}
}
	
	