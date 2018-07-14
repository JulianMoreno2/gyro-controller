package com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer;


import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.Bounds;

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

    public void reverse(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        reverseX(translation, previousTranslation, bounds);
        reverseY(translation, previousTranslation, bounds);
        reverseZ(translation, previousTranslation);
    }

    private void reverseX(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        this.xAccel -= translation.getXAccel() * 100;
        Log.d(": ", "X: " + this.xAccel);
    }

    private void reverseY(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        this.yAccel -= translation.getYAccel() * 100;
        Log.d(": ", "Y: " + this.yAccel);
    }

    private void reverseZ(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation) {
        float actualZAccel = -translation.getZAccel();
        this.zAccel -= actualZAccel * 100;
        Log.d(": ", "Z: " + this.zAccel);
    }

    public void sum(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        moveX(translation, previousTranslation, bounds);
        moveY(translation, previousTranslation, bounds);
        moveZ(translation, previousTranslation);
    }

    private void moveX(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        this.xAccel += translation.getXAccel() * 50;
        Log.d(": ", "X: " + this.xAccel);
    }

    private void moveY(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        this.yAccel += translation.getYAccel() * 50;
        Log.d(": ", "Y: " + this.yAccel);
    }

    private void moveZ(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation) {
        float actualZAccel = -translation.getZAccel();
        this.zAccel += actualZAccel * 50;
        Log.d(": ", "Z: " + this.zAccel);
    }
}
