package com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer;


public class AccelerometerTranslation {

    private static final float EPSILON = 0.3f;
    private static final float ALPHA = 2.5f;
    private static final float MAX_DEPTH = -2f;
    private static final float MIN_DEPTH = -6f;

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

    public void restartZAccel() {
        this.zAccel = 0;
    }

    public float getZAccel() {
        return zAccel;
    }

    public void sum(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, float maxHeight, float minHeight, float maxWidth, float minWidth) {

        if (Math.abs(previousTranslation.getXAccel() - translation.getXAccel()) > EPSILON) {
            if (Math.abs(this.getXAccel()) < maxWidth) {
                this.xAccel += translation.getXAccel() / 5;
            }
            if (this.getXAccel() <= minWidth && translation.getXAccel() >= 0) {
                this.xAccel += translation.getXAccel() / 5;
            }
            if (this.getXAccel() >= maxWidth && translation.getXAccel() <= 0) {
                this.xAccel += translation.getXAccel() / 5;
            }
        }

        if (Math.abs(previousTranslation.getYAccel() - translation.getYAccel()) > EPSILON) {
            if (Math.abs(this.getYAccel()) < maxHeight) {
                this.yAccel += translation.getYAccel() / 5;
            }
            if (this.getYAccel() <= minHeight && translation.getYAccel() >= 0) {
                this.yAccel += translation.getYAccel() / 5;
            }
            if (this.getYAccel() >= maxHeight && translation.getYAccel() <= 0) {
                this.yAccel += translation.getYAccel() / 5;
            }
        }


        float actualZAccel = -translation.getZAccel();;

        if (Math.abs(previousTranslation.getZAccel() - actualZAccel) > ALPHA) {
            if (this.getZAccel() < MAX_DEPTH && this.getZAccel() > MIN_DEPTH) {
                this.zAccel += actualZAccel / 5;
            }
            if (this.getZAccel() <= MIN_DEPTH && actualZAccel >= 0) {
                this.zAccel += actualZAccel / 5;
            }
            if (this.getZAccel() >= MAX_DEPTH && actualZAccel <= 0) {
                this.zAccel += actualZAccel / 5;
            }
            if(this.zAccel >= MAX_DEPTH) {
                this.zAccel = MAX_DEPTH;
            }
            if (this.zAccel <= MIN_DEPTH) {
                this.zAccel = MIN_DEPTH;
            }
        }

    }
}
