package com.untref.infoindustrial.gyrocontroller.core.provider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.socket.ReceiverBluetoothSocketConnectionThread;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;

import io.reactivex.subjects.PublishSubject;

public class Provider {

    private static PublishSubject<String> bluetoothReaderPublishSubject;
    private static BluetoothClient bluetoothClient;
    private static PublishSubject<BluetoothDevice> receiverBluetoothSocketConnectionPublishSubject;
    private static PublishSubject<GyroscopeCoordinates> gyroscopeCoordinatesPublishSubject;
    private static ReceiverBluetoothSocketConnectionThread receiverBluetoothSocketConnectionThread;

    public static PublishSubject<String> provideBluetoothReaderPublishSubject() {
        if (bluetoothReaderPublishSubject == null) {
            bluetoothReaderPublishSubject = PublishSubject.create();
        }
        return bluetoothReaderPublishSubject;
    }

    public static BluetoothClient provideBluetoothClient() {
        if (bluetoothClient == null) {
            bluetoothClient = new BluetoothClient(
                    BluetoothAdapter.getDefaultAdapter(),
                    provideBluetoothReaderPublishSubject());
        }
        return bluetoothClient;
    }

    public static PublishSubject<BluetoothDevice> provideReceiverBluetoothSocketConnectionPublishSubject() {
        if (receiverBluetoothSocketConnectionPublishSubject == null) {
            receiverBluetoothSocketConnectionPublishSubject = PublishSubject.create();
        }
        return receiverBluetoothSocketConnectionPublishSubject;
    }

    public static PublishSubject<GyroscopeCoordinates> provideGyroscopeCoordinatesPublishSubject() {
        if (gyroscopeCoordinatesPublishSubject == null) {
            gyroscopeCoordinatesPublishSubject = PublishSubject.create();
        }
        return gyroscopeCoordinatesPublishSubject;
    }

    public static ReceiverBluetoothSocketConnectionThread provideReceiverBluetoothSocketConnectionThread() {
        return new ReceiverBluetoothSocketConnectionThread(
                BluetoothAdapter.getDefaultAdapter(),
                provideReceiverBluetoothSocketConnectionPublishSubject()
        );
    }
}
