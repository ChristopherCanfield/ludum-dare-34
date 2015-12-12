package com.christopherdcanfield;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Graphics
{
	private final SpriteBatch batch;

	private final Texture dirtTexture;
	private final Texture grassTexture;
	private final Texture tallGrassTexture;
	private final Texture shallowWaterTexture;
	private final Texture deepWaterTexture;

	public Graphics(SpriteBatch batch)
	{
		this.batch = batch;

		dirtTexture = new Texture("dirt.png");
		grassTexture = new Texture("grass.png");
		tallGrassTexture = new Texture("tallGrass.png");
		shallowWaterTexture = new Texture("water.png");
		deepWaterTexture = new Texture("deepWaterTexture.png");
	}

	public void render(World world)
	{
		byte[][] blocks = world.get();
		
		for (int column = 0; column < blocks.length; column++) {
			for (int row = 0; row < blocks[0].length; row++) {
				int x = Block.worldColumnToPixelX(column);
				int y = Block.worldRowToPixelY(row);
				
				final Texture texture;
				switch (blocks[column][row]) {
					case Block.TYPE_DIRT:
						texture = dirtTexture;
						break;
					case Block.TYPE_GRASS:
						texture = grassTexture;
						break;
					case Block.TYPE_TALL_GRASS:
						texture = tallGrassTexture;
						break;
					case Block.TYPE_SHALLOW_WATER:
						texture = shallowWaterTexture;
						break;
					case Block.TYPE_DEEP_WATER:
						texture = deepWaterTexture;
					default:
						throw new RuntimeException("Unknown block type: " + blocks[column][row]);
				}
				
				batch.draw(dirtTexture, x, y, Block.PIXELS_WIDTH, Block.PIXELS_HEIGHT);
			}
		}
	}
}
