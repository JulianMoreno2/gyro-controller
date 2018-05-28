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
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;
import com.untref.infoindustrial.gyrocontroller.core.util.InvalidJsonException;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ListenGyroscopeRotationFromBluetoothAction {

    private final Observable<String> bluetoothMessageObservable;
    private final PublishSubject<GyroscopeRotation> gyroscopeRotationPublishSubject;
    private final Gson gson;

    public ListenGyroscopeRotationFromBluetoothAction(
            Observable<String> bluetoothMessageObservable,
            PublishSubject<GyroscopeRotation> gyroscopeRotationPublishSubject) {

        this.bluetoothMessageObservable = bluetoothMessageObservable;
        this.gyroscopeRotationPublishSubject = gyroscopeRotationPublishSubject;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(GyroscopeRotation.class, new GyroscopeRotationDeserializer());
        gson = gsonBuilder.create();
    }

    public void execute() {
        this.bluetoothMessageObservable
                .filter(this::isValidJson)
                .doOnNext(this::log)
                .map(message -> this.gson.fromJson(message, GyroscopeRotation.class))
                .doOnNext(gyroscopeRotationPublishSubject::onNext)
                .subscribe();
    }

    private boolean isValidJson(String message) {
        try {
            GyroscopeRotation gyroscopeRotation = gson.fromJson(message, GyroscopeRotation.class);
            return gyroscopeRotation != null;
        } catch (InvalidJsonException | JsonSyntaxException e) {
            return false;
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "GyroscopeRotation BL From: " + message);
    }

    private class GyroscopeRotationDeserializer implements JsonDeserializer<GyroscopeRotation> {

        @Override
        public GyroscopeRotation deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            try {
                final float xGyro = jsonObject.get("xGyro").getAsFloat();
                final float yGyro = jsonObject.get("yGyro").getAsFloat();
                final float zGyro = jsonObject.get("zGyro").getAsFloat();
                final float wGyro = jsonObject.get("wGyro").getAsFloat();

                return new GyroscopeRotation(xGyro, yGyro, zGyro, wGyro);

            } catch (NullPointerException e) {
                throw new InvalidJsonException("Cannot parse json " + jsonObject.toString());
            }
        }
    }
}
