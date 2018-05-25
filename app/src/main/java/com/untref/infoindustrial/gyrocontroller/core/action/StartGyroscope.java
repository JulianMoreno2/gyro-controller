package com.untref.infoindustrial.gyrocontroller.core.action;

import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.CalibratedGyroscope;

import io.reactivex.Completable;

public class StartGyroscope {

    private final CalibratedGyroscope calibratedGyroscope;

    public StartGyroscope(CalibratedGyroscope calibratedGyroscope) {
        this.calibratedGyroscope = calibratedGyroscope;
    }

    public Completable executeStart() {
        return Completable.fromAction(this.calibratedGyroscope::start);
    }

    public Completable executeStop() {
        return Completable.fromAction(this.calibratedGyroscope::stop);
    }
}
