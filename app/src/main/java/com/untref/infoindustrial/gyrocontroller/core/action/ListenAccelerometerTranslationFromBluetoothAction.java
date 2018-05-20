package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ListenAccelerometerTranslationFromBluetoothAction {

    private final Observable<String> bluetoothMessageObservable;
    private final PublishSubject<AccelerometerTranslation> gyroscopeTranslationPublishSubject;
    private final Gson gson;

    public ListenAccelerometerTranslationFromBluetoothAction(
            Observable<String> bluetoothMessageObservable,
            PublishSubject<AccelerometerTranslation> gyroscopeTranslationPublishSubject) {

        this.bluetoothMessageObservable = bluetoothMessageObservable;
        this.gyroscopeTranslationPublishSubject = gyroscopeTranslationPublishSubject;
        this.gson = new Gson();
    }

    public void execute() {
        this.bluetoothMessageObservable
                .doOnNext(this::log)
                .filter(this::isValidJson)
                .map(message -> this.gson.fromJson(message, AccelerometerTranslation.class))
                .doOnNext(gyroscopeTranslationPublishSubject::onNext)
                .subscribe();
    }

    private boolean isValidJson(String message) {
        try {
            AccelerometerTranslation accelerometerTranslation = gson.fromJson(message, AccelerometerTranslation.class);
            return accelerometerTranslation != null;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "BL From: " + message);
    }
}
