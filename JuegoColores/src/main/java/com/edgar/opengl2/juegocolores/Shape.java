package com.edgar.opengl2.juegocolores;

/**
 * Created by edgar on 12/28/13.
 */
public interface Shape {

    public float getRightPoint();
    public float getTopPoint();
    public float getBotPoint();
    public float getLeftPoint();
    public void draw(float[] mvpMatrix);
}
