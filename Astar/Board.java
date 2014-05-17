import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.ArrayList;

	// Board contains objects of type square, the player is created within
	// the board class.
	
	public class Board extends JPanel
	{
  		private final int SIZE;
		private final int TILE_SIZE;
		
		private Timer timer;
		
		private JLabel score, hScore, pause;
		private JButton reset;

		private int HIGHSCORE = 0;
		private int SCORE = 0;
		private Color scoreColor = Color.yellow;
		private Color menuColor = Color.white;
		private int fontSize = 12;
		private Font font = new Font("TimesRoman", Font.PLAIN, fontSize);
		private int scorePosX = 0;
		private int scorePosY = 0;

		private int drawTime = 300;

		private boolean isPlaying = true; // check player's pause/play status

		private Player player;
		private Square target;
		private Grid grid;

		private Board board;
		private Thread djikstraThread = null;
		private Thread manhattenThread = null;
		private Thread euclideanThread = null;
		
		// Constructor for board
		public Board(int size, int tile_size)
		{
			SIZE = size;
			TILE_SIZE = tile_size;
			
			addKeyListener(new PlayerListener());
			setBackground(Color.black);
			setPreferredSize(new Dimension(SIZE,SIZE));
			
			grid = new Grid(SIZE,TILE_SIZE);
			
			setFocusable(true);

			board = this;

			
			fontSize = (int)SIZE/25;
			scorePosX = SIZE/2-15-fontSize;
			scorePosY = 3*TILE_SIZE/7+fontSize/2;
			font = new Font("TimesRoman",Font.PLAIN,fontSize);
			//font.deriveFont(fontSize);
			init();
		}
		
		// Start game, is called at the beginning, or when reset.
		
		public void init()
		{
			int[] playerStart = grid.getTile(2,3);
			int[] targetStart = grid.getTile(1,1);
			player = new Player(playerStart[0],playerStart[1],TILE_SIZE); // spawn the player in the center of the board
			target = new Square(targetStart[0],targetStart[0],TILE_SIZE);
			target.setTarget();
		}
		
		public void paintComponent(Graphics g)
		{
			if(isPlaying)
			{
				super.paintComponent(g);
				player.paintComponent(g);
				target.paintComponent(g);
				grid.paintComponent(g);
				paintScore(g);
			}
			else
			{
				paintPause(g);
			}
		}

		public void paintScore(Graphics g)
		{
			g.setFont(font);
			g.setColor(scoreColor);
			g.drawString("Score: "+SCORE,scorePosX,scorePosY);
		}

		public void paintPause(Graphics g)
		{
			g.setFont(font);
			g.setColor(menuColor);
			g.drawString("PAUSE",SIZE/2,SIZE/2);
		}

		public void setGrid(String[][] boardMap)
		{
			grid = new Grid(SIZE,TILE_SIZE,boardMap,this);
			grid.setDrawTime(drawTime);
		}

		public void setGrid(String[][] boardMap, String heuristic)
		{
			grid = new Grid(SIZE,TILE_SIZE,boardMap,heuristic);
			grid.setDrawTime(drawTime);
		}		

		public void setPlayer(int[] playerLocation)
		{
			int[] tileLocation = grid.getTile(playerLocation[0],playerLocation[1]);
			player = new Player(tileLocation[0],tileLocation[1],TILE_SIZE);
		}

		public void setTarget(int[] targetLocation)
		{
			int[] tileLocation = grid.getTile(targetLocation[0],targetLocation[1]);
			target = new Square(tileLocation[0],tileLocation[1],TILE_SIZE);
			target.setTarget();
		}
		
		// Accessor
		public int getBoardSize()
		{
			return SIZE;
		}

		public int getTileSize()
		{
			return TILE_SIZE;
		}

		public void setDrawTime(int newTime)
		{
			drawTime = newTime;
			grid.setDrawTime(newTime);
		}
		
		// Checks if the player collides with the target piece
		
		public boolean targetReached()
		{
		   boolean targetReached = false;
			if (player.getX() == target.getX() && player.getY() == target.getY())
			{
				targetReached = true;
			}
			return targetReached;
		}

		public void restartThreads()
		{
			if(manhattenThread!=null)
			{
				manhattenThread.stop();
			}
			if(djikstraThread!=null)
			{
				djikstraThread.stop();
			}
		}

		public void pauseThreads()
		{
			if(manhattenThread!=null)
			{
				manhattenThread.suspend();
			}
			if(djikstraThread!=null)
			{
				djikstraThread.suspend();
			}
		}

		public void resumeThreads()
		{
			if(manhattenThread!=null)
			{
				manhattenThread.resume();
			}
			if(djikstraThread!=null)
			{
				djikstraThread.resume();
			}
		}
		
		// Listens to the direction keys and space bar for directionality and pause function
			
		private class PlayerListener implements KeyListener
		{
			public void keyPressed(KeyEvent e)
			{
				if ((e.getKeyCode() == KeyEvent.VK_UP) && isPlaying && !grid.checkCollisionUp(player))
				{
					player.moveUp();
				}
				if ((e.getKeyCode() == KeyEvent.VK_DOWN) && isPlaying && !grid.checkCollisionDown(player))
				{
				   player.moveDown();
				}
				if ((e.getKeyCode() == KeyEvent.VK_LEFT) && isPlaying && !grid.checkCollisionLeft(player))
				{
					player.moveLeft();
				}
				if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && isPlaying && !grid.checkCollisionRight(player))
				{
					player.moveRight();
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					if (isPlaying)
					{
						pauseThreads();
						revalidate();
						repaint();
						isPlaying = false;
						return;
					}
					if (!isPlaying)
					{
						resumeThreads();
						isPlaying = true;
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_M)
				{
					restartThreads();
					grid.setHeuristic("Manhatten");
					grid.resetTiles();
					manhattenThread = new Thread(new Runnable()
					{
						public void run()
						{
							grid.calculatePath(player,target);
						}
					});
					manhattenThread.start();
					
				}

				if(e.getKeyCode() == KeyEvent.VK_E)
				{
					restartThreads();
					grid.setHeuristic("Euclidean");
					grid.resetTiles();
					euclideanThread = new Thread(new Runnable()
					{
						public void run()
						{
							grid.calculatePath(player,target);
						}
					});
					euclideanThread.start();
				}

				if(e.getKeyCode() == KeyEvent.VK_D)
				{
					restartThreads();
					grid.setHeuristic("Djikstra");
					grid.resetTiles();
					djikstraThread = new Thread(new Runnable()
					{
						public void run()
						{
							grid.calculatePath(player,target);
						}
					});
					djikstraThread.start();
				}
					
				if (targetReached())
				{
					int[] newLocation = grid.getRespawnLocation(player,target);
					target.setX(newLocation[0]);
					target.setY(newLocation[1]);
					target.setSteps(0);
					SCORE+=10;
					grid.resetTiles();
				}
				repaint();
			}
				
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		}
			
		// This class defines the actionlistener for the reset button, and
		// it calls the init() method to reset the game.
		
		private class ResetListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Thread.sleep(3000);
				}
				catch(InterruptedException IE){}
				removeAll();
				repaint();
				init();
			}
		}
	}