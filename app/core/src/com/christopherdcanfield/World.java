package com.christopherdcanfield;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.google.common.math.IntMath;

public class World
{
	public static final int TERRAIN_COLUMNS = 1000;
	public static final int TERRAIN_ROWS = 1000;
	public static final int TERRAIN_BLOCK_COUNT = TERRAIN_COLUMNS * TERRAIN_ROWS;
	
	public static final int FEATURES_COLUMNS = TERRAIN_COLUMNS * Terrain.PIXELS_WIDTH / TerrainFeature.PIXELS_WIDTH;
	public static final int FEATURES_ROWS = TERRAIN_ROWS * Terrain.PIXELS_HEIGHT / TerrainFeature.PIXELS_HEIGHT;
	
	private final byte[][] terrain = new byte[TERRAIN_COLUMNS][TERRAIN_ROWS];
	private final byte[][] features = new byte[FEATURES_COLUMNS][FEATURES_ROWS];
	
	private final Rectangle bounds;
	
	private final ArrayList<GridPoint2> blob = new ArrayList<>();
	private final Random random = new Random();
	
	private final ArrayList<BlobObserver> blobObserver = new ArrayList<>();
	
	public World()
	{
		for (int column = 0; column < TERRAIN_COLUMNS; column++) {
			for (int row = 0; row < TERRAIN_ROWS; row++) {

			}
		}
		
		for (int column = 0; column < TERRAIN_COLUMNS; column++) {
			terrain[column][0] = Terrain.TYPE_DEEP_WATER;
			terrain[column][TERRAIN_ROWS-1] = Terrain.TYPE_DEEP_WATER;
		}
		
		for (int row = 0; row < TERRAIN_ROWS; row++) {
			terrain[0][row] = Terrain.TYPE_DEEP_WATER;
			terrain[TERRAIN_COLUMNS-1][row] = Terrain.TYPE_DEEP_WATER;
		}
		
		bounds = new Rectangle(0, 0, TERRAIN_COLUMNS * Terrain.PIXELS_WIDTH, TERRAIN_ROWS * Terrain.PIXELS_HEIGHT);
		
		blob.add(new GridPoint2(TERRAIN_COLUMNS - 2, 1));
		terrain[TERRAIN_COLUMNS-2][1] = Terrain.TYPE_BLOB;
		
		App.scheduledExecutor.scheduleAtFixedRate(() -> {
			try {
				int log2Blob = IntMath.log2(blob.size(), RoundingMode.CEILING);
				final int growthCount = Math.max(1, log2Blob * log2Blob);
				final List<GridPoint2> emptyAdjacent = World.getEmptyAdjacentToBlob(World.this, blob);
				if (emptyAdjacent.isEmpty()) {
					System.err.println("emptyAdjacent list is empty, but should not be.");
					return;
				}
				
				Gdx.app.postRunnable(() -> {
					System.out.println("Growing: " + growthCount);
					int growthRemaining = growthCount;
					while (growthRemaining > 0)
					{
						int emptyCellIndex = random.nextInt(emptyAdjacent.size());
						GridPoint2 emptyCell = emptyAdjacent.get(emptyCellIndex);
						if (terrain[emptyCell.x][emptyCell.y] != Terrain.TYPE_BLOB) {
							double takeoverChance = Terrain.getBlobTakeoverChance(terrain[emptyCell.x][emptyCell.y]);
							if (takeoverChance >= random.nextDouble()) {
								terrain[emptyCell.x][emptyCell.y] = Terrain.TYPE_BLOB;
								blob.add(emptyCell);
								emptyAdjacent.remove(emptyCell);
								growthRemaining--;
								
								double blobPctOfWorld = blob.size() / (double)TERRAIN_BLOCK_COUNT;
								for (BlobObserver listener : blobObserver) {
									listener.onBlobExpanded(blobPctOfWorld);
								}
								
								if (emptyAdjacent.isEmpty()) {
									System.err.println("emptyAdjacent is now empty; exiting early.");
									return;
								}
							}
						} else {
							System.out.println(emptyCell + " is not empty");
							return;
						}
					}
					
					System.out.println("Grew");
				});
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}, 1000, 175, TimeUnit.MILLISECONDS);
	}
	
	public byte[][] getTerrain()
	{
		return terrain;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	public void addBlobListener(BlobObserver listener)
	{
		blobObserver.add(listener);
	}
	
	static List<GridPoint2> getEmptyAdjacentToBlob(World world, ArrayList<GridPoint2> blob)
	{
		final byte[][] blocks = world.getTerrain();
		
		List<GridPoint2> blobsWithEmptyAdjacent = blob.parallelStream()
				.filter(blobPosition -> {
					boolean notFirstColumn = blobPosition.x > 0;
					boolean notLastColumn = blobPosition.x < World.TERRAIN_COLUMNS - 1;
					boolean notFirstRow = blobPosition.y > 0;
					boolean notLastRow = blobPosition.y < World.TERRAIN_ROWS - 1;
					
					if (notFirstColumn && blocks[blobPosition.x-1][blobPosition.y] != Terrain.TYPE_BLOB) {
						return true;
					}
					if (notLastColumn && blocks[blobPosition.x+1][blobPosition.y] != Terrain.TYPE_BLOB) {
						return true;
					}
					if (notFirstRow && blocks[blobPosition.x][blobPosition.y-1] != Terrain.TYPE_BLOB) {
						return true;
					}
					if (notLastRow && blocks[blobPosition.x][blobPosition.y+1] != Terrain.TYPE_BLOB) {
						return true;
					}
					return false;
				})
				.distinct()
				.collect(Collectors.toList());
		
		HashSet<GridPoint2> emptyAdjacent = new HashSet<>();
		for (GridPoint2 blobPosition : blobsWithEmptyAdjacent) {
			boolean notFirstColumn = blobPosition.x > 0;
			boolean notLastColumn = blobPosition.x < World.TERRAIN_COLUMNS - 1;
			boolean notFirstRow = blobPosition.y > 0;
			boolean notLastRow = blobPosition.y < World.TERRAIN_ROWS - 1;
			
			if (notFirstColumn && blocks[blobPosition.x - 1][blobPosition.y] != Terrain.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x-1, blobPosition.y));
			}
			if (notLastColumn && blocks[blobPosition.x + 1][blobPosition.y] != Terrain.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x+1, blobPosition.y));
			}
			if (notFirstRow && blocks[blobPosition.x][blobPosition.y - 1] != Terrain.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x, blobPosition.y-1));
			}
			if (notLastRow && blocks[blobPosition.x][blobPosition.y + 1] != Terrain.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x, blobPosition.y+1));
			}
		}
		
		return new ArrayList<>(emptyAdjacent);
	}
}
