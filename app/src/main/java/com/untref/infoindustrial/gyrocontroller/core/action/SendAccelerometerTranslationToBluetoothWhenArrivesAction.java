package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SendAccelerometerTranslationToBluetoothWhenArrivesAction {

    private BluetoothService bluetoothService;
    private final Observable<AccelerometerTranslation> accelerometerTranslationObservable;
    private final Gson gson;

    public SendAccelerometerTranslationToBluetoothWhenArrivesAction(
            BluetoothService bluetoothService,
            Observable<AccelerometerTranslation> accelerometerTranslationObservable) {

        this.bluetoothService = bluetoothService;
        this.accelerometerTranslationObservable = accelerometerTranslationObservable;
        this.gson = new Gson();
    }

    public Observable<String> execute() {
        return this.accelerometerTranslationObservable
                .map(gson::toJson)
                .doOnNext(this::log)
                .doOnNext(message -> bluetoothService.write(message.getBytes()))
                .subscribeOn(Schedulers.io());
    }

    private void log(String message) {
        Log.d("DEVICE", "Rotation BL To: " + message);
    }
}
