package com.christopherdcanfield;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Graphics
{
	private final SpriteBatch batch;

	private final Texture grassTexture;
	private final Texture dirtTexture;
	private final Texture waterTexture;

	public Graphics(SpriteBatch batch)
	{
		this.batch = batch;

		grassTexture = null;
		dirtTexture = null;
		waterTexture = null;
	}

	public void render(byte[][] world)
	{
		for (int column = 0; column < world.length; column++) {
			for (int row = 0; row < world[0].length; row++) {
				switch (world[column][row]) {
					case Block.TYPE_DIRT:
						break;
					case Block.TYPE_GRASS:
						break;
					case Block.TYPE_WATER:
						break;
				}
			}
		}
	}
}
