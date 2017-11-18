package chainreaction;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.Reflection;
import javafx.geometry.Pos;

import java.io.*;
import java.util.concurrent.FutureTask;

public class Game implements Serializable
{
    private Board board;
    private int numberOfPlayers;
    private Player[] players;
    private int boardDimensionM;
    private int boardDimensionN;
    private String[] playerColours;
    private int currentPlayer;
    private int currentRound;
    private boolean gameInProgress;

    private int currentVersion;
    private int maxVersionCount;
    public static Board[] boardVersions;
    public static int[] currentPlayerVersions;
    public static int[] roundNoVersions;

    public Game(int m, int n, int numberOfPlayers, String[] playerColours, double cellSize)
    {
        boardDimensionM = m;
        boardDimensionN = n;
        this.numberOfPlayers = numberOfPlayers;
        this.playerColours = playerColours;
        currentPlayer = 0;
        currentRound = 0;
        currentVersion = 0;
        gameInProgress = true;

        players = new Player[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(playerColours[i], this, i);

        board = new Board(m, n, players, cellSize, this);

        maxVersionCount = 1000;

        boardVersions = new Board[maxVersionCount];
        currentPlayerVersions = new int[maxVersionCount];
        roundNoVersions = new int[maxVersionCount];

        for (int i = 0; i < maxVersionCount; i++)
        {
            boardVersions[i] = new Board(m, n, players, cellSize, this);
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

    public int getCurrentPlayerNo()
    {
        return currentPlayer;
    }

    public int getNumberOfPlayers()
    {
        return numberOfPlayers;
    }

    public boolean getGameInProgress()
    {
        return gameInProgress;
    }

    public void move(int i, int j, Group[][] groupMatrix, Pane root)
    {
        //System.out.println("[+] " + this.currentPlayer + " is starting its Turn");
        //players[currentPlayer].takeTurn(i, j, groupMatrix, root);
        int x = this.getBoard().addOrb(i, j, currentPlayer, groupMatrix, root);
        if(x != -1)
            this.currentPlayer = x;
        //System.out.println("[-] " + this.currentPlayer + " is exiting move");
        //System.out.println("[ ] "+currentVersion+" Current Version in Memory");
        //System.out.println();
    }

    public int endMove(int playah)
    {
        this.currentPlayer = playah;
        saveState();

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
            if(players[k].getNumberOfCellsOccupied() == 0 && currentRound != 0)
                players[k].kill();
            else
                players[k].revive();
        }

        if(getNumberOfPlayersAlive() == 1)
        {
            gameInProgress = false;
            BorderPane toor = new BorderPane();
            toor.setPrefSize(600.0, 330.0);
            Text name = new Text("Player " + (currentPlayer + 1) + " wins!");
            name.setFill(Color.STEELBLUE);
            name.setFont(Font.font("SanSerif", FontWeight.BOLD, 60));
            Reflection ref = new Reflection();
            name.setEffect(ref);

            VBox vboxName = new VBox();
            vboxName.setAlignment(Pos.CENTER);
            vboxName.getChildren().add(name);
            vboxName.setPrefHeight(330);
            toor.setCenter(vboxName);
            Scene scn = new Scene(toor, 600.0, 330.0);
            Stage win = new Stage();
            win.setScene(scn);
            win.setResizable(false);
            win.show();
        }

        if(currentPlayer == numberOfPlayers - 1)
            currentRound = 1;
        do
        {
            currentPlayer = (currentPlayer + 1) % numberOfPlayers;
        }
        while(!players[currentPlayer].isAlive());
        return this.currentPlayer;
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

    public void saveState()
    {
        try
        {
            String filenameBoard = "gameboard.ser";
            String filenameCurrentPlayer = "gamecurrentplayer.ser";
            //String filenameRoundNo = "gameRoundNo.ser";

            FileOutputStream fileBoard = new FileOutputStream (filenameBoard);
            FileOutputStream fileCurrentPlayer = new FileOutputStream (filenameCurrentPlayer);
            //FileOutputStream fileRoundNo = new FileOutputStream (filenameRoundNo);

            ObjectOutputStream outBoard = new ObjectOutputStream (fileBoard);
            ObjectOutputStream outCurrentPlayer = new ObjectOutputStream (fileCurrentPlayer);
            //ObjectOutputStream outRoundNo = new ObjectOutputStream (fileRoundNo);

            outBoard.writeObject(board);
            outCurrentPlayer.writeObject(currentPlayer);
            //outRoundNo.writeObject(currentRound);

            outBoard.close();
            outCurrentPlayer.close();
            //outRoundNo.close();

            fileBoard.close();
            fileCurrentPlayer.close();
            //fileRoundNo.close();

            FileInputStream fileBoard2 = new FileInputStream (filenameBoard);
            FileInputStream fileCurrentPlayer2 = new FileInputStream (filenameCurrentPlayer);
            //FileInputStream fileRoundNo2 = new FileInputStream (filenameRoundNo);

            ObjectInputStream inBoard = new ObjectInputStream (fileBoard2);
            ObjectInputStream inCurrentPlayer = new ObjectInputStream (fileCurrentPlayer2);

            currentVersion = (currentVersion + 1) % maxVersionCount;

            boardVersions[currentVersion] = (Board)inBoard.readObject();
            currentPlayerVersions[currentVersion] = (int)inCurrentPlayer.readObject();
            roundNoVersions[currentVersion] = currentRound;

            inBoard.close();
            inCurrentPlayer.close();

            fileBoard2.close();
            fileCurrentPlayer2.close();
            //System.out.println("[ ] "+currentVersion+" Save State Current Version");
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
            currentRound = roundNoVersions[currentVersion];

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
                if(players[k].getNumberOfCellsOccupied() == 0 && currentRound != 0)
                    players[k].kill();
                else
                    players[k].revive();
            }

            do
            {
                currentPlayer = (currentPlayer + 1) % numberOfPlayers;
            }
            while(!players[currentPlayer].isAlive());
            //System.out.println("[u] "+currentPlayer+" will now take turn");
            //System.out.println("[ ] "+currentVersion+" Current Version\n");
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

    public void restart(Group[][] groupMatrix, Pane root, double cellSize)
    {
        board = new Board(boardDimensionM, boardDimensionN, players, cellSize, this);
        currentPlayer = 0;
        currentVersion = 0;
        currentRound = 0;
        gameInProgress = true;
        board.display(groupMatrix, root);
    }

    public void exit()
    {
        ////System.out.println("Stage is closing");
        if(this.getGameInProgress())
        {
            try
            {
                String filenameBoard = "gameData.ser";
                FileOutputStream fileBoard = new FileOutputStream (filenameBoard);
                ObjectOutputStream outBoard = new ObjectOutputStream (fileBoard);
                currentVersion = 0;
                outBoard.writeObject(this);
                outBoard.close();
                fileBoard.close();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }    
        }
        else
        {
            File file = new File("gameData.ser");
            file.delete();
        }
        // File f2 = new File("gameboard.ser");
        // f2.delete();
        // f2 = new File("gamecurrentplayer.ser");
        // f2.delete();
    }
}