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

    public void restartXAccel() {
        this.xAccel = 0;
    }

    public void restartYAccel() {
        this.yAccel = 0;
    }

    public float getZAccel() {
        return zAccel;
    }

    public void sum(AccelerometerTranslation translation, float maxHeight, float minHeight, float maxWidth, float minWidth) {
        if(Math.abs(this.getXAccel()) < maxWidth) {
            this.xAccel += translation.getXAccel() / 5;
        }
        if(this.getXAccel() <= minWidth && translation.getXAccel() >= 0) {
            this.xAccel += translation.getXAccel() / 5;
        }
        if(this.getXAccel() >= maxWidth && translation.getXAccel() <= 0) {
            this.xAccel += translation.getXAccel() / 5;
        }

        if(Math.abs(this.getYAccel()) < maxHeight) {
            this.yAccel += translation.getYAccel() / 5;
        }
        if(this.getYAccel() <= minHeight && translation.getYAccel() >= 0) {
            this.yAccel += translation.getYAccel() / 5;
        }
        if(this.getYAccel() >= maxHeight && translation.getYAccel() <= 0) {
            this.yAccel += translation.getYAccel() / 5;
        }
    }
}
