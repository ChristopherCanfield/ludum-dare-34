package com.christopherdcanfield;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BuildingInputHandler implements InputProcessor
{
	private final OrthographicCamera camera;
	
	private final Texture transparentGrayPixelTexture;
	private final Texture blackTexture;
	
	private final ArrayList<BuildingIcon> buildingIcons = new ArrayList<>();
	private final int buildingIconAreaWidth;
	private final int buildingIconAreaStartX;
	private final int iconHeight;
	
	public byte selectedBuildingType = TerrainFeature.TYPE_NONE;
	public byte hoveredBuildingType = TerrainFeature.TYPE_NONE;
	
	public BuildingInputHandler(OrthographicCamera camera, Texture transparentGrayPixelTexture)
	{
		this.camera = camera;
		
		this.transparentGrayPixelTexture = transparentGrayPixelTexture;
		this.blackTexture = new Texture("blackPixel.png");
		
		int marginBetweenIcons = 5;
		int iconWidth = TerrainFeature.PIXELS_WIDTH * 2;
		iconHeight = TerrainFeature.PIXELS_HEIGHT * 2;
		buildingIconAreaWidth = (iconWidth + marginBetweenIcons) * 5 + iconWidth + marginBetweenIcons;
		buildingIconAreaStartX = Gdx.graphics.getWidth() / 2 - buildingIconAreaWidth / 2;
		
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX, 5, iconWidth, iconHeight, TerrainFeature.TYPE_WOOD_WALL));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + iconWidth + marginBetweenIcons, 5, iconWidth, iconHeight, TerrainFeature.TYPE_STONE_WALL));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 2, 5, iconWidth, iconHeight, TerrainFeature.TYPE_MOAT));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 3, 5, iconWidth, iconHeight, TerrainFeature.TYPE_ROAD));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 4, 5, iconWidth, iconHeight, TerrainFeature.TYPE_GUARD_TOWER));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 5, 5, iconWidth, iconHeight, TerrainFeature.TYPE_FARM));
	}
	
	public void render(SpriteBatch batch)
	{
		final float cameraLeft = camera.position.x - (camera.viewportWidth / 2f);
		final float cameraBottom = camera.position.y - (camera.viewportHeight / 2f);
		
		batch.enableBlending();
		batch.begin();
		
		batch.draw(transparentGrayPixelTexture, cameraLeft + buildingIconAreaStartX - 5, cameraBottom, buildingIconAreaWidth + 5, iconHeight + 10);
		for (Rectangle icon : buildingIcons) {
			batch.draw(blackTexture, cameraLeft + icon.x, cameraBottom + icon.y, icon.width, icon.height);
		}
		
		batch.end();
	}

	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		BuildingIcon icon = getBuildingIcon(screenX, screenY);
		if (icon != null) {
			selectedBuildingType = icon.buildingType;
			System.out.println("Clicked on " + icon.name);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		BuildingIcon icon = getBuildingIcon(screenX, screenY);
		if (icon != null) {
			hoveredBuildingType = icon.buildingType;
			return true;
		}
		
		return false;
	}
	
	private BuildingIcon getBuildingIcon(int screenX, int screenY)
	{
		for (BuildingIcon icon : buildingIcons) {
			if (icon.contains(screenX, Gdx.graphics.getHeight() - screenY)) {
				return icon;
			}
		}
		
		return null;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
	
	static class BuildingIcon extends Rectangle
	{
		private static final long serialVersionUID = 1L;
	
		public final String name;
		public final byte buildingType;
		
		BuildingIcon(float x, float y, float width, float height, byte buildingType)
		{
			super(x, y, width, height);
			this.buildingType = buildingType;
			this.name = TerrainFeature.getName(buildingType);
		}
	}
}
