package com.untref.infoindustrial.gyrocontroller.core.action;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ListenBluetoothDeviceConnectionAction {

    private Observable<BluetoothDevice> onReceiveBluetoothDevice;
    private BluetoothClient bluetoothClient;

    public ListenBluetoothDeviceConnectionAction(Observable<BluetoothDevice> onReceiveBluetoothDevice,
                                                 BluetoothClient bluetoothClient) {
        this.onReceiveBluetoothDevice = onReceiveBluetoothDevice;
        this.bluetoothClient = bluetoothClient;
    }

    public Single<BluetoothDevice> execute() {
        return onReceiveBluetoothDevice
                .doOnNext(bluetoothDevice -> Log.d("DEVICE", "BluetoothDevice connected :" + bluetoothDevice.getName()))
                .doOnNext(bluetoothDevice -> bluetoothClient.connectToPairDevice(bluetoothDevice))
                .firstOrError();
    }
}
