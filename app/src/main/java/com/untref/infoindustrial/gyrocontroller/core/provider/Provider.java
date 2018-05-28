package com.untref.infoindustrial.gyrocontroller.core.provider;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;

import io.reactivex.subjects.PublishSubject;

public class Provider {

    private static PublishSubject<String> bluetoothReaderPublishSubject;
    private static PublishSubject<GyroscopeRotation> gyroscopeRotationPublishSubject;
    private static PublishSubject<AccelerometerTranslation> accelerometerTranslationPublishSubject;
    private static BluetoothService bluetoothService;

    public static PublishSubject<String> provideBluetoothReaderPublishSubject() {
        if (bluetoothReaderPublishSubject == null) {
            bluetoothReaderPublishSubject = PublishSubject.create();
        }
        return bluetoothReaderPublishSubject;
    }

    public static PublishSubject<GyroscopeRotation> provideGyroscopeRotationPublishSubject() {
        if (gyroscopeRotationPublishSubject == null) {
            gyroscopeRotationPublishSubject = PublishSubject.create();
        }
        return gyroscopeRotationPublishSubject;
    }

    public static BluetoothService provideBluetoothService() {
        if (bluetoothService == null) {
            bluetoothService = new BluetoothService(provideBluetoothReaderPublishSubject());
        }
        return bluetoothService;
    }

    public static PublishSubject<AccelerometerTranslation> provideAccelerometerTranslationPublishSubject() {
        if (accelerometerTranslationPublishSubject == null) {
            accelerometerTranslationPublishSubject = PublishSubject.create();
        }
        return accelerometerTranslationPublishSubject;
    }
}
