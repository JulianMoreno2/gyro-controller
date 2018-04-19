package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ListenGyroscopeCoordinatesFromBluetoothAction {

    private final Observable<String> observeBluetoothMessage;
    private final PublishSubject<GyroscopeCoordinates> publishGyroscopeCoordinates;
    private final Gson gson;

    public ListenGyroscopeCoordinatesFromBluetoothAction(
            Observable<String> observeBluetoothMessage,
            PublishSubject<GyroscopeCoordinates> publishGyroscopeCoordinates) {

        this.observeBluetoothMessage = observeBluetoothMessage;
        this.publishGyroscopeCoordinates = publishGyroscopeCoordinates;
        this.gson = new Gson();
    }

    public Observable<GyroscopeCoordinates> execute() {
        return this.observeBluetoothMessage
                .subscribeOn(Schedulers.io())
                .doOnNext(this::log)
                .map(this::fromJson)
                .doOnNext(publishGyroscopeCoordinates::onNext);
    }

    private GyroscopeCoordinates fromJson(String message) {
        try {
            return gson.fromJson(message, GyroscopeCoordinates.class);
        } catch (Exception e) {
            return new GyroscopeCoordinates(0,0,0,0);
        }
    }

    private int log(String message) {
        return Log.d("BL", "From: " + message);
    }
}
