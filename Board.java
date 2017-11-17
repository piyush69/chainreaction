package chainreaction;

import javafx.scene.Scene;
import java.util.*;

public class Board
{
    private int m;
    private int n;
    private Cell[][] board;
    private Queue<Cell> explosionQueue;
    private Scene scene;
    private double cellSize;

    // To be removed.
    private Player[] players;

    public Board(int m, int n, Player[] players /*To be removed*/, Scene scene, double cellSize)
    {
        this.m = m;
        this.n = n;
        this.scene = scene;
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

    public void explode(int i, int j, Player player, int explodeDepth)
    {
        player.decrementNumberOfCellsOccupied();
        if(i > 0)
            explosiveAddOrb(i - 1, j, player, explodeDepth);
        if(i < m - 1)
            explosiveAddOrb(i + 1, j, player, explodeDepth);
        if(j > 0)
            explosiveAddOrb(i, j - 1, player, explodeDepth);
        if(j < n - 1)
            explosiveAddOrb(i, j + 1, player, explodeDepth);
    }

    public boolean addOrb(int i, int j, Player player)
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
            cell.setExplodeDepth(1);
            explosionQueue.add(cell);
        }

        while(!explosionQueue.isEmpty())
        {
            Cell explodeCell = explosionQueue.remove();
            explode(explodeCell.getRow(), explodeCell.getCol(), player, explodeCell.getExplodeDepth() + 1);
        }

        return true;
    }

    public void explosiveAddOrb(int i, int j, Player player, int explodeDepth)
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
            cell.setExplodeDepth(explodeDepth);
            explosionQueue.add(cell);
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