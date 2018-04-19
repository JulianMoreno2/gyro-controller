package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SendGyroscopeCoordinatesToBluetoothWhenArrivesAction {

    private final BluetoothClient bluetoothClient;
    private final Observable<GyroscopeCoordinates> observeGyroscopeCoordinates;
    private final Gson gson;

    public SendGyroscopeCoordinatesToBluetoothWhenArrivesAction(
            BluetoothClient bluetoothClient,
            Observable<GyroscopeCoordinates> observeGyroscopeCoordinates) {

        this.bluetoothClient = bluetoothClient;
        this.observeGyroscopeCoordinates = observeGyroscopeCoordinates;
        this.gson = new Gson();
    }

    public Observable<String> execute() {
        return this.observeGyroscopeCoordinates
                .map(gson::toJson)
                .doOnNext(this::log)
                .doOnNext(bluetoothClient::send)
                .subscribeOn(Schedulers.io());
    }

    private int log(String message) {
        return Log.d("BL", "To: " + message);
    }
}
