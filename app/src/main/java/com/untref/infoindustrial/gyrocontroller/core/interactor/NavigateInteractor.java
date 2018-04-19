package com.untref.infoindustrial.gyrocontroller.core.interactor;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;

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

    private BluetoothClient transportClient;

    public NavigateInteractor(BluetoothClient transportClient) {
        this.transportClient = transportClient;
    }

    public void navigate() {
        transportClient.send(NAVIGATE);
    }

    public void stop() {
        transportClient.send(STOP);
    }

    public void disconnect() {
        transportClient.disconnect(DISCONNECT);
    }

    public void goToBackward() {
        transportClient.send(BACKWARD_VEL_MED);
    }

    public void gotToForward() {
        transportClient.send(FORWARD_VEL_MED);
    }

}
