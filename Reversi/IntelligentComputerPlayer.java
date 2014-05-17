import java.util.*;

public class IntelligentComputerPlayer extends ComputerPlayer {

public String thisMove=null;

public IntelligentComputerPlayer(int playerNumber, ReversiBoard thisBoard) 
   {
	//Call to constructor of parent, which calls construct of its own parent
   super(playerNumber,thisBoard);
   }
   
public void move() {
	//First make sure there is a vald move to make
   // pause(); // COMMENT THIS LINE OUT TO EFFICIENTLY TEST GAME STATS IN A SERIES
   if (hasMove()) {
      int row,column;
      //Store the intelligent move in an array on ints
      int[] smartMove = getSmartMove();
      row = smartMove[0];
      column = smartMove[1];
      //Show the move as it's being made and display outcome to user
      displayComputerMove(row,column);
      System.out.println("Player "+color+" move: "+thisMove);
      board.playerMove(row,column,color);
      }
   //If there are no possible moves, display a message and move to next player
   else {
      System.out.println("Player "+color+" has no possible moves.");
      }
   }
   
public int[] getSmartMove() {
	//Store move in array on ints of size 2
   int[] smartMove = new int[2];

   //Use arraylist to make all moves that return the highest yield. We will do a tie-breaker.
   ArrayList<String> highestMoves = new ArrayList<String>();
   int row,column;
   String possibleMove = null;
   int maxFill = 0;
   //Store the split move in a string array of size two. This is for easy access later.
   String splitMove[]=new String[2];
   ReversiBoard testBoard = new ReversiBoard(board.n);
   
   //Make a new board to test all possible highest moves
   testBoard.reversiBoard = ReversiBoard.deepCopy(board.reversiBoard);
   ArrayList<String> possibleMoves = testBoard.getPossibleMoves(color);
   //If it is possible that we can make the opposite color have no moves, just pick a random move.
   if (board.getPossibleMoves(oppositeColor).size()==0)
   {
	   //We loop through all moves that the intelligent computer can possibly make...
      for (int i=0;i<possibleMoves.size();i++) {
      testBoard.reversiBoard=ReversiBoard.deepCopy(board.reversiBoard);
      possibleMove = possibleMoves.get(i);
      //Use " " as a delimiter to separate the two integers we have saved from getPossibleMoves()
      splitMove = possibleMove.split(" ");
      row = Integer.parseInt(splitMove[0])-1;
      column = Integer.parseInt(splitMove[1])-1;
      testBoard.playerMove(row,column,color);
      
      //...and see how well the opposing player can do after that move
      if (testBoard.fillCount>maxFill){
         maxFill = testBoard.fillCount;
         splitMove = possibleMove.split(" ");
         //Parse the moves into ints and make the moves on the temporary board
         smartMove[0]=Integer.parseInt(splitMove[0])-1;
         smartMove[1]=Integer.parseInt(splitMove[1])-1;
         return smartMove;
         }
      }
   }

   //Now we loop through the possible moves...
   for (int i=0;i<possibleMoves.size();i++) {
      testBoard.reversiBoard=ReversiBoard.deepCopy(board.reversiBoard);
      possibleMove = possibleMoves.get(i);
      splitMove = possibleMove.split(" ");
      row = Integer.parseInt(splitMove[0])-1;
      column = Integer.parseInt(splitMove[1])-1;
      testBoard.playerMove(row,column,color);
      
      //...and see how many new spaces we will convert with that move 
      //If it is greater than all the previous converted spaces, then we clear
      //the arraylist of highest moves and add this one instead
      if (testBoard.fillCount>maxFill){
         maxFill = testBoard.fillCount;
         
         highestMoves.clear();
         highestMoves.add(possibleMove);
         highestMoves.trimToSize();
         
         }
      //If it is equal to the number of colors converted from a previous move, we
      //will add to the arraylist of highest moves and do a tie-breaker 
      else if (testBoard.fillCount == maxFill) {
         highestMoves.add(possibleMove);
         highestMoves.trimToSize();
      }
   }
   //Return the move we have decided is most defensive
   smartMove=getHighestDefensiveMove(highestMoves);
   thisMove = (smartMove[0]+1)+" "+(smartMove[1]+1);
   return smartMove;
}
   
//This method will act as a tie-break for moves that yield the same number of spaces gained
public int[] getHighestDefensiveMove(ArrayList<String> highestMoves) {
   int[] move = new int[2];
   int rowPossMove=0, columnPossMove=0, rowMove=0, columnMove=0,minOppFill=board.getDimension(),thisFill;
   String[] splitMove = new String[2];
   //Loop through all moves that we have decided yield the most spaces
   for (int i=0;i<highestMoves.size();i++) 
      {
      splitMove=highestMoves.get(i).split(" ");
      rowPossMove = Integer.parseInt(splitMove[0])-1;
      columnPossMove = Integer.parseInt(splitMove[1])-1;
      thisFill = getHighestOpposingMove(rowPossMove,columnPossMove);
      if (thisFill<minOppFill) 
         {
         minOppFill = thisFill;
         rowMove = rowPossMove;
         columnMove = columnPossMove;
         move[0]=rowMove;
         move[1]=columnMove;
        }
      }
   return move;
}

public int getHighestOpposingMove(int row, int column) {
   ReversiBoard tempBoard1 = new ReversiBoard(board.n);
   ReversiBoard tempBoard2 = new ReversiBoard(board.n);
   tempBoard1.reversiBoard = ReversiBoard.deepCopy(board.reversiBoard);
   tempBoard1.playerMove(row,column,color);
   tempBoard2.reversiBoard = ReversiBoard.deepCopy(tempBoard1.reversiBoard);
   int thisRow=0,thisColumn=0;
   int thisFill = 0,maxFill=0;
   String splitMove[]=new String[2];
   // for this move sent to the method, the computer checks all of the opponents possible moves
   // and finds the amount of the highest yielding move in the list of the opponents possible moves
   for (int i=0;i<tempBoard2.getPossibleMoves(oppositeColor).size();i++) {
      splitMove=tempBoard2.getPossibleMoves(oppositeColor).get(i).split(" ");
      thisRow = Integer.parseInt(splitMove[0])-1;
      thisColumn = Integer.parseInt(splitMove[1])-1;
      tempBoard2.playerMove(thisRow,thisColumn,oppositeColor);
     thisFill = tempBoard2.fillCount;
     if (maxFill < thisFill) {
         maxFill = thisFill;
         }
      tempBoard2.reversiBoard = ReversiBoard.deepCopy(tempBoard1.reversiBoard);
      }
   return maxFill;
	}
}
