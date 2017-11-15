package chainreaction;

import java.io.*;

public class Cell implements Serializable
{
    private int row;
    private int col;
    private Player playerInControl;
    private int criticalMass;
    private int numberOfOrbs;
    private int explodeDepth;

    public Cell(int i, int j, int criticalMass)
    {
        row = i;
        col = j;
        playerInControl = null;
        this.criticalMass = criticalMass;
        numberOfOrbs = 0;
        explodeDepth = 0;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public Player getPlayerInControl()
    {
        return playerInControl;
    }

    public void setPlayerInControl(Player player)
    {
        playerInControl = player;
    }

    public int getNumberOfOrbs()
    {
        return numberOfOrbs;
    }

    public int getExplodeDepth()
    {
        return explodeDepth;
    }

    public void setExplodeDepth(int explodeDepth)
    {
        this.explodeDepth = explodeDepth;
    }

    public boolean incrementOrbAndCheckExplosion()
    {
        if(++numberOfOrbs >= criticalMass)
        {
            numberOfOrbs = 0;
            this.getPlayerInControl().decrementNumberOfCellsOccupied();
            playerInControl = null;
            return true;
        }

        return false;
    }
}
