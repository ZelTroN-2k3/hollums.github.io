import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.BorderFactory;
import java.awt.*;

public class Slider extends JPanel 
{
	private Board board = null;
	private JPanel menu = null;
	private JTextField txt = null;
	private JSlider slider = null;

	public Slider(Board theBoard)
	{

		//petList.addActionListener()
		slider = new JSlider(JSlider.VERTICAL,0,10000,500);

		slider.setMajorTickSpacing(1000);
		slider.setMinorTickSpacing(250);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		//txt = new JTextField(4);
		//txt.setPreferredSize(new Dimension(20,20));
		board = theBoard;
		board.setDrawTime(slider.getValue());
		slider.setPreferredSize(new Dimension(70,board.getBoardSize()-board.getBoardSize()/3));
		slider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				//txt.setText(String.valueOf(slider.getValue()));
				board.setDrawTime(slider.getValue());
				board.requestFocus();
			}
		});
		add(slider);
		setVisible(true);

		//add(txt);
	}
}