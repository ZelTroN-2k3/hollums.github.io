import java.util.*;

public class RandomComputerPlayer extends ComputerPlayer 
{
	
	public String thisMove;
	
	public RandomComputerPlayer(int playerNumber, ReversiBoard thisBoard)
	{
		super(playerNumber, thisBoard);
	}

	public void move() 
	{
      // pause(); // COMMENT THIS LINE OUT TO EFFICIENTLY TEST GAME STATS IN A SERIES
		//First make sure there are valid moves to be made. If not, skip turn.
		if (board.hasMove(color)) 
		{
			//Call getRandomMove() from own class definition
			int[] randomMove = getRandomMove();
		
			//Make the move and display the board
			displayComputerMove(randomMove[0],randomMove[1]);
			
			System.out.println("Player "+color+" move: "+thisMove);
			board.playerMove(randomMove[0], randomMove[1], color);
		}
		else 
		{
			board.underlineMoves(color);
			System.out.println("Player "+color+" has no possible moves.");
		}
	}
	// Get a random move by taking any random value in the arraylist of possible moves
	// and make that move that was picked.
	private int[] getRandomMove() 
	{
		ArrayList<String> possibleMoves;
		//Make array of all possible moves we can choose from
		int[] randomMove = new int[2];
		int randomIndex,row,column;
		
		//Call up the possible moves from the board class
		possibleMoves=board.getPossibleMoves(color);
		//Pick a random move return that value
		randomIndex=(int)(Math.random()*possibleMoves.size());
		thisMove = possibleMoves.get(randomIndex);
		String move[] = new String[2];
		move = thisMove.split(" ");
		row = Integer.parseInt(move[0])-1;
		column = Integer.parseInt(move[1])-1;
		
		randomMove[0]=row;
		randomMove[1]=column;
		
		return randomMove;
	}


}
	
	
