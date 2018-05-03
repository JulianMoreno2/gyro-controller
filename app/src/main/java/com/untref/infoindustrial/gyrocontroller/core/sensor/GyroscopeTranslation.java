package com.untref.infoindustrial.gyrocontroller.core.sensor;

public class GyroscopeTranslation {

    private float x;
    private float y;
    private float z;

    public GyroscopeTranslation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
