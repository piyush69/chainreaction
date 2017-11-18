package chainreaction;



import java.io.Serializable;


/**
 * The Cell class is used to store the states of each cell individually on the board.
 */
public class Cell implements Serializable
{
    private int row;
    private int col;
    private int playerNoInControl;
    private int criticalMass;
    private int numberOfOrbs;
    private int explodeDepth;

    /**
     * @param i The row number of the cell
     * @param j The column number of the cell
     * @param criticalMass The number of orthogonally adjacent cells
     */
    public Cell(int i, int j, int criticalMass)
    {
        row = i;
        col = j;
        playerNoInControl = -1;
        this.criticalMass = criticalMass;
        numberOfOrbs = 0;
        explodeDepth = 0;
    }

    /**
     * @return Row number of the cell
     */
    public int getRow()
    {
        return row;
    }

    /**
     * @return Column number of the cell
     */
    public int getCol()
    {
        return col;
    }

    /**
     * @return Number of the player who is in control of the cell
     */
    public int getPlayerNoInControl()
    {
        return playerNoInControl;
    }

    /**
     * @param playerNo Number of the player to which the cell is to be assigned
     */
    public void setPlayerNoInControl(int playerNo)
    {
        playerNoInControl = playerNo;
    }

    /**
     * @return Number of orbs currently in the cell
     */
    public int getNumberOfOrbs()
    {
        return numberOfOrbs;
    }

    /**
     * @return Depth at which the cell is in the BFS queue in case of an explosion
     */
    public int getExplodeDepth()
    {
        return explodeDepth;
    }

    /**
     * @param explodeDepth Depth at which the cell is put in the BFS queue at the time of an explosion
     */
    public void setExplodeDepth(int explodeDepth)
    {
        this.explodeDepth = explodeDepth;
    }

    /**
     * @return true if an explosion occured after adding an orb to the cell; false otherwise
     */
    public boolean incrementOrbAndCheckExplosion()
    {
        if(++numberOfOrbs >= criticalMass)
        {
            numberOfOrbs = 0;
            playerNoInControl = -1;
            return true;
        }

        return false;
    }
}