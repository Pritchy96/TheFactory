package com.HexiStudios.The_Factory;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class MainState extends BasicState {
	
	   private Texture leftPlayer,  rightPlayer, background, product;
	   private SpriteBatch batch;
	   private int leftLevel = 1, rightLevel = 0, numberOfLevels = 5, score = 0, lives = 3;
	   private long timeBetweenProducts = 1000, lastSpawnTime;
	   //Default value for Right (Changed in Constructor)
	   private float leftX = 0, rightX = 200, leftEdge = 320, rightEdge = 956, platformGap = 297, bottomOffset = 241;	   
	   private ArrayList<Product> products = new ArrayList<Product>();	   
	   
	   public MainState(Manager manager)  {
		   super(manager);
		   
	      //Load the images.
		  leftPlayer = new Texture(Gdx.files.internal("droid_left.png"));
		  rightPlayer = new Texture(Gdx.files.internal("droid_right.png"));
		  product = new Texture(Gdx.files.internal("product.png"));
		  background = new Texture(Gdx.files.internal("background.png"));		   	      

	      rightX = 1280 - rightPlayer.getWidth();
	      
	      batch = manager.getBatch();
	   }
	   
	   @Override
	   public void draw() {
		      batch.draw(background, 0, 0);
		      batch.draw(leftPlayer, leftX, bottomOffset + (leftLevel * platformGap));
		      batch.draw(rightPlayer, rightX, bottomOffset + (rightLevel * platformGap));
		      
		      Iterator<Product> iter = products.iterator();
		      while(iter.hasNext()) {
		         Product p = iter.next();
		         batch.draw(p.bitmap, p.xPosition, ((bottomOffset + (platformGap * p.level))));
		      }
		      super.draw();
	   }
	   
	   @Override
	   public void drawGUI() {
		      manager.getFont().setScale(4);
		      manager.getFont().draw(batch, "Score: " + Integer.toString(score), background.getWidth() - 330, background.getHeight()- 20);
		      manager.getFont().draw(batch, "Lives: " + Integer.toString(lives), background.getWidth() - 330, background.getHeight() - 70);
		      super.drawGUI();
	   }   
	   
	   @Override
	   public void update() {
		// check if we need to create a new product
		      if(TimeUtils.nanoTime() - lastSpawnTime > timeBetweenProducts) spawnProduct();

		      Iterator<Product> iter = products.iterator();
		      while(iter.hasNext()) {
		         Product p = iter.next();
		         
		       //Check its not at end
	                if (p.vector < 0 && p.xPosition < leftEdge - p.bitmap.getWidth()/2)
	                {
	                    if (leftLevel == p.level)
	                    {
	                        if (p.level < numberOfLevels) {
	                            p.level++;
	                            score++;
	                            p.vector = -p.vector;
	                            p.timeLeft = p.totalTimeLeft;
	                        }
	                        else
	                        {
	                            score+=10;
	                            p.level++;
	                            p.timeLeft = p.totalTimeLeft;
	                            iter.remove();
	                        }
	                    }
	                    else
	                    {
	                    	if (p.timeLeft > 0)
	                    	{
	                    		p.timeLeft -= Gdx.graphics.getDeltaTime();
	                    		
	                    	}
	                    	else
	                    	{
	                    		iter.remove();
	                    		lives--;
	                    	} 	
	                    }
	                }
	                else if (p.vector > 0 && p.xPosition > rightEdge - p.bitmap.getWidth()/2)
	                {
	                    if (rightLevel == p.level)
	                    {
	                        p.level++;
	                        score++;
	                        p.vector = -p.vector;
	                        p.timeLeft = p.totalTimeLeft;
	                    }
	                    else
	                    {
	                    	if (p.timeLeft > 0)
	                    	{
	                    		p.timeLeft -= Gdx.graphics.getDeltaTime();
	                    		
	                    	}
	                    	else
	                    	{
	                    		iter.remove();
	                    		lives--;
	                    	} 	
	                    }
	                }
	                 else
	                {
	                    p.xPosition += p.vector * 6 * (1/(Gdx.graphics.getDeltaTime()*40));
	                }
		      }
		      
		      //Reset game if lives < 1.
	  		if (lives < 1)
	  		{
	  			lives = 3;
	  			products.clear();
	  			score = 0;
	  			rightLevel = 0;
	  			leftLevel = 1;
	  		}
	  		
	  		super.update();
	   }
	   
	   private void spawnProduct() {
		      products.add(new Product(product, 604));
		      lastSpawnTime = TimeUtils.nanoTime();
		      timeBetweenProducts = TimeUtils.millisToNanos((MathUtils.random(1, 7)*1000));
		   }
		
	   public void touchDown(int screenX, int screenY, int pointer, int button) {
		      // process user input
		         Vector3 touchPos = new Vector3();
		         touchPos.set(screenX, screenY, 0);
		         manager.getCamera().unproject(touchPos);
		         
		         if (touchPos.x > 1280/2)
		         {
		             if (touchPos.y < 1920/2)
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
		             if (touchPos.y < 1920/2)
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
		
	   public void keyDown(int keycode) {
		   
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
			
	   @Override
	   public void dispose() {
	      batch.dispose();
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