README

Alex Hollums & Cindy Lu
ReversiGame: Project 2

This program simulates the game "Reversi", or also known as "Othello".
It includes three different types of players: Human, RandomComputer, and 
IntelligentComputer.  The program can be run with any of these players, or
a mirror match up of the same kind of player.
Human player includes some interactive menu and help features, and in this
program we have included some useful program testing commands for the user.
These features are primarily used for the actual coding process and testing,
we left some of them in for anyone who wants to check our program's efficiency.
Simply type 'help' when the game prompts the user to make a move.
A feature we have added in the main method is a number of games determination,
if the user runs multiple games in the same run, the program will tally up
the total wins and total losses as well as the win ratio of the winning player.
Note: To run a series of games between two computer players, you may want to
comment out the 'pause()' statement in both 'move()' methods of 
the classes RandomComputerPlayer and IntelligentComputerPlayer.

Some information about running the program:

When you're in the program's directory on the terminal, type 'make compile'
to compile all .java files.  To run the program, type 'make run' to run a
game of HumanPlayer vs RandomComputer (this is set as the default run).
To run a more specific instance of the program, type 'make compile' and then
'java ReversiGame <Player Type> vs <Player Type>', and if desirable, you can
also determine the size of the board as the 4th argument.  To clean up the 
program's directory, type 'make clean' and this will remove all of the files
of type .class.

Enjoy our game, and don't forget to clean up the mess when you're done. :)

