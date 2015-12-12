package com.christopherdcanfield;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameApp extends ApplicationAdapter {
	private World world;
	private Graphics graphics;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	public GameApp()
	{
	}

	@Override
	public void create () {
		try {
			batch = new SpriteBatch();
	
			world = new World();
			camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
			camera.update();
			graphics = new Graphics(batch, camera);
			
			Gdx.input.setInputProcessor(new UserInputHandler(camera));
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
	public void render () {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		graphics.render(world);
		batch.end();
	}
}
