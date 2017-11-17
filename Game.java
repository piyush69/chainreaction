package chainreaction;

import javafx.scene.Scene;

public class Game
{
    private Board board;
    private int numberOfPlayers;
    private Player[] players;
    private String[] playerColours;
    private int turnPlayer;

    public Game(int m, int n, int numberOfPlayers, String[] playerColours, Scene scene, double cellSize)
    {
        // board = new Board(m, n); // To be restored
        this.numberOfPlayers = numberOfPlayers;
        this.playerColours = playerColours;
        players = new Player[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(playerColours[i], this);
        turnPlayer = 0;

        // To be removed
        board = new Board(m, n, players, scene, cellSize);
    }

    public Board getBoard()
    {
        return board;
    }

    public int move(int i, int j)
    {
        boolean success = players[turnPlayer].takeTurn(i, j);

        if(success)
        {
            if(getNumberOfPlayersAlive() == 1)
                return turnPlayer + 1;

            do
            {
                turnPlayer = (turnPlayer + 1) % numberOfPlayers;
            }
            while(!players[turnPlayer].isAlive());

            return 0;
        }

        return -1;
    }

    public int getNumberOfPlayersAlive()
    {
        int alive = 0;

        for(int i = 0; i < numberOfPlayers; i++)
        {
            if(players[i].isAlive())
                alive++;
        }

        return alive;
    }

    /*
    public void play()
    {
        boolean done = false;

        while(!done)
        {
            for(int i = 0; i < numberOfPlayers; i++)
            {
                if(players[i].isAlive())
                {
                    int numberOfPlayersAlive = 0;
                    for(int j = 0; j < numberOfPlayers; j++)
                    {
                        if(players[j].isAlive())
                            numberOfPlayersAlive++;
                    }

                    if(numberOfPlayersAlive == 1)
                    {
                        // Player has won. Terminate.
                        System.out.println("Player " + (i + 1) + " wins!");
                        done = true;
                        break;
                    }

                    // To be removed.
                    System.out.println("Player " + (i + 1));

                    players[i].takeTurn();

                    // To be removed.
                    board.display();
                }
            }
        }
    }
    */

    private void serialize()
    {
        // Code to be written.
    }

    private void deserialize()
    {
        // Code to be written.
    }

    public void pause()
    {
        // Code to be written.
    }

    public void undo()
    {
        // Code to be written.
    }

    public void restart()
    {
        // Code to be written.
    }

    public void exit()
    {
        // Code to be written.
    }
}