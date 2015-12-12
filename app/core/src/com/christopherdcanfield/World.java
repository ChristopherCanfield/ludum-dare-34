package com.christopherdcanfield;

import com.badlogic.gdx.math.Rectangle;

public class World
{
	public static final int COLUMNS = 1000;
	public static final int ROWS = 1000;
	public static final int BLOCK_COUNT = COLUMNS * ROWS;
	
	private final byte[][] world = new byte[COLUMNS][ROWS];
	private final Rectangle bounds;
	
	
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
		
		for (int column = 0; column < COLUMNS; column++) {
			world[column][0] = Block.TYPE_DEEP_WATER;
			world[column][ROWS-1] = Block.TYPE_DEEP_WATER;
		}
		
		for (int row = 0; row < ROWS; row++) {
			world[0][row] = Block.TYPE_DEEP_WATER;
			world[COLUMNS-1][row] = Block.TYPE_DEEP_WATER;
		}
		
		bounds = new Rectangle(0, 0, COLUMNS * Block.PIXELS_WIDTH, ROWS * Block.PIXELS_HEIGHT);
	}
	
	public byte[][] get()
	{
		return world;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
}
