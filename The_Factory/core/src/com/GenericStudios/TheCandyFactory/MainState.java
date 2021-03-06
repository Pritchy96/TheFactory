package com.GenericStudios.TheCandyFactory;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MainState extends BasicState {

	private Texture leftPlayerTex,  rightPlayerTex, backgroundF1Tex, backgroundF2Tex, candyDropTex, fallingProductTex;
	private SpriteBatch batch;
	private int leftLevel = 1, rightLevel = 0, numberOfLevels = 5, score = 0, highscore= 0, lives = 3, passes = 0, panicCounter = 0;
	//Default value for Right (Changed in Constructor)
	private float leftX = 0, rightX = 107, leftEdge = 150, rightEdge = 531, platformGap = 161, bottomOffset = 129, backgroundAnimTimer = 0.3f, pauseTimer = 0f, fallingAnimY = 0, fallingAnimX = 0;	   
	private ArrayList<Product> products = new ArrayList<Product>();	   
	private boolean backgroundAnimBool = false, sound = false, music= false;


	private SpawnManager spawnManager = new SpawnManager(this);
	private float[] candyDropTimers = new float[6];


	public MainState(Manager manager)  {
		super(manager);
		//Load the images.
		leftPlayerTex = new Texture(Gdx.files.internal("armLeft.png"));
		rightPlayerTex = new Texture(Gdx.files.internal("armRight.png"));
		backgroundF1Tex = new Texture(Gdx.files.internal("backgroundFrame1.png"));
		backgroundF2Tex = new Texture(Gdx.files.internal("backgroundFrame2.png"));	
		candyDropTex = new Texture(Gdx.files.internal("droppingCandy.png"));
		fallingProductTex = new Texture(Gdx.files.internal("productFalling.png"));
		rightX = 683 - rightPlayerTex.getWidth();    

		sound = manager.getPrefs().getBoolean("sound", true);
		music = manager.getPrefs().getBoolean("music", true);
		highscore = manager.getPrefs().getInteger("highscore", 0);
		batch = manager.getBatch();

		//Load add ready for game over screen
		manager.getActionResolver().LoadInterstital();

		manager.getCamera().setToOrtho(false, 1000, 1000);
		manager.getCamera().setToOrtho(false, 683, 1023);
	}

	@Override
	public void draw() {
		//batch.draw(backgroundF2Tex, 0, 0);

		batch.draw(backgroundF1Tex, 0, 0);

		if (!backgroundAnimBool)
		{
			batch.draw(backgroundF2Tex, 0, 0);
		}

		batch.draw(leftPlayerTex, leftX, bottomOffset + (leftLevel * platformGap));
		batch.draw(rightPlayerTex, rightX, bottomOffset + (rightLevel * platformGap));
		Iterator<Product> iter = products.iterator();
		while(iter.hasNext()) {
			Product p = iter.next();
			p.Draw(batch, this);
		}

		//Draw candy drop animations
		//Candy Droppers
		for (int i = 0; i < numberOfLevels; i++)
		{
			if (candyDropTimers[i] > 0)
			{
				batch.draw(candyDropTex, 333, 190 - 26 + (platformGap * i));
			}
		}
		
		if (pauseTimer > 0)
		{
			batch.draw(fallingProductTex, fallingAnimX, fallingAnimY);
		}

		super.draw();
	}

	@Override
	public void drawGUI() {
		manager.getFont().setScale(2);      
		manager.getFont().draw(batch, "Score: " + Integer.toString(score), 20, backgroundF1Tex.getHeight() - 6);

		if (score > highscore)
			manager.getFont().draw(batch, "High Score: " + Integer.toString(score), 20, backgroundF1Tex.getHeight() - 36);	
		else
			manager.getFont().draw(batch, "High Score: " + Integer.toString(highscore), 20, backgroundF1Tex.getHeight() - 36);	
		
		manager.getFont().draw(batch, "Lives: " + Integer.toString(lives), backgroundF1Tex.getWidth() - 115, backgroundF1Tex.getHeight() - 6);
		super.drawGUI();
	}

	@Override
	public void update() {

		if (pauseTimer <= 0)
		{
			//Spawn products if ready.
			spawnManager.Update();
	
			//Move products along, check they're not supposed to be falling.
			updateProducts();
	
			//Reduce frame timer and change frame if needed.
			animHandler();
			
			if (music != manager.getBgm().isPlaying())
			{
				manager.setMusic();
			}
			
			checkLives();
	
			//float pitch = (((float)score)  / 10000)*4;
			//manager.getBgm().setPitch(0, 0.7f + pitch);
			super.update();
		} 
		else
		{	
			manager.getBgm().stop();
			pauseTimer -= Gdx.graphics.getDeltaTime();
			fallingAnimY-=10;
		}
	}

	public void updateProducts()
	{
		Iterator<Product> iter = products.iterator();

		while(iter.hasNext()) {
			Product p = iter.next();

			//Check its not at end
			if (p.vector < 0 && p.xPosition < leftEdge - p.emptyTex.getWidth()/2)
			{
				if (leftLevel == p.level)
				{
					if (p.level < numberOfLevels) {
						p.level++;
						score++;
						passes++;
						panicCounter++;
						p.vector = -p.vector;
						p.timeLeft = p.totalTimeLeft;
						if (sound) {manager.getMoveUpSound().play();};

					}
					else	//At the top, being removed.
					{
						score+=2;
						p.level++;
						passes++;
						panicCounter++;
						p.timeLeft = p.totalTimeLeft;
						iter.remove();
						if (sound) {manager.getMoveUpSound().play();};
					}
				}
				else	//At an edge.
				{	
					if (p.timeLeft > 0)	//If not ready to fall
					{
						p.timeLeft -= Gdx.graphics.getDeltaTime();

					}
					else	//Fall, lose a life!
					{
						iter.remove();
						loseLife(p);
						break;
						
					} 	
				}
			}
			//Same as above but for the Right side.
			else if (p.vector > 0 && p.xPosition > rightEdge - p.emptyTex.getWidth()/2)
			{
				if (rightLevel == p.level)
				{
					p.level++;
					score++;
					passes++;
					panicCounter++;
					p.vector = -p.vector;
					p.timeLeft = p.totalTimeLeft;
					if (sound) {manager.getMoveUpSound().play();};
				}
				else
				{
					if (p.timeLeft > 0)	//Fall Timer
					{
						p.timeLeft -= Gdx.graphics.getDeltaTime();

					}
					else
					{
						iter.remove();
						loseLife(p);
						break;
					} 	
				}
			}
			else
			{
				//Move Product.
				p.xPosition += p.vector * 3 * (1/(Gdx.graphics.getDeltaTime()*40));
			}


			if (p.xPosition > 305 && p.xPosition < 340 && p.fill == p.level)
			{
				//Add 300ms to anim timer.
				candyDropTimers[p.level] += 0.075f;
				p.fill++;
			}
		}
	}
	
	public void loseLife(Product p)
	{
		//Makes products spawn slower again so player can start again.
		panicCounter = 0;
		//Pauses game to give player a break and see what fell.
		pauseTimer += 1f;
		//Setting up falling animation
		fallingAnimX = p.xPosition;
		fallingAnimY = (bottomOffset - p.halfTex.getHeight() + (platformGap * p.level));
		
		manager.getBgm().stop();
		
		if (sound)
		{
			manager.getFailSound().play();
		}
		//Destroys all products so player can start again.
		products.clear();
	
		lives--;
	}

	public void animHandler ()
	{
		//background.
		backgroundAnimTimer -= Gdx.graphics.getDeltaTime();
		if (backgroundAnimTimer <= 0)
		{
			backgroundAnimTimer = 0.2f;
			backgroundAnimBool = !backgroundAnimBool;
		}

		//Candy Droppers
		for (int i = 0; i <= numberOfLevels; i++)
		{
			if (candyDropTimers[i] > 0)
			{
				candyDropTimers[i] -= Gdx.graphics.getDeltaTime();
			}
		}
	}

	public void checkLives()
	{
		if (lives < 1)
		{
			if (score > highscore)
			{
				highscore = score;
				//Save Score
				manager.getPrefs().putInteger("highscore", highscore);
				//persist preferences
				manager.getPrefs().flush();
			}

			//Reset game.
			manager.changeState(new GameOverState(manager, score));
			/*
			lives = 3;
			products.clear();
			score = 0;
			manager.getBgm().stop();
			manager.getBgm().loop(1f, 0.7f, 0f);

			rightLevel = 0;
			leftLevel = 1;
			 */
		}
	}

	public void touchDown(int screenX, int screenY, int pointer, int button) {

		if (pauseTimer <= 0)
		{
			Vector3 touchPos = new Vector3();
			touchPos.set(screenX, screenY, 0);
			manager.getCamera().unproject(touchPos);
	
			if (touchPos.x > 683/2)
			{
				if (touchPos.y < 1024/2)
				{
					//Bottom Right tap.
					if (rightLevel > 0)
					{
						rightLevel-=2;
					}
				}
				else
				{
					//Top Right tap.
					if (rightLevel < numberOfLevels-1)
					{
						rightLevel+=2;
					}
				}
			}
			else
			{
				if (touchPos.y < 1024/2)
				{
	
					//Bottom Left tap.
					if (leftLevel > 1)
					{
						leftLevel-=2;
					}
				}
				else
				{
					//Top Left tap.
					if (leftLevel < numberOfLevels)
					{
						leftLevel+=2;
					}
				}
			}
			super.touchDown(screenX, screenY, pointer, button);
		}
	}

	public void keyDown(int keycode) {
		if (pauseTimer <= 0)
		{
			//LeftUp
			if (keycode == com.badlogic.gdx.Input.Keys.W)
			{
				if (leftLevel < numberOfLevels) {leftLevel+=2;}	
			}
			else if (keycode == com.badlogic.gdx.Input.Keys.S)
			{
				if (leftLevel > 1) {leftLevel-=2;}
			}
			else if (keycode == com.badlogic.gdx.Input.Keys.UP)
			{
				if (rightLevel < (numberOfLevels-1)) {rightLevel+=2;}
			}
			else if (keycode == com.badlogic.gdx.Input.Keys.DOWN)
			{
				if (rightLevel > 0) {rightLevel-=2;}
			}
	
			super.keyDown(keycode);
		}
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
		manager.getPrefs().putInteger("score", score);
		manager.getPrefs().putInteger("passes", passes);
		manager.getPrefs().putInteger("lives", lives);

		if (score > highscore)
		{
			manager.getPrefs().putInteger("highscore", score);
		}

		super.pause();

	}

	@Override

	public void resume() {
		super.resume();
		//if game has reset
		if (score == 0)
		{
			score = manager.getPrefs().getInteger("score", 0);
			passes = manager.getPrefs().getInteger("passes", score);
			lives = manager.getPrefs().getInteger("lives", 0);
		}

		//Load add ready for game over screen (might unload on pause?)
		manager.getActionResolver().LoadInterstital();
	}

	public int getPasses() {
		return passes;
	}

	public void setPasses(int passes) {
		this.passes = passes;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPanicCounter() {
		return panicCounter;
	}

	public void setPanicCounter(int panicCounter) {
		this.panicCounter = panicCounter;
	}

	public float getPlatformGap() {
		return platformGap;
	}

	public void setPlatformGap(float platformGap) {
		this.platformGap = platformGap;
	}

	public float getBottomOffset() {
		return bottomOffset;
	}

	public void setBottomOffset(float bottomOffset) {
		this.bottomOffset = bottomOffset;
	}
	
	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
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