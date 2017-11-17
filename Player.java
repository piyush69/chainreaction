package chainreaction;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

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

    public boolean takeTurn(int i, int j, Group[][] groupMatrix, Pane root)
    {
        if(currentGame.getBoard().addOrb(i, j, this, groupMatrix, root))
            return true;

        return false;

        /*
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
        */
    }
}