package com.HexiStudios.The_Factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Product {
    public int level;
    public Texture bitmap;
    public int vector = 1;
    public int xPosition;
    float timeLeft;
    float totalTimeLeft = 1f;

    public Product(Texture bitmap, int xPosition) {
        level = 0;
        this.bitmap = bitmap;
        this.xPosition = xPosition;
        timeLeft = totalTimeLeft;
    }
}
