package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeTranslation;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ListenGyroscopeTranslationFromBluetoothAction {

    private final Observable<String> bluetoothMessageObservable;
    private final PublishSubject<GyroscopeTranslation> gyroscopeTranslationPublishSubject;
    private final Gson gson;

    public ListenGyroscopeTranslationFromBluetoothAction(
            Observable<String> bluetoothMessageObservable,
            PublishSubject<GyroscopeTranslation> gyroscopeTranslationPublishSubject) {

        this.bluetoothMessageObservable = bluetoothMessageObservable;
        this.gyroscopeTranslationPublishSubject = gyroscopeTranslationPublishSubject;
        this.gson = new Gson();
    }

    public void execute() {
        this.bluetoothMessageObservable
                .doOnNext(this::log)
                .filter(this::isValidJson)
                .map(message -> this.gson.fromJson(message, GyroscopeTranslation.class))
                .doOnNext(gyroscopeTranslationPublishSubject::onNext)
                .subscribe();
    }

    private boolean isValidJson(String message) {
        try {
            GyroscopeTranslation gyroscopeTranslation = gson.fromJson(message, GyroscopeTranslation.class);
            return gyroscopeTranslation != null;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "BL From: " + message);
    }
}
