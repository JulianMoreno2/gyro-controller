package com.untref.infoindustrial.gyrocontroller.core.provider;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeTranslation;

import io.reactivex.subjects.PublishSubject;

public class Provider {

    private static PublishSubject<String> bluetoothReaderPublishSubject;
    private static PublishSubject<GyroscopeCoordinates> gyroscopeCoordinatesPublishSubject;
    private static PublishSubject<GyroscopeTranslation> gyroscopeTranslationPublishSubject;
    private static BluetoothService bluetoothService;

    public static PublishSubject<String> provideBluetoothReaderPublishSubject() {
        if (bluetoothReaderPublishSubject == null) {
            bluetoothReaderPublishSubject = PublishSubject.create();
        }
        return bluetoothReaderPublishSubject;
    }

    public static PublishSubject<GyroscopeCoordinates> provideGyroscopeCoordinatesPublishSubject() {
        if (gyroscopeCoordinatesPublishSubject == null) {
            gyroscopeCoordinatesPublishSubject = PublishSubject.create();
        }
        return gyroscopeCoordinatesPublishSubject;
    }

    public static BluetoothService provideBluetoothService() {
        if (bluetoothService == null) {
            bluetoothService = new BluetoothService(provideBluetoothReaderPublishSubject());
        }
        return bluetoothService;
    }

    public static PublishSubject<GyroscopeTranslation> provideGyroscopeTranslationPublishSubject() {
        if (gyroscopeTranslationPublishSubject == null) {
            gyroscopeTranslationPublishSubject = PublishSubject.create();
        }
        return gyroscopeTranslationPublishSubject;
    }
}
