package com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer;

public class AccelerometerTranslation {

    private float xAccel;
    private float yAccel;
    private float zAccel;

    public AccelerometerTranslation(float xAccel, float yAccel, float zAccel) {
        this.xAccel = xAccel;
        this.yAccel = yAccel;
        this.zAccel = zAccel;
    }

    public float getXAccel() {
        return xAccel;
    }

    public float getYAccel() {
        return yAccel;
    }

    public float getZAccel() {
        return zAccel;
    }

    public void sum(AccelerometerTranslation translation) {
        this.xAccel += translation.getXAccel();
        this.yAccel += translation.getYAccel();
    }
}
