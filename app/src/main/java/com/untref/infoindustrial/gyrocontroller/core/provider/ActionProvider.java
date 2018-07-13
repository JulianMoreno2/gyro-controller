package com.untref.infoindustrial.gyrocontroller.core.provider;

import android.hardware.SensorManager;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeRotationFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenAccelerometerTranslationFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendAccelerometerTranslationToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeRotationToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendAccelerometerTranslationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeRotationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.StartAccelerometer;
import com.untref.infoindustrial.gyrocontroller.core.action.StartGyroscope;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerCompass;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.CalibratedGyroscope;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.HasCollisionBetweenObjects;

public class ActionProvider {

    private static StartGyroscope startGyroscope;
    private static SendGyroscopeRotationToBluetoothWhenArrivesAction sendGyroscopeRotationToBluetoothWhenArrivesAction;
    private static ListenGyroscopeRotationFromBluetoothAction listenGyroscopeRotationFromBluetoothAction;
    private static SendRandomGyroscopeRotationAction sendRandomGyroscopeRotationAction;
    private static SendAccelerometerTranslationAction sendAccelerometerTranslationAction;
    private static ListenAccelerometerTranslationFromBluetoothAction listenAccelerometerTranslationFromBluetoothAction;
    private static SendAccelerometerTranslationToBluetoothWhenArrivesAction sendAccelerometerTranslationToBluetoothWhenArrivesAction;
    private static StartAccelerometer startAccelerometer;
    private static HasCollisionBetweenObjects hasCollisionBetweenObjects;

    public static StartGyroscope getStartGyroscopeAction(SensorManager sensorManager) {
        if (startGyroscope == null) {
            startGyroscope = new StartGyroscope(new CalibratedGyroscope(sensorManager,
                    Provider.provideGyroscopeRotationPublishSubject()));
        }
        return startGyroscope;
    }

    public static SendGyroscopeRotationToBluetoothWhenArrivesAction getSendGyroscopeRotationToBluetoothWhenArrivesAction() {
        if (sendGyroscopeRotationToBluetoothWhenArrivesAction == null) {
            sendGyroscopeRotationToBluetoothWhenArrivesAction = new SendGyroscopeRotationToBluetoothWhenArrivesAction(
                    Provider.provideBluetoothService(),
                    Provider.provideGyroscopeRotationPublishSubject()
            );
        }
        return sendGyroscopeRotationToBluetoothWhenArrivesAction;
    }

    public static ListenGyroscopeRotationFromBluetoothAction getListenGyroscopeRotationFromBluetoothAction() {
        if (listenGyroscopeRotationFromBluetoothAction == null) {
            listenGyroscopeRotationFromBluetoothAction = new ListenGyroscopeRotationFromBluetoothAction(
                    Provider.provideBluetoothReaderPublishSubject(),
                    Provider.provideGyroscopeRotationPublishSubject()
            );
        }
        return listenGyroscopeRotationFromBluetoothAction;
    }

    public static SendRandomGyroscopeRotationAction getSendRandomGyroscopeRotation() {
        if (sendRandomGyroscopeRotationAction == null) {
            sendRandomGyroscopeRotationAction = new SendRandomGyroscopeRotationAction(
                    Provider.provideBluetoothService());
        }
        return sendRandomGyroscopeRotationAction;
    }

    public static SendAccelerometerTranslationAction getSendAccelerometerTranslationAction() {
        if (sendAccelerometerTranslationAction == null) {
            sendAccelerometerTranslationAction = new SendAccelerometerTranslationAction(
                    Provider.provideBluetoothService());
        }
        return sendAccelerometerTranslationAction;
    }

    public static ListenAccelerometerTranslationFromBluetoothAction getListenAccelerometerTranslationFromBluetoothAction() {
        if (listenAccelerometerTranslationFromBluetoothAction == null) {
            listenAccelerometerTranslationFromBluetoothAction = new ListenAccelerometerTranslationFromBluetoothAction(
                    Provider.provideBluetoothReaderPublishSubject(),
                    Provider.provideAccelerometerTranslationPublishSubject()
            );
        }
        return listenAccelerometerTranslationFromBluetoothAction;
    }

    public static SendAccelerometerTranslationToBluetoothWhenArrivesAction getSendAccelerometerTranslationToBluetoothWhenArrivesAction() {
        if (sendAccelerometerTranslationToBluetoothWhenArrivesAction == null) {
            sendAccelerometerTranslationToBluetoothWhenArrivesAction = new SendAccelerometerTranslationToBluetoothWhenArrivesAction(
                    Provider.provideBluetoothService(),
                    Provider.provideAccelerometerTranslationPublishSubject()
            );
        }
        return sendAccelerometerTranslationToBluetoothWhenArrivesAction;
    }

    public static StartAccelerometer getStartAccelerometerAction(SensorManager sensorManager) {
        if (startAccelerometer == null) {
            startAccelerometer = new StartAccelerometer(new AccelerometerCompass(sensorManager,
                    Provider.provideAccelerometerTranslationPublishSubject()));
        }
        return startAccelerometer;
    }

    public static HasCollisionBetweenObjects getHasCollisionBetweenObjects() {
        if (hasCollisionBetweenObjects == null) {
            hasCollisionBetweenObjects = new HasCollisionBetweenObjects();
        }
        return hasCollisionBetweenObjects;
    }
}
