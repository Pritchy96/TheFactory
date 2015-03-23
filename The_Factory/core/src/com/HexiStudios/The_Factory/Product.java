package com.HexiStudios.The_Factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Product {
    public int level, fill;
    public Texture emptyTex, halfTex, fullTex;
    public int vector = 1;
    public int xPosition;
    float timeLeft;
    float totalTimeLeft = 0.6f;

    public Product(int xPosition) {
        level = 0;
        fill = 0;
        this.emptyTex = new Texture(Gdx.files.internal("productEmptyTex.png"));
        this.halfTex = new Texture(Gdx.files.internal("productHalfTex.png"));
        this.fullTex = new Texture(Gdx.files.internal("productFullTex.png"));
        this.xPosition = xPosition;
        timeLeft = totalTimeLeft;
    }
    
    
    public void Draw (SpriteBatch batch, MainState mainState)
    {
    	if (fill < 3)
    	{
    		batch.draw(emptyTex, xPosition, ((mainState.getBottomOffset() + (mainState.getPlatformGap() * level))));
    	}
    	else if (fill <  5)
    	{
    		batch.draw(halfTex, xPosition, ((mainState.getBottomOffset() + (mainState.getPlatformGap() * level))));
    	}
    	else
    	{
    		batch.draw(fullTex, xPosition, ((mainState.getBottomOffset() + (mainState.getPlatformGap() * level))));
    	}
    }
}
