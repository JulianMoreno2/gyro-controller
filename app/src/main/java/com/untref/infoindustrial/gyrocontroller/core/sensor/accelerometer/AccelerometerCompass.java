package com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.untref.infoindustrial.gyrocontroller.core.sensor.domain.MatrixF4x4;
import com.untref.infoindustrial.gyrocontroller.core.sensor.domain.Quaternion;

import io.reactivex.subjects.PublishSubject;

/**
 * The orientation provider that delivers the current orientation from the {@link Sensor#TYPE_ACCELEROMETER
 * Accelerometer} and {@link Sensor#TYPE_MAGNETIC_FIELD Compass}.
 *
 * @author Alexander Pacha
 */
public class AccelerometerCompass implements SensorEventListener {

    final private float[] magnitudeValues = new float[3];
    final private float[] accelerometerValues = new float[3];
    final float[] inclinationValues = new float[16];

    private SensorManager sensorManager;
    private Quaternion correctedQuaternion;
    private MatrixF4x4 rotationMatrix;
    private PublishSubject<AccelerometerTranslation> translationPublishSubject;

    public AccelerometerCompass(SensorManager sensorManager,
                                PublishSubject<AccelerometerTranslation> translationPublishSubject) {
        this.sensorManager = sensorManager;
        this.translationPublishSubject = translationPublishSubject;
        this.correctedQuaternion = new Quaternion();
        this.rotationMatrix = new MatrixF4x4();
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
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnitudeValues, 0, magnitudeValues.length);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerValues, 0, accelerometerValues.length);
        }

        // Fuse accelerometer with compass
        SensorManager.getRotationMatrix(rotationMatrix.getMatrix(), inclinationValues,
                accelerometerValues, magnitudeValues);
        // Transform rotation matrix to quaternion
        correctedQuaternion.setRowMajor(rotationMatrix.getMatrix());

        float x = correctedQuaternion.getX();
        float y = correctedQuaternion.getY();
        float z = correctedQuaternion.getZ();
        float w = correctedQuaternion.getW();
        translationPublishSubject.onNext(new AccelerometerTranslation(x, y, z));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}