
public abstract class ComputerPlayer extends Player 
{
	public ComputerPlayer(int playerNumber, ReversiBoard thisBoard) 
	{
		super(playerNumber, thisBoard);
	}
		
	//This method will be the same for a random and intelligent computer
	//It shows the intermediate step of which move the computer made so the human player can keep track
	public void displayComputerMove(int row, int column) 
	{
		board = this.getBoard();
		String[][] tempBoard = ReversiBoard.deepCopy(board.getBoard());
		board.getBoard()[row][column] = "*";
		board.printBoard();
		board.reversiBoard=tempBoard;
	}

}
	