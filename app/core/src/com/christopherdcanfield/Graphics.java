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
		long startTime = System.nanoTime();
		byte[][] blocks = world.getTerrain();

		int left = (int)(camera.position.x - viewportHalfWidth);
		int right = (int)(camera.position.x + viewportHalfWidth);
		int bottom = (int)(camera.position.y - viewportHalfHeight);
		int top = (int)(camera.position.y + viewportHalfHeight);

		// Commented out for now, since this decreased, rather than increased, the fps.
//		renderBlockType(world, Block.TYPE_BLOB);
//		renderBlockType(world, Block.TYPE_DEEP_WATER);
//		renderBlockType(world, Block.TYPE_DIRT);
//		renderBlockType(world, Block.TYPE_GRASS);
//		renderBlockType(world, Block.TYPE_SHALLOW_WATER);
//		renderBlockType(world, Block.TYPE_TALL_GRASS);
		
		final int startColumn = Terrain.worldXToColumn(left);
		final int endColumn = Terrain.worldXToColumn(right);
		final int startRow = Terrain.worldYToRow(bottom);
		final int endRow = Terrain.worldYToRow(top);
		
		for (int column = startColumn; column < endColumn; column++) {
			for (int row = startRow; row < endRow; row++) {
				int x = Terrain.worldColumnToPixelX(column);
				int y = Terrain.worldRowToPixelY(row);

				if (x >= left && x <= right && y <= top && y >= bottom)
				{
					final Texture texture;
					switch (blocks[column][row]) {
						case Terrain.TYPE_DIRT:
							texture = dirtTexture;
							break;
						case Terrain.TYPE_GRASS:
							texture = grassTexture;
							break;
						case Terrain.TYPE_TALL_GRASS:
							texture = tallGrassTexture;
							break;
						case Terrain.TYPE_SHALLOW_WATER:
							texture = shallowWaterTexture;
							break;
						case Terrain.TYPE_DEEP_WATER:
							texture = deepWaterTexture;
							break;
						case Terrain.TYPE_BLOB:
							texture = blobTexture;
							break;
						default:
							throw new RuntimeException("Unknown block type: " + blocks[column][row]);
					}

					batch.draw(texture, x, y, Terrain.PIXELS_WIDTH, Terrain.PIXELS_HEIGHT);
				}
			}
		}
		System.out.println("World render time: " + (System.nanoTime() - startTime) / 1_000_000.0);
	}
	
//	private void renderBlockType(World world, byte blockType)
//	{
//		byte[][] blocks = world.get();
//
//		final float left = camera.position.x - viewportHalfWidth;
//		final float right = camera.position.x + viewportHalfWidth;
//		final float top = camera.position.y + viewportHalfHeight;
//		final float bottom = camera.position.y - viewportHalfHeight;
//
//		for (int column = 0; column < blocks.length; column++) {
//			for (int row = 0; row < blocks[0].length; row++) {
//				if (blocks[column][row] == blockType) {
//					int x = Block.worldColumnToPixelX(column);
//					int y = Block.worldRowToPixelY(row);
//
//					if (x >= left && x <= right && y <= top && y >= bottom)
//					{
//						final Texture texture;
//						switch (blockType) {
//							case Block.TYPE_DIRT:
//								texture = dirtTexture;
//								break;
//							case Block.TYPE_GRASS:
//								texture = grassTexture;
//								break;
//							case Block.TYPE_TALL_GRASS:
//								texture = tallGrassTexture;
//								break;
//							case Block.TYPE_SHALLOW_WATER:
//								texture = shallowWaterTexture;
//								break;
//							case Block.TYPE_DEEP_WATER:
//								texture = deepWaterTexture;
//								break;
//							case Block.TYPE_BLOB:
//								texture = blobTexture;
//								break;
//							default:
//								throw new RuntimeException("Unknown block type: " + blocks[column][row]);
//						}
//
//						batch.draw(texture, x, y, Block.PIXELS_WIDTH, Block.PIXELS_HEIGHT);
//					}
//				}
//			}
//		}
//	}
}
