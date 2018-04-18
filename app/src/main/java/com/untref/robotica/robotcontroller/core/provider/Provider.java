package com.untref.robotica.robotcontroller.core.provider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.untref.robotica.robotcontroller.core.bluetoothclient.BluetoothClient;

import io.reactivex.subjects.PublishSubject;

public class Provider {

    static PublishSubject<String> bluetoothReaderPublishSubject;
    static BluetoothClient bluetoothClient;
    private static PublishSubject<BluetoothDevice> receiverBluetoothSocketConnectionPublishSubject;

    public static PublishSubject<String> provideBluetoothReaderPublishSubject() {
        if (bluetoothReaderPublishSubject != null) {
            return bluetoothReaderPublishSubject;
        }
        bluetoothReaderPublishSubject = PublishSubject.create();
        return bluetoothReaderPublishSubject;
    }

    public static BluetoothClient provideBluetoothClient() {
        if (bluetoothClient != null) {
            return bluetoothClient;
        }
        bluetoothClient = new BluetoothClient(BluetoothAdapter.getDefaultAdapter(), provideBluetoothReaderPublishSubject());
        return bluetoothClient;
    }

    public static PublishSubject<BluetoothDevice> provideReceiverBluetoothSocketConnectionPublishSubject() {
        if (bluetoothReaderPublishSubject != null) {
            return receiverBluetoothSocketConnectionPublishSubject;
        }
        receiverBluetoothSocketConnectionPublishSubject = PublishSubject.create();
        return receiverBluetoothSocketConnectionPublishSubject;
    }
}
