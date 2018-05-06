package com.untref.infoindustrial.gyrocontroller.core.provider;

import android.hardware.SensorManager;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeCoordinatesFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeTranslationFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeTranslationToBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeCoordinatesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.StartGyroscope;
import com.untref.infoindustrial.gyrocontroller.core.sensor.CalibratedGyroscope;

public class ActionProvider {

    private static StartGyroscope startGyroscope;
    private static SendGyroscopeCoordinatesToBluetoothWhenArrivesAction sendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
    private static ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction;
    private static SendRandomGyroscopeCoordinatesAction sendRandomGyroscopeCoordinatesAction;
    private static SendGyroscopeTranslationToBluetoothAction sendGyroscopeTranslationToBluetoothAction;
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

    public static SendGyroscopeTranslationToBluetoothAction getSendGyroscopeTranslationToBluetoothAction() {
        if (sendGyroscopeTranslationToBluetoothAction == null) {
            sendGyroscopeTranslationToBluetoothAction = new SendGyroscopeTranslationToBluetoothAction(
                    Provider.provideBluetoothService());
        }
        return sendGyroscopeTranslationToBluetoothAction;
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
