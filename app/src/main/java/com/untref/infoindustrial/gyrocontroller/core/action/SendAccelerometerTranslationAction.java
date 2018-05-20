package com.untref.infoindustrial.gyrocontroller.core.action;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.Translation;

import io.reactivex.Single;

public class SendAccelerometerTranslationAction {

    private static final float TRANSLATE = 0.1f;
    private static final float NONE = 0f;

    private final BluetoothService bluetoothService;
    private final Gson gson;

    public SendAccelerometerTranslationAction(BluetoothService bluetoothService) {
        this.bluetoothService = bluetoothService;
        this.gson = new Gson();
    }

    public Single<String> execute(Translation translation) {
        return Single.fromCallable(() -> translation)
                .map(this::toGyroscopeTranslation)
                .map(this.gson::toJson)
                .doOnSuccess(this::log)
                .doOnSuccess(traslationJson -> bluetoothService.write(traslationJson.getBytes()));
    }

    private AccelerometerTranslation toGyroscopeTranslation(Translation translation) {
        switch (translation) {
            case LEFT:
                return createGyroscopeTranslation(-TRANSLATE, NONE);
            case RIGHT:
                return createGyroscopeTranslation(TRANSLATE, NONE);
            case UP:
                return createGyroscopeTranslation(NONE, TRANSLATE);
            case DOWN:
                return createGyroscopeTranslation(NONE, -TRANSLATE);
            default:
                return createGyroscopeTranslation(NONE, NONE);
        }
    }

    @NonNull
    private AccelerometerTranslation createGyroscopeTranslation(float x, float y) {
        return new AccelerometerTranslation(x, y, 0f);
    }

    private void log(String message) {
        Log.d("DEVICE", "Send AccelerometerTranslation " + message + " to BL");
    }
}
