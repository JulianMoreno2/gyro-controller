package com.untref.infoindustrial.gyrocontroller.core.interactor;

import android.bluetooth.BluetoothDevice;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;
import com.untref.infoindustrial.gyrocontroller.core.repository.DevicesRepository;

import java.util.Set;

public class DevicesInteractor {

    private BluetoothClient bluetoothClient;
    private DevicesRepository devicesRepository;

    public DevicesInteractor(BluetoothClient bluetoothClient, DevicesRepository devicesRepository) {
        this.bluetoothClient = bluetoothClient;
        this.devicesRepository = devicesRepository;
    }

    public void startDiscovery() {
        if (bluetoothClient.isEnabled())
            bluetoothClient.startDiscovery();
    }

    public void addDevice(BluetoothDevice device) {
        devicesRepository.addDevice(device);
    }

    public Set<BluetoothDevice> getDevices() {
        return devicesRepository.getDevices();
    }

    public Set<BluetoothDevice> getBoundedDevices() {
        return bluetoothClient.getBoundedDevices();
    }

    public void connectToPairDevice(BluetoothDevice device) {
        bluetoothClient.connectToPairDevice(device);
    }
}
