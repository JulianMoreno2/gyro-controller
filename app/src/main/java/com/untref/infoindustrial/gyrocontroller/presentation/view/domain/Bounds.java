package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;


public class Bounds {

    private float maxHeight;
    private float minHeight;
    private float maxWidth;
    private float minWidth;

    public Bounds(float maxHeight, float minHeight, float maxWidth, float minWidth) {

        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public float getMinWidth() {
        return minWidth;
    }
}
