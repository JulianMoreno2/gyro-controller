package com.untref.infoindustrial.gyrocontroller.core.repository;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DevicesRepository {

    private Set<BluetoothDevice> devices;

    public DevicesRepository() {
        this.devices = new HashSet<>();
    }

    public void addDevice(BluetoothDevice device) {
        devices.add(device);
    }

    public Set<BluetoothDevice> getDevices() {
        return devices;
    }
}
