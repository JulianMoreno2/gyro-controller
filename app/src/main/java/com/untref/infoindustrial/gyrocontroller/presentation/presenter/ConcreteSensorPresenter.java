package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.SendAccelerometerTranslationToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeRotationToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendAccelerometerTranslationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeRotationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.StartAccelerometer;
import com.untref.infoindustrial.gyrocontroller.core.action.StartGyroscope;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.Translation;

public class ConcreteSensorPresenter extends Presenter<ConcreteSensorPresenter.View> {

    private final StartGyroscope startGyroscope;
    private final StartAccelerometer startAccelerometer;
    private final SendGyroscopeRotationToBluetoothWhenArrivesAction sendGyroscopeRotationToBluetoothWhenArrives;
    private final SendRandomGyroscopeRotationAction sendRandomGyroscopeRotation;
    private final SendAccelerometerTranslationAction sendAccelerometerTranslationAction;
    private final SendAccelerometerTranslationToBluetoothWhenArrivesAction sendAccelerometerTranslationToBluetoothWhenArrives;

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

        sendGyroscopeRotationToBluetoothWhenArrives.execute().subscribe();
        startGyroscope.execute().subscribe();
    }

    public void onAccelerometerStart() {

        getView().startAccelerometer();

        sendAccelerometerTranslationToBluetoothWhenArrives.execute().subscribe();
        startAccelerometer.execute().subscribe();
    }

    public void onSendRandomGyroscopeRotation() {
        sendRandomGyroscopeRotation.execute()
                .doOnSuccess(message -> getView().sendRandomGyroscopeRotation(message))
                .subscribe();
    }

    public void onSendGyroscopeTranslation(Translation translation) {
        sendAccelerometerTranslationAction.execute(translation)
                .subscribe();
    }

    public interface View extends Presenter.View {

        void startGyroscope();

        void startAccelerometer();

        void sendRandomGyroscopeRotation(String message);
    }
}
