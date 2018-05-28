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
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;
import com.untref.infoindustrial.gyrocontroller.core.util.InvalidJsonException;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ListenAccelerometerTranslationFromBluetoothAction {

    private final Observable<String> bluetoothMessageObservable;
    private final PublishSubject<AccelerometerTranslation> accelerometerTranslationPublishSubject;
    private final Gson gson;

    public ListenAccelerometerTranslationFromBluetoothAction(
            Observable<String> bluetoothMessageObservable,
            PublishSubject<AccelerometerTranslation> accelerometerTranslationPublishSubject) {

        this.bluetoothMessageObservable = bluetoothMessageObservable;
        this.accelerometerTranslationPublishSubject = accelerometerTranslationPublishSubject;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AccelerometerTranslation.class, new AccelerometerTranslationDeserializer());
        gson = gsonBuilder.create();
    }

    public void execute() {
        this.bluetoothMessageObservable
                .filter(this::isValidJson)
                .doOnNext(this::log)
                .map(message -> this.gson.fromJson(message, AccelerometerTranslation.class))
                .doOnNext(accelerometerTranslationPublishSubject::onNext)
                .subscribe();
    }

    private boolean isValidJson(String message) {
        try {
            AccelerometerTranslation accelerometerTranslation = gson.fromJson(message, AccelerometerTranslation.class);
            return accelerometerTranslation != null;
        } catch (InvalidJsonException | JsonSyntaxException e) {
            return false;
        }
    }

    private void log(String message) {
        Log.d("DEVICE", "AccelerometerTranslation BL From: " + message);
    }

    private class AccelerometerTranslationDeserializer implements JsonDeserializer<AccelerometerTranslation> {

        @Override
        public AccelerometerTranslation deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            try {
                final float xAccel = jsonObject.get("xAccel").getAsFloat();
                final float yAccel = jsonObject.get("yAccel").getAsFloat();
                final float zAccel = jsonObject.get("zAccel").getAsFloat();

                return new AccelerometerTranslation(xAccel, yAccel, zAccel);

            } catch (NullPointerException e) {
                throw new InvalidJsonException("Cannot parse json " + json.toString());
            }
        }
    }
}
