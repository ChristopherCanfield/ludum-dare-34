package com.christopherdcanfield;

import com.badlogic.gdx.math.GridPoint2;

public class HoveredBlock
{
	public GridPoint2 terrain;
	public GridPoint2 terrainFeature;
	
	public boolean isSet() {
		return terrain != null && terrainFeature != null;
	}
}
