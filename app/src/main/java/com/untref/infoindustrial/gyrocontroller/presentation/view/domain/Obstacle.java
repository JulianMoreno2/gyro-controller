package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import javax.microedition.khronos.opengles.GL10;

public class Obstacle {

    private final Cube cube;
    private final float x;
    private final float y;
    private final float size;

    public Obstacle(float size) {
        this.size = size;
        this.cube = new Cube(size);
        this.x = 0;
        this.y = 5f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(GL10 gl, float depth) {
        gl.glLoadIdentity();
        gl.glTranslatef(this.x, this.y, depth);
        this.cube.draw(gl);
    }

    public boolean intersect(float previousX, float previousY, float x, float y) {
        return ((previousX + (x / 5) >= x - size) && (x + size >= previousX - (x / 5))) ||
                ((previousY + (y / 5) >= y - size) && (y + size >= previousY - (y / 5)));
    }
}
