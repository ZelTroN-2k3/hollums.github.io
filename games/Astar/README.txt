README.txt

Files needed:
	AstarGame.java
	Board.java
	Grid.java
	Menu.java
	Player.java
	Slider.java
	Square.java
	MapChooser.java
	makefile
	bigMap.txt
	boardMap1.txt
	boardMap2.txt
	boardMap3.txt
	boardMap4.txt
	boardMap5.txt
	boardMap6.txt
	demoMap.txt

To run the program:

	Navigate to program directory
	Type:
		make compile
		make run

Playing the game:

	You may select different loaded maps from the file directory from
	the added feature UI on the left side.  Note that when switching a
	map, you must first adjust the speed slider on the side before 
	playing in the new map. (This is due to a frame focus issue).

	Press E for a Euclidean Heuristic search
	Press D for a Djikstra Heuristic search
	Press M for a Manhattan Heuristic search
	Press SPACE to pause the search, SPACE to unpause.

	To move, use the left, right, up, or down arrows. Or pressing 
	arrows simultaneously for diagonal movement.

	Adjust the algorithm's animation speed by sliding the speed slider
	up to increase the time, and down to reduce the time between ticks.
	In addition to the adjustable algorithm speed time, you also have the
	option to pause by the algorithm mid session, so that you can examine 
	closely the steps in process.