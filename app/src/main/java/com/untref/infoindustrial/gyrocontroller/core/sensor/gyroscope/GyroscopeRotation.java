package com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope;

public class GyroscopeRotation {

    private final float x;
    private final float y;
    private final float z;
    private final float w;

    public GyroscopeRotation(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public float getW() {
        return w;
    }

    @Override
    public String toString() {
        return "GyroscopeRotation{ x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + " }";
    }
}
