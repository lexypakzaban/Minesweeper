

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

public class MinePanel extends JPanel {

	public static final int numCellsAcross = 20;
	public static final int numCellsDown = 15;
	public static final int numMines = 20;
	private MineSquare[][] mySquares;
	private MineSquare pressedSquare;
	/**
	 * Creates the mine panel, including a grid of (numCellsAcross x numCellsDown) MineSquares.
	 *
	 */
	public MinePanel()
	{
		super(new GridLayout(numCellsDown,numCellsAcross));
		mySquares = new MineSquare[numCellsAcross][numCellsDown];
		for (int i=0; i<numCellsDown;i++)
			for (int j=0; j<numCellsAcross; j++)
			{
				mySquares[j][i] = new MineSquare();
				add(mySquares[j][i]);
			}
		setPreferredSize(new Dimension(numCellsAcross*MineSquare.size,numCellsDown*MineSquare.size));
		setRandomMines();
		doNeighborCount();
		addMouseListener(new clickListener());
	}
	
	/**
	 * precondition: all the cells are cleared - no mines!
	 * postcondition: randomly distributes exactly numMines mines around the grid. 
	 */
	public void setRandomMines()
	{
		Random generator = new Random();
		int x, y;
		boolean placed;
		for (int n = 0; n<numMines; n++)
		{
			placed = false;
			do
			{
				x = generator.nextInt(numCellsAcross);
				y = generator.nextInt(numCellsDown);
				if (!mySquares[x][y].hasAMine())
				{
					mySquares[x][y].setMine(true);
					placed = true;
				}
				//System.out.println("("+x+", "+y+")");
			}while (!placed);
		}
	}
	/**
	 * precondition: the cells in the grid already exist. Presumably, there are some mines out there.
	 * postcondition: each cell in the area now knows how many bombs [0...8] there
	 * are in its neighborhood.
	 *
	 */
	public void doNeighborCount()
	{
		for (int i=0; i<numCellsAcross; i++)
			for (int j=0; j<numCellsDown; j++)
				countMyNeighbors(i,j);
	}
	
	/**
	 * A "safe" way to check whether there is a mine at the given location, (x,y).
	 * Precondition: The cells in the array exist, but x and y do not need to be
	 * in the range of the grid.
	 * @return true if there is a mine at this location; false if there is no mine or if (x,y) is out of bounds.
	 */
	private boolean locationHasMine(int x, int y)
	{
		if ((x>=0)&&(x<numCellsAcross)&&(y>=0)&&(y<numCellsDown))
			return mySquares[x][y].hasAMine();
		return false;
	}
	
	/**
	 * Precondition: the cell at (x,y) exists
	 * Postcondition: the cell now knows how many mines [0...8] are in its 
	 * immediate neighborhood. 
	 */
	private void countMyNeighbors(int x, int y)
	{
		int count = 0;
		for (int i=-1;i<2; i++)
			for (int j=-1;j<2;j++)
				if (locationHasMine(x+i,y+j))
					count++;
		if (locationHasMine(x,y))
			count--;
		mySquares[x][y].setNeighboringMines(count);
	}	
	
	/**
	 * precondition: the cells all exist.
	 * postcondition: any cell with a mine in it has its appearance changed: 
	 * if it has a flag, it shows the bomb, but if it doesn't, it shows an explosion.
	 */
	public void revealAllMines()
	{
		for (int i=0; i<numCellsAcross; i++)
			for (int j=0; j<numCellsDown; j++)
				if (mySquares[i][j].hasAMine())
					if (mySquares[i][j].getMyStatus()==MineStatus.FLAGGED)
						mySquares[i][j].setMyStatus(MineStatus.BOMB_REVEALED);
					else
						mySquares[i][j].setMyStatus(MineStatus.EXPLODED);
		repaint();
	}
	/**
	 * precondition: all the cells exist.
	 * postcondition: the cells are cleared of mines, new mines are distributed,
	 * the neighboring cells are counted, and the appearance of all the cells
	 * are reset.
	 */
	public void reset()
	{
		for (int i=0; i<numCellsAcross; i++)
			for (int j=0; j<numCellsDown; j++)
			{	mySquares[i][j].setMyStatus(MineStatus.ORIGINAL);
				mySquares[i][j].setMine(false);
			}
		setRandomMines();
		doNeighborCount();
		pressedSquare=null;
		repaint();
	}
	/**
	 * precondition: the cell exists
	 * postcondition: if this cell has zero mines in its neighborhood, it reveals
	 * all its neighbors. Of course, if any of them have zero mines, they reveal 
	 * their neighbors, too.
	 */
	public void checkForZeroes(int x, int y)
	{
		//TODO: this is the method you need to write!

		System.out.println("x: " + x + " y: " + y);

		//base case 1: has it hit out of bounds
		if (x >= 0 && x < 20 && y >= 0 && y < 15){

			//base case 2: has it been revealed already
			if (mySquares[x][y].getMyStatus().equals(MineStatus.ORIGINAL) || mySquares[x][y].getMyStatus().equals(MineStatus.FLAGGED)){

				//reveals it
				mySquares[x][y].setMyStatus(MineStatus.NUMBER_REVEALED);

				//base case 3: if it doesn't have a mine
				if (mySquares[x][y].getNeighboringMines() == 0){

					checkForZeroes(x+1,y); //bottom neighbor
					checkForZeroes(x+1, y-1); //bottom left neighbor
					checkForZeroes(x+1, y+1); //bottom right neighbor

					checkForZeroes(x-1, y); //top neighbor
					checkForZeroes(x-1,y-1); //top left neighbor
					checkForZeroes(x-1, y+1); //top right neighbor

					checkForZeroes(x, y-1); //left neighbor
					checkForZeroes(x, y+1); //right neighbor

				}

			}
		}
		repaint();
	}
	public class clickListener extends MouseAdapter
	{
		/**
		 * postcondition: the variable pressedSquare is set to the cell where the
		 * button was pressed.
		 */
		public void mousePressed(MouseEvent me)
		{
			pressedSquare = mySquares[me.getX()/MineSquare.size]
			                          [me.getY()/MineSquare.size];
		}
		/**
		 * postcondition: if this is the same cell as when the button was pressed,
		 * it will handle the action of clicking this cell.
		 */
		public void mouseReleased(MouseEvent me)
		{
			System.out.println("Clicked.");
			int whichX = me.getX()/MineSquare.size;
			int whichY = me.getY()/MineSquare.size;
			if (whichX<0 || whichY<0 || whichX>=numCellsAcross || whichY>=numCellsDown)
				return;
			MineSquare clickedSquare = mySquares[whichX][whichY];
			if (clickedSquare != pressedSquare)
			{
				pressedSquare = null;
				return;
			}
			if ((me.getModifiers()&MouseEvent.SHIFT_MASK)==MouseEvent.SHIFT_MASK)
			{
				if (clickedSquare.getMyStatus()==MineStatus.ORIGINAL)
					clickedSquare.setMyStatus(MineStatus.FLAGGED);
				else if (clickedSquare.getMyStatus()==MineStatus.FLAGGED)
					clickedSquare.setMyStatus(MineStatus.ORIGINAL);
			}
			else
			{
				if (clickedSquare.hasAMine())
				{
					revealAllMines();
					JOptionPane.showMessageDialog(null, "Play Again?");
					reset();
				}
				else
				{
					checkForZeroes(whichX,whichY);
					clickedSquare.setMyStatus(MineStatus.NUMBER_REVEALED);
				}
			}
			pressedSquare = null;
		}
	}
}
