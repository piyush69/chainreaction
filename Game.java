package chainreaction;

import java.io.*;

public class Game implements Serializable
{
    private static final long serialversionUID = 19980906L;
    private Board board;
    private int numberOfPlayers;
    private int currentPlayer;
    private int boardDimensionM;
    private int boardDimensionN;
    private Player[] players;
    private String[] playerColours;


    private int currentVersion;
    private int maxVersionCount;
    public static Board[] boardVersions;
    public static int[] currentPlayerVersions;

    public Game(int m, int n, int numberOfPlayers, String[] playerColours)
    {
        boardDimensionM = m;
        boardDimensionN = n;
        // board = new Board(boardDimensionM, boardDimensionN); // To be restored
        this.numberOfPlayers = numberOfPlayers;
        this.currentPlayer = 0;
        this.playerColours = playerColours;
        players = new Player[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(playerColours[i], this);

        // To be removed
        board = new Board(boardDimensionM, boardDimensionN, players);

        currentVersion = 0;
        maxVersionCount = 1000;
        boardVersions = new Board[maxVersionCount];
        currentPlayerVersions = new int[maxVersionCount];

        for (int i = 0; i < maxVersionCount; i++)
        {
            boardVersions[i] = new Board(m, n, players);
            currentPlayerVersions[i] = 0;
        }
    }

    public Board getBoard()
    {
        return board;
    }

    public void play()
    {
        boolean done = false;

        while(!done)
        {
            for(currentPlayer = 0; currentPlayer < numberOfPlayers; currentPlayer++)
            {
                if(players[currentPlayer].isAlive())
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
                        System.out.println("Player " + (currentPlayer + 1) + " wins!");
                        done = true;
                        break;
                    }

                    // To be removed.
                    System.out.println("Player " + (currentPlayer + 1));

                    players[currentPlayer].takeTurn();

                    // To be removed.
                    board.display();
                }
            }
        }
    }

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
            
            System.out.println(currentVersion);

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

    // not needed imo
    // private void deserialize()
    // {
    //     Board[] boardArray = new Board[maxVersionCount];
    //     int[] currentPlayerArray = new int[maxVersionCount];
    //     try
    //     {
    //         String filename = "gameData.ser";
    //         FileInputStream file = new FileInputStream (filename);
    //         ObjectInputStream in = new ObjectInputStream (file);

    //         boardArray = (Board[])in.readObject();
    //         currentPlayerArray = (int[])in.readObject();
    //     }
    //     catch (IOException ex)
    //     {
    //         //do something
    //         //System.out.println("IOException is caught");
    //     }
    //     catch (ClassNotFoundException ex)
    //     {
    //         //do something
    //         //System.out.println("ClassNotFoundException" +" is caught");
    //     }
    // }

    public void pause()
    {
        // Code to be written.
    }

    public void undo()
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

            System.out.println(currentVersion);

            inBoard.close();
            inCurrentPlayer.close();

            fileBoard2.close();
            fileCurrentPlayer2.close();
        }
        catch (IOException e)
        {
            //do something
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException" +" is caught");
        }
    }

    public void restart()
    {
        board = new Board(boardDimensionM, boardDimensionN, players);
        currentPlayer = 0;
        currentVersion = 0;
    }

    public void exit()
    {
        // Code to be written.
    }
}