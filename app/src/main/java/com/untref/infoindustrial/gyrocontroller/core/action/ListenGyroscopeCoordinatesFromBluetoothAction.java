package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ListenGyroscopeCoordinatesFromBluetoothAction {

    private final Observable<String> bluetoothMessageObservable;
    private final PublishSubject<GyroscopeCoordinates> gyroscopeCoordinatesPublishSubject;
    private final Gson gson;

    public ListenGyroscopeCoordinatesFromBluetoothAction(
            Observable<String> bluetoothMessageObservable,
            PublishSubject<GyroscopeCoordinates> gyroscopeCoordinatesPublishSubject) {

        this.bluetoothMessageObservable = bluetoothMessageObservable;
        this.gyroscopeCoordinatesPublishSubject = gyroscopeCoordinatesPublishSubject;
        this.gson = new Gson();
    }

    public void execute() {
        this.bluetoothMessageObservable
                .doOnNext(this::log)
                .map(this::fromJson)
                .doOnNext(gyroscopeCoordinatesPublishSubject::onNext)
                .subscribe();
    }

    private GyroscopeCoordinates fromJson(String message) {
        try {
            return gson.fromJson(message, GyroscopeCoordinates.class);
        } catch (Exception e) {
            return new GyroscopeCoordinates(0, 0, 0, 0);
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "BL From: " + message);
    }
}
