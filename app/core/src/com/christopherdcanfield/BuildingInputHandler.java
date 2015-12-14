package com.christopherdcanfield;

import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

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
		
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX, 5, iconWidth, iconHeight, "Wood Wall"));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + iconWidth + marginBetweenIcons, 5, iconWidth, iconHeight, "Stone Wall"));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 2, 5, iconWidth, iconHeight, "Moat"));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 3, 5, iconWidth, iconHeight, "Road"));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 4, 5, iconWidth, iconHeight, "Guard Tower"));
		buildingIcons.add(new BuildingIcon(buildingIconAreaStartX + (iconWidth + marginBetweenIcons) * 5, 5, iconWidth, iconHeight, "Farm"));
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
		for (BuildingIcon icon : buildingIcons) {
			if (icon.contains(screenX, Gdx.graphics.getHeight() - screenY)) {
				System.out.println("Over icon: " + icon.name);
				return true;
			}
		}
		
		return false;
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
		
		BuildingIcon(float x, float y, float width, float height, String name)
		{
			super(x, y, width, height);
			this.name = name;
		}
	}
}
