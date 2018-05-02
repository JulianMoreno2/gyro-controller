package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.core.action.GyroscopeAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeCoordinatesAction;

public class ConcreteGyroscopePresenter extends Presenter<ConcreteGyroscopePresenter.View> {

    private final GyroscopeAction gyroscopeAction;
    private final SendGyroscopeCoordinatesToBluetoothWhenArrivesAction sendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
    private final SendRandomGyroscopeCoordinatesAction sendRandomGyroscopeCoordinates;

    public ConcreteGyroscopePresenter(GyroscopeAction gyroscopeAction,
                                      SendGyroscopeCoordinatesToBluetoothWhenArrivesAction sendGyroscopeCoordinatesToBluetoothWhenArrivesAction,
                                      SendRandomGyroscopeCoordinatesAction sendRandomGyroscopeCoordinates) {
        this.gyroscopeAction = gyroscopeAction;
        this.sendGyroscopeCoordinatesToBluetoothWhenArrivesAction = sendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
        this.sendRandomGyroscopeCoordinates = sendRandomGyroscopeCoordinates;
    }

    public void onStart() {
        sendGyroscopeCoordinatesToBluetoothWhenArrivesAction.execute()
                .flatMapCompletable(s -> gyroscopeAction.executeStart())
                .doOnComplete(() -> getView().start())
                .doOnComplete(() -> log("onStart"))
                .subscribe();
    }

    public void onStop() {
        gyroscopeAction.executeStop()
                .doOnComplete(() -> getView().stop())
                .doOnComplete(() -> log("onStop"))
                .subscribe();
    }

    public void onSendRandomGyroscopeCoordinates() {
        sendRandomGyroscopeCoordinates.execute()
                .doOnSuccess(message -> getView().sendRandomGyroscopeCoordinates(message) )
                .subscribe();
    }

    private int log(String message) {
        return Log.d("DEVICE", message);
    }

    public interface View extends Presenter.View {

        void start();

        void stop();

        void sendRandomGyroscopeCoordinates(String message);
    }
}
