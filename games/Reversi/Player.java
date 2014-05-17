public abstract class Player 
{

	public String color;
	public String oppositeColor;
	public ReversiBoard board;

	public Player(int playerNumber, ReversiBoard thisBoard) 
	{
		//Match the player with the game board
		board = thisBoard;
		//Assign colors. 'X' is player 1; always goes first.
		if (playerNumber == 1) 
		{
			color = "X";
			oppositeColor = "O";
		}
		else 
		{
			color = "O";
			oppositeColor = "X";
		}
		
	}

	//Every player will have a different way to make a move
	public abstract void move();
	
	//accessor methods
	public String getColor()
	{
		return color;
	}
	public ReversiBoard getBoard()
	{
		return board;
	}
	
	//returns true if a move for the current color can be made, false otherwise
	public boolean hasMove() 
	{
		board = this.getBoard();
		color = this.getColor();
		//initialize to false
		boolean hasMove = false;
		//call the hasMove method of the specific board we are playing with
		if (board.hasMove(color))
			hasMove = true;
		return hasMove;
	}
	
	public int getScore() 
	{
		board = this.getBoard();
		color = this.getColor();
		//call the getScore method of a particular board
		int score=board.getScore(color);
		return score;
	}
    // This method will create a wait time before the program continues to the next line
    // We use this method to make the computer's response time manageable for a user to
    // watch what move the computer picks.
    public void pause() 
	{
		try
		{
            // Increment i to increase/decrease response time by seconds.
			for (int i=0;i<1;i++) {
				Thread.sleep(1000);
                // the sleep method belongs to objects of type thread, and it takes in a parameter
                // for milliseconds, in our case, we make it cease program execution for 1 SECOND
                // in each iteration of the loop.
            }
		}
		catch(InterruptedException e){
            // The sleep method throws the InterruptedException, in our case we are not concerned about this,
            // so the catch block is empty.
        }
	}
}
	
