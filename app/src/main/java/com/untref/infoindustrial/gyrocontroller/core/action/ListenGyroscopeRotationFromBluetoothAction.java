package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ListenGyroscopeRotationFromBluetoothAction {

    private final Observable<String> bluetoothMessageObservable;
    private final PublishSubject<GyroscopeRotation> gyroscopeRotationPublishSubject;
    private final Gson gson;

    public ListenGyroscopeRotationFromBluetoothAction(
            Observable<String> bluetoothMessageObservable,
            PublishSubject<GyroscopeRotation> gyroscopeRotationPublishSubject) {

        this.bluetoothMessageObservable = bluetoothMessageObservable;
        this.gyroscopeRotationPublishSubject = gyroscopeRotationPublishSubject;
        this.gson = new Gson();
    }

    public void execute() {
        this.bluetoothMessageObservable
                .doOnNext(this::log)
                .filter(this::isValidJson)
                .map(message -> this.gson.fromJson(message, GyroscopeRotation.class))
                .doOnNext(gyroscopeRotationPublishSubject::onNext)
                .subscribe();
    }

    private boolean isValidJson(String message) {
        try {
            GyroscopeRotation gyroscopeRotation = gson.fromJson(message, GyroscopeRotation.class);
            return gyroscopeRotation != null;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "BL From: " + message);
    }
}
