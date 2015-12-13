package com.christopherdcanfield;


import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public class UserInputHandler implements InputProcessor
{
	private final Rectangle worldBounds;
	private final float viewportHalfWidth;
	private final float viewportHalfHeight;
	
	private final OrthographicCamera camera;
	private static final float cameraMoveSpeed = 30f;
	private static final float cameraMoveSpeedFast = cameraMoveSpeed * 4;
	
	private final Set<GridPoint2> selectedFeatures;
	private final GridPoint2[] hoveredBlock;
	
	private boolean moveCameraLeft;
	private boolean moveCameraRight;
	private boolean moveCameraUp;
	private boolean moveCameraDown;
	
	public UserInputHandler(OrthographicCamera camera, Rectangle worldBounds, Set<GridPoint2> selectedFeatures, GridPoint2[] hoveredBlock)
	{
		this.camera = camera;
		this.worldBounds = worldBounds;
		
		this.viewportHalfWidth = camera.viewportWidth / 2f;
		this.viewportHalfHeight = camera.viewportHeight / 2f;
		System.out.println("World bounds: " + worldBounds);
		System.out.println("Viewport: " + camera.viewportWidth + "x" + camera.viewportHeight);
		
		this.selectedFeatures = selectedFeatures;
		this.hoveredBlock = hoveredBlock;
	}
	
	public void update()
	{
		boolean useFasterCameraSpeed = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT);
		float cameraSpeed = useFasterCameraSpeed ? cameraMoveSpeedFast : cameraMoveSpeed;
		
		if (moveCameraLeft) {
			camera.position.x -= cameraSpeed;
			float cameraLeftBound = camera.position.x - viewportHalfWidth;
			if (cameraLeftBound < worldBounds.x) {
				camera.position.x = viewportHalfWidth;
				System.out.println("Moving camera to: " + viewportHalfWidth);
			}
		} else if (moveCameraRight) {
			camera.position.x += cameraSpeed;
			float cameraRightBound = camera.position.x + viewportHalfWidth;
			if (cameraRightBound > worldBounds.width) {
				camera.position.x = worldBounds.width - viewportHalfWidth;
				System.out.println("Moving camera to: " + (worldBounds.width - viewportHalfWidth));
			}
		}
		
		if (moveCameraUp) {
			camera.position.y += cameraSpeed;
			float cameraUpperBound = camera.position.y + viewportHalfHeight;
			if (cameraUpperBound > worldBounds.height) {
				camera.position.y = worldBounds.height - viewportHalfHeight;
				System.out.println("Moving camera to: " + (worldBounds.height - viewportHalfHeight));
			}
			
		} else if (moveCameraDown) {
			camera.position.y -= cameraSpeed;
			float cameraLowerBound = camera.position.y - viewportHalfHeight;
			if (cameraLowerBound < worldBounds.y) {
				camera.position.y = viewportHalfHeight;
				System.out.println("Moving camera to: " + viewportHalfHeight);
			}
		}
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		switch (keycode) {
			case Keys.ESCAPE:
				Gdx.app.exit();
				break;
			case Keys.A:
			case Keys.LEFT:
				moveCameraLeft = true;
				break;
			case Keys.D:
			case Keys.RIGHT:
				moveCameraRight = true;
				break;
			case Keys.W:
			case Keys.UP:
				moveCameraUp = true;
				break;
			case Keys.S:
			case Keys.DOWN:
				moveCameraDown = true;
				break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		switch (keycode) {
			case Keys.A:
			case Keys.LEFT:
				moveCameraLeft = false;
				break;
			case Keys.D:
			case Keys.RIGHT:
				moveCameraRight = false;
				break;
			case Keys.W:
			case Keys.UP:
				moveCameraUp = false;
				break;
			case Keys.S:
			case Keys.DOWN:
				moveCameraDown = false;
				break;
		}
		
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
		int left = (int)(camera.position.x - (camera.viewportWidth / 2f));
		int bottom = (int)(camera.position.y - (camera.viewportHeight / 2f));
		int column = TerrainFeature.screenXToColumn(screenX + left);
		int row = TerrainFeature.screenYToRow(screenY - bottom, Gdx.graphics.getHeight());
		selectedFeatures.add(new GridPoint2(column, row));
		
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
		int left = (int)(camera.position.x - (camera.viewportWidth / 2f));
		int bottom = (int)(camera.position.y - (camera.viewportHeight / 2f));
		int column = TerrainFeature.screenXToColumn(screenX + left);
		int row = TerrainFeature.screenYToRow(screenY - bottom, Gdx.graphics.getHeight());
		selectedFeatures.add(new GridPoint2(column, row));
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		int left = (int)(camera.position.x - (camera.viewportWidth / 2f));
		int bottom = (int)(camera.position.y - (camera.viewportHeight / 2f));
		int column = TerrainFeature.screenXToColumn(screenX + left);
		int row = TerrainFeature.screenYToRow(screenY - bottom, Gdx.graphics.getHeight());
		hoveredBlock[0] = new GridPoint2(column, row);
		
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

}
