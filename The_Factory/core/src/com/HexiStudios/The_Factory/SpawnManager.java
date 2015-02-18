package com.HexiStudios.The_Factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;



public class SpawnManager {
	private MainState mainstate;
	private long timeBetweenProducts = 1000, lastSpawnTime;
	private Texture productTex = new Texture(Gdx.files.internal("product.png"));
	
	public SpawnManager(MainState mainstate)
	{
		this.mainstate = mainstate;
	}
	
	public void Update()
	{
		if(TimeUtils.nanoTime() - lastSpawnTime > timeBetweenProducts) spawnProduct();
		
		//Reset panic timer.
		if (mainstate.getPanicCounter() > 100)
		{
			mainstate.setPanicCounter(0);
		}
	}
		
	private void spawnProduct() {
		mainstate.getProducts().add(new Product(productTex, 604)); 