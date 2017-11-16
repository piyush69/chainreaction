package chainreaction;

import java.util.*;
import java.io.*;

public class Player implements Serializable
{
    private String colour;
    private int numberOfCellsOccupied;
    private int playerNumber;
    private boolean alive;
    private Game currentGame;
    private boolean fairChance;

    public Player(String colour, Game game, int p)
    {
        this.colour = colour;
        numberOfCellsOccupied = 0;
        playerNumber = p;
        alive = true;
        currentGame = game;
        fairChance = false;
    }

    public int getPlayerNumber()
    {
        return playerNumber;
    }

    public boolean gotFairChance()
    {
        return fairChance;
    }

    public void setFairChance(boolean chance)
    {
        fairChance = chance;
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

    public void setNumberOfCellsOccupied(int num)
    {
        numberOfCellsOccupied = num;
    }

    public void addNumberOfCellsOccupied(int num)
    {
        numberOfCellsOccupied += num;
    }

    public void takeTurn()
    {
        // To be removed.
        Scanner input = new Scanner(System.in);
        boolean done = false;
        this.fairChance = true;
        while(!done)
        {
            System.out.println("Enter i and j");
            int i = input.nextInt();
            int j = input.nextInt();
            
            //System.out.println("Sending "+Integer.toString(this.getPlayerNumber())+" to addORb");

            if(i == -1 && j == -1)
            {
                int x = this.getPlayerNumber() - 1;
                if(x < 0)
                {
                    x = this.currentGame.getNumberOfPlayers() - 1;
                }
                this.currentGame.getPlayer(x).setFairChance(false);
                this.setFairChance(false);
                currentGame.undo();
                done = true;
            }
            else if(i == -2 && j == -2) //to be placed somewhere else
            {
                currentGame.restart();
                done = true;
            }
            else if(!currentGame.getBoard().addOrb(i, j, this.getPlayerNumber()))
                System.out.println("Try again");
            else
            {
                currentGame.saveState();
                done = true;
            }
        }
    }
}
