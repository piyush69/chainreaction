package chainreaction;

import java.util.*;

public class Player
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
            if(!currentGame.getBoard().addOrb(i, j, this))
                System.out.println("Try again");
            else
                done = true;
        }
    }
}
