package com.untref.robotica.robotcontroller.presentation.presenter.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.untref.robotica.robotcontroller.presentation.presenter.DevicesPresenter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BluetoothReceiver extends BroadcastReceiver {

    private DevicesPresenter devicesPresenter;

    public BluetoothReceiver(DevicesPresenter devicesPresenter) {
        this.devicesPresenter = devicesPresenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        devicesPresenter.getView().showLoading();
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Log.d("DEVICE", "Action: Discovery started");

        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Log.d("DEVICE", "Action: Discovery finished");
            List<BluetoothDevice> devices = devicesPresenter.getDevices();
            if (!devices.isEmpty()) {
                devicesPresenter.getView().hideLoading();
                devicesPresenter.getView().renderDevices(devices);
            } else {
                devicesPresenter.getView().showDevicesNotFoundMessage();
            }

        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d("DEVICE", "Action: Device found - " + device.getName());
            devicesPresenter.addDevice(device);
        }
    }
}
