package com.untref.infoindustrial.gyrocontroller.core.action;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;

import java.util.Random;

import io.reactivex.Single;

public class SendRandomGyroscopeCoordinatesAction {

    private final BluetoothClient bluetoothClient;
    private final Random rand;
    private final Gson gson;

    public SendRandomGyroscopeCoordinatesAction(BluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
        this.rand = new Random();
        this.gson = new Gson();
    }

    public Single<String> execute() {
        return Single.fromCallable(this::createGyroscopeCoordinates)
                .map(gson::toJson)
                .doOnSuccess(bluetoothClient::send);
    }

    @NonNull
    private GyroscopeCoordinates createGyroscopeCoordinates() {
        float mX = rand.nextFloat();
        float mY = rand.nextFloat();
        float mZ = rand.nextFloat();
        float mW = rand.nextFloat();

        return new GyroscopeCoordinates(mX, mY, mZ, -mW);
    }
}
