package com.christopherdcanfield;

public class TerrainFeature
{
	public static final int PIXELS_WIDTH = 32;
	public static final int PIXELS_HEIGHT = 32;
	
	public static final byte TYPE_NONE = 0;
	public static final byte TYPE_WOOD_WALL = 1;
	public static final byte TYPE_STONE_WALL = 2;
	
	public static int worldColumnToPixelX(int column)
	{
		return column * PIXELS_WIDTH;
	}

	public static int worldRowToPixelY(int row)
	{
		return row * PIXELS_HEIGHT;
	}

	public static int screenXToColumn(int screenX)
	{
		return screenX / PIXELS_WIDTH;
	}

	public static int screenYToRow(int screenY, int screenHeightPixels)
	{
		return (screenHeightPixels - screenY) / PIXELS_HEIGHT;
	}
	
	public static int worldXToColumn(int x)
	{
		return x / PIXELS_WIDTH;
	}
	
	public static int worldYToRow(int y)
	{
		return y / PIXELS_HEIGHT;
	}
}
