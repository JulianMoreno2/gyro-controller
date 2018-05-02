package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.BluetoothService;

public class HomePresenter extends Presenter<HomePresenter.View> {

    public HomePresenter(BluetoothService bluetoothService) {
        bluetoothService.start(() -> getView().renderGyroscopeRepresentationActivity());
    }


    public interface View extends Presenter.View {

        void enableBluetooth();

        void renderGyroscopeRepresentationActivity();
    }
}