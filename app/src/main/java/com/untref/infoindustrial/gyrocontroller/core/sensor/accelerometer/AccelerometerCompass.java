package com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import io.reactivex.subjects.PublishSubject;

public class AccelerometerCompass implements SensorEventListener {

    final private float[] gravityValues = new float[3];
    final private float[] accelerometerValues = new float[3];
    final private float[] linearAccelerationValues = new float[3];
    final float alpha = 0.8f; //para la gravedad

    private SensorManager sensorManager;
    private PublishSubject<AccelerometerTranslation> translationPublishSubject;

    public AccelerometerCompass(SensorManager sensorManager,
                                PublishSubject<AccelerometerTranslation> translationPublishSubject) {
        this.sensorManager = sensorManager;
        this.translationPublishSubject = translationPublishSubject;
    }

    public void start() {
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            System.arraycopy(event.values, 0, gravityValues, 0, gravityValues.length);
        }
        else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerValues, 0, accelerometerValues.length);
        }

        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        // Isolate the force of gravity with the low-pass filter.
        gravityValues[0] = alpha * gravityValues[0] + (1 - alpha) * accelerometerValues[0];
        gravityValues[1] = alpha * gravityValues[1] + (1 - alpha) * accelerometerValues[1];
        gravityValues[2] = alpha * gravityValues[2] + (1 - alpha) * accelerometerValues[2];

        // Remove the gravity contribution with the high-pass filter.
        linearAccelerationValues[0] = accelerometerValues[0] - gravityValues[0];
        linearAccelerationValues[1] = accelerometerValues[1] - gravityValues[1];
        linearAccelerationValues[2] = accelerometerValues[2] - gravityValues[2];


        translationPublishSubject.onNext(new AccelerometerTranslation(linearAccelerationValues[0], linearAccelerationValues[1],  linearAccelerationValues[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}