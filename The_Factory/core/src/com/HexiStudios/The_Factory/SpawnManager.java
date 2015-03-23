package com.HexiStudios.The_Factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class SpawnManager {
	private MainState mainstate;
	private float timeBetweenProducts = 10000, lastSpawnTime = 3000, longestSpawn = 6000, shortestSpawn  = 500;

	public SpawnManager(MainState mainstate)
	{
		this.mainstate = mainstate;
	}
   
	public void Update()
	{
		if(TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - lastSpawnTime > timeBetweenProducts)
		{
			mainstate.getProducts().add(new Product(322)); 
			lastSpawnTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
			
			float longestSpawnPanicked = longestSpawn - (mainstate.getPanicCounter() * 20);
			float range = longestSpawn - shortestSpawn;
			float modifier = ((long)mainstate.getPasses()) / 1000;
			float delta = longestSpawn - (range * modifier);
			
			
			float mode = delta + shortestSpawn;
			timeBetweenProducts = MathUtils.randomTriangular(shortestSpawn, longestSpawn, mode);
		}
		//MathUtils.randomTriangular(min, max, mode);
		
		//Reset panic timer.
		if (mainstate.getPanicCounter() > 100)
		{  
			mainstate.setPanicCounter(0);
		}
	}
}