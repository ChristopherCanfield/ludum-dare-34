package com.christopherdcanfield;

public class Block
{
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
}
