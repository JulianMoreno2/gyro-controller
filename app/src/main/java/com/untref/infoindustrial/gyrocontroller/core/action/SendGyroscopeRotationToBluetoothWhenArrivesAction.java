package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SendGyroscopeRotationToBluetoothWhenArrivesAction {

    private BluetoothService bluetoothService;
    private final Observable<GyroscopeRotation> gyroscopeRotationObservable;
    private final Gson gson;

    public SendGyroscopeRotationToBluetoothWhenArrivesAction(
            BluetoothService bluetoothService,
            Observable<GyroscopeRotation> gyroscopeRotationObservable) {

        this.bluetoothService = bluetoothService;
        this.gyroscopeRotationObservable = gyroscopeRotationObservable;
        this.gson = new Gson();
    }

    public Observable<String> execute() {
        return this.gyroscopeRotationObservable
                .map(gson::toJson)
                .doOnNext(this::log)
                .doOnNext(message -> bluetoothService.write(message.getBytes()))
                .subscribeOn(Schedulers.io());
    }

    private void log(String message) {
        Log.d("DEVICE", "Rotation BL To: " + message);
    }
}
