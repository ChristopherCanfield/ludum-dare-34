package com.christopherdcanfield;

public class World
{
	public static final int COLUMNS = 1000;
	public static final int ROWS = 1000;
	public static final int BLOCK_COUNT = COLUMNS * ROWS;
	
	private final byte[][] world = new byte[COLUMNS][ROWS];
	
	
	public World()
	{
		for (int column = 0; column < COLUMNS; column++) {
			for (int row = 0; row < ROWS; row++) {
				world[column][row] = Block.TYPE_GRASS;
			}
		}
	}
	
	public byte[][] get()
	{
		return world;
	}
}
