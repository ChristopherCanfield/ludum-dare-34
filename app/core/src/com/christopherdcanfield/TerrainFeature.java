package com.christopherdcanfield;

public class TerrainFeature
{
	public static final int PIXELS_WIDTH = 32;
	public static final int PIXELS_HEIGHT = 32;
	
	public static final byte TYPE_NONE = 0;
	public static final byte TYPE_WOOD_WALL = 1;
	public static final byte TYPE_STONE_WALL = 2;
	public static final byte TYPE_MOAT = 3;
	public static final byte TYPE_ROAD = 4;
	public static final byte TYPE_GUARD_TOWER = 5;
	public static final byte TYPE_FARM = 6;
	
	
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
	
	public static String getName(byte block)
	{
		switch (block) {
			case TYPE_WOOD_WALL:
				return "Wood Wall";
			case TYPE_STONE_WALL:
				return "Stone Wall";
			case TYPE_MOAT:
				return "Moat";
			case TYPE_ROAD:
				return "Road";
			case TYPE_GUARD_TOWER:
				return "Guard Tower";
			case TYPE_FARM:
				return "Farm";
			default:
				throw new IllegalArgumentException("Unknown terrain feature type: " + block);
		}
	}
	
	public static double getBlobTakeoverChance(byte block)
	{
		switch (block) {
			case TYPE_WOOD_WALL:
				return 0.5;
			case TYPE_STONE_WALL:
				return 0.1;
			case TYPE_MOAT:
				return 0.075;
			case TYPE_ROAD:
				return 0.975;
			case TYPE_GUARD_TOWER:
				return 0.15;
			case TYPE_FARM:
				return 0.975;
			default:
				return 0;
		}
	}
	
	public static double getBlobResistance(byte block)
	{
		return 1 - getBlobTakeoverChance(block);
	}
}
