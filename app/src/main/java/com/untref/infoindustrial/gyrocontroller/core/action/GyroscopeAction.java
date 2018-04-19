package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.core.sensor.CalibratedGyroscope;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class GyroscopeAction {

    private final CalibratedGyroscope calibratedGyroscope;

    public GyroscopeAction(CalibratedGyroscope calibratedGyroscope) {
        this.calibratedGyroscope = calibratedGyroscope;
    }

    public Completable executeStart() {
        return Completable.fromAction(this.calibratedGyroscope::start)
                .subscribeOn(Schedulers.computation())
                .doOnComplete(() -> log("executeStart"));
    }

    public Completable executeStop() {
        return Completable.fromAction(this.calibratedGyroscope::stop)
                .subscribeOn(Schedulers.computation())
                .doOnComplete(() -> log("executeStop"));
    }

    private int log(String message) {
        return Log.d("DEBUG", message);
    }
}
