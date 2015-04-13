package com.GenericStudios.TheCandyFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class OptionsState extends BasicState {

	private Manager manager;
	Texture background, buttons, scrollingBackground, musicOff, soundOff, debugRect;	//DEBUG
	private Rectangle musicRect, soundRect, backRect;
	boolean music, sound;

	public OptionsState(Manager manager)  {
		super(manager);	      
		this.manager = manager;

		//Load textures
		buttons  = new Texture(Gdx.files.internal("optionsButtons.png"));
		musicOff = new Texture(Gdx.files.internal("optionsMusicOffBtn.png"));
		soundOff  = new Texture(Gdx.files.internal("optionsSoundOffBtn.png"));
		//DEBUG
		debugRect  = new Texture(Gdx.files.internal("debugRect.png"));

		//Calculate button textures.
		musicRect = new Rectangle(162, manager.getHeight() - (326 + 128), 367, 128);
		soundRect = new Rectangle(162, manager.getHeight() - (530 + 128), 367, 128);
		backRect = new Rectangle(162, manager.getHeight() - (723 + 128), 612, 128);

		//Set booleans (more efficient than constantly retrieving from prefs)
		music = manager.getPrefs().getBoolean("music", true);
		sound = manager.getPrefs().getBoolean("sound", true);
	}

	@Override
	public void draw() {
		manager.getBatch().draw(buttons, 0, 0);

		if (!music)
		{
			manager.getBatch().draw(musicOff, musicRect.x, musicRect.y, musicRect.width, musicRect.height);
		}

		if (!sound)
		{
			manager.getBatch().draw(soundOff, soundRect.x, soundRect.y, soundRect.width, soundRect.height);
		}



		super.draw();
	}

	@Override 
	public void drawGUI() {
		manager.getFont().setScale(4);
		super.drawGUI();
	}   

	@Override
	public void update() {
		super.update();
	}

	public void touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 touchPos = new Vector3();
		touchPos.set(screenX, screenY, 0);
		manager.getCamera().unproject(touchPos);
		Vector2 point = new Vector2(touchPos.x, touchPos.y);

		if (musicRect.contains(point))
		{
			music = !music;
			manager.getPrefs().putBoolean("music", music);
			manager.getPrefs().flush();
			manager.setMusic();
		}
		else if(soundRect.contains(point))
		{
			sound = !sound;
			manager.getPrefs().putBoolean("sound", sound);
			manager.getPrefs().flush();
			
			if (sound)
			{
				//Play sound to let user know sound is on.
				manager.getMoveUpSound().play();
			}
		}
		else if(backRect.contains(point))
		{

			manager.changeState(new MenuState(manager));
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


/*
public class Drop implements ApplicationListener {
   private Texture dropImage;
   private Texture bucketImage;
   private Sound dropSound;
   private Music rainMusic;
   private SpriteBatch batch;
   private OrthographicCamera camera;
   private Rectangle bucket;
   private Array<Rectangle> raindrops;
   private long lastDropTime;

   @Override
   public void create() {
      // load the images for the droplet and the bucket, 64x64 pixels each
      dropImage = new Texture(Gdx.files.internal("droplet.png"));
      bucketImage = new Texture(Gdx.files.internal("bucket.png"));

      // load the drop sound effect and the rain background "music"
      dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
      rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

      // start the playback of the background music immediately
      rainMusic.setLooping(true);
      rainMusic.play();

      // create the camera and the SpriteBatch
      camera = new OrthographicCamera();
      camera.setToOrtho(false, 800, 480);
      batch = new SpriteBatch();

      // create a Rectangle to logically represent the bucket
      bucket = new Rectangle();
      bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
      bucket.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
      bucket.width = 64;
      bucket.height = 64;

      // create the raindrops array and spawn the first raindrop
      raindrops = new Array<Rectangle>();
      spawnRaindrop();
   }

   private void spawnRaindrop() {
      Rectangle raindrop = new Rectangle();
      raindrop.x = MathUtils.random(0, 800-64);
      raindrop.y = 480;
      raindrop.width = 64;
      raindrop.height = 64;
      raindrops.add(raindrop);
      lastDropTime = TimeUtils.nanoTime();
   }

   @Override
   public void render() {
      // clear the screen with a dark blue color. The
      // arguments to glClearColor are the red, green
      // blue and alpha component in the range [0,1]
      // of the color to be used to clear the screen.
      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      // tell the camera to update its matrices.
      camera.update();

      // tell the SpriteBatch to render in the
      // coordinate system specified by the camera.
      batch.setProjectionMatrix(camera.combined);

      // begin a new batch and draw the bucket and
      // all drops
      batch.begin();
      batch.draw(bucketImage, bucket.x, bucket.y);
      for(Rectangle raindrop: raindrops) {
         batch.draw(dropImage, raindrop.x, raindrop.y);
      }
      batch.end();

      // process user input
      if(Gdx.input.isTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);
         bucket.x = touchPos.x - 64 / 2;
      }
      if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
      if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

      // make sure the bucket stays within the screen bounds
      if(bucket.x < 0) bucket.x = 0;
      if(bucket.x > 800 - 64) bucket.x = 800 - 64;

      // check if we need to create a new raindrop
      if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

      // move the raindrops, remove any that are beneath the bottom edge of
      // the screen or that hit the bucket. In the later case we play back
      // a sound effect as well.
      Iterator<Rectangle> iter = raindrops.iterator();
      while(iter.hasNext()) {
         Rectangle raindrop = iter.next();
         raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
         if(raindrop.y + 64 < 0) iter.remove();
         if(raindrop.overlaps(bucket)) {
            dropSound.play();
            iter.remove();
         }
      }
   }

   @Override
   public void dispose() {
      // dispose of all the native resources
      dropImage.dispose();
      bucketImage.dispose();
      dropSound.dispose();
      rainMusic.dispose();
      batch.dispose();
   }

   @Override
   public void resize(int width, int height) {
   }

   @Override
   public void pause() {
   }

   @Override
   public void resume() {
   }
}
 */