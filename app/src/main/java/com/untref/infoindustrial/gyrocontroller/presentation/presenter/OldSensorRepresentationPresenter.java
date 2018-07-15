package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenAccelerometerTranslationFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeRotationFromBluetoothAction;

public class OldSensorRepresentationPresenter extends Presenter<OldSensorRepresentationPresenter.View> {

    private final ListenGyroscopeRotationFromBluetoothAction listenGyroscopeRotationFromBluetoothAction;
    private final ListenAccelerometerTranslationFromBluetoothAction listenAccelerometerTranslationFromBluetoothAction;

    public OldSensorRepresentationPresenter(
            ListenGyroscopeRotationFromBluetoothAction listenGyroscopeRotationFromBluetoothAction,
            ListenAccelerometerTranslationFromBluetoothAction listenAccelerometerTranslationFromBluetoothAction) {

        this.listenGyroscopeRotationFromBluetoothAction = listenGyroscopeRotationFromBluetoothAction;
        this.listenAccelerometerTranslationFromBluetoothAction = listenAccelerometerTranslationFromBluetoothAction;
    }

    public void onResume() {
        listenGyroscopeRotationFromBluetoothAction.execute();
        listenAccelerometerTranslationFromBluetoothAction.execute();
    }

    public void onPause() {

    }

    public interface View extends Presenter.View {

    }
}
