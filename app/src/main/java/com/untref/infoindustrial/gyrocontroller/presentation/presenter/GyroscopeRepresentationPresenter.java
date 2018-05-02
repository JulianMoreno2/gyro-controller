package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeCoordinatesFromBluetoothAction;

public class GyroscopeRepresentationPresenter extends Presenter<GyroscopeRepresentationPresenter.View> {

    private final ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction;

    public GyroscopeRepresentationPresenter(
            ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction) {

        this.listenGyroscopeCoordinatesFromBluetoothAction = listenGyroscopeCoordinatesFromBluetoothAction;
    }

    public void onResume() {
        listenGyroscopeCoordinatesFromBluetoothAction.execute();
    }

    public void onPause() {

    }

    public interface View extends Presenter.View {

    }
}
