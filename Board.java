package chainreaction;

import java.util.*;
import java.io.*;

public class Board implements Serializable
{
    private int m;
    private int n;
    private Cell[][] board;
    private Queue<Cell> explosionQueue;

    // To be removed.
    private Player[] players;

    public Board(int m, int n, Player[] players)
    {
        this.m = m;
        this.n = n;

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

    public Cell getCell(int i, int j)
    {
        return board[i][j];
    }
    public void explode(int i, int j, int playerNo, int explodeDepth)
    {
        players[playerNo].decrementNumberOfCellsOccupied();
        if(i > 0)
            explosiveAddOrb(i - 1, j, playerNo, explodeDepth);
        if(i < m - 1)
            explosiveAddOrb(i + 1, j, playerNo, explodeDepth);
        if(j > 0)
            explosiveAddOrb(i, j - 1, playerNo, explodeDepth);
        if(j < n - 1)
            explosiveAddOrb(i, j + 1, playerNo, explodeDepth);
    }

    public boolean addOrb(int i, int j, int playerNo)
    {
        if(i < 0 || i >= m || j < 0 || j >= n)
        {
            // Throw exception.
            return false;
        }

        Cell cell = board[i][j];

        if(cell.getPlayerNoInControl() != -1 && cell.getPlayerNoInControl() != playerNo)
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
            cell.setExplodeDepth(1);
            explosionQueue.add(cell);
        }

        while(!explosionQueue.isEmpty())
        {
            Cell explodeCell = explosionQueue.remove();
            explode(explodeCell.getRow(), explodeCell.getCol(), playerNo, explodeCell.getExplodeDepth() + 1);
        }

        return true;
    }

    public void explosiveAddOrb(int i, int j, int playerNo, int explodeDepth)
    {
        Cell cell = board[i][j];

        if(cell.getPlayerNoInControl() != playerNo)
        {
            if(cell.getPlayerNoInControl() != -1)
            {
                players[cell.getPlayerNoInControl()].decrementNumberOfCellsOccupied();
                //if(players[cell.getPlayerNoInControl()].getNumberOfCellsOccupied() == 0)
                //    players[cell.getPlayerNoInControl()].kill();
            }
            cell.setPlayerNoInControl(playerNo);
            players[playerNo].incrementNumberOfCellsOccupied();
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
        //System.out.println(players[0]);
        //System.out.println(players[1]);
        for(int i = 0; i < m; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(board[i][j].getNumberOfOrbs() != 0)
                {
                    System.out.print(board[i][j].getNumberOfOrbs());    
                }
                else
                {
                    System.out.print(" ");
                }
                
                //System.out.print(board[i][j].getPlayerNoInControl());
                if(board[i][j].getPlayerNoInControl() == 0)
                    System.out.print("a ");
                else if(board[i][j].getPlayerNoInControl() == 1)
                    System.out.print("b ");
                else
                    System.out.print("_ ");
            }
            System.out.println();
        }
        System.out.println();
    }
}