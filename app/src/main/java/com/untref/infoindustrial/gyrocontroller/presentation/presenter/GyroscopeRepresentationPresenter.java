package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeCoordinatesFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeTranslationFromBluetoothAction;

public class GyroscopeRepresentationPresenter extends Presenter<GyroscopeRepresentationPresenter.View> {

    private final ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction;
    private final ListenGyroscopeTranslationFromBluetoothAction listenGyroscopeTranslationFromBluetoothAction;

    public GyroscopeRepresentationPresenter(
            ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction,
            ListenGyroscopeTranslationFromBluetoothAction listenGyroscopeTranslationFromBluetoothAction) {

        this.listenGyroscopeCoordinatesFromBluetoothAction = listenGyroscopeCoordinatesFromBluetoothAction;
        this.listenGyroscopeTranslationFromBluetoothAction = listenGyroscopeTranslationFromBluetoothAction;
    }

    public void onResume() {
        listenGyroscopeCoordinatesFromBluetoothAction.execute();
        listenGyroscopeTranslationFromBluetoothAction.execute();
    }

    public void onPause() {

    }

    public interface View extends Presenter.View {

    }
}
