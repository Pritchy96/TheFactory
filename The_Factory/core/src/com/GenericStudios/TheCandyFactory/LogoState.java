package com.GenericStudios.TheCandyFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LogoState extends BasicState {

	private SpriteBatch batch;
	private long fadeTime = 700, waitTime = 250, startTime;
	private Texture logo; 

	public LogoState(Manager manager)  {
		super(manager);	      

		batch = manager.getBatch();
		logo  = new Texture(Gdx.files.internal("logo.png"));
		startTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
	}

	@Override
	public void draw() {
		long currentTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - startTime;

		//Time into current fade or wait.
		float timeIntoPhase = 0;

		//Fade in
		if (currentTime < fadeTime)
		{
			timeIntoPhase = currentTime;
			float alpha = (timeIntoPhase / fadeTime);
			batch.setColor(1.0f, 1.0f, 1.0f, alpha);
			batch.draw(logo, 0, 0);

		}
		//Wait
		else if (currentTime < (fadeTime + waitTime))
		{
			timeIntoPhase = currentTime - fadeTime;
			batch.draw(logo, 0, 0);
		}
		//Fade out
		else if (currentTime < ((2*fadeTime) + waitTime))
		{
			timeIntoPhase = currentTime - (fadeTime + waitTime);
			float alpha = 1-(timeIntoPhase / fadeTime);
			batch.setColor(1.0f, 1.0f, 1.0f, alpha);
			batch.draw(logo, 0, 0);

		}
		//Change State
		else
		{
			batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			manager.changeState(new MenuState(manager));
		}
		
		super.draw();
	}

	@Override
	public void drawGUI() {	

		super.drawGUI();
	}   

	@Override
	public void update() {
		super.update();
	}

	public void touchDown(int screenX, int screenY, int pointer, int button) {
		// process user input
		Vector3 touchPos = new Vector3();
		touchPos.set(screenX, screenY, 0);
		manager.getCamera().unproject(touchPos);
		Vector2 point = new Vector2(touchPos.x, touchPos.y);

		//manager.changeState(new MenuState(manager));
		super.touchDown(screenX, screenY, pointer, button);
	}

	public void keyDown(int keycode) {

		//LeftUp
		if (keycode == com.badlogic.gdx.Input.Keys.W)
		{
		}

		super.keyDown(keycode);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}