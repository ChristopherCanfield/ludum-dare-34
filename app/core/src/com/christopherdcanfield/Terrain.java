package com.christopherdcanfield;

public class Terrain
{
	public static final int PIXELS_WIDTH = 2;
	public static final int PIXELS_HEIGHT = 2;

	public static final byte TYPE_DIRT = 1;
	public static final byte TYPE_GRASS = 0;
	public static final byte TYPE_TALL_GRASS = 2;
	public static final byte TYPE_SHALLOW_WATER = 3;
	public static final byte TYPE_DEEP_WATER = 4;
	public static final byte TYPE_BLOB = 5;

//	public static byte getType(byte block)
//	{
//		int type = block & TYPE_BIT_MASK;
//		assert (type == TYPE_DIRT ||
//				type == TYPE_GRASS ||
//				type == TYPE_WATER);
//		return (byte)type;
//	}
	
	public static double getBlobTakeoverChance(byte block)
	{
		switch (block) {
			case TYPE_GRASS:
				return 0.75;
			case TYPE_TALL_GRASS:
				return 0.65;
			case TYPE_DIRT:
				return 0.9;
			case TYPE_SHALLOW_WATER:
				return 0.15;
			case TYPE_DEEP_WATER:
				return 0.025;
			default:
				return 0;
		}
	}

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
