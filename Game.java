// Need to check for colour.


package App;



class Player
{
	int colour;
}

class Cell
{
	int criticalMass;
	int numberOfOrbs;
	int playerNo;
	
	public Cell(int criticalMass)
	{
		this.criticalMass = criticalMass;
		numberOfOrbs = 0;
		playerNo = 0;
	}
	
	// Returns true if the stack explodes; false otherwise.
	public boolean addOrbAndCheckExplosion(int playerNo)
	{
		this.playerNo = playerNo;
		// Can also keep ==.
		if(++numberOfOrbs >= criticalMass)
		{
			numberOfOrbs = 0;
			return true;
		}
		return false;
	}
	
	public int getPlayerNo()
	{
		return playerNo;
	}
}

class Board
{
	private int m;
	private int n;
	private Cell[][] board;
	
	public Board(int m, int n)
	{
		this.m = m;
		this.n = n;
		board = new Cell[m][n];
	}
	
	// Adds orb at the given location.
	// Returns true if addition of orb was successful; false otherwise.
	public boolean addOrb(int i, int j, int playerNo)
	{
		try
		{
			int playerAtCell = board[i][j].getPlayerNo();
			if(playerAtCell == 0 || playerAtCell == playerNo)
			{
				// Invokes explosiveAddOrb on all 4 adjacent cells.
				// Non-existence of cells handled in catch.
				if(board[i][j].addOrbAndCheckExplosion(playerNo));
				{
					explosiveAddOrb(i - 1, j, playerNo);
					explosiveAddOrb(i, j - 1, playerNo);
					explosiveAddOrb(i + 1, j, playerNo);
					explosiveAddOrb(i, j + 1, playerNo);
				}
				
				return true;
			}
			
			return false;
		}
		
		// To be done.
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	public boolean explosiveAddOrb(int i, int j, int playerNo)
	{
		try
		{
			int playerAtCell = board[i][j].getPlayerNo();
			{
				// Invokes explosiveAddOrb on all 4 adjacent cells.
				// Non-existence of cells handled in catch.
				if(board[i][j].addOrbAndCheckExplosion(playerNo));
				{
					explosiveAddOrb(i - 1, j, playerNo);
					explosiveAddOrb(i, j - 1, playerNo);
					explosiveAddOrb(i + 1, j, playerNo);
					explosiveAddOrb(i, j + 1, playerNo);
				}
				
				return true;
			}
		}
		
		// To be done.
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
}

public class Game
{
	public static void main(String[] args)
	{
		
	}
}
