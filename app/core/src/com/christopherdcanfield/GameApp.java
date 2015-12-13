package com.christopherdcanfield;

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


public class GameApp extends ApplicationAdapter {
	private World world;
	private Graphics graphics;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private UserInputHandler inputHandler;
	private Set<GridPoint2> selectedFeatures;
	
	private Texture transparentGrayPixelTexture;
	private Texture selectedTexture;

	private BitmapFont debugInfoFont;
	private String debugInfoText;
	private GlyphLayout debugInfoLayout;
	private DebugInfo debugInfo;
	
	public GameApp()
	{
	}

	@Override
	public void create() {
		try {
			batch = new SpriteBatch(5000);
	
			world = new World();
			camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
			camera.update();
			graphics = new Graphics(batch, camera);
			
			transparentGrayPixelTexture = new Texture("transparentGrayPixel.png");
			selectedTexture = new Texture("selected.png");
			
			FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/source-code-pro/SourceCodePro-Light.otf"));
			FreeTypeFontParameter fontParams = new FreeTypeFontParameter();
			fontParams.size = 10;
			fontParams.color = new Color(0, 0, 0, 1);
			debugInfoFont = fontGenerator.generateFont(fontParams);
			fontGenerator.dispose();

			debugInfo = new DebugInfo(batch);
			GLProfiler.enable();
			
			selectedFeatures = new HashSet<>();
			inputHandler = new UserInputHandler(camera, world.getBounds(), selectedFeatures);
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
				batch.draw(selectedTexture, x, y, TerrainFeature.PIXELS_WIDTH, TerrainFeature.PIXELS_WIDTH);
			}
			batch.end();
			
			
			/* Draw text. */
			if (debugInfo != null && debugInfoLayout != null) {
				float right = camera.position.x + (camera.viewportWidth / 2f);
				float bottom = camera.position.y - (camera.viewportHeight / 2f);
				
				batch.begin();
				batch.enableBlending();
				batch.draw(transparentGrayPixelTexture, right - debugInfoLayout.width - 4, bottom, debugInfoLayout.width + 4, debugInfoLayout.height + 8);
				debugInfoFont.draw(batch, debugInfoText, right - debugInfoLayout.width, bottom + debugInfoLayout.height + 4);
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
}
