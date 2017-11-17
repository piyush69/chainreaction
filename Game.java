package chainreaction;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.*;

public class Game implements Serializable
{
    private Board board;
    private int numberOfPlayers;
    private Player[] players;
    private int boardDimensionM;
    private int boardDimensionN;
    private String[] playerColours;
    private int currentPlayer;

    private int currentVersion;
    private int maxVersionCount;
    public static Board[] boardVersions;
    public static int[] currentPlayerVersions;

    public Game(int m, int n, int numberOfPlayers, String[] playerColours, double cellSize)
    {
        // board = new Board(m, n); // To be restored

        boardDimensionM = m;
        boardDimensionN = n;
        this.numberOfPlayers = numberOfPlayers;
        this.playerColours = playerColours;
        players = new Player[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(playerColours[i], this, i);
        currentPlayer = 0;

        // (not) To be removed
        board = new Board(m, n, players, cellSize);

        currentVersion = 0;
        maxVersionCount = 1000;
        boardVersions = new Board[maxVersionCount];
        currentPlayerVersions = new int[maxVersionCount];

        for (int i = 0; i < maxVersionCount; i++)
        {
            boardVersions[i] = new Board(m, n, players, cellSize);
            currentPlayerVersions[i] = -1;
        }
    }

    public Board getBoard()
    {
        return board;
    }

    public Player getPlayer(int i)
    {
        return players[i];
    }

    public Player getCurrentPlayer()
    {
        return players[currentPlayer];
    }

    public int getNumberOfPlayers()
    {
        return numberOfPlayers;
    }

    public int move(int i, int j, Group[][] groupMatrix, Pane root)
    {
        boolean success = players[currentPlayer].takeTurn(i, j, groupMatrix, root);

        if(success)
        {
            int numCellsEmpty = boardDimensionM * boardDimensionN;

            for (int k = 0; k < numberOfPlayers; k++)
            {
                players[k].setNumberOfCellsOccupied(0);
            }
            for (int k = 0; k < boardDimensionM; k++)
            {
                for (int l = 0; l < boardDimensionN; l++)
                {
                    int temp = board.getCell(k, l).getPlayerNoInControl();
                    if(temp != -1)
                    {
                        players[temp].addNumberOfCellsOccupied(1);
                        numCellsEmpty -= 1;
                    }
                }
            }
            for (int k = 0; k < numberOfPlayers; k++)
            {
                if(players[k].getNumberOfCellsOccupied() == 0 && players[k].gotFairChance())
                    players[k].kill();
                else
                    players[k].revive();
            }

            if(getNumberOfPlayersAlive() == 1)
                return currentPlayer + 1;

            do
            {
                currentPlayer = (currentPlayer + 1) % numberOfPlayers;
            }
            while(!players[currentPlayer].isAlive());

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

    public void saveState()
    {
        try
        {
            String filenameBoard = "gameboard.ser";
            String filenameCurrentPlayer = "gamecurrentplayer.ser";

            FileOutputStream fileBoard = new FileOutputStream (filenameBoard);
            FileOutputStream fileCurrentPlayer = new FileOutputStream (filenameCurrentPlayer);

            ObjectOutputStream outBoard = new ObjectOutputStream (fileBoard);
            ObjectOutputStream outCurrentPlayer = new ObjectOutputStream (fileCurrentPlayer);

            outBoard.writeObject(board);
            outCurrentPlayer.writeObject(currentPlayer);

            outBoard.close();
            outCurrentPlayer.close();

            fileBoard.close();
            fileCurrentPlayer.close();

            FileInputStream fileBoard2 = new FileInputStream (filenameBoard);
            FileInputStream fileCurrentPlayer2 = new FileInputStream (filenameCurrentPlayer);

            ObjectInputStream inBoard = new ObjectInputStream (fileBoard2);
            ObjectInputStream inCurrentPlayer = new ObjectInputStream (fileCurrentPlayer2);

            currentVersion = (currentVersion + 1) % maxVersionCount;

            boardVersions[currentVersion] = (Board)inBoard.readObject();
            currentPlayerVersions[currentVersion] = (int)inCurrentPlayer.readObject();

            inBoard.close();
            inCurrentPlayer.close();

            fileBoard2.close();
            fileCurrentPlayer2.close();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException" +" is caught");
        }
    }

    public void undo(Group[][] groupMatrix, Pane root)
    {
        try
        {
            String filenameBoard = "gameboard.ser";
            String filenameCurrentPlayer = "gamecurrentplayer.ser";

            FileOutputStream fileBoard = new FileOutputStream (filenameBoard);
            FileOutputStream fileCurrentPlayer = new FileOutputStream (filenameCurrentPlayer);

            ObjectOutputStream outBoard = new ObjectOutputStream (fileBoard);
            ObjectOutputStream outCurrentPlayer = new ObjectOutputStream (fileCurrentPlayer);

            if(currentVersion != 0)
            {
                currentVersion -= 1;
            }

            outBoard.writeObject(boardVersions[currentVersion]);
            outCurrentPlayer.writeObject(currentPlayerVersions[currentVersion]);

            outBoard.close();
            outCurrentPlayer.close();

            fileBoard.close();
            fileCurrentPlayer.close();

            FileInputStream fileBoard2 = new FileInputStream (filenameBoard);
            FileInputStream fileCurrentPlayer2 = new FileInputStream (filenameCurrentPlayer);

            ObjectInputStream inBoard = new ObjectInputStream (fileBoard2);
            ObjectInputStream inCurrentPlayer = new ObjectInputStream (fileCurrentPlayer2);

            board = (Board)inBoard.readObject();
            currentPlayer = (int)inCurrentPlayer.readObject();

            inBoard.close();
            inCurrentPlayer.close();

            fileBoard2.close();
            fileCurrentPlayer2.close();

            board.display(groupMatrix, root);

            int numCellsEmpty = boardDimensionM * boardDimensionN;

            for (int k = 0; k < numberOfPlayers; k++)
            {
                players[k].setNumberOfCellsOccupied(0);
            }
            for (int k = 0; k < boardDimensionM; k++)
            {
                for (int l = 0; l < boardDimensionN; l++)
                {
                    int temp = board.getCell(k, l).getPlayerNoInControl();
                    if(temp != -1)
                    {
                        players[temp].addNumberOfCellsOccupied(1);
                        numCellsEmpty -= 1;
                    }
                }
            }
            for (int k = 0; k < numberOfPlayers; k++)
            {
                if(players[k].getNumberOfCellsOccupied() == 0 && players[k].gotFairChance())
                    players[k].kill();
                else
                    players[k].revive();
            }

            do
            {
                currentPlayer = (currentPlayer + 1) % numberOfPlayers;
            }
            while(!players[currentPlayer].isAlive());
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException" +" is caught");
        }
    }

    public void restart(double cellSize)
    {
        board = new Board(boardDimensionM, boardDimensionN, players, cellSize);
        currentPlayer = -1;
        currentVersion = 0;
    }

    public void exit()
    {
        try
        {
            String filenameBoard = "gameboardData.ser";
            String filenameCurrentPlayer = "gamecurrentplayerData.ser";
            String filenamePlayerList = "gamePlayerListData.ser";

            FileOutputStream fileBoard = new FileOutputStream (filenameBoard);
            FileOutputStream fileCurrentPlayer = new FileOutputStream (filenameCurrentPlayer);
            FileOutputStream filePlayerList = new FileOutputStream (filenamePlayerList);

            ObjectOutputStream outBoard = new ObjectOutputStream (fileBoard);
            ObjectOutputStream outCurrentPlayer = new ObjectOutputStream (fileCurrentPlayer);
            ObjectOutputStream outPlayerList = new ObjectOutputStream (filePlayerList);

            outBoard.writeObject(board);
            outCurrentPlayer.writeObject(currentPlayer);
            outPlayerList.writeObject(players);

            outBoard.close();
            outCurrentPlayer.close();
            outPlayerList.close();

            fileBoard.close();
            fileCurrentPlayer.close();
            filePlayerList.close();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }


    /*
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
    */
}