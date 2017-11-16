package chainreaction;

import java.io.*;

public class Cell implements Serializable
{
    private int row;
    private int col;
    private int playerNoInControl;
    private int criticalMass;
    private int numberOfOrbs;
    private int explodeDepth;

    public Cell(int i, int j, int criticalMass)
    {
        row = i;
        col = j;
        playerNoInControl = -1;
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

    public int getPlayerNoInControl()
    {
        return playerNoInControl;
    }

    public void setPlayerNoInControl(int playerNo)
    {
        playerNoInControl = playerNo;
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
            //this.getPlayerNoInControl().decrementNumberOfCellsOccupied();
            playerNoInControl = -1;
            return true;
        }

        return false;
    }
}
