package com.HexiStudios.The_Factory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Manager extends ApplicationAdapter {
	
	   private SpriteBatch batch;
	   private OrthographicCamera camera;
	   public Input inputProcessor;
	   private BitmapFont font;
	   private BasicState currentState;


	@Override
	   public void create() {
		   inputProcessor= new Input(this);
		   Gdx.input.setInputProcessor(inputProcessor);
		   
		   setFont(new BitmapFont());

	      // create the camera and the SpriteBatch
	      setCamera(new OrthographicCamera());
	      camera.setToOrtho(false, 1280, 1920);
	      setBatch(new SpriteBatch());
	      
	      currentState = new MenuState(this);
	   }

	   @Override
	   public void render() {
		  // clear the screen with Black.
		  Gdx.gl.glClearColor(0, 0, 0f, 1);
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		      
	      //Tell the camera to update its matrices.
	      camera.update();
	      currentState.update();
	      

	      //Tell the SpriteBatch to render in the
	      //coordinate system specified by the camera, for game drawing.
	      batch.setProjectionMatrix(getCamera().combined);
	      //Game Drawing
	      batch.begin();
	      currentState.draw();
	      batch.end();
	      
	      //GUI Drawing
	      batch.begin();
	      currentState.drawGUI();
	      batch.end();
	   }
	   
	   public void touchDown(int screenX, int screenY, int pointer, int button) {
		   currentState.touchDown(screenX, screenY, pointer, button);
		}
		
	   public void keyDown(int keycode) {
		   currentState.keyDown(keycode);
	   }
			
	   @Override
	   public void dispose() {
		  currentState.dispose();
	      getBatch().dispose();
	   }

	   @Override
	   public void resize(int width, int height) {
		   currentState.resize(width, height);
	   }

	   @Override
	   public void pause() {
		   currentState.pause();
	   }

	   @Override
	   public void resume() {
		   currentState.resume();
	   }
	   
	   public void changeState(BasicState state)
	   {
		   this.currentState = state; 
	   }
	   
	   /**
		 * @return the batch
		 */
		public SpriteBatch getBatch() {
			return batch;
		}

		/**
		 * @param batch the batch to set
		 */
		public void setBatch(SpriteBatch batch) {
			this.batch = batch;
		}

	/**
		 * @return the font
		 */
		public BitmapFont getFont() {
			return font;
		}

		/**
		 * @param font the font to set
		 */
		public void setFont(BitmapFont font) {
			this.font = font;
		}

	/**
		 * @return the camera
		 */
		public OrthographicCamera getCamera() {
			return camera;
		}

		/**
		 * @param camera the camera to set
		 */
		public void setCamera(OrthographicCamera camera) {
			this.camera = camera;
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