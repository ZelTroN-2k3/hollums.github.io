import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

	// Board contains objects of type square, the snake is created within
	// the board class.
	
	public class Board extends JPanel
	{
  		private final int SIZE = 300;
		private final int DIGIT = 10;
		private int DELAY = 300;
		private int SCORE;
		private int HIGHSCORE = 0;
		
		private Timer timer;
		
		private JLabel score, hScore, pause;
		private JButton reset;
		
		private boolean isPlaying=true;
		
		private SnakeFood food;
		private LinkedList<Square> snake = new LinkedList<Square>();
		private Square head;
		
		// Constructor for board
		public Board()
		{
			addKeyListener(new SnakeListener());
			setBackground(Color.black);
			setPreferredSize(new Dimension(SIZE,SIZE));
			
			score = new JLabel(Integer.toString(SCORE));
			score.setForeground(Color.white);
			
			head = new Square(SIZE/2,SIZE/2);
			snake.add(head);
			food = new SnakeFood(this);
			
			setFocusable(true);
			
			init();
		}
		
		// Start game, is called at the beginning, or when reset.
		
		public void init()
		{
			snake.clear();
			head = new Square(SIZE/2,SIZE/2);
			snake.add(head);
			SCORE = -2;
			add(head);
			addDigit();
			addDigit();
			add(food);
			add(score);
			add(hScore = new JLabel("Highscore: "+Integer.toString(HIGHSCORE)));
			hScore.setForeground(Color.white);
			TimerListener moving = new TimerListener();
			timer = new Timer(DELAY,moving);
			timer.setInitialDelay(1500);
			timer.start();
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);	
		}
		
		// Main logic of snake movement method, is called every time timer fires event
		
		public void move()
		{	
			Square next = deepCopy(snake.peekFirst());
			Square head = deepCopy(snake.peekFirst());
			
			// The algorithm for snakes movement is simple:
			// 	First the head is moved up in the direction of last key direction
			//		Then all pieces following fill the place of the piece that was before it
			//		The direction of the piece before is saved in a temporary variable
			//		Then it is reassigned to the following piece.
			//		This process is accomplished by using a queue, having each square
			//		assigned to a space in the queue.
			for (int i=0;i<snake.size();i++)
			{
				if (i==0)
				{
					Square temp = snake.remove();
					motion(temp,temp.getDirection());
					snake.add(temp);
				}
				else
				{
					head = deepCopy(snake.peekFirst());
					Square temp = snake.remove();
					motion(temp,head.getDirection());
					temp.setDirection(next.getDirection());
					snake.add(temp);
					next = deepCopy(head);
				}

			}
		}
		
		// I thought it would be useful to have a deepCopy method mostly to be safe
		// and ensure that I was not having any objects point to other objects as
		// references to their variables.
		
		public static Square deepCopy(Square original)
		{
			int x=0,y=0;
			String direction = null;
			Square copy = null;
			
			for(int i=0;i<original.getX();i++)
			{
				x++;
			}
			for (int i=0;i<original.getY();i++)
			{
				y++;
			}
			if (original.getDirection().equals("up"))
				direction = "up";
			if (original.getDirection().equals("down"))
				direction = "down";
			if (original.getDirection().equals("left"))
				direction = "left";
			if (original.getDirection().equals("right"))
				direction = "right";
			
			copy = new Square(x,y);
			copy.setDirection(direction);
			return copy;
		}
		
		// This method assists the move() method, make it look cleaner.
		// This method just increments the piece according to its directionality.
		
		private Square motion(Square sq, String dir)
		{
			int x = sq.getX();
			int y = sq.getY();
			
			if(dir.equals("right"))
			{
				sq.setX(x+10);
			}
			if(dir.equals("left"))
			{
				sq.setX(x-10);
			}
			if(dir.equals("up"))
			{
				sq.setY(y-10);
			}
			if(dir.equals("down"))
			{
				sq.setY(y+10);
			}
			return sq;
		}
		
		// Add a new square to the snake, and increment the score as this occurs.
		// The new square is added to the end of the snake and takes the direction
		// of the tail piece.
		
		public void addDigit()
		{
			SCORE++;
			if (SCORE>0 && SCORE < 20)
				timer.setDelay(DELAY-(15*SCORE));
				
			score.setText("Score: "+Integer.toString(SCORE));
			if (SCORE > HIGHSCORE)
			{
				HIGHSCORE = SCORE;
				hScore.setText("Highscore: "+Integer.toString(HIGHSCORE));
			}
			
			Square digit=null;
			String direction = null;
			if (snake.size()==1)
				direction = snake.peekFirst().getDirection();
			else
				direction = snake.peekLast().getDirection();
				
			int x = 0;
			int y = 0;
			
			if (direction.equals("right"))
			{
				x = snake.peekLast().getX()-10;
				y = snake.peekLast().getY();
				digit = new Square(x,y);
				digit.setDirection(direction);
				snake.add(digit);
				add(digit);
			}
			if (direction.equals("left"))
			{
				x = snake.peekLast().getX()+10;
				y = snake.peekLast().getY();
				digit = new Square(x,y);
				digit.setDirection(direction);
				snake.add(digit);
				add(digit);
			}
			if (direction.equals("up"))
			{
				x = snake.peekLast().getX();
				y = snake.peekLast().getY()+10;
				digit = new Square(x,y);
				digit.setDirection(direction);
				snake.add(digit);
				add(digit);
			}
			if (direction.equals("down"))
			{
				x = snake.peekLast().getX();
				y = snake.peekLast().getY()-10;
				digit = new Square(x,y);
				digit.setDirection(direction);
				snake.add(digit);
				add(digit);
			}
			revalidate();
		}
		
		// Checks for a collision with the snake body or a boundary wall
		
		public boolean collision()
		{
			boolean collides=false;
			
			for(int i=0;i<snake.size();i++)
			{
				Square current = snake.get(i);
				if(i!=0 && (snake.get(0).getX()==current.getX() && snake.get(0).getY()==current.getY()))
				{
					collides = true;
				}
				if(snake.get(0).getX()<0 || snake.get(0).getX()>SIZE-10 || snake.get(0).getY()< 0 || snake.get(0).getY()>SIZE-10)
				{
					collides = true;
				}
			}
			return collides;
		}
		
		// Accessor
		
		public int getBoardSize()
		{
			return SIZE;
		}
		
		// Checks if the snake collides with the food piece
		
		public boolean foodEaten()
		{
		   boolean foodEaten = false;
			if (head.getX() == food.getX() && head.getY() == food.getY())
			{
				foodEaten = true;
				addDigit();
			}
			return foodEaten;
			}
		
		// Listens to the direction keys and space bar for directionality and pause function
			
		private class SnakeListener implements KeyListener
		{
			public void keyPressed(KeyEvent e)
			{
				if ((e.getKeyCode() == KeyEvent.VK_UP) && (head.getDirection()!="down") && isPlaying)
				{
					head.setDirection("up");
				}
				if ((e.getKeyCode() == KeyEvent.VK_DOWN) && (head.getDirection()!="up") && isPlaying)
				{
				   head.setDirection("down");
				}
				if ((e.getKeyCode() == KeyEvent.VK_LEFT) && (head.getDirection()!="right") && isPlaying)
				{
					head.setDirection("left");
				}
				if ((e.getKeyCode() == KeyEvent.VK_RIGHT) && (head.getDirection()!="left")&& isPlaying)
				{
					head.setDirection("right");
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					if (isPlaying)
					{
						timer.stop();
						pause = new JLabel("PAUSED");
						pause.setForeground(Color.white);
						add(pause);
						revalidate();
						repaint();
						isPlaying = false;
						return;
					}
					if (!isPlaying)
					{
					timer.start();
					remove(pause);
					isPlaying = true;
					}
				}
					
				if (foodEaten())
				{
					food.setX((int)(Math.random()*(SIZE/10))*10);
					food.setY((int)(Math.random()*(SIZE/10))*10);
				}
			}
				
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		}
			
		// This is the class that makes the program run via timer, each time
		// the timer fires an event (like a stopwatch per second) this method
		// calls the move method and repaints the panel.
		
		private class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				move();
				if (foodEaten())
				{
					food.setX((int)(Math.random()*(SIZE/10))*10);
					food.setY((int)(Math.random()*(SIZE/10))*10);
				}
				if (collision())
				{
					JLabel gameOver = new JLabel("GAME OVER");
					gameOver.setForeground(Color.white);
					gameOver.setHorizontalTextPosition(JLabel.CENTER);
					gameOver.setVerticalTextPosition(JLabel.CENTER);
					gameOver.setPreferredSize(new Dimension(140,280));
					
					add(gameOver);
					revalidate();
					repaint();
					
					try
					{
						Thread.sleep(2000);
					}
					catch(InterruptedException IE){}
					
					removeAll();
					reset = new JButton("AGAIN?");
					reset.addActionListener(new ResetListener());
					add(reset);
					add(score);
					add(gameOver);
					timer.stop();
				}
				revalidate();
				repaint();	
				}
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