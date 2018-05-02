package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SendGyroscopeCoordinatesToBluetoothWhenArrivesAction {

    private BluetoothService bluetoothService;
    private final Observable<GyroscopeCoordinates> observeGyroscopeCoordinates;
    private final Gson gson;

    public SendGyroscopeCoordinatesToBluetoothWhenArrivesAction(
            BluetoothService bluetoothService,
            Observable<GyroscopeCoordinates> observeGyroscopeCoordinates) {

        this.bluetoothService = bluetoothService;
        this.observeGyroscopeCoordinates = observeGyroscopeCoordinates;
        this.gson = new Gson();
    }

    public Observable<String> execute() {
        return this.observeGyroscopeCoordinates
                .map(gson::toJson)
                .doOnNext(this::log)
                .doOnNext(message -> bluetoothService.write(message.getBytes()))
                .subscribeOn(Schedulers.io());
    }

    private void log(String message) {
        Log.d("DEVICE", "BL To: " + message);
    }
}
