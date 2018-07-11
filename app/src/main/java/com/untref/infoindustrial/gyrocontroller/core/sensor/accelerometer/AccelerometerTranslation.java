package com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer;


import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.Bounds;

import io.reactivex.functions.Action;

public class AccelerometerTranslation {

    private static final float EPSILON = 0.3f;
    private static final float ALPHA = 2.5f;
    private static final float MAX_DEPTH = -2f;
    private static final float MIN_DEPTH = -6f;

    private float xAccel;
    private float yAccel;
    private float zAccel;
    private Action vibrate;

    public AccelerometerTranslation(float xAccel, float yAccel, float zAccel) {
        this.xAccel = xAccel;
        this.yAccel = yAccel;
        this.zAccel = zAccel;
    }

    public void setVibrateAction(Action vibrate) {
        this.vibrate = vibrate;
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

    public void sum(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        if (!hasIntersect(translation)) {
            moveX(translation, previousTranslation, bounds);
            moveY(translation, previousTranslation, bounds);
            moveZ(translation, previousTranslation);
        } else {
            try {
                if (vibrate != null) vibrate.run();
            } catch (Exception e) {
                Log.e("DEVICE", "Cannot vibrate");
            }
        }
    }

    private boolean hasIntersect(AccelerometerTranslation translation) {

        float x = 0f;
        float y = 5f;
        float size = 0.5f;

        boolean bool = ((this.xAccel + (translation.getXAccel() / 5) >= x - size) && (x + size >= this.xAccel - (x / 5))) ||
                ((this.yAccel + (translation.getYAccel() / 5) >= y - size) && (y + size >= this.yAccel - (y / 5)));

        Log.d("DEVICE", "Has Intersect: " + bool);

        return bool;
    }

    private void moveX(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        if (Math.abs(previousTranslation.getXAccel() - translation.getXAccel()) > EPSILON) {
            if (Math.abs(this.getXAccel()) < bounds.getMaxWidth()) {
                this.xAccel += translation.getXAccel() / 5;
            }
            if (this.getXAccel() <= bounds.getMinWidth() && translation.getXAccel() >= 0) {
                this.xAccel += translation.getXAccel() / 5;
            }
            if (this.getXAccel() >= bounds.getMaxWidth() && translation.getXAccel() <= 0) {
                this.xAccel += translation.getXAccel() / 5;
            }
        }
    }

    private void moveY(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation, Bounds bounds) {
        if (Math.abs(previousTranslation.getYAccel() - translation.getYAccel()) > EPSILON) {
            if (Math.abs(this.getYAccel()) < bounds.getMaxHeight()) {
                this.yAccel += translation.getYAccel() / 5;
            }
            if (this.getYAccel() <= bounds.getMinHeight() && translation.getYAccel() >= 0) {
                this.yAccel += translation.getYAccel() / 5;
            }
            if (this.getYAccel() >= bounds.getMaxHeight() && translation.getYAccel() <= 0) {
                this.yAccel += translation.getYAccel() / 5;
            }
        }
    }

    private void moveZ(AccelerometerTranslation translation, AccelerometerTranslation previousTranslation) {
        float actualZAccel = -translation.getZAccel();

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
            if (this.zAccel >= MAX_DEPTH) {
                this.zAccel = MAX_DEPTH;
            }
            if (this.zAccel <= MIN_DEPTH) {
                this.zAccel = MIN_DEPTH;
            }
        }
    }
}
