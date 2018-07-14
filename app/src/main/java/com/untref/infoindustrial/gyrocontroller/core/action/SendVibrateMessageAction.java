package com.untref.infoindustrial.gyrocontroller.core.action;


import android.util.Log;

import com.google.gson.Gson;
import com.untref.infoindustrial.gyrocontroller.core.domain.VibrateMessage;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;

import io.reactivex.Single;

public class SendVibrateMessageAction {

    private final BluetoothService bluetoothService;
    private final Gson gson;

    public SendVibrateMessageAction(BluetoothService bluetoothService) {
        this.bluetoothService = bluetoothService;
        this.gson = new Gson();
    }

    public Single<String> execute() {
        return Single.fromCallable(this::createVibrateMessage)
                .map(this.gson::toJson)
                .doOnSuccess(this::log)
                .doOnSuccess(message -> bluetoothService.write(message.getBytes()));
    }

    private VibrateMessage createVibrateMessage() {
        return new VibrateMessage("vibrate");
    }

    private void log(String message) {
        Log.d("DEVICE", "Send VibrateMessage " + message + " to BL");
    }
}
