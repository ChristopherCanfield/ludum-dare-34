package com.christopherdcanfield;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.GridPoint2;


public class GameApp extends ApplicationAdapter implements BlobObserver
{
	private World world;
	private Graphics graphics;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private UserInputHandler inputHandler;
	private Set<GridPoint2> selectedFeatures;
	private HoveredBlock hoveredBlock = new HoveredBlock();
	private String hoverText;
	
	private Texture transparentGrayPixelTexture;
	private Texture selectedTexture;
	private Texture hoverTexture;

	private BitmapFont debugInfoFont;
	private String debugInfoText;
	private GlyphLayout debugInfoLayout;
	private DebugInfo debugInfo;
	
	private BitmapFont uiFont;
	private BitmapFont hoverFont;
	
	private String blobSizeText;
	private GlyphLayout blobSizeTextLayout;
	
	private String kingdomInfoText;
	private GlyphLayout kingdomInfoTextLayout;

	
	public GameApp()
	{
	}

	@Override
	public void create() {
		try {
			batch = new SpriteBatch(5000);
	
			world = new World();
			world.addBlobListener(this);
			camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
			camera.update();
			graphics = new Graphics(batch, camera);
			
			transparentGrayPixelTexture = new Texture("transparentGrayPixel.png");
			selectedTexture = new Texture("selected.png");
			hoverTexture = new Texture("hover.png");
			
			{
				FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/source-code-pro/SourceCodePro-Light.otf"));
				FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
				fontParams.size = 14;
				fontParams.color = new Color(0, 0, 0, 1);
				uiFont = fontGenerator.generateFont(fontParams);
				
				fontParams = new FreeTypeFontParameter();
				fontParams.size = 9;
				fontParams.color = new Color(60/255f, 60/255f, 60/255f, 1);
				hoverFont = fontGenerator.generateFont(fontParams);
				
				fontGenerator.dispose();
			}
						
			kingdomInfoText = "Ducats: 500\nCitizens: 1000\nDead Citizens: 0";
			kingdomInfoTextLayout = new GlyphLayout(uiFont, kingdomInfoText);
			
			{
				FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/source-code-pro/SourceCodePro-Light.otf"));
				FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
				fontParams.size = 10;
				fontParams.color = new Color(0, 0, 0, 1);
				debugInfoFont = fontGenerator.generateFont(fontParams);
				fontGenerator.dispose();
			}

			debugInfo = new DebugInfo(batch);
			GLProfiler.enable();
			
			selectedFeatures = new HashSet<>();
			inputHandler = new UserInputHandler(camera, world.getBounds(), selectedFeatures, hoveredBlock);
			Gdx.input.setInputProcessor(inputHandler);
			
			App.scheduledExecutor.scheduleAtFixedRate(() -> {
				Gdx.app.postRunnable(() -> {
					debugInfoText = debugInfo.getInfo();
					debugInfoLayout = new GlyphLayout(debugInfoFont, debugInfoText);
				});
			}, 1, 1, TimeUnit.SECONDS);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			batch.dispose();
			graphics.dispose();
			
			Gdx.app.exit();
		}
	}

	@Override
	public void render() {
		try {
			inputHandler.update();
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			
			final float cameraLeft = camera.position.x - (camera.viewportWidth / 2f);
			final float cameraRight = camera.position.x + (camera.viewportWidth / 2f);
			final float cameraBottom = camera.position.y - (camera.viewportHeight / 2f);
			final float cameraTop = camera.position.y + (camera.viewportHeight / 2f);
			
			batch.totalRenderCalls = 0;
			batch.maxSpritesInBatch = 0;
			GLProfiler.reset();
			
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			/* Render the world. */
			batch.disableBlending();
			batch.begin();
			graphics.render(world);
			batch.end();
			
			/* Render selected features. */
			batch.enableBlending();
			batch.begin();
			for (GridPoint2 selectedCell : selectedFeatures) {
				int x = TerrainFeature.worldColumnToPixelX(selectedCell.x);
				int y = TerrainFeature.worldRowToPixelY(selectedCell.y);
				System.out.println("Drawing selected " + x + "," + y);
				batch.draw(selectedTexture, x, y, TerrainFeature.PIXELS_WIDTH, TerrainFeature.PIXELS_HEIGHT);
			}
			batch.end();
			
			/* Render hovered block and text. */
			if (hoveredBlock.isSet() &&
					world.isTerrainWithinWorld(hoveredBlock.terrain.x, hoveredBlock.terrain.y) &&
					world.isTerrainFeatureWithinWorld(hoveredBlock.terrainFeature.x, hoveredBlock.terrainFeature.y))
			{
				byte terrainBlock = world.getTerrain()[hoveredBlock.terrain.x][hoveredBlock.terrain.y];
				double blobResistance = ((1 - Terrain.getBlobTakeoverChance(terrainBlock)) * 100);
				hoverText = Terrain.toString(terrainBlock) + (terrainBlock != Terrain.TYPE_BLOB ? ("\n" + blobResistance + "% resistance") : "");
				batch.begin();
				batch.enableBlending();
				hoverFont.draw(batch, hoverText, cameraLeft + Gdx.input.getX() - 10, cameraTop - Gdx.input.getY() - 30);
				batch.end();
				
				batch.begin();
				int x = TerrainFeature.worldColumnToPixelX(hoveredBlock.terrainFeature.x);
				int y = TerrainFeature.worldRowToPixelY(hoveredBlock.terrainFeature.y);
				batch.draw(hoverTexture, x, y, TerrainFeature.PIXELS_WIDTH, TerrainFeature.PIXELS_HEIGHT);
				batch.end();
			}
			
			/* Draw debug text. */
			if (debugInfo != null && debugInfoLayout != null) {
				batch.begin();
				batch.enableBlending();
				batch.draw(transparentGrayPixelTexture, cameraRight - debugInfoLayout.width - 4, cameraBottom, debugInfoLayout.width + 4, debugInfoLayout.height + 8);
				debugInfoFont.draw(batch, debugInfoText, cameraRight - debugInfoLayout.width, cameraBottom + debugInfoLayout.height + 4);
				batch.end();
			}
			
			/* Draw UI text: blob size. */
			if (blobSizeTextLayout != null) {
				batch.begin();
				batch.enableBlending();
				uiFont.draw(batch, blobSizeText, cameraRight - blobSizeTextLayout.width - 8, cameraTop - 10);
				batch.end();
			}
			
			/* Draw UI text: citizens. */
			{
				batch.begin();
				batch.enableBlending();
				uiFont.draw(batch, kingdomInfoText, cameraLeft + 8, cameraTop - 10);
				batch.end();
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			batch.dispose();
			graphics.dispose();
			
			Gdx.app.exit();
		}
	}
	
	@Override
	public void dispose()
	{
		App.scheduledExecutor.shutdownNow();
	}

	@Override
	public void onBlobExpanded(double amount)
	{
		blobSizeText = "Blob control: " + DecimalFormat.getPercentInstance().format(amount);
		blobSizeTextLayout = new GlyphLayout(uiFont, blobSizeText);
	}
}
