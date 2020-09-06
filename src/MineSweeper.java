import javax.swing.JFrame;
import java.awt.FlowLayout;

public class MineSweeper extends JFrame 
{
	public static void main(String[] args)
	{
		MineSweeper app = new MineSweeper();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public MineSweeper()
	{
		super("MineSweeper");
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(new MinePanel());
		setSize(MineSquare.size*MinePanel.numCellsAcross,MineSquare.size*MinePanel.numCellsDown+32);
		setVisible(true);
		setResizable(false);
	}
}
