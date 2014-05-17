import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MapChooser extends JPanel
{
	JComboBox fileMenu;
	Board board = null;
	public MapChooser(Board pBoard)
	{
		String[] files = {"boardMap1.txt","boardMap2.txt","boardMap3.txt","boardMap4.txt","boardMap5.txt","boardMap6.txt","demoMap.txt","bigMap.txt","hugeMap.txt"};
		fileMenu = new JComboBox(files);
		board = pBoard;
		fileMenu.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JComboBox cb = (JComboBox) e.getSource();
					String mapChosen = (String) cb.getSelectedItem();
					System.out.println("User chose the "+mapChosen+" map");
					/*
					String[][] boardMap = AstarGame.getBoardMap(mapChosen);
					board = new Board(board.getBoardSize(),board.getTileSize());
					board.setGrid(boardMap);
					board.setPlayer(AstarGame.getPlayerLocation(boardMap));
					board.setTarget(AstarGame.getTargetLocation(boardMap));
					*/

					AstarGame.reload(mapChosen);
					fileMenu.transferFocus();
				}	
			});
		add(fileMenu);
		setVisible(true);
	}
}