package com.untref.infoindustrial.gyrocontroller.core.provider;

import android.hardware.SensorManager;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeCoordinatesFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeTranslationFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeTranslationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeCoordinatesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.StartGyroscope;
import com.untref.infoindustrial.gyrocontroller.core.sensor.CalibratedGyroscope;

public class ActionProvider {

    private static StartGyroscope startGyroscope;
    private static SendGyroscopeCoordinatesToBluetoothWhenArrivesAction sendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
    private static ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction;
    private static SendRandomGyroscopeCoordinatesAction sendRandomGyroscopeCoordinatesAction;
    private static SendGyroscopeTranslationAction sendGyroscopeTranslationAction;
    private static ListenGyroscopeTranslationFromBluetoothAction listenGyroscopeTranslationFromBluetoothAction;

    public static StartGyroscope getStartGyroscopeAction(SensorManager sensorManager) {
        if (startGyroscope == null) {
            startGyroscope = new StartGyroscope(new CalibratedGyroscope(sensorManager, Provider.provideGyroscopeCoordinatesPublishSubject()));
        }
        return startGyroscope;
    }

    public static SendGyroscopeCoordinatesToBluetoothWhenArrivesAction getSendGyroscopeCoordinatesToBluetoothWhenArrivesAction() {
        if (sendGyroscopeCoordinatesToBluetoothWhenArrivesAction == null) {
            sendGyroscopeCoordinatesToBluetoothWhenArrivesAction = new SendGyroscopeCoordinatesToBluetoothWhenArrivesAction(
                    Provider.provideBluetoothService(),
                    Provider.provideGyroscopeCoordinatesPublishSubject()
            );
        }
        return sendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
    }

    public static ListenGyroscopeCoordinatesFromBluetoothAction getListenGyroscopeCoordinatesFromBluetoothAction() {
        if (listenGyroscopeCoordinatesFromBluetoothAction == null) {
            listenGyroscopeCoordinatesFromBluetoothAction = new ListenGyroscopeCoordinatesFromBluetoothAction(
                    Provider.provideBluetoothReaderPublishSubject(),
                    Provider.provideGyroscopeCoordinatesPublishSubject()
            );
        }
        return listenGyroscopeCoordinatesFromBluetoothAction;
    }

    public static SendRandomGyroscopeCoordinatesAction getSendRandomGyroscopeCoordinates() {
        if (sendRandomGyroscopeCoordinatesAction == null) {
            sendRandomGyroscopeCoordinatesAction = new SendRandomGyroscopeCoordinatesAction(
                    Provider.provideBluetoothService());
        }
        return sendRandomGyroscopeCoordinatesAction;
    }

    public static SendGyroscopeTranslationAction getSendGyroscopeTranslationAction() {
        if (sendGyroscopeTranslationAction == null) {
            sendGyroscopeTranslationAction = new SendGyroscopeTranslationAction(
                    Provider.provideBluetoothService());
        }
        return sendGyroscopeTranslationAction;
    }

    public static ListenGyroscopeTranslationFromBluetoothAction getListenGyroscopeTranslationFromBluetoothAction() {
        if (listenGyroscopeTranslationFromBluetoothAction == null) {
            listenGyroscopeTranslationFromBluetoothAction = new ListenGyroscopeTranslationFromBluetoothAction(
                    Provider.provideBluetoothReaderPublishSubject(),
                    Provider.provideGyroscopeTranslationPublishSubject()
            );
        }
        return listenGyroscopeTranslationFromBluetoothAction;
    }
}
