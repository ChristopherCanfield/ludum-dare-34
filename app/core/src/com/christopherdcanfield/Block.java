package com.christopherdcanfield;

public class Block
{
	public static final int PIXELS_WIDTH = 4;
	public static final int PIXELS_HEIGHT = 4;

	public static final byte TYPE_DIRT = 0b0000_0000;
	public static final byte TYPE_GRASS = 0b0000_0001;
	public static final byte TYPE_WATER = 0b0000_0100;

//	public static byte getType(byte block)
//	{
//		int type = block & TYPE_BIT_MASK;
//		assert (type == TYPE_DIRT ||
//				type == TYPE_GRASS ||
//				type == TYPE_WATER);
//		return (byte)type;
//	}

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
}
