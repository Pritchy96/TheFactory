package com.GenericStudios.TheCandyFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class GameOverState extends BasicState {

	private SpriteBatch batch;
	private int highscore = manager.getPrefs().getInteger("highscore", 0), score = 0, textScale = 3;
	TextBounds line1Bounds, line2Bounds, line3Bounds;
	String line1, line2, line3;
	Rectangle submit, viewScores, cont;
	Texture buttons;
	//Separate font instances so the .getbound method works (libgtx is bugged)
	BitmapFont font1 = new BitmapFont(), font2 = new BitmapFont(), font3 = new BitmapFont();
	long startTime;

	public GameOverState(Manager manager, int score)  {
		super(manager);	

		//Show advertisement.
		manager.getActionResolver().ShowInterstital();
		
		batch = manager.getBatch();
		buttons  = new Texture(Gdx.files.internal("gameOverButtons.png"));

		//Set text.
		line1 = "Game over!";
		line2 = "Score: " + Integer.toString(score);
		line3 = ("High Score: " + highscore);
		

		this.highscore = manager.getPrefs().getInteger("highscore");
		this.score = score;

		font1.setScale(textScale);
		font2.setScale(textScale);
		font3.setScale(textScale);
		
		manager.getFont().setScale(textScale);

		line1Bounds = font1.getBounds(line1);
		line2Bounds = font2.getBounds(line2);
		line3Bounds = font3.getBounds(line3);
	
		startTime = TimeUtils.millis();
		
		submit = new Rectangle(162, manager.getHeight() - (454 + 128), 366, 128);
		cont = new Rectangle(162, manager.getHeight() - (581 + 128), 366, 128);
		viewScores = new Rectangle(162, manager.getHeight() - (765 + 128), 612, 128);
		
		manager.getActionResolver().submitScoreGPGS(score);;
		
	}

	@Override
	public void draw() {
		batch.draw(buttons, 0, 0);
		super.draw();
	}

	@Override
	public void drawGUI() {	
		//How far down the page to draw each line.
		//Is further offset for the second line so it is drawn under the first and so on.
		int lineOffsetY = manager.getHeight() - 280;		

		manager.getFont().draw(batch, line1, (manager.getWidth()/2) - (line1Bounds.width/2), lineOffsetY);	

		lineOffsetY -= (int) (manager.getFont().getLineHeight());
		manager.getFont().draw(batch, line2, (manager.getWidth()/2) - (line2Bounds.width/2), lineOffsetY);	

		lineOffsetY -= (int) (manager.getFont().getLineHeight());
		manager.getFont().draw(batch, line3, (manager.getWidth()/2) - (line3Bounds.width/2), lineOffsetY);	
		
		super.drawGUI();
	}   

	@Override
	public void update() {
		super.update();
	}

	public void touchDown(int screenX, int screenY, int pointer, int button) {
		if (TimeUtils.timeSinceMillis(startTime) > 2000)
		{
			// process user input
			Vector3 touchPos = new Vector3();
			touchPos.set(screenX, screenY, 0);
			manager.getCamera().unproject(touchPos);
			Vector2 point = new Vector2(touchPos.x, touchPos.y);
			
			if (cont.contains(point))
			{
				manager.changeState(new MenuState(manager));
			}
			/*
			else if(submit.contains(point))
			{
				manager.getActionResolver().submitScoreGPGS(score);
				manager.getActionResolver().getLeaderboardGPGS();
			}
			*/
			else if(viewScores.contains(point))
			{
				manager.getActionResolver().getLeaderboardGPGS();
			}

	
			
			//manager.changeState(new MenuState(manager));
			super.touchDown(screenX, screenY, pointer, button);
		}
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