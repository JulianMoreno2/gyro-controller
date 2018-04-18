package com.untref.robotica.robotcontroller.core.interactor;

import android.bluetooth.BluetoothDevice;

import com.untref.robotica.robotcontroller.core.bluetoothclient.BluetoothClient;

public class NavigateInteractor {

    private static final String NAVIGATE = "1\r\n";
    private static final String STOP = "0\r\n";
    private static final String DISCONNECT = "Disconnect";
    private static final String FRONT_DISTANCE = "i001f";
    private static final String BACK_DISTANCE = "i001f";
    private static final String LEFT_DISTANCE = "i001f";
    private static final String RIGHT_DISTANCE = "i001f";
    private static final String FORWARD_VEL_MED = "i115f\r\n";
    private static final String FORWARD_VEL_MAX = "i119f";
    private static final String BACKWARD_VEL_MED = "i105f\r\n";
    private static final String BACKWARD_VEL_MAX = "i109f";

    private BluetoothClient bluetoothClient;

    public NavigateInteractor(BluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    public void navigate() {
        bluetoothClient.sendToBluetoothSocket(NAVIGATE);
    }

    public void stop() {
        bluetoothClient.sendToBluetoothSocket(STOP);
    }

    public void disconnect() {
        bluetoothClient.disconnectBluetooth(DISCONNECT);
    }

    public void goToBackward() {
        bluetoothClient.sendToBluetoothSocket(BACKWARD_VEL_MED);
    }

    public void gotToForward() {
        bluetoothClient.sendToBluetoothSocket(FORWARD_VEL_MED);
    }

}
