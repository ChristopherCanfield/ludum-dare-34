package com.christopherdcanfield;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameApp extends ApplicationAdapter {
	private World world;
	private Graphics graphics;
	
	private SpriteBatch batch;
	
	public GameApp()
	{
	}

	@Override
	public void create () {
		try {
			batch = new SpriteBatch();
	
			Gdx.input.setInputProcessor(new UserInputHandler());
			
			world = new World();
			graphics = new Graphics(batch);
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		graphics.render(world);
		batch.end();
	}
}
