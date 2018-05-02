package com.untref.infoindustrial.gyrocontroller.core.action;

import com.untref.infoindustrial.gyrocontroller.core.sensor.CalibratedGyroscope;

import io.reactivex.Completable;

public class GyroscopeAction {

    private final CalibratedGyroscope calibratedGyroscope;

    public GyroscopeAction(CalibratedGyroscope calibratedGyroscope) {
        this.calibratedGyroscope = calibratedGyroscope;
    }

    public Completable execute() {
        return Completable.fromAction(this.calibratedGyroscope::start);
    }

}
