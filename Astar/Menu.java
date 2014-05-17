import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel
{
	JPanel menu;
	public Menu(Board board)
	{
		menu = new JPanel();
		JPanel labelPanel = new JPanel();
		JLabel label1 = new JLabel("Menu:");
		label1.setHorizontalAlignment(SwingConstants.LEFT);
		label1.setFont(new Font("TimesRoman",Font.PLAIN,15));
		label1.setVisible(true);
		labelPanel.add(label1);
		labelPanel.setLayout(new BorderLayout(0,0));

		labelPanel.setVisible(true);

		Slider slider = new Slider(board);
		MapChooser mc = new MapChooser(board);
		
		BoxLayout layout = new BoxLayout(menu,BoxLayout.Y_AXIS);
		menu.setBorder(BorderFactory.createLineBorder(Color.black));
		//layout.setVgap(0);
		menu.setLayout(layout);
		menu.add(label1);
		menu.add(slider);
		menu.add(mc);
		add(menu);
		setVisible(true);
	}
}