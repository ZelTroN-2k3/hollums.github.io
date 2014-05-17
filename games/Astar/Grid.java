import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Grid
{
	private int TILE_SIZE = 10;
	private int BOARD_SIZE = 300;
	private int tilesPerRow = 0;
	private Square[][] grid_tiles;
	private ArrayList<Square> emptyTiles = null;
	private Board thisBoard = null;
	private int drawTime = 300;

	private String HEURISTIC = "Manhatten";

	public Grid(int board_size,int tile_size)
	{
		TILE_SIZE = tile_size;
		BOARD_SIZE = board_size;
		tilesPerRow = board_size/tile_size;
		grid_tiles = new Square[tilesPerRow][tilesPerRow];

		int thisRow = 0;
		int thisColumn = 0;
		for(int i=0;i<tilesPerRow;i++)
		{
			for(int j=0;j<tilesPerRow;j++)
			{
				Square thisTile = new Square(thisColumn,thisRow,TILE_SIZE);
				grid_tiles[i][j] = thisTile;
				thisColumn+=TILE_SIZE;
			}
			thisRow+=TILE_SIZE;
			thisColumn=0;
		}
		// printGrid();
	}

	public Grid(int board_size, int tile_size, String[][] boardMap)
	{
		TILE_SIZE = tile_size;
		BOARD_SIZE = board_size;
		tilesPerRow = board_size/tile_size;
		if(boardMap.length!=tilesPerRow)
		{
			System.out.println("The board map given does not match the board size!");
		}

		grid_tiles = new Square[tilesPerRow][tilesPerRow];

		emptyTiles = new ArrayList<Square>();
		int thisRow = 0;
		int thisColumn = 0;
		for(int i=0;i<tilesPerRow;i++)
		{
			for(int j=0;j<tilesPerRow;j++)
			{
				Square thisTile = new Square(thisColumn,thisRow,TILE_SIZE);
				thisTile.setCollision(boardMap[i][j].equals("x"));
				if(!thisTile.isCollision())
				{
					emptyTiles.add(thisTile);
				}
				grid_tiles[i][j] = thisTile;
				thisColumn+=TILE_SIZE;
			}
			thisRow+=TILE_SIZE;
			thisColumn=0;
		}
		// printGrid();
	}

	public Grid(int board_size, int tile_size, String[][] boardMap, String pHeuristic)
	{
		this(board_size,tile_size,boardMap);
		HEURISTIC = pHeuristic;
	}

	public Grid(int board_size, int tile_size, String[][] boardMap, Board pBoard)
	{
		this(board_size,tile_size,boardMap);
		thisBoard = pBoard;
	}

	public int getTilesPerRow()
	{
		return tilesPerRow;
	}
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.blue);
		for(int i=0;i<tilesPerRow;i++)
		{
			for(int j=0;j<tilesPerRow;j++)
			{
				grid_tiles[i][j].paintComponent(g);
			}
		}
	}

	public void printGrid()
	{
		for(int i=0;i<tilesPerRow;i++)
		{
			for(int j=0;j<tilesPerRow;j++)
			{
				System.out.print(grid_tiles[i][j].getY()+":"+grid_tiles[i][j].getX()+"\t");
			}
			System.out.println();
		}
	}

	public Square getTileObject(int i,int j)
	{
		return grid_tiles[i][j];
	}

	public int[] getTile(int i, int j)
	{
		int[] thisTile = {grid_tiles[i][j].getX(),grid_tiles[i][j].getY()};
		return thisTile;
	}

	public void setCollision(int i, int j)
	{
		System.out.println("Changing collision of tile "+i+", "+j+".");
		grid_tiles[i][j].setCollision(true);
	}

	public int[] getRandomTile()
	{
		int tileColumn = (int) (Math.random()*tilesPerRow);
		int tileRow = (int) (Math.random()*tilesPerRow);

		System.out.println("Row = "+tileRow);
		System.out.println("Column = "+tileColumn);
		int[] thisTile = {tileRow,tileColumn};
		return thisTile;
	}

	public boolean checkCollisionUp(Player player)
	{
		for(int i=0;i<grid_tiles.length;i++)
		{
			for(int j=0;j<grid_tiles.length;j++)
			{
				if((grid_tiles[i][j].isCollision() &&
					player.getX() == grid_tiles[i][j].getX() &&
					player.getY()-player.getSpeed() == grid_tiles[i][j].getY()) ||
					player.getY() == 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkCollisionDown(Player player)
	{
		for(int i=0;i<grid_tiles.length;i++)
		{
			for(int j=0;j<grid_tiles.length;j++)
			{
				if((grid_tiles[i][j].isCollision() &&
					player.getX() == grid_tiles[i][j].getX() &&
					player.getY()+player.getSpeed() == grid_tiles[i][j].getY()) ||
					player.getY()+player.getSquareSize()+player.getSpeed() > BOARD_SIZE)
					return true;
			}
		}
		return false;
	}

	public boolean checkCollisionLeft(Player player)
	{
		for(int i=0;i<grid_tiles.length;i++)
		{
			for(int j=0;j<grid_tiles.length;j++)
			{
				if((grid_tiles[i][j].isCollision() &&
					player.getX()-player.getSpeed() == grid_tiles[i][j].getX() &&
					player.getY() == grid_tiles[i][j].getY()) ||
					player.getX()-player.getSpeed() < 0)
					return true;
			}
		}
		return false;
	}

	public boolean checkCollisionRight(Player player)
	{
		for(int i=0;i<grid_tiles.length;i++)
		{
			for(int j=0;j<grid_tiles.length;j++)
			{
				if((grid_tiles[i][j].isCollision() &&
					player.getX()+player.getSpeed() == grid_tiles[i][j].getX() &&
					player.getY() == grid_tiles[i][j].getY()) ||
					player.getX()+player.getSquareSize()+player.getSpeed() > BOARD_SIZE)
					return true;
			}
		}
		return false;
	}

	public int[] getRespawnLocation(Player player, Square target)
	{
		int randomChoice = (int) (Math.random()*emptyTiles.size());
		int newX = emptyTiles.get(randomChoice).getX();
		int newY = emptyTiles.get(randomChoice).getY();
		boolean valid = false;
		while(!valid)
		{
			if(!(player.getX()==newX && player.getY()==newY) && !(target.getX()==newX && target.getY()==newY))
				valid = true;
			else
			{
				randomChoice = (int) Math.random()*emptyTiles.size();
				newX = emptyTiles.get(randomChoice).getX();
				newY = emptyTiles.get(randomChoice).getY();
				valid = false;
			}
		}
		int[] newLocation = {newX,newY};
		return newLocation;
	}

	public void calculatePath(Player player, Square target)
	{
		int[] playerCell = {player.getX()/TILE_SIZE,player.getY()/TILE_SIZE};
		Square playerTile = grid_tiles[playerCell[1]][playerCell[0]];
		playerTile.setG(0);
		playerTile.setH(getManhattenHeuristic(playerTile,target));
		playerTile.setF(playerTile.getH());
		int[] targetCell = getCoordinates(target);
		ArrayList<Square> surroundingTiles = new ArrayList<Square>();
		ArrayList<Square> openList = new ArrayList<Square>();
		ArrayList<Square> closedList = new ArrayList<Square>();
		closedList.add(playerTile);

		
		boolean targetFound = false;
		Square currentTile = playerTile;
		while(!targetFound)
		{
			// Get the surrounding tiles of the current tile being examined
			surroundingTiles = getSurroundingTiles(currentTile,target,closedList,openList);

			// Add all the surrounding tiles of the current tile being examined
			// to the open list variable.
			for(int i=0;i<surroundingTiles.size();i++)
			{
				openList.add(surroundingTiles.get(i));
			}
			int leastF = 4*10*tilesPerRow;
			int indexOfLeastF = 0;
			// Find the least value in the surrounding tiles
			for(int i=0;i<openList.size();i++)
			{
				if(openList.get(i).getF()<leastF)
				{
					leastF = openList.get(i).getF();
					indexOfLeastF = i;
				}
			}
			// Set the current tile to the least costly move found
			// of all the surrounding tiles to the previously used
			// current tile.
			currentTile = openList.get(indexOfLeastF);
			// If this tile was found to be the target tile, then
			// break from the loop.
			if(getCoordinates(currentTile)[0]==targetCell[0] &&
				getCoordinates(currentTile)[1]==targetCell[1])
				targetFound = true;
			System.out.println("Least costly tile found: "+(getCoordinates(currentTile)[1]+1)+"-"+(getCoordinates(currentTile)[0]+1));

			// Add the tile found to be the least costly tile to the closed
			// array list, then remove this tile from the open list.
			closedList.add(openList.get(indexOfLeastF));
			System.out.println("Added "+(getCoordinates(openList.get(indexOfLeastF))[1]+1)+","+(getCoordinates(openList.get(indexOfLeastF))[0]+1)+" to the closedList");
			openList.remove(indexOfLeastF);

			// Repaint the tiles in openList and closedList
			for(int i=0;i<openList.size();i++)
			{
				openList.get(i).setBorderColor(Color.green);
			}
			for(int i=0;i<closedList.size();i++)
			{
				closedList.get(i).setBorderColor(Color.blue);
			}
			try
			{
				Thread.sleep(drawTime);
			}catch(InterruptedException e){}
			thisBoard.repaint();
		}
		// Draw the path tracing the route back from the target by each tile's parent.
		//System.out.println("Tracing back from "+(getCoordinates(currentTile)[1]+1)+","+(getCoordinates(currentTile)[0]+1));
		int steps = 0;
		while(currentTile.getParent()!=null)
		{
			currentTile = currentTile.getParent();
			//System.out.println("\twas the parent of "+(getCoordinates(currentTile)[1]+1)+","+(getCoordinates(currentTile)[0]+1));
			//grid_tiles[getCoordinates(currentTile)[1]][getCoordinates(currentTile)[0]].setOptimalTile();
			currentTile.setOptimalTile();
			steps++;
		}
		closedList.get(closedList.size()-1).setSteps(steps);
		target.setSteps(steps);

	}

	public ArrayList<Square> getSurroundingTiles(Square thisTile, Square thisTarget, ArrayList<Square> closedList, ArrayList<Square> openList)
	{
		int[] tileCoordinates = getCoordinates(thisTile);
		ArrayList<Square> surroundingTiles = new ArrayList<Square>();
		// Check right tile
		System.out.println("Checking right tile...");
		if(tileCoordinates[0]+1 < tilesPerRow && !grid_tiles[tileCoordinates[1]][tileCoordinates[0]+1].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]][tileCoordinates[0]+1],closedList))
		{
			boolean goodMove = true;
			// Check if the tile being examined is in the open list
			if(checkListForTile(grid_tiles[tileCoordinates[1]][tileCoordinates[0]+1],openList))
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]][tileCoordinates[0]+1].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]][tileCoordinates[0]+1]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);
				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 10 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}
		// Check left tile
		System.out.println("Checking left tile...");
		if(tileCoordinates[0]-1 >= 0 && !grid_tiles[tileCoordinates[1]][tileCoordinates[0]-1].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]][tileCoordinates[0]-1],closedList))
		{
			boolean goodMove = true;
			// Check if the tile being examined is in the open list
			if(checkListForTile(grid_tiles[tileCoordinates[1]][tileCoordinates[0]-1],openList))
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]][tileCoordinates[0]-1].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]][tileCoordinates[0]-1]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);
				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 10 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}
		// Check bottom tile
		System.out.println("Checking bottom tile...");
		if(tileCoordinates[1]+1 < tilesPerRow && !grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]],closedList))
		{
			boolean goodMove = true;
			// Check if the tile being examined is in the open list
			if(checkListForTile(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]],openList))
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);
				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 10 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}
		// Check top tile
		System.out.println("Checking top tile...");
		if(tileCoordinates[1]-1 >= 0 && !grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]],closedList))
		{
			boolean goodMove = true;
			// Check if the tile being examined is in the open list
			if(checkListForTile(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]],openList))
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);
				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 10 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}
		// Check the top-right tile
		System.out.println("Checking top-right tile...");
		if(tileCoordinates[1]-1 >= 0 && tileCoordinates[0]+1 < tilesPerRow && 
			!grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]+1].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]+1],closedList))
		{
			boolean goodMove = true;
			//Check move doesn't go through wall
			// First check tile to the left is collision, then check tile above is collision
			if(grid_tiles[tileCoordinates[1]][tileCoordinates[0]+1].isCollision()
				|| grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]].isCollision())
					goodMove =false;
			// Check if the tile being examined is in the open list
			if(checkListForTile(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]+1],openList)
				&& goodMove)
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]+1].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]+1]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);
				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 14 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}
		// Check the top-left tile
		System.out.println("Checking top-left tile...");
		if(tileCoordinates[1]-1 >= 0 && tileCoordinates[0]-1 >= 0 &&
			!grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]-1].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]-1],closedList))
		{
			boolean goodMove = true;
			//Check move doesn't go through wall
			// First check tile to the left is collision, then check tile above is collision
			if(grid_tiles[tileCoordinates[1]][tileCoordinates[0]-1].isCollision()
				|| grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]].isCollision())
					goodMove =false;
			// Check if the tile being examined is in the open list, or if the move to the tile is valid
			System.out.println("\tOpen list:");
			if(checkListForTile(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]-1],openList)
				&& goodMove)
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]-1].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]-1][tileCoordinates[0]-1]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);
				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 14 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}
		// Check the bottom-left tile
		System.out.println("Checking bottom-left tile...");
		if(tileCoordinates[1]+1 < tilesPerRow && tileCoordinates[0]-1 >= 0 && 
			!grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]-1].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]-1],closedList))
		{
			boolean goodMove = true;
			//Check move doesn't go through wall:
			// First check tile to the left is collision, then check tile below is collision
			if(grid_tiles[tileCoordinates[1]][tileCoordinates[0]-1].isCollision()
				|| grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]].isCollision())
					goodMove =false;
			// Check if the tile being examined is in the open list
			if(checkListForTile(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]-1],openList)
				&& goodMove)
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]-1].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]-1]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);

				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 14 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}
		// Check the bottom-right tile
		System.out.println("Checking bottom-right tile...");
		if(tileCoordinates[1]+1 < tilesPerRow && tileCoordinates[0]+1 < tilesPerRow && 
			!grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]+1].isCollision()
			&& !checkListForTile(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]+1],closedList))
		{
			boolean goodMove = true;
			//Check move doesn't go through wall:
			// First check tile to the right is collision, then check tile below is collision
			if(grid_tiles[tileCoordinates[1]][tileCoordinates[0]+1].isCollision()
				|| grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]].isCollision())
					goodMove =false;
			// Check if the tile being examined is in the open list
			if(checkListForTile(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]+1],openList)
				&& goodMove)
			{
				int adjacentTileMoveWeight = grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]].getG();
				int thisMoveWeight = 10+thisTile.getG();
				if(adjacentTileMoveWeight < thisMoveWeight)
					goodMove = false;
			}
			if(goodMove)
			{
				surroundingTiles.add(grid_tiles[tileCoordinates[1]+1][tileCoordinates[0]+1]);
				Square tileAdded = surroundingTiles.get(surroundingTiles.size()-1);
				tileAdded.setParent(thisTile);

				Square parentTile = tileAdded.getParent();
				int parentG = parentTile.getG();
				int moveWeight = 14 + parentG;
				int heuristic = getHeuristic(tileAdded,thisTarget);
				int total = moveWeight+heuristic;
				tileAdded.setG(moveWeight);
				tileAdded.setH(heuristic);
				tileAdded.setF(total);
				tileAdded.setSearched(true);
			}
		}

		
		System.out.println("Surrounding Tiles: ");
		for(int i=0;i<surroundingTiles.size();i++)
		{
			int[] theseCoordinates = getCoordinates(surroundingTiles.get(i));
			System.out.println((theseCoordinates[1]+1)+":"+(theseCoordinates[0]+1));
		}
		
		return surroundingTiles;
		
	}

	public boolean checkListForTile(Square tile,ArrayList<Square> list)
	{
		/*
		System.out.println("\tChecking for this tile in list "+(getCoordinates(tile)[1]+1)+":"+(getCoordinates(tile)[0]+1));
		System.out.println("\tList: ");
		for(int i=0;i<list.size();i++)
		{
			System.out.println("\t\t"+(getCoordinates(list.get(i))[1]+1)+":"+(getCoordinates(list.get(i))[0]+1));
		}
		*/
		for(int j=0;j<list.size();j++)
		{				
			// If the surrounding tile being examined is equal to any closedList tile,
			// remove that tile from the surroundingTiles list. Then exit the loop through the				// closedList loop, and examine the next surrounding tile element again with the
			// tiles in the clostList array.
			if(getCoordinates(tile)[0]==getCoordinates(list.get(j))[0] 
				&& getCoordinates(tile)[1]==getCoordinates(list.get(j))[1])
			{
				//System.out.println("\tFound tile in list!");
				return true;
			}
		}
		return false;
	}

	public int getHeuristic(Square tile, Square target)
	{
		if(HEURISTIC.equals("Manhatten"))
		{
			return getManhattenHeuristic(tile,target);
		}
		else if(HEURISTIC.equals("Djikstra"))
		{
			return getDjikstraHeuristic();
		}
		else if(HEURISTIC.equals("Euclidean"))
		{
			return getEuclideanHeuristic(tile,target);
		}
		else
		{
			System.out.println("ERROR: No Heuristic method for "+HEURISTIC);
			System.out.println("Using default Manhatten heuristic...");
			return getManhattenHeuristic(tile,target);
		}
	}

	public int getManhattenHeuristic(Square thisSquare, Square thisTarget)
	{
		int[] tileCoordinates = getCoordinates(thisSquare);
		int[] targetCoordinates = getCoordinates(thisTarget);
		int distance = (Math.abs(tileCoordinates[0]-targetCoordinates[0]))+(Math.abs(tileCoordinates[1]-targetCoordinates[1]));
		distance*=10;
		return distance;
	}

	public int getDjikstraHeuristic()
	{
		return 0;
	}

	public int getEuclideanHeuristic(Square tile, Square target)
	{
		int euclidean =0;
		int[] tileCoordinates = getCoordinates(tile);
		int[] targetCoordinates = getCoordinates(target);
		int deltaX = (Math.abs(tileCoordinates[0]-targetCoordinates[0]));
		int deltaY = (Math.abs(tileCoordinates[1]-targetCoordinates[1]));
		euclidean = (int)(Math.sqrt(Math.pow(deltaX,2))+Math.pow(deltaY,2));
		return euclidean;
	}

	public void setHeuristic(String thisHeuristic)
	{
		HEURISTIC = thisHeuristic;
	}

	public int[] getCoordinates(Square thisSquare)
	{
		int[] coordinates = {thisSquare.getX()/TILE_SIZE,thisSquare.getY()/TILE_SIZE};
		return coordinates;
	}

	public void resetTiles()
	{
		for(int i=0;i<tilesPerRow;i++)
		{
			for(int j=0;j<tilesPerRow;j++)
			{
				grid_tiles[i][j].resetTile();
			}
		}
	}

	public void setDrawTime(int newTime)
	{
		drawTime = newTime;
	}
}
	
	