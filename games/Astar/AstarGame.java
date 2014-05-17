import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class AstarGame
{
	public static void main(String args[])
	{
		// Small maps
		
		int TILE_SIZE = 50;
		int BOARD_SIZE = 500;
		
		// Big maps
		/*
		int TILE_SIZE = 25;
		int BOARD_SIZE = 625 ;
		*/
		JFrame frame = new JFrame("A* Navigation!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Board board = new Board(BOARD_SIZE,TILE_SIZE);
		String mapName = "boardMap1.txt";
		String[][] boardMap = getBoardMap(mapName);
		board.setGrid(boardMap);
		board.setPlayer(getPlayerLocation(boardMap));
		board.setTarget(getTargetLocation(boardMap));

		frame.setLayout(new FlowLayout());
		Menu menu = new Menu(board);
		frame.add(menu);
		frame.add(board);
		//frame.getContentPane().add(board);
		
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	public static String[][] getBoardMap(String fileName)
	{
		File boardMap = null;
		Scanner search = null;
		ArrayList<String> boardMapArray = null;
		try 
		{
			boardMap = new File(fileName);
			search = new Scanner(boardMap);
			String thisLine = null;
			boardMapArray = new ArrayList<String>();
			while (search.hasNextLine()) 
			{ // Continue while loop until the file has no more lines.
				thisLine = search.nextLine().toString();
				boardMapArray.add(thisLine);
			}

			String[][] boardArray = new String[boardMapArray.size()][boardMapArray.size()];

			for(int i=0;i<boardMapArray.size();i++)
			{
				boardArray[i] = boardMapArray.get(i).split(" ");
			}

			System.out.println("The board map in file: ");
			for(int i=0;i<boardMapArray.size();i++)
			{
				for(int j=0;j<boardMapArray.size();j++)
				{
					System.out.print(boardArray[i][j]+" ");
				}
				System.out.println();
			}
			return boardArray;
		}
		catch(FileNotFoundException e) 
		{
			System.out.println("The program could not find the boardMap file.");
			System.out.println("Make sure that the boardMap.txt file is in the same directory as the program.\nProgram terminating.");
			return null;
		}
	}

	public static void reload(String mapChosen)
	{
		// Small maps
		
		int TILE_SIZE = 50;
		int BOARD_SIZE = 500;
		
		// Big maps
		
		if(mapChosen.contains("big"))
		{
			TILE_SIZE = 25;
			BOARD_SIZE = 625 ;
		}

		if(mapChosen.contains("huge"))
		{
			TILE_SIZE = 10;
			BOARD_SIZE = 9000;
		}
		
		JFrame frame = new JFrame("A* Navigation!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Board board = new Board(BOARD_SIZE,TILE_SIZE);
		board.setFocusable(true);
		String mapName = mapChosen;

		String[][] boardMap = getBoardMap(mapName);
		board.setGrid(boardMap);
		board.setPlayer(getPlayerLocation(boardMap));
		board.setTarget(getTargetLocation(boardMap));

		frame.setLayout(new FlowLayout());
		Menu menu = new Menu(board);
		frame.add(menu);
		frame.add(board);
		//frame.getContentPane().add(board);
		
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		frame.requestFocusInWindow();
		menu.transferFocus();
		board.requestFocus();
	}

	public static int[] getPlayerLocation(String[][] boardMap)
	{
		int[] playerLocation = new int[2];
		for(int i=0;i<boardMap.length;i++)
		{
			for(int j=0;j<boardMap.length;j++)
			{
				if(boardMap[i][j].equals("p"))
				{
					playerLocation[0] = i;
					playerLocation[1] = j;
				}
			}
		}
		return playerLocation;
	}

	public static int[] getTargetLocation(String[][] boardMap)
	{
		int[] targetLocation = new int[2];
		for(int i=0;i<boardMap.length;i++)
		{
			for(int j=0;j<boardMap.length;j++)
			{
				if(boardMap[i][j].equals("t"))
				{
					targetLocation[0] = i;
					targetLocation[1] = j;
				}
			}
		}
		return targetLocation;
	}
	
}