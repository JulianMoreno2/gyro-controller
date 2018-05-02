package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import android.content.Context;

import com.untref.infoindustrial.gyrocontroller.core.interactor.HomeInteractor;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.HomeActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.fragment.approach.BluetoothService;

public class HomePresenter extends Presenter<HomePresenter.View> {

    private HomeInteractor interactor;

    public HomePresenter(HomeInteractor interactor, BluetoothService bluetoothService) {
        this.interactor = interactor;
        bluetoothService.start(() -> getView().renderGyroscopeRepresentationActivity());
    }

    public void onEnableBluetooth(Context context, HomeActivity homeActivity) {
        boolean enableBluetooth = interactor.enableBluetooth(context, homeActivity);
        if (enableBluetooth) {
            getView().enableBluetooth();
        } else {
            getView().disableBluetooth();
        }
    }

    public boolean isBluetoothEnable() {
        return interactor.isBluetoothEnable();
    }

    public interface View extends Presenter.View {

        void enableBluetooth();

        void disableBluetooth();

        void renderGyroscopeRepresentationActivity();
    }
}