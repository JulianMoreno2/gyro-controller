package com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope;

public class GyroscopeRotation {

    private final float xGyro;
    private final float yGyro;
    private final float zGyro;
    private final float wGyro;

    public GyroscopeRotation(float xGyro, float yGyro, float zGyro, float wGyro) {
        this.xGyro = xGyro;
        this.yGyro = yGyro;
        this.zGyro = zGyro;
        this.wGyro = wGyro;
    }

    public float getXGyro() {
        return xGyro;
    }

    public float getYGyro() {
        return yGyro;
    }

    public float getZGyro() {
        return zGyro;
    }

    public float getWGyro() {
        return wGyro;
    }

    @Override
    public String toString() {
        return "GyroscopeRotation{ xGyro=" + xGyro + ", yGyro=" + yGyro + ", zGyro=" + zGyro + ", wGyro=" + wGyro + " }";
    }
}
