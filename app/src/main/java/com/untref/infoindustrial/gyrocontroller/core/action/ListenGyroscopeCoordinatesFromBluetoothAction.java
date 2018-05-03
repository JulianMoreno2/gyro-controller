package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
                .filter(this::isValidJson)
                .map(message -> this.gson.fromJson(message, GyroscopeCoordinates.class))
                .doOnNext(gyroscopeCoordinatesPublishSubject::onNext)
                .subscribe();
    }

    private boolean isValidJson(String message) {
        try {
            GyroscopeCoordinates gyroscopeCoordinates = gson.fromJson(message, GyroscopeCoordinates.class);
            return gyroscopeCoordinates != null;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "BL From: " + message);
    }
}
