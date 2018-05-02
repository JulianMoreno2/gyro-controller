package com.untref.infoindustrial.gyrocontroller.core.provider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.socket.ReceiverBluetoothSocketConnectionThread;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;
import com.untref.infoindustrial.gyrocontroller.presentation.view.fragment.approach.BluetoothService;

import io.reactivex.subjects.PublishSubject;

public class Provider {

    private static PublishSubject<String> bluetoothReaderPublishSubject;
    private static BluetoothClient bluetoothClient;
    private static PublishSubject<BluetoothDevice> receiverBluetoothSocketConnectionPublishSubject;
    private static PublishSubject<GyroscopeCoordinates> gyroscopeCoordinatesPublishSubject;
    private static ReceiverBluetoothSocketConnectionThread receiverBluetoothSocketConnectionThread;
    private static BluetoothService bluetoothService;

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
                provideReceiverBluetoothSocketConnectionPublishSubject(),
                provideIncommingMessageHandler(provideBluetoothReaderPublishSubject()));
    }

    private static Handler provideIncommingMessageHandler(PublishSubject<String> bluetoothReaderPublishSubject) {
        return new Handler(message -> {
            String s = (String) message.obj;
            bluetoothReaderPublishSubject.onNext(s);
            Log.d("DEVICE", "Handle message " + message);
            return true;
        });
    }

    public static BluetoothService provideBluetoothService() {
        if (bluetoothService == null) {
            bluetoothService = new BluetoothService(provideBluetoothReaderPublishSubject());
        }
        return bluetoothService;
    }
}
