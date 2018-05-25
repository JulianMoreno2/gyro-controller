package com.untref.infoindustrial.gyrocontroller.core.action;

import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerCompass;

import io.reactivex.Completable;

public class StartAccelerometer {

    private AccelerometerCompass accelerometerCompass;

    public StartAccelerometer(AccelerometerCompass accelerometerCompass) {
        this.accelerometerCompass = accelerometerCompass;
    }

    public Completable execute() {
        return Completable.fromAction(this.accelerometerCompass::start);
    }

    public Completable executeStop() {
        return Completable.fromAction(this.accelerometerCompass::stop);
    }
}
