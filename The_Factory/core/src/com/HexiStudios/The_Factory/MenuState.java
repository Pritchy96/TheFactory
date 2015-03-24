package com.HexiStudios.The_Factory;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends BasicState {

	private SpriteBatch batch;
	Texture buttons, debugRect;	//DEBUG
	private Rectangle playRect, howRect, optionsRect, creditsRect;

	public MenuState(Manager manager)  {
		super(manager);	      
		
		batch = manager.getBatch();
		
		buttons  = new Texture(Gdx.files.internal("menuButtons.png"));
		debugRect  = new Texture(Gdx.files.internal("debugRect.png"));	//DEBUG
		
		//Rectangles are awkward: They start at bottom left.
		playRect = new Rectangle(162, manager.getHeight() - (270 + 128), 366, 128);
		howRect = new Rectangle(162, manager.getHeight() - (454 + 128), 366, 128);
		optionsRect = new Rectangle(162, manager.getHeight() - (638 + 128), 366, 128);
		creditsRect = new Rectangle(162, manager.getHeight() - (822 + 128), 612, 128);
	}

	@Override
	public void draw() {
		batch.draw(buttons, 0, 0);
		
		//DEBUG
		//batch.draw(debugRect, playRect.x, playRect.y, playRect.width, playRect.height);
		super.draw();
	}

	@Override
	public void drawGUI() {
		manager.getFont().setScale(4);
		//manager.getFont().draw(batch, "Tap to play!", 500, 700);
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
		
		if (playRect.contains(point))
		{
			manager.changeState(new MainState(manager));
		}
		else if(howRect.contains(point))
		{
			manager.changeState(new HowToPlayState(manager));
		}
		else if(optionsRect.contains(point))
		{
			manager.changeState(new OptionsState(manager));
		}
		else if(creditsRect.contains(point))
		{
			manager.changeState(new AboutState(manager));
		}
		
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