package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.SendAccelerometerTranslationToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeRotationToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendAccelerometerTranslationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeRotationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.StartAccelerometer;
import com.untref.infoindustrial.gyrocontroller.core.action.StartGyroscope;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.Translation;

import io.reactivex.disposables.Disposable;

public class ConcreteSensorPresenter extends Presenter<ConcreteSensorPresenter.View> {

    private final StartGyroscope startGyroscope;
    private final StartAccelerometer startAccelerometer;
    private final SendGyroscopeRotationToBluetoothWhenArrivesAction sendGyroscopeRotationToBluetoothWhenArrives;
    private final SendRandomGyroscopeRotationAction sendRandomGyroscopeRotation;
    private final SendAccelerometerTranslationAction sendAccelerometerTranslationAction;
    private final SendAccelerometerTranslationToBluetoothWhenArrivesAction sendAccelerometerTranslationToBluetoothWhenArrives;
    private Disposable accelerometerDisposable;
    private Disposable gyroscopeDisposable;

    public ConcreteSensorPresenter(StartGyroscope startGyroscope,
                                   StartAccelerometer startAccelerometer,
                                   SendGyroscopeRotationToBluetoothWhenArrivesAction sendGyroscopeRotationToBluetoothWhenArrives,
                                   SendRandomGyroscopeRotationAction sendRandomGyroscopeRotation,
                                   SendAccelerometerTranslationAction sendAccelerometerTranslationAction,
                                   SendAccelerometerTranslationToBluetoothWhenArrivesAction sendAccelerometerTranslationToBluetoothWhenArrives) {

        this.startGyroscope = startGyroscope;
        this.startAccelerometer = startAccelerometer;
        this.sendGyroscopeRotationToBluetoothWhenArrives = sendGyroscopeRotationToBluetoothWhenArrives;
        this.sendRandomGyroscopeRotation = sendRandomGyroscopeRotation;
        this.sendAccelerometerTranslationAction = sendAccelerometerTranslationAction;
        this.sendAccelerometerTranslationToBluetoothWhenArrives = sendAccelerometerTranslationToBluetoothWhenArrives;
    }

    public void onGyroscopeStart() {
        getView().startGyroscope();

        gyroscopeDisposable = sendGyroscopeRotationToBluetoothWhenArrives.execute().subscribe();
        startGyroscope.executeStart().subscribe();
    }

    public void onGyroscopeStop() {
        getView().stopGyroscope();

        if(!gyroscopeDisposable.isDisposed()) {
            gyroscopeDisposable.dispose();
        }

        startGyroscope.executeStop();
    }

    public void onAccelerometerStart() {
        getView().startAccelerometer();

        accelerometerDisposable = sendAccelerometerTranslationToBluetoothWhenArrives.execute().subscribe();
        startAccelerometer.execute().subscribe();
    }

    public void onAccelerometerStop() {
        getView().stopAccelerometer();

        if(!accelerometerDisposable.isDisposed()) {
            accelerometerDisposable.dispose();
        }

        startAccelerometer.executeStop().subscribe();
    }

    public void onSendRandomGyroscopeRotation() {
        sendRandomGyroscopeRotation.execute()
                .subscribe();
    }

    public void onSendGyroscopeTranslation(Translation translation) {
        sendAccelerometerTranslationAction.execute(translation)
                .subscribe();
    }

    public interface View extends Presenter.View {

        void startGyroscope();

        void stopGyroscope();

        void startAccelerometer();

        void stopAccelerometer();

    }
}
