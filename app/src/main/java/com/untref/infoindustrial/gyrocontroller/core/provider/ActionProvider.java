package com.untref.infoindustrial.gyrocontroller.core.provider;

import android.hardware.SensorManager;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenBluetoothDeviceConnectionAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeCoordinatesFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.GyroscopeAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeCoordinatesAction;
import com.untref.infoindustrial.gyrocontroller.core.sensor.CalibratedGyroscope;

public class ActionProvider {

    private static GyroscopeAction gyroscopeAction;
    private static SendGyroscopeCoordinatesToBluetoothWhenArrivesAction sendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
    private static ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction;
    private static SendRandomGyroscopeCoordinatesAction sendRandomGyroscopeCoordinatesAction;
    private static ListenBluetoothDeviceConnectionAction listenBluetoothDeviceConnectionAction;

    public static GyroscopeAction getStartGyroscopeAction(SensorManager sensorManager) {
        if (gyroscopeAction == null) {
            gyroscopeAction = new GyroscopeAction(new CalibratedGyroscope(sensorManager, Provider.provideGyroscopeCoordinatesPublishSubject()));
        }
        return gyroscopeAction;
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

    public static ListenBluetoothDeviceConnectionAction getListenBluetoothDeviceConnectionAction() {
        if (listenBluetoothDeviceConnectionAction == null) {
            listenBluetoothDeviceConnectionAction = new ListenBluetoothDeviceConnectionAction(
                    Provider.provideReceiverBluetoothSocketConnectionPublishSubject(),
                    Provider.provideBluetoothClient()
            );
        }
        return listenBluetoothDeviceConnectionAction;
    }
}
