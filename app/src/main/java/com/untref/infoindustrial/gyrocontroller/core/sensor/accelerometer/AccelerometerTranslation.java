package com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer;

public class AccelerometerTranslation {

    private float x;
    private float y;
    private float z;

    public AccelerometerTranslation(float x, float y, float z) {
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

    public void sum(AccelerometerTranslation translation) {
        this.x += translation.getX();
        this.y += translation.getY();
    }
}
