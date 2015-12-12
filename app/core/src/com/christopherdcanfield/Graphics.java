package com.christopherdcanfield;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Graphics
{
	private final SpriteBatch batch;

	private final Texture dirtTexture;
	private final Texture grassTexture;
	private final Texture tallGrassTexture;
	private final Texture waterTexture;

	public Graphics(SpriteBatch batch)
	{
		this.batch = batch;

		dirtTexture = null;
		grassTexture = null;
		tallGrassTexture = null;
		waterTexture = null;
	}

	public void render(byte[][] world)
	{
		for (int column = 0; column < world.length; column++) {
			for (int row = 0; row < world[0].length; row++) {
				int x = Block.worldColumnToPixelX(column);
				int y = Block.worldRowToPixelY(row);
				
				final Texture texture;
				switch (world[column][row]) {
					case Block.TYPE_DIRT:
						texture = dirtTexture;
						break;
					case Block.TYPE_GRASS:
						texture = grassTexture;
						break;
					case Block.TYPE_TALL_GRASS:
						texture = tallGrassTexture;
						break;
					case Block.TYPE_WATER:
						texture = waterTexture;
						break;
					default:
						throw new RuntimeException("Unknown block type: " + world[column][row]);
				}
				
				batch.draw(dirtTexture, x, y, Block.PIXELS_WIDTH, Block.PIXELS_HEIGHT);
			}
		}
	}
}
