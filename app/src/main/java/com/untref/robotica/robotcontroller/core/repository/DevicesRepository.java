package com.untref.robotica.robotcontroller.core.repository;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

public class DevicesRepository {

    private List<BluetoothDevice> devices;

    public DevicesRepository() {
        this.devices = new ArrayList<>();
    }

    public void addDevice(BluetoothDevice device) {
        devices.add(device);
    }

    public List<BluetoothDevice> getDevices() {
        return devices;
    }
}
