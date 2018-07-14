package com.untref.infoindustrial.gyrocontroller.core.action;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.untref.infoindustrial.gyrocontroller.core.domain.VibrateMessage;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;
import com.untref.infoindustrial.gyrocontroller.core.util.InvalidJsonException;

import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class ListenVibrateMessageAction {

    private final Observable<String> bluetoothMessageObservable;
    private final Gson gson;

    public ListenVibrateMessageAction(Observable<String> bluetoothMessageObservable) {

        this.bluetoothMessageObservable = bluetoothMessageObservable;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(GyroscopeRotation.class, new VibrateMessageDeserializer());
        gson = gsonBuilder.create();
    }

    public Completable execute() {
        return bluetoothMessageObservable
                .filter(this::isValidJson)
                .doOnNext(this::log)
                .map(message -> this.gson.fromJson(message, VibrateMessage.class))
                .flatMapCompletable(vibrateMessage -> Completable.complete());
    }

    private boolean isValidJson(String message) {
        try {
            VibrateMessage vibrateMessage = gson.fromJson(message, VibrateMessage.class);
            return vibrateMessage != null;
        } catch (InvalidJsonException | JsonSyntaxException e) {
            return false;
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "VibrateMessage BL From: " + message);
    }

    private class VibrateMessageDeserializer implements JsonDeserializer<VibrateMessage> {

        @Override
        public VibrateMessage deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            try {
                final String vibrate = jsonObject.get("vibrate").getAsString();
                return new VibrateMessage(vibrate);

            } catch (NullPointerException e) {
                throw new InvalidJsonException("Cannot parse json " + jsonObject.toString());
            }
        }
    }
}
