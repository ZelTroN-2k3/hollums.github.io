// The board interface sets up all the methods that any class that
// implements the board interface will have in common.

public interface Board 
{
	public void playerMove(int row, int column, String color);
	public void initBoard();
	public boolean movesRemaining();
	public int getScore(String color);
	public boolean isValidMove(int row, int column, String color);
	public void printBoard();
}