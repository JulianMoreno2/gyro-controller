package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by INSPIRON on 14/6/2018.
 */

public class Obstacle {

    private final Cube cube;
    private final float x;
    private final float y;

    public Obstacle(float size, float maxWidth, float minWidth, float maxHeight, float minHeight) {
        this.cube = new Cube(size);
        this.x = createInitialX(maxWidth, minWidth, maxHeight, minHeight);
        this.y = createInitialY(maxWidth, minWidth, maxHeight, minHeight);
    }

    private float createInitialX(float maxWidth, float minWidth, float maxHeight, float minHeight) {
        return 10f;
    }

    private float createInitialY(float maxWidth, float minWidth, float maxHeight, float minHeight) {
        return 10f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(GL10 gl, float depth) {
        gl.glTranslatef(this.x, this.y, depth);
        this.cube.draw(gl);
    }
}
