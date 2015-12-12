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
				if ((column + row) % 5 == 0) {
					world[column][row] = Block.TYPE_SHALLOW_WATER;
				} else if ((column + row) % 4 == 0) {
					world[column][row] = Block.TYPE_DIRT;
				} else {
					world[column][row] = Block.TYPE_GRASS;
				}
			}
		}
	}
	
	public byte[][] get()
	{
		return world;
	}
}
