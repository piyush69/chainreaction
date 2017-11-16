package chainreaction;

import java.util.*;
import java.io.*;

public class Player implements Serializable
{
    private String colour;
    private int numberOfCellsOccupied;
    private boolean alive;
    private Game currentGame;

    public Player(String colour, Game game)
    {
        this.colour = colour;
        numberOfCellsOccupied = 0;
        alive = true;
        currentGame = game;
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

    public void takeTurn()
    {
        // To be removed.
        Scanner input = new Scanner(System.in);
        boolean done = false;
        while(!done)
        {
            System.out.println("Enter i and j");
            int i = input.nextInt();
            int j = input.nextInt();
            if(i == -1 && j == -1)
            {
                currentGame.undo();
                done = true;
            }
            else if(i == -2 && j == -2) //to be placed somewhere else
            {
                currentGame.restart();
                done = true;
            }
            else if(!currentGame.getBoard().addOrb(i, j, this))
                System.out.println("Try again");
            else
                currentGame.saveState();
                done = true;
        }
    }
}
