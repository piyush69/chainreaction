package chainreaction;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable
{
    private int m;
    private int n;
    private Cell[][] board;
    private Queue<Cell> explosionQueue;
    private double cellSize;
    private Player[] players;

    public Board(int m, int n, Player[] players, double cellSize)
    {
        this.m = m;
        this.n = n;
        this.players = players;
        this.cellSize = cellSize;

        board = new Cell[m][n];
        for(int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (i == 0 && j == 0 || i == 0 && j == n - 1 || i == m - 1 && j == 0 || i == m - 1 && j == n - 1)
                    board[i][j] = new Cell(i, j, 2);

                else if(i == 0 || i == m - 1 || j == 0 || j == n - 1)
                    board[i][j] = new Cell(i, j, 3);

                else
                    board[i][j] = new Cell(i, j, 4);
            }
        }

        explosionQueue = new LinkedList<Cell>();
    }

    public Cell getCell(int i, int j)
    {
        return board[i][j];
    }

    public void explode(int i, int j, int playerNo, int explodeDepth, Group[][] groupMatrix, Pane root)
    {
        players[playerNo].decrementNumberOfCellsOccupied();
        if(i > 0)
            explosiveAddOrb(i - 1, j, playerNo, explodeDepth, groupMatrix, root);
        if(i < m - 1)
            explosiveAddOrb(i + 1, j, playerNo, explodeDepth, groupMatrix, root);
        if(j > 0)
            explosiveAddOrb(i, j - 1, playerNo, explodeDepth, groupMatrix, root);
        if(j < n - 1)
            explosiveAddOrb(i, j + 1, playerNo, explodeDepth, groupMatrix, root);
    }

    public boolean addOrb(int i, int j, int playerNo, Group[][] groupMatrix, Pane root)
    {
        if(i < 0 || i >= m || j < 0 || j >= n)
        {
            // Throw exception.
            return false;
        }

        Cell cell = board[i][j];

        if(cell.getPlayerNoInControl() != -1 & cell.getPlayerNoInControl() != playerNo)
        {
            // Throw exception.
            return false;
        }

        if(cell.getPlayerNoInControl() == -1)
        {
            cell.setPlayerNoInControl(playerNo);
            players[playerNo].incrementNumberOfCellsOccupied();
        }

        // Need to incorporate parallel.

        if(cell.incrementOrbAndCheckExplosion())
        {
            // GUI
            groupMatrix[i][j].getChildren().clear();

            Cluster c = new Cluster("#000000", 0);
            Group g = c.createCluster(cellSize);

            groupMatrix[i][j] = g;
            groupMatrix[i][j].setTranslateX(j * cellSize);
            groupMatrix[i][j].setTranslateY(60 + i * cellSize);
            RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
            rotate.setByAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
            root.getChildren().add(groupMatrix[i][j]);

            cell.setExplodeDepth(1);
            explosionQueue.add(cell);
        }

        else
        {
            groupMatrix[i][j].getChildren().clear();

            Cluster c = new Cluster(players[playerNo].getColour(), cell.getNumberOfOrbs());
            Group g = c.createCluster(cellSize);

            groupMatrix[i][j] = g;
            groupMatrix[i][j].setTranslateX(j * cellSize);
            groupMatrix[i][j].setTranslateY(60 + i * cellSize);
            groupMatrix[i][j].setMouseTransparent(true);
            RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
            rotate.setByAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
            root.getChildren().add(groupMatrix[i][j]);
        }

        while(!explosionQueue.isEmpty())
        {
            Cell explodeCell = explosionQueue.remove();
            explode(explodeCell.getRow(), explodeCell.getCol(), playerNo, explodeCell.getExplodeDepth() + 1, groupMatrix, root);
        }

        return true;
    }

    public void explosiveAddOrb(int i, int j, int playerNo, int explodeDepth, Group[][] groupMatrix, Pane root)
    {
        Cell cell = board[i][j];

        if(cell.getPlayerNoInControl() != playerNo)
        {
            if(cell.getPlayerNoInControl() != -1)
            {
                //cell.getPlayerInControl().decrementNumberOfCellsOccupied();
                players[cell.getPlayerNoInControl()].decrementNumberOfCellsOccupied();
                //if(cell.getPlayerInControl().getNumberOfCellsOccupied() == 0)
                    //cell.getPlayerInControl().kill();
            }
            cell.setPlayerNoInControl(playerNo);
            players[playerNo].incrementNumberOfCellsOccupied();
        }

        if(cell.incrementOrbAndCheckExplosion())
        {
            groupMatrix[i][j].getChildren().clear();

            Cluster c = new Cluster("#000000", 0);
            Group g = c.createCluster(cellSize);

            groupMatrix[i][j] = g;
            groupMatrix[i][j].setTranslateX(j * cellSize);
            groupMatrix[i][j].setTranslateY(60 + i * cellSize);
            RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
            rotate.setByAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
            root.getChildren().add(groupMatrix[i][j]);

            cell.setExplodeDepth(explodeDepth);
            explosionQueue.add(cell);
        }

        else
        {
            groupMatrix[i][j].getChildren().clear();

            Cluster c = new Cluster(players[playerNo].getColour(), cell.getNumberOfOrbs());
            Group g = c.createCluster(cellSize);

            groupMatrix[i][j] = g;
            groupMatrix[i][j].setTranslateX(j * cellSize);
            groupMatrix[i][j].setTranslateY(60 + i * cellSize);
            groupMatrix[i][j].setMouseTransparent(true);
            RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
            rotate.setByAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
            root.getChildren().add(groupMatrix[i][j]);
        }
    }


    public void display(Group[][] groupMatrix, Pane root)
    {
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                int playerNo = board[i][j].getPlayerNoInControl();

                if(playerNo == -1)
                {
                    groupMatrix[i][j].getChildren().clear();

                    Cluster c = new Cluster("#000000", 0);
                    Group g = c.createCluster(cellSize);

                    groupMatrix[i][j] = g;
                    groupMatrix[i][j].setTranslateX(j * cellSize);
                    groupMatrix[i][j].setTranslateY(60 + i * cellSize);
                    RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
                    rotate.setByAngle(360);
                    rotate.setCycleCount(Timeline.INDEFINITE);
                    rotate.setInterpolator(Interpolator.LINEAR);
                    rotate.play();
                    root.getChildren().add(groupMatrix[i][j]);
                }

                else
                {
                    groupMatrix[i][j].getChildren().clear();

                    Cluster c = new Cluster(players[playerNo].getColour(), board[i][j].getNumberOfOrbs());
                    Group g = c.createCluster(cellSize);

                    groupMatrix[i][j] = g;
                    groupMatrix[i][j].setTranslateX(j * cellSize);
                    groupMatrix[i][j].setTranslateY(60 + i * cellSize);
                    groupMatrix[i][j].setMouseTransparent(true);
                    RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
                    rotate.setByAngle(360);
                    rotate.setCycleCount(Timeline.INDEFINITE);
                    rotate.setInterpolator(Interpolator.LINEAR);
                    rotate.play();
                    root.getChildren().add(groupMatrix[i][j]);
                }
            }
        }
    }

    // To be removed
    /*
    public void display()
    {
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(board[i][j].getNumberOfOrbs());
                if(board[i][j].getPlayerNoInControl() == 0)
                    System.out.print("a ");
                else if(board[i][j].getPlayerNoInControl() == 1)
                    System.out.print("b ");
                else if(board[i][j].getPlayerNoInControl() == 2)
                    System.out.print("c ");
                else
                    System.out.print("_ ");
            }
            System.out.println();
        }
        System.out.println();
    }
    */
}