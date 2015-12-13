package com.christopherdcanfield;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

public class DebugInfo
{
	private final SpriteBatch spriteBatch;

	private long maxMemoryUsed;
	private long maxMemoryUsedMB;
	private final long maxMemory;
	private int availableProcessors;

	public DebugInfo(SpriteBatch batch)
	{
		this.spriteBatch = batch;

		Runtime runtime = Runtime.getRuntime();
		this.maxMemory = runtime.maxMemory();
		this.availableProcessors = runtime.availableProcessors();
	}

	public String getInfo()
	{
		Runtime runtime = Runtime.getRuntime();
		long totalMemory = runtime.totalMemory();
		long totalMemoryMB = totalMemory / 1024 / 1024;
		long freeMemory = runtime.freeMemory();
		long freeMemoryMB = freeMemory / 1024 / 1024;
		long memoryUsed = totalMemory - freeMemory;
		long memoryUsedMB = totalMemoryMB - freeMemoryMB;
		if (memoryUsed > maxMemoryUsed) {
			maxMemoryUsed = memoryUsed;
			maxMemoryUsedMB = memoryUsedMB;
		}
		long maxMemoryMB = maxMemory / 1024 / 1024;

		String debugInfo =
				"Total Memory: " + totalMemory + " (" + totalMemoryMB + " MB) \n" +
				"Free Memory: " + freeMemory + " (" + freeMemoryMB + " MB) \n" +
				"Memory Used: " + memoryUsed + " (" + memoryUsedMB + " MB) \n" +
				"Max Memory Used: " + maxMemoryUsed + " (" + maxMemoryUsedMB + " MB) \n" +
				"Max Memory: " + maxMemory + " (" + maxMemoryMB + " MB) \n" +
				"Available processors: " + availableProcessors + "\n" +
				"SpriteBatch Render Calls: " + spriteBatch.totalRenderCalls + "\n" +
				"Max Sprites in Batch: " + spriteBatch.maxSpritesInBatch + "\n" +
				"GL Render Calls: " + GLProfiler.drawCalls + "\n" +
				"Texture Bindings: " + GLProfiler.textureBindings + "\n" +
				"Avg Vertex Count: " + GLProfiler.vertexCount.count + "\n" +
				"Shader Switches: " + GLProfiler.shaderSwitches + "\n" +
				"FPS: " + Gdx.graphics.getFramesPerSecond() + "\n" +
				"Screen Size: " + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight();
		return debugInfo;
	}
}
