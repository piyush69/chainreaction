package chainreaction;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Sphere;
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
    private Game myGame;
    private int x = -100;
    public Board(int m, int n, Player[] players, double cellSize, Game game)
    {
        this.m = m;
        this.n = n;
        this.players = players;
        this.cellSize = cellSize;
        this.myGame = game;

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

    public Game getMyGame()
    {
        return myGame;
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

    public int addOrb(int i, int j, int playerNo, Group[][] groupMatrix, Pane root)
    {
        if(i < 0 || i >= m || j < 0 || j >= n)
        {
            // Throw exception.
            return -1;
        }

        Cell cell = board[i][j];

        if(cell.getPlayerNoInControl() != -1 && cell.getPlayerNoInControl() != playerNo)
        {
            // Throw exception.
            return -1;
        }

        if(cell.getPlayerNoInControl() == -1)
        {
            cell.setPlayerNoInControl(playerNo);
            players[playerNo].incrementNumberOfCellsOccupied();
        }

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

        return BurstAnimation(playerNo, 1, groupMatrix, root);
    }

    public int BurstAnimation(int playerNo, int depth, Group[][] groupMatrix, Pane root)
    {
        //System.out.println("[?] "+ playerNo+" doing animation");
        if(!explosionQueue.isEmpty())
        {
            /*
            Cell explodeCell = explosionQueue.remove();
            explode(explodeCell.getRow(), explodeCell.getCol(), playerNo, explodeCell.getExplodeDepth() + 1, groupMatrix, root);
            */

            Orb orb = new Orb(players[playerNo].getColour());

            ParallelTransition pt = new ParallelTransition();
            Queue<Cell> cellsToExplode = new LinkedList<Cell>();

            while(explosionQueue.peek().getExplodeDepth() == depth)
            {
                Cell explodeCell = explosionQueue.remove();
                cellsToExplode.add(explodeCell);
                //explode(explodeCell.getRow(), explodeCell.getCol(), playerNo, explodeCell.getExplodeDepth() + 1, groupMatrix, root);

                int a = explodeCell.getRow();
                int b = explodeCell.getCol();

                if(a > 0)
                {
                    Sphere s1 = orb.makeOrb(cellSize);
                    root.getChildren().add(s1);
                    TranslateTransition tUp = new TranslateTransition(Duration.millis(500), s1);
                    tUp.setCycleCount(1);
                    tUp.setAutoReverse(false);
                    tUp.setFromX(b * cellSize + cellSize / 2);
                    tUp.setFromY(60 + a * cellSize + cellSize / 2);
                    tUp.setToX(b * cellSize + cellSize / 2);
                    tUp.setToY(60 + a * cellSize - cellSize / 2);
                    tUp.setOnFinished(e -> { root.getChildren().remove(s1); });
                    pt.getChildren().add(tUp);
                }

                if(a < m - 1)
                {
                    Sphere s2 = orb.makeOrb(cellSize);
                    root.getChildren().add(s2);
                    TranslateTransition tDown = new TranslateTransition(Duration.millis(500), s2);
                    tDown.setCycleCount(1);
                    tDown.setAutoReverse(false);
                    tDown.setFromX(b * cellSize + cellSize / 2);
                    tDown.setFromY(60 + a * cellSize + cellSize / 2);
                    tDown.setToX(b * cellSize + cellSize / 2);
                    tDown.setToY(60 + a * cellSize + 3 * cellSize / 2);
                    tDown.setOnFinished(e -> { root.getChildren().remove(s2); });
                    pt.getChildren().add(tDown);
                }

                if(b > 0)
                {
                    Sphere s3 = orb.makeOrb(cellSize);
                    root.getChildren().add(s3);
                    TranslateTransition tLeft = new TranslateTransition(Duration.millis(500), s3);
                    tLeft.setCycleCount(1);
                    tLeft.setAutoReverse(false);
                    tLeft.setFromX(b * cellSize + cellSize / 2);
                    tLeft.setFromY(60 + a * cellSize + cellSize / 2);
                    tLeft.setToX(b * cellSize - cellSize / 2);
                    tLeft.setToY(60 + a * cellSize + cellSize / 2);
                    tLeft.setOnFinished(e -> { root.getChildren().remove(s3); });
                    pt.getChildren().add(tLeft);
                }

                if(b < n - 1)
                {
                    Sphere s4 = orb.makeOrb(cellSize);
                    root.getChildren().add(s4);
                    TranslateTransition tRight = new TranslateTransition(Duration.millis(500), s4);
                    tRight.setCycleCount(1);
                    tRight.setAutoReverse(false);
                    tRight.setFromX(b * cellSize + cellSize / 2);
                    tRight.setFromY(60 + a * cellSize + cellSize / 2);
                    tRight.setToX(b * cellSize + 3 * cellSize / 2);
                    tRight.setToY(60 + a * cellSize + cellSize / 2);
                    tRight.setOnFinished(e -> { root.getChildren().remove(s4); });
                    pt.getChildren().add(tRight);
                }

                if(explosionQueue.isEmpty())
                    break;
            }
            pt.setOnFinished(e ->
            {
                while(!cellsToExplode.isEmpty())
                {
                    Cell explodeCell = cellsToExplode.remove();
                    explode(explodeCell.getRow(), explodeCell.getCol(), playerNo, explodeCell.getExplodeDepth() + 1, groupMatrix, root);
                }
                x = BurstAnimation(playerNo, depth + 1, groupMatrix, root);
            });

            pt.play();
            return x;
        }

        else
        {
            //System.out.println("[-] " + this.getMyGame().getCurrentPlayerNo() + " is ending its Turn");
            return this.getMyGame().endMove(playerNo);
            //players[playerNo].getCurrentGame().endMove();
        }
        //return 0;
    }

    private void explosiveAddOrb(int i, int j, int playerNo, int explodeDepth, Group[][] groupMatrix, Pane root)
    {
        Cell cell = board[i][j];

        if(cell.getPlayerNoInControl() != playerNo)
        {
            if(cell.getPlayerNoInControl() != -1)
            {
                players[cell.getPlayerNoInControl()].decrementNumberOfCellsOccupied();
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
}