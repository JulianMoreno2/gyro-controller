package com.untref.infoindustrial.gyrocontroller.core.interactor;

import android.bluetooth.BluetoothDevice;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;

import java.util.Set;

public class DevicesInteractor {

    private BluetoothService bluetoothClient;

    public DevicesInteractor(BluetoothService bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    public void startDiscovery() {
        if (bluetoothClient.isEnabled())
            bluetoothClient.startDiscovery();
    }

    public Set<BluetoothDevice> getBoundedDevices() {
        return bluetoothClient.getBoundedDevices();
    }
}
