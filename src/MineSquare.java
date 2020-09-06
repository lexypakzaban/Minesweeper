/**
 * 
 */

import javax.swing.*;
import java.awt.*;
/**
 * @author harlanhowe
 *
 */
public class MineSquare extends JPanel {
	private boolean hasAMine;
	private int neighboringMines;
	private MineStatus myStatus;
	private static Image flagImage; // the "static" on these variables means
	private static Image bombImage; // that all the cells share these images.
	private static Image[] numberImage;
	public static final int size = 25; // I made this public so other classes
									// could access it. The "final" means that
									// I can't change it once the program is running.
	/**
	 *  Postcondition: This cell is created and clear. The static Image variables are
	 *  loaded with the images from files. Note: even though we are likely to create 
	 *  hundreds of MineSquares, the loading of these images will only happen once.
	 */
	public MineSquare() 
	{
		myStatus = MineStatus.ORIGINAL;
		neighboringMines=0;
		hasAMine=false;
		if (flagImage == null)
			flagImage = (new ImageIcon("flag.gif")).getImage();
		if (bombImage == null)
			bombImage = (new ImageIcon("bomb.gif")).getImage();
		if (numberImage == null)
		{
			numberImage = new Image[9];
			for (int i=0; i<9; i++)
				numberImage[i]=(new ImageIcon(i+".gif")).getImage();
		}
		setPreferredSize(new Dimension(size,size));
	}
	
	/**
	 * @return whether this cell has a mine.
	 */
	public boolean hasAMine() {
		return hasAMine;
	}
	/**
	 * @return  myStatus
	 */
	public MineStatus getMyStatus() {
		return myStatus;
	}
	/**
	 * @return the neighboringMines around this cell.
	 */
	public int getNeighboringMines() {
		return neighboringMines;
	}

	/**
	 * postcondition: sets this cell to either have a mine (true), or not (false).
	 */
	public void setMine(boolean hasAMine) {
		this.hasAMine = hasAMine;
	}
	/**
	 * @param sets myStatus to a given MineStatus.
	 */
	public void setMyStatus(MineStatus myStatus) {
		this.myStatus = myStatus;
		repaint();
	}
	/**
	 * @param sets the number of neighboringMines
	 */
	public void setNeighboringMines(int neighboringMines) {
		this.neighboringMines = neighboringMines;
	}	

	/**
	 * displays the current appearance of the mine cell, based on myStatus and neighboringMines.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (myStatus == MineStatus.ORIGINAL)
			;
		if (myStatus == MineStatus.FLAGGED)
			g.drawImage(flagImage, 0, 0, size, size, this);
		if (myStatus == MineStatus.NUMBER_REVEALED)
		{
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, size, size);
			g.drawImage(numberImage[getNeighboringMines()], 0, 0, size, size, this);
		}
		if ((myStatus == MineStatus.BOMB_REVEALED)&&(hasAMine))
		{
			g.setColor(new Color(200,100,0));
			g.fillRect(0,0,size,size);
			g.drawImage(bombImage, 0, 0, size, size, this);
		}
		if ((myStatus == MineStatus.EXPLODED))
		{
			g.setColor(Color.RED);
			g.fillRect(0,0,size,size);
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, size-1, size-1);
	}

	public String toString()
	{
		return "neighbors: "+getNeighboringMines()+" has bomb: "+hasAMine();
	}
	
}
