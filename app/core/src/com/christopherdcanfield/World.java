package com.christopherdcanfield;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import com.google.common.math.IntMath;

public class World
{
	public static final int COLUMNS = 1000;
	public static final int ROWS = 1000;
	public static final int BLOCK_COUNT = COLUMNS * ROWS;
	
	private final byte[][] world = new byte[COLUMNS][ROWS];
	private final Rectangle bounds;
	
	private final ArrayList<GridPoint2> blob = new ArrayList<>();
	private final Random random = new Random();
	
	public World()
	{
		for (int column = 0; column < COLUMNS; column++) {
			for (int row = 0; row < ROWS; row++) {
				if ((column + row) % 5 == 0) {
					world[column][row] = Block.TYPE_SHALLOW_WATER;
				} else if ((column + row) % 4 == 0) {
					world[column][row] = Block.TYPE_DIRT;
				} else {
					world[column][row] = Block.TYPE_GRASS;
				}
			}
		}
		
		for (int column = 0; column < COLUMNS; column++) {
			world[column][0] = Block.TYPE_DEEP_WATER;
			world[column][ROWS-1] = Block.TYPE_DEEP_WATER;
		}
		
		for (int row = 0; row < ROWS; row++) {
			world[0][row] = Block.TYPE_DEEP_WATER;
			world[COLUMNS-1][row] = Block.TYPE_DEEP_WATER;
		}
		
		bounds = new Rectangle(0, 0, COLUMNS * Block.PIXELS_WIDTH, ROWS * Block.PIXELS_HEIGHT);
		
		blob.add(new GridPoint2(COLUMNS - 2, 1));
		world[COLUMNS-2][1] = Block.TYPE_BLOB;
		
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				final int growthCount = Math.max(1, IntMath.log2(blob.size(), RoundingMode.CEILING));
				int growthRemaining = growthCount;
				List<GridPoint2> emptyAdjacent = World.getEmptyAdjacentToBlob(World.this, blob);
				System.out.println("Growing");
				
				while (growthRemaining > 0)
				{
					int emptyCellIndex = random.nextInt(emptyAdjacent.size());
					GridPoint2 emptyCell = emptyAdjacent.get(emptyCellIndex);
					if (world[emptyCell.x][emptyCell.y] != Block.TYPE_BLOB) {
						System.out.println("Adding new blob: " + emptyCell);
						world[emptyCell.x][emptyCell.y] = Block.TYPE_BLOB;
						blob.add(emptyCell);
						growthRemaining--;
					}
				}
				
				System.out.println("Grew");
			}
		}, 2, 2);
	}
	
	public byte[][] get()
	{
		return world;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	static List<GridPoint2> getEmptyAdjacentToBlob(World world, ArrayList<GridPoint2> blob)
	{
		final byte[][] blocks = world.get();
		
		List<GridPoint2> blobsWithEmptyAdjacent = blob.parallelStream()
				.filter(blobPosition -> {
					boolean notFirstColumn = blobPosition.x > 0;
					boolean notLastColumn = blobPosition.x < World.COLUMNS - 1;
					boolean notFirstRow = blobPosition.y > 0;
					boolean notLastRow = blobPosition.y < World.ROWS - 1;
					
					if (notFirstColumn && blocks[blobPosition.x-1][blobPosition.y] != Block.TYPE_BLOB) {
						return true;
					}
					if (notLastColumn && blocks[blobPosition.x+1][blobPosition.y] != Block.TYPE_BLOB) {
						return true;
					}
					if (notFirstRow && blocks[blobPosition.x][blobPosition.y-1] != Block.TYPE_BLOB) {
						return true;
					}
					if (notLastRow && blocks[blobPosition.x][blobPosition.y+1] != Block.TYPE_BLOB) {
						return true;
					}
					return false;
				})
				.distinct()
				.collect(Collectors.toList());
		
		HashSet<GridPoint2> emptyAdjacent = new HashSet<>();
		for (GridPoint2 blobPosition : blobsWithEmptyAdjacent) {
			boolean notFirstColumn = blobPosition.x > 0;
			boolean notLastColumn = blobPosition.x < World.COLUMNS - 1;
			boolean notFirstRow = blobPosition.y > 0;
			boolean notLastRow = blobPosition.y < World.ROWS - 1;
			
			if (notFirstColumn && blocks[blobPosition.x - 1][blobPosition.y] != Block.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x-1, blobPosition.y));
			}
			if (notLastColumn && blocks[blobPosition.x + 1][blobPosition.y] != Block.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x+1, blobPosition.y));
			}
			if (notFirstRow && blocks[blobPosition.x][blobPosition.y - 1] != Block.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x, blobPosition.y-1));
			}
			if (notLastRow && blocks[blobPosition.x][blobPosition.y + 1] != Block.TYPE_BLOB) {
				emptyAdjacent.add(new GridPoint2(blobPosition.x, blobPosition.y+1));
			}
		}
		
		return new ArrayList<>(emptyAdjacent);
	}
}
