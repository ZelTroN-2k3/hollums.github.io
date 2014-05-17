import java.util.ArrayList;

public class ReversiBoard 
{

	//Variable declaration
	public String empty=".";
	public String[][] reversiBoard;
	public int fillCount;
	//Default reversi board is 8x8 squares
	public int n;
	
	// Constructor for default ReversiBoard instance, initialize the board with n = 8
	public ReversiBoard() 
	{	
		n = 8;
		reversiBoard = new String[n][n];
		initBoard();
	}
	// Constructor for ReversiBoard of the size passed by parameter
	public ReversiBoard(int size) 
	{
		n = size;
		reversiBoard = new String[n][n];
		initBoard();
	}
	// Accessor method
	public String[][] getBoard() 
	{
		return reversiBoard;
	}
	// This method calls fillRow, fillColumn, and fillDiagonal to make the player's move.
	public void playerMove(int row, int column, String color) 
	{
		fillCount = 0;
		reversiBoard[row][column] = color;
		this.fillRow(row, column, color);
		this.fillColumn(row, column, color);
		this.fillDiagonal(row, column, color);
	}
	
	private void fillRow(int row, int column, String color) 
	{
		boolean fillRow=true;
		//Change all 'O's to 'X's between two values of 'X' in the same row
		for (int i=0;i<column-1;i++)
		{ // Fill from left
			fillRow = true;
			if (reversiBoard[row][i].equals(color))
			{
			// If an 'X' is found in the row, check if all values to the right
			// are 'O's.
				for(int j=i+1;j<column;j++)
				{	
					if(reversiBoard[row][j].equals(color) || reversiBoard[row][j].equals(empty)) 
					{
						fillRow=false;
					}
				}
			// If the row passes the check, fill the section with 'X's.
				if(fillRow) 
				{
					for(int j=i+1;j<column;j++)
					{
						reversiBoard[row][j]=color;
						fillCount++;
					}
				}
			}	
		}
		// Fill from right
		for (int i=n-1;i>column;i--) 
		{
			fillRow = true;
			if(reversiBoard[row][i].equals(color)) 
			{
				for(int j=i-1;j>column;j--) 
				{
					if(reversiBoard[row][j].equals(color) || reversiBoard[row][j].equals(empty)) 
					{
						fillRow=false;
					}
				}
				if (fillRow) 
				{
					for(int j=i-1;j>column;j--) 
					{
						reversiBoard[row][j]=color;
						fillCount++;
					}
				}
			}
		}
	}
		
	private void fillColumn(int row, int column, String color) 
	{
		boolean fillColumn=true;
		for (int i=0;i<row-1;i++)
		{ // Fill from top to point
			fillColumn = true;
			if (reversiBoard[i][column].equals(color))
			{
				for(int j=i+1;j<row;j++)
				{	
					if(reversiBoard[j][column].equals(color) || reversiBoard[j][column].equals(empty)) 
					{
						fillColumn=false;
					}
				}
				if(fillColumn) 
				{
					for(int j=i+1;j<row;j++)
					{
						reversiBoard[j][column]=color;
						fillCount++;
					}
				}
			}	
		}
		// Fill from bottom to point
		for (int i=n-1;i>row;i--) 
		{
			fillColumn=true;
	
			if(reversiBoard[i][column].equals(color)) 
			{
				for(int j=i-1;j>row;j--) 
				{
					if(reversiBoard[j][column].equals(color) || reversiBoard[j][column].equals(empty)) 
					{
						fillColumn=false;
					}
				}
				if (fillColumn) 
				{
					for(int j=i-1;j>row;j--) 
					{
						reversiBoard[j][column]=color;
						fillCount++;
					}
				}
			}
		}
			
	}
	
	private void fillDiagonal(int row, int column, String color) 
	{
		// Get the opposite color of this method calling parameter
		String oppositeColor;
		if (color.equals("X"))
			oppositeColor = "O";
		else
			oppositeColor = "X";
			
		boolean fillDiagonal = false;
		
		// Fill from the point up and left
		if (row > 1 && column > 1) 
		{
			if (reversiBoard[row-1][column-1].equals(oppositeColor)) 
			{
				for (int i=row-2,j=column-2;i>=0 && j>=0;i--,j--) 
				{
					if (reversiBoard[i][j].equals(color))
						fillDiagonal=true;
				}
			}
			if(fillDiagonal) 
			{	
				for (int i=row-1,j=column-1;reversiBoard[i][j].equals(oppositeColor);i--,j--) 
				{
					reversiBoard[i][j]=color;
					fillCount++;
				}
			}
		}
		// Fill from the point left and down
		if (row<n-2 && column>1) 
		{
			fillDiagonal=false;
			if (reversiBoard[row+1][column-1].equals(oppositeColor)) 
			{
				for (int i=row+2,j=column-2;i<n && j>=0;i++,j--) 
				{
					if (reversiBoard[i][j].equals(color))
						fillDiagonal=true;
				}
			} // Conclude if condition that determines fillDiagonal
			if (fillDiagonal) 
			{
				for(int i=row+1,j=column-1;reversiBoard[i][j].equals(oppositeColor);i++,j--) 
				{
					reversiBoard[i][j] = color;
					fillCount++;
				}
			} // Conclude if condition that fills diagonal row if fillDiagonal is true
		}
		// Fill from the point right and up
		if (row>1 && column<n-2) 
		{
			fillDiagonal=false;
			if (reversiBoard[row-1][column+1].equals(oppositeColor)) 
			{
				for(int i=row-2,j=column+2;i>=0 && j<n;i--,j++) 
				{
					if (reversiBoard[i][j].equals(color))
						fillDiagonal=true;
				}
			}
			if (fillDiagonal) 
			{
				for (int i=row-1,j=column+1;reversiBoard[i][j].equals(oppositeColor);i--,j++) 
				{
					reversiBoard[i][j]=color;
					fillCount++;
				}
			}
		}
		// Fill from the point right and down
		if (row<n-2 && column <n-2) 
		{
			fillDiagonal=false;
			if (reversiBoard[row+1][column+1].equals(oppositeColor)) 
			{
				for (int i=row+2,j=column+2;i<n && j<n;i++,j++) 
				{
					if (reversiBoard[i][j].equals(color))
						fillDiagonal=true;
				}
			}
			if (fillDiagonal) 
			{
				for(int i=row+1,j=column+1;reversiBoard[i][j].equals(oppositeColor);i++,j++) 
				{
					reversiBoard[i][j]=color;
					fillCount++;
				}
			}
		}
		
	}
	// Print the board to the screen in correct format
	public void printBoard() 
	{
		String[][] thisBoard = this.getBoard();
		int row = 0;
		System.out.print(" ");
		for(int i=0;i<n;i++) 
		{
			System.out.print(" "+(i+1));
		}
		System.out.println();
		for(int i=0;i<n;i++)
		{
			row++;
			System.out.print(row+" ");
			for (int j=0;j<n;j++) 
			{
				System.out.print(thisBoard[i][j]+" ");
				if (j==n-1)
					System.out.println();
			}
		}
	}
	// Start the board with the center pieces as a checkerboard pattern.
	public void initBoard() 
	{
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				reversiBoard[i][j] = empty;
			}
		}
		reversiBoard[(n/2)-1][(n/2)-1] = "X";
		reversiBoard[(n/2)-1][(n/2)] = "O";
		reversiBoard[(n/2)][(n/2)] = "X";
		reversiBoard[(n/2)][(n/2)-1] = "O";
	}
	// Check if there are any moves remaining for either player on the board
	public boolean movesRemaining() 
	{
		boolean movesRemaining=false;
		if (this.hasMove("X")==false && this.hasMove("O")==false) 
		{
			movesRemaining = false;
			return movesRemaining;
		}
			for (int i=0;i<n;i++) 
			{
				for (int j=0;j<n;j++) 
				{
					if (reversiBoard[i][j].equals("."))
						movesRemaining = true;
				}
			}
		return movesRemaining;
	}
	
	//Count up the spaces each color owns
	public int getScore(String color) 
	{
		int score = 0;
		for (int i=0;i<n;i++) 
		{
			for (int j=0;j<n;j++) 
			{
				//Add to score count if there is a matching color on space [i][j]
				if (reversiBoard[i][j].equals(color))
					score++;
			}
		}
		return score;
	}
	
	public boolean hasMove(String color) 
	{
		// Starting at every empty space on the board, check if there is a move for that color
		boolean hasMove=false;
		fillCount=0;
		for (int i=0;i<n;i++) 
		{
			for(int j=0;j<n;j++) 
			{
				if (reversiBoard[i][j].equals(empty) && this.isValidMove(i,j,color))
				{
					hasMove=true;
					return hasMove;
				}
			}
		}
		return hasMove;
	}
	// Test a move to see if it fills any spaces according to the move method
	// Reset the board to original state after determination	
	public boolean isValidMove(int row, int column, String color) 
	{
		boolean validMove = true;
		//We use a copy of the game board for this method instead of the permanent board itself
		String[][] tempBoard=deepCopy(reversiBoard);
		if (row < 0 || row > n || column < 0 || column > n)
		{
			validMove=false;
			return validMove;
		}
			
		if (reversiBoard[row][column].equals(empty)==false)
		{
			validMove=false;
			return validMove;
		}

		playerMove(row,column,color);

		// If the board fills no sections from the move, then it must be invalid.
		if (fillCount==0)
			validMove=false;
		// Restore the original board
		reversiBoard = tempBoard;
		return validMove;
	}
	
	//This method turns empty spaces into spaces with underlines if 
	//a valid move can be made in that space
	public void underlineMoves(String color) 
	{
		//Create a new board so we can work with that instead of the permanent game board
		String[][] underlinedBoard = deepCopy(reversiBoard);
		//Loop through all spaces on the board
		for (int i=0;i<n;i++) 
		{
			for (int j=0;j<n;j++) 
			{
				//Underline the space if a valid move can be made there
				if (this.isValidMove(i,j,color)) 
				{
					underlinedBoard[i][j]="_";
				}
			}
		}
		int row = 0;
		System.out.print(" ");
		for(int i=0;i<n;i++) 
		{
			System.out.print(" "+(i+1));
		}
		System.out.println();
		for(int i=0;i<n;i++)
		{
			row++;
			System.out.print(row+" ");
			for (int j=0;j<n;j++) 
			{
				System.out.print(underlinedBoard[i][j]+" ");
				if (j==n-1)
					System.out.println();
			}
		}
			
	}	
	
	//We use an ArrayList because we don't yet know how many possible moves there are
	public ArrayList<String> getPossibleMoves(String color) 
	{
		ArrayList<String> possibleMoves = new ArrayList<String>();
		//Loop through all spaces on the board
		for (int i=0;i<n;i++) 
		{
			for (int j=0;j<n;j++) 
			{
				//We add a move to the array if it is valid.

				if (this.isValidMove(i,j,color))
					possibleMoves.add((i+1)+" "+(j+1));		
			}
		}
		possibleMoves.trimToSize();
		return possibleMoves;
	}
		
	//This method creates a new reversiboard that has the same characteristics
	//as the permanent board currently in play. 
	public static String[][] deepCopy(String[][] permanentBoard) 
	{
		int n = permanentBoard.length;
		String empty = ".";
		String[][] tempBoard = new String[n][n];
		//Loop through all spaces on the board
		for(int i=0;i<n;i++) 
		{
			for (int j=0;j<n;j++) 
			{
				//Create a new string object based on what's stored in the permanent board
				//and have the temporary board point to it
				if (permanentBoard[i][j].equals("X"))
					tempBoard[i][j]="X";
				else if (permanentBoard[i][j].equals("O"))
					tempBoard[i][j]="O";
				else
					tempBoard[i][j]=empty;
			}
		}
		return tempBoard;
	}	
	
	public int getDimension()
	{
		return n;
	}
		
	// TESTING FEATURES
	
	public void clear() 
	{
	// Clear the board to make all empty spaces
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				reversiBoard[i][j] = empty;
				}
			}
	}
	public void setTestBoard() 
	{
		// This method was used to set up a specific layout for a board to test specific situations
		// We set the instance variable reversiBoard to whatever content was desirable for testing.
	}
	public void paint(int row, int column, String color) 
	{
		// This method changes the board value at [row][column] to the color of the string passed
		reversiBoard[row][column]=color.toUpperCase();
	}	
}