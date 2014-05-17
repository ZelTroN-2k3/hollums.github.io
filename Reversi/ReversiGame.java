import java.util.InputMismatchException;
import java.util.Scanner;

public class ReversiGame 
{

	public static void main(String[] args) 
	{
		if (args.length == 0 || args.length > 4)
		{
			System.out.println("You have entered an invalid number of arguments.");
			System.out.println("You must enter the arguments on the command line in the format:"
									+"\n\t<Player>[vs]<Player> <Board Size>(This argument is optional)");
			System.exit(0);
		}
		//Make new scanner to read input from players
		String player1=null;
		String player2=null;
		String versus=null;
		//Assign inputs from command line to variables
		try 
		{
			player1 = args[0].toLowerCase();
			versus = args[1].toLowerCase();
			player2 = args[2].toLowerCase();
		}
		//Catch the case that the wrong number of arguments were supplied
		//If there is the wrong number of arguments, we display an error and exit
		catch(ArrayIndexOutOfBoundsException e) 
		{
			System.out.println("You must enter the players in the argument in the format:");
			System.out.println("\n\t\t<Player> vs <Player> [Optional]<Board Size>\n");
			System.out.println("The player type can be one or both of the following: \n-\tRandomComputer\n-\tIntelligentComputer\n-\tHuman");
			System.out.println("\nCorrect the arguments in the command line to fit this format and re-run the program.");
			System.exit(0);
		}
		
		//Make sure the second word in the command line argument is 'vs'.
		//If not, display error message and exit program.
		if (versus.equals("vs")==false)
		{
			System.out.println("You must enter the players in the argument in the format:");
			System.out.println("\t\t<Player> vs <Player> [Optional] <Board Size>");
			System.out.println("The player type can be one or both of the following: \n-\tRandomComputer\n-\tIntelligentComputer\n-\tHuman");
			System.out.println("\nCorrect the arguments in the command line to fit this format and re-run the program.");
			System.exit(0);
		}
		ReversiBoard board = null;
		//Create new board and players
		int size=0;
		if (args.length == 4)
		{
			try 
			{
				size = Integer.parseInt(args[3]);
			}
			catch(NumberFormatException e)
			{
				System.out.println("You have entered an invalid character for board size.");
				System.out.println("The board size must be a positive even integer greater than 4.");
				System.exit(0);
			}
			if (size >= 4 && size%2==0)
			 	{
					board = new ReversiBoard(size);
				}		
			else
			{
				System.out.println("The size of the board must be an even integer greater than or equal to 4.");
				System.exit(0);
			}
		}	
		else
		{
			board = new ReversiBoard();
		}
		
		Player p1 = getPlayer(1,player1,board);
		Player p2 = getPlayer(2,player2,board);
		//Ask how many games the human player wants to play

		int numberOfGames = 0; 
		boolean isValidGameNumber = false;
		do
		{
			try
			{
				System.out.println("How many games would you like to play?");
				Scanner valid = new Scanner(System.in);
				numberOfGames = valid.nextInt();
				if (numberOfGames < 0)
				{
					System.out.println("The number of games must be a positive integer.");
					continue;
				}
				isValidGameNumber = true;
			}
			catch(InputMismatchException e) 
			{
				System.out.println("You entered an invalid character, enter a positive integer.");
				continue;
			}
		}while(!isValidGameNumber);
				
		//If the player doesn't want to play, say 'Bye!' and exit.
		if(numberOfGames == 0)
		{
			System.out.println("Bye!");
			System.exit(0);
		}
		int totalGames = numberOfGames;
		//Initialize total number of wins for each player
		double playerOneWins = 0;
		double playerTwoWins = 0;
		int tie = 0;
		for (int i=0;i<numberOfGames;i++)
		{
			//Display game number and play
			System.out.println("GAME #"+(i+1));
			play(p1,p2,board);
			//Keep track of scores
			if (p1.getScore()>p2.getScore()) 
			{
				playerOneWins+=1;
			}
			else if (p1.getScore()<p2.getScore())
				playerTwoWins+=1;
			else 
			{
				tie++;
				totalGames--;
			}
		}
		//Print out results of games
		System.out.println("\nGAME RESULTS:");
		System.out.println("\nPlayer 1 won "+(int)playerOneWins+" times.");
		System.out.println("Player 2 won "+(int)playerTwoWins+" times.");
		System.out.println("There were "+tie+" tie games.");
		System.out.println("\nFor "+totalGames+" games played (excluding the games that resulted in a tie),");
		if (playerOneWins > playerTwoWins)
			System.out.println("Player 1 had a "+(int)((playerOneWins/totalGames)*100)+"% win rate over Player 2.");
		else if (playerOneWins < playerTwoWins)
			System.out.println("Player 2 had a "+(int)((playerTwoWins/totalGames)*100)+"% win rate over Player 1.");
		else
			System.out.println("Player 1 and Player 2 are equally matched.");
	}
		

	public static void play(Player p1, Player p2, ReversiBoard board) 
	{
		int playerScore1,playerScore2;
		int turn = 1;
		//Initialize the game board with reversiboard definition
		board.initBoard();
		System.out.println("Note: Human players can type 'help' to view player options.");
		System.out.println("      Computer player's moves are displayed with an asterisk <*> for ease of viewing.");
		System.out.println("\nStarting game!");
		//Alternate turns. First player's turn number is always odd, so the remainder when it is 
		//divided by 2 will always be positive.
		do 
		{
			System.out.println();
			if (turn%2>0) 
			{
				//Move and switch turns.
				p1.move();
				turn++;
			}
			else
			{
				p2.move();
				turn++;
			}
		}while(board.movesRemaining());
		//Display game board at the end for the human player to see
		System.out.println();
		board.printBoard();
		System.out.println("\nThere are no possible moves for either players remaining!");
		
		//Show the number of spaces each player owns at the end
		playerScore1=p1.getScore();
		playerScore2=p2.getScore();
		
		//Tally up scores and display winner at the conclusion of each game.
		
		System.out.println("\nPlayer 1 score: "+playerScore1);
		System.out.println("Player 2 score: "+playerScore2);
		
		if (playerScore1>playerScore2)
			System.out.println("\nPlayer 1 (X) wins!");
		else if (playerScore1<playerScore2)
			System.out.println("\nPlayer 2 (O) wins!");
		else
			System.out.println("\nIt's a tie!");
	}

	public static Player getPlayer(int playerNumber, String thisPlayer, ReversiBoard board) 
	{
		String human = "human";
		String random = "randomcomputer";
		String intelligent = "intelligentcomputer";
			
		Player player=null;
		thisPlayer = thisPlayer.toLowerCase();

		//Create players depending on arguemnts. 
		if (thisPlayer.equals(human))
			player = new HumanPlayer(playerNumber, board);
		else if (thisPlayer.equals(random))
			player = new RandomComputerPlayer(playerNumber,board);
		else if (thisPlayer.equals(intelligent))
			player = new IntelligentComputerPlayer(playerNumber,board);
		//Display error message if types don't match
		else 
		{
			System.out.println(thisPlayer+" is not a valid player type.");
			System.out.println("\nThe valid player types are:\n-\tRandomComputer\n-\tIntelligentComputer\n-\tHuman");
			System.exit(0);
		}
		return player;
	}
	
}
		
