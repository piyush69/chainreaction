package chainreaction;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.io.Serializable;


/**
 * The Player class is used to store the state of the player
 */
public class Player implements Serializable
{
    private String colour;
    private int numberOfCellsOccupied;
    private boolean alive;
    private Game currentGame;
    private int playerNumber;

    /**
     * @param colour String representing the colour of the player
     * @param game Instance of the game in which the player is present
     * @param p Player number
     */
    public Player(String colour, Game game, int p)
    {
        this.colour = colour;
        numberOfCellsOccupied = 0;
        alive = true;
        currentGame = game;
        playerNumber = p;
    }

    /**
     * @return Player number
     */
    public int getPlayerNumber()
    {
        return playerNumber;
    }

    /**
     * @return Instance of the current game
     */
    public Game getCurrentGame()
    {
        return currentGame;
    }

    /**
     * @return Colour of the player
     */
    public String getColour()
    {
        return colour;
    }

    /**
     * @return true if the player is alive; false otherwise
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Kills the player
     */
    public void kill()
    {
        alive = false;
    }

    /**
     * Revives the player
     */
    public void revive()
    {
        alive = true;
    }

    /**
     * Increments the number of cells occupied by the player by one
     */
    public void incrementNumberOfCellsOccupied()
    {
        numberOfCellsOccupied++;
    }

    /**
     * Increments the number of cells occupied by the player by one
     */
    public void decrementNumberOfCellsOccupied()
    {
        numberOfCellsOccupied--;
    }

    /**
     * @return Number of cells occupied by the player
     */
    public int getNumberOfCellsOccupied()
    {
        return numberOfCellsOccupied;
    }

    /**
     * @param num Number of cells to be occupied by the player
     */
    public void setNumberOfCellsOccupied(int num)
    {
        numberOfCellsOccupied = num;
    }

    /**
     * @param num Number of cells to be added
     */
    public void addNumberOfCellsOccupied(int num)
    {
        numberOfCellsOccupied += num;
    }
}