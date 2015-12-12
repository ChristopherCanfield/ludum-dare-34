package com.christopherdcanfield;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Graphics
{
	private final SpriteBatch batch;
	private final OrthographicCamera camera;
	
	private final int viewportHalfWidth;
	private final int viewportHalfHeight;

	private final Texture dirtTexture;
	private final Texture grassTexture;
	private final Texture tallGrassTexture;
	private final Texture shallowWaterTexture;
	private final Texture deepWaterTexture;
	private final Texture blobTexture;

	public Graphics(SpriteBatch batch, OrthographicCamera camera)
	{
		this.batch = batch;
		this.camera = camera;
		
		this.viewportHalfWidth = (int)(camera.viewportWidth / 2f + 1);
		this.viewportHalfHeight = (int)(camera.viewportHeight / 2f + 1);

		dirtTexture = new Texture("dirt.png");
		grassTexture = new Texture("grass.png");
		tallGrassTexture = new Texture("tallGrass.png");
		shallowWaterTexture = new Texture("water.png");
		deepWaterTexture = new Texture("deepWater.png");
		blobTexture = new Texture("blob.png");
	}
	
	public void dispose()
	{
		try {
			dirtTexture.dispose();
			grassTexture.dispose();
			tallGrassTexture.dispose();
			shallowWaterTexture.dispose();
			deepWaterTexture.dispose();
		} catch (Exception e) {
			
		}
	}

	public void render(World world)
	{
		byte[][] blocks = world.get();
		
		for (int column = 0; column < blocks.length; column++) {
			for (int row = 0; row < blocks[0].length; row++) {
				int x = Block.worldColumnToPixelX(column);
				int y = Block.worldRowToPixelY(row);
				
				float left = camera.position.x - viewportHalfWidth;
				float right = camera.position.x + viewportHalfWidth;
				float top = camera.position.y + viewportHalfHeight;
				float bottom = camera.position.y - viewportHalfHeight;
				
				if (x >= left && x <= right && y <= top && y >= bottom)
				{
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
							break;
						case Block.TYPE_BLOB:
							texture = blobTexture;
							break;
						default:
							throw new RuntimeException("Unknown block type: " + blocks[column][row]);
					}
					
					batch.draw(texture, x, y, Block.PIXELS_WIDTH, Block.PIXELS_HEIGHT);
				}
			}
		}
	}
}
