package chainreaction;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.io.Serializable;


public class Player implements Serializable
{
    private String colour;
    private int numberOfCellsOccupied;
    private boolean alive;
    private Game currentGame;
    private int playerNumber;

    public Player(String colour, Game game, int p)
    {
        this.colour = colour;
        numberOfCellsOccupied = 0;
        alive = true;
        currentGame = game;
        playerNumber = p;
    }

    public int getPlayerNumber()
    {
        return playerNumber;
    }

    public Game getCurrentGame()
    {
        return currentGame;
    }

    public String getColour()
    {
        return colour;
    }

    public void setColour(String colour)
    {
        this.colour = colour;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void kill()
    {
        alive = false;
    }

    public void revive()
    {
        alive = true;
    }

    public void incrementNumberOfCellsOccupied()
    {
        numberOfCellsOccupied++;
    }

    public void decrementNumberOfCellsOccupied()
    {
        numberOfCellsOccupied--;
    }

    public int getNumberOfCellsOccupied()
    {
        return numberOfCellsOccupied;
    }

    public void setNumberOfCellsOccupied(int num)
    {
        numberOfCellsOccupied = num;
    }

    public void addNumberOfCellsOccupied(int num)
    {
        numberOfCellsOccupied += num;
    }

    // public boolean takeTurn(int i, int j, Group[][] groupMatrix, Pane root)
    // {
    //     if(currentGame.getBoard().addOrb(i, j, playerNumber, groupMatrix, root))
    //     {
    //         return true;
    //     }

    //     return false;
    // }

    // public void undoTurn(Group[][] groupMatrix, Pane root)
    // {
    //     int x = this.getPlayerNumber() - 1;
    //     if(x < 0)
    //         x = this.currentGame.getNumberOfPlayers() - 1;
    //     this.currentGame.getPlayer(x).setFairChance(false);
    //     this.setFairChance(false);
    //     currentGame.undo(groupMatrix, root);
    // }

    // public void restart(double cellSize)
    // {
    //     currentGame.restart(cellSize);
    // }
}