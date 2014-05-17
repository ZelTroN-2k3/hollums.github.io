import java.util.*;

public class HumanPlayer extends Player
{

	//Color is either 'X' or 'O'
	private Scanner input = new Scanner(System.in);
	
	public HumanPlayer(int playerNumber, ReversiBoard thisBoard) 
	{
		super(playerNumber, thisBoard);
	}
	
	public void move() 
	{
		//Use the hasMove method from parent class; all subclasses have same hasMove method
		if (super.hasMove()) 
		{
			//Make array for where player wants to move
			int[] playerInput = getPlayerInput();
			int row, column;
			//Assign move to row and column, if player provided two ints
			if (playerInput.length == 2) 
			{
				row = playerInput[0];
				column = playerInput[1];
				board.playerMove(row,column,color);
			}
		}
		//If the current player has no valid moves, we skip the turn.
		else 
		{
				System.out.println("Player "+color+" does not have any valid moves.");
				System.out.println("Moving to next player's turn...");
		}
	}
		
	private int[] getPlayerInput() 
	{
		int[] playerMove=new int[2];
		int[] skip = new int[1];
		String testCode;
		int row, column;
		//First set moveComplete to false, because we haven't made a move yet. We will
		//change it to true when we are done.
		boolean moveComplete = false;
		do
		{
			//First we display what moves can be made
			board.underlineMoves(color);
			//Prompt for the human player's move
			System.out.print("Player "+color+" move: ");
			try 
			{
				testCode = input.next().toLowerCase();
				//If the player types 'help', display commands they can type for various things.
				
				// TESTING CODE
				if (testCode.equals("testboard"))
				{
					// Useful way to set up a board with specific layout during run time
					// for the players to interact with
					board.setTestBoard();
					continue;
				}
				if (testCode.equals("paint")) 
				{
					// Useful method that paints a reversi board from a clean slate
					// during run time, players interact with when finished.
					board.clear();
					String answer = "yes";
					do
					{
						System.out.print("Paint: ");
						board.paint(input.nextInt()-1,input.nextInt()-1,input.next());
						board.printBoard();
						System.out.println("Continue? y/n");
						answer = input.next();
					}while (answer.equals("y") || answer.equals("yes"));
					continue;
				}			
				// HELPFUL USER INTERFACE MENU/OPTIONS
				if (testCode.equals("help")) 
				{
					String response;
					System.out.println("To view the game rules, type 'rules'.");
					System.out.println("\nNote:\tThe following options are primarily used for testing purposes"+
									   "\n\tand can be entered from the help menu, or during the player move prompt."); 
					System.out.println("\tThese options are not necessarily consistent with the original game's rules.");
					System.out.println("\nIf you'd like to quit the game, type 'quit'.");
					System.out.println("If you'd like to see what your available moves are, type 'moves'.");
					System.out.println("If you'd like to skip your turn, type 'skip'.");
					System.out.println("If you'd like to restart the game, type 'restart'.");
					System.out.println("\nType in a command, or just type 'continue' to keep playing.");
					response = input.next();
					if (response.equals("continue")) 
					{
						System.out.println("\nResuming game...\n");
						continue;
					}
					// Whatever the player types out of the help menu that's not 'continue'
					// is either picked up by another following menu command or entered as
					// the player's next move.
					else
						testCode=response;
				}
				//Give player option to quit whenever
				if (testCode.equals("quit"))
					System.exit(0);
				//Let's player skip their turn and go to the computer player
				if (testCode.equals("skip")) 
				{
					System.out.println("Player "+color+"'s turn was skipped!");
					moveComplete=true;
					return skip;
				}
				//If player types 'rules', display rules of reversi and how to play.
				if (testCode.equals("rules")) 
				{
					System.out.println("\nGame rules:\n");
					System.out.println("-\tYou must enter a move in the form: <integer of row>[space]<integer of column>");
					System.out.println("-\tThe move you make will fill all occurrences of the opposing players color between an occurrence"+
								   	   "\n\tof your color on the same row, column, or diagonal position and the move you chose."+
								       "\n\tIn other words, you are trying to create a 'sandwich' consisting of the opposing players color"+
								       "\n\tand two instances of your color."+
								       "\n-\tIt is possible to make multiple 'sandwiches' in a single move.");
											
					System.out.println("-\tThe move you enter must be a valid move, the valid moves are underlined on the game board.");
					System.out.println("-\tIf there are no valid moves for a player, the player's turn will be skipped.");
					System.out.println("-\tIf neither players have any remaining moves to make, the game is finished.");
					System.out.println("-\tYour score is determined by how many occurrences of your color is on the board.");
					System.out.println("-\tAt the conclusion of the game, the player with the highest score wins.");
					System.out.println("\nResuming the game...\n");
					continue;
				}
				//Allow human player to start over.
				if (testCode.equals("restart")) 
				{
					//Reset the board to initialboard.
					board.initBoard();
					System.out.println("Restarting the game...\n");
					int playerNumber = 0;
				
					do 
					{
						//Let player choose to go first or second
						System.out.print("Which player would you like to be?\nEnter 1 or 2: ");
						int answer;
						Scanner valid;
						try 
						{
							valid = new Scanner(System.in);
							answer = valid.nextInt();
							playerNumber = answer;
							if (playerNumber != 1 || playerNumber !=2)
							{
								System.out.println("The player number must be a 1 or 2.");
								playerNumber = 0;
							}
						}
						//Catch in case the string entered can't be parsed into an int
						catch (InputMismatchException e) 
						{
							System.out.println("The player number must be 1 or 2.");
						}
					}while(playerNumber==0);

				//Display message to signal new game.
				System.out.print("\nNew game! ");
				if (playerNumber == 1)
				{
					System.out.println("It's player "+color+"'s turn.\n");
					continue;
				}
				//If human chooses to be player 2, skip this turn and go to computer's turn.
				else if (playerNumber == 2)
				{
					System.out.println("It's player "+oppositeColor+"'s turn.\n");
					return skip;			}
				}
				//Shows which moves are possible
				if (testCode.equals("moves")) 
				{
					int testRow=0;
					int testColumn=0;
					//Get all possible moves and list them for the player
					ArrayList<String> playerMoves = board.getPossibleMoves(color);
					ReversiBoard testBoard = new ReversiBoard(board.n);
					testBoard.reversiBoard = ReversiBoard.deepCopy(board.reversiBoard);
					System.out.println("\nYour possible moves are: ");
					String[] testMove = new String[2];
					//Loop through all moves in playerMoves arraylist and display them
					for (int i=0;i<playerMoves.size();i++) 
					{
						testMove = playerMoves.get(i).split(" ");						
						testRow = Integer.parseInt(testMove[0])-1;
						testColumn = Integer.parseInt(testMove[1])-1;
						testBoard.playerMove(testRow,testColumn,color);
						System.out.println(playerMoves.get(i)+" fills "+testBoard.fillCount+" spaces.");
						testBoard.reversiBoard = ReversiBoard.deepCopy(board.reversiBoard);
					}
					System.out.println("\nResuming game...\n");
					//Start over with the do-while loop for the player
					continue;
				}
				//Take input and subtract 1 to make them into valid moves. 
				//Reversiboard starts counting at 1, but arrays start at 0
				row = Integer.parseInt(testCode)-1;
				column = input.nextInt()-1;
				//If the move is valid, make the move and end the turn.
				if (board.isValidMove(row,column,color)) 
				{
					moveComplete = true;
					playerMove[0] = row;
					playerMove[1] = column;
				}
				//If the move is not valid, tell the player what to do.
				else
					System.out.println("Your move is invalid, type 'rules' to view the game rules, or type 'help' for more player options.");
			}
			//Catch cases where the player made invalid moves
			catch(InputMismatchException e) 
			{
				System.out.println("Your input was invalid, type 'rules' to view the game rules, or type 'help' for more player options.");
				System.out.println();
			}
			catch(NumberFormatException e) 
			{
				System.out.println("Your input was invalid, type 'rules' to view the game rules, or type 'help' for more player options.");
				System.out.println();
			}
		}while(moveComplete==false);
		return playerMove;
	}
}