package com.untref.infoindustrial.gyrocontroller.core.action;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;

import java.util.Random;

import io.reactivex.Single;

public class SendRandomGyroscopeCoordinatesAction {

    private final Random rand;
    private final Gson gson;
    private final BluetoothService bluetoothService;

    public SendRandomGyroscopeCoordinatesAction(BluetoothService bluetoothService) {
        this.bluetoothService = bluetoothService;
        this.rand = new Random();
        this.gson = new Gson();
    }

    public Single<String> execute() {
        return Single.fromCallable(this::createGyroscopeCoordinates)
                .map(gson::toJson)
                .doOnSuccess(this::log)
                .doOnSuccess(s -> bluetoothService.write(s.getBytes()));
    }

    private void log(String message) {
        Log.d("DEVICE", "BL RandomCoord To: " + message);
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
