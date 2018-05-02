package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

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
                .subscribe();

        gyroscopeAction.execute()
                .subscribe();
    }

    public void onSendRandomGyroscopeCoordinates() {
        sendRandomGyroscopeCoordinates.execute()
                .doOnSuccess(message -> getView().sendRandomGyroscopeCoordinates(message))
                .subscribe();
    }

    public interface View extends Presenter.View {

        void start();

        void sendRandomGyroscopeCoordinates(String message);
    }
}
