package chainreaction;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.*;

public class Board
{
    private int m;
    private int n;
    private Cell[][] board;
    private Queue<Cell> explosionQueue;
    private double cellSize;

    // To be removed.
    private Player[] players;

    public Board(int m, int n, Player[] players /*To be removed*/, double cellSize)
    {
        this.m = m;
        this.n = n;
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

        // To be removed.
        this.players = players;
    }

    public void explode(int i, int j, Player player, int explodeDepth, Group[][] groupMatrix, Pane root)
    {
        player.decrementNumberOfCellsOccupied();
        if(i > 0)
            explosiveAddOrb(i - 1, j, player, explodeDepth, groupMatrix, root);
        if(i < m - 1)
            explosiveAddOrb(i + 1, j, player, explodeDepth, groupMatrix, root);
        if(j > 0)
            explosiveAddOrb(i, j - 1, player, explodeDepth, groupMatrix, root);
        if(j < n - 1)
            explosiveAddOrb(i, j + 1, player, explodeDepth, groupMatrix, root);
    }

    public boolean addOrb(int i, int j, Player player, Group[][] groupMatrix, Pane root)
    {
        if(i < 0 || i >= m || j < 0 || j >= n)
        {
            // Throw exception.
            return false;
        }

        Cell cell = board[i][j];

        if(cell.getPlayerInControl() != null & cell.getPlayerInControl() != player)
        {
            // Throw exception.
            return false;
        }

        if(cell.getPlayerInControl() == null)
        {
            cell.setPlayerInControl(player);
            player.incrementNumberOfCellsOccupied();
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
            groupMatrix[i][j].setTranslateY(i * cellSize);
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

            Cluster c = new Cluster(player.getColour(), cell.getNumberOfOrbs());
            Group g = c.createCluster(cellSize);

            groupMatrix[i][j] = g;
            groupMatrix[i][j].setTranslateX(j * cellSize);
            groupMatrix[i][j].setTranslateY(i * cellSize);
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
            explode(explodeCell.getRow(), explodeCell.getCol(), player, explodeCell.getExplodeDepth() + 1, groupMatrix, root);
        }

        return true;
    }

    public void explosiveAddOrb(int i, int j, Player player, int explodeDepth, Group[][] groupMatrix, Pane root)
    {
        Cell cell = board[i][j];

        if(cell.getPlayerInControl() != player)
        {
            if(cell.getPlayerInControl() != null)
            {
                cell.getPlayerInControl().decrementNumberOfCellsOccupied();
                if(cell.getPlayerInControl().getNumberOfCellsOccupied() == 0)
                    cell.getPlayerInControl().kill();
            }
            cell.setPlayerInControl(player);
            player.incrementNumberOfCellsOccupied();
        }

        if(cell.incrementOrbAndCheckExplosion())
        {
            groupMatrix[i][j].getChildren().clear();

            Cluster c = new Cluster("#000000", 0);
            Group g = c.createCluster(cellSize);

            groupMatrix[i][j] = g;
            groupMatrix[i][j].setTranslateX(j * cellSize);
            groupMatrix[i][j].setTranslateY(i * cellSize);
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

            Cluster c = new Cluster(player.getColour(), cell.getNumberOfOrbs());
            Group g = c.createCluster(cellSize);

            groupMatrix[i][j] = g;
            groupMatrix[i][j].setTranslateX(j * cellSize);
            groupMatrix[i][j].setTranslateY(i * cellSize);
            groupMatrix[i][j].setMouseTransparent(true);
            RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
            rotate.setByAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.play();
            root.getChildren().add(groupMatrix[i][j]);
        }
    }

    // To be removed
    public void display()
    {
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                System.out.print(board[i][j].getNumberOfOrbs());
                if(board[i][j].getPlayerInControl() == players[0])
                    System.out.print("a ");
                else if(board[i][j].getPlayerInControl() == players[1])
                    System.out.print("b ");
                else if(board[i][j].getPlayerInControl() == players[2])
                    System.out.print("c ");
                else
                    System.out.print("_ ");
            }
            System.out.println();
        }
        System.out.println();
    }
}