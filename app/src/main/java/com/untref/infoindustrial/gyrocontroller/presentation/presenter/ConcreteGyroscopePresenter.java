package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeCoordinatesToBluetoothWhenArrivesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendGyroscopeTranslationAction;
import com.untref.infoindustrial.gyrocontroller.core.action.SendRandomGyroscopeCoordinatesAction;
import com.untref.infoindustrial.gyrocontroller.core.action.StartGyroscope;
import com.untref.infoindustrial.gyrocontroller.core.sensor.Translation;

public class ConcreteGyroscopePresenter extends Presenter<ConcreteGyroscopePresenter.View> {

    private final StartGyroscope startGyroscope;
    private final SendGyroscopeCoordinatesToBluetoothWhenArrivesAction sendGyroscopeCoordinatesToBluetoothWhenArrives;
    private final SendRandomGyroscopeCoordinatesAction sendRandomGyroscopeCoordinates;
    private final SendGyroscopeTranslationAction sendGyroscopeTranslationAction;

    public ConcreteGyroscopePresenter(StartGyroscope startGyroscope,
                                      SendGyroscopeCoordinatesToBluetoothWhenArrivesAction sendGyroscopeCoordinatesToBluetoothWhenArrives,
                                      SendRandomGyroscopeCoordinatesAction sendRandomGyroscopeCoordinates,
                                      SendGyroscopeTranslationAction sendGyroscopeTranslationAction) {
        this.startGyroscope = startGyroscope;
        this.sendGyroscopeCoordinatesToBluetoothWhenArrives = sendGyroscopeCoordinatesToBluetoothWhenArrives;
        this.sendRandomGyroscopeCoordinates = sendRandomGyroscopeCoordinates;
        this.sendGyroscopeTranslationAction = sendGyroscopeTranslationAction;
    }

    public void onStart() {
        sendGyroscopeCoordinatesToBluetoothWhenArrives.execute()
                .subscribe();

        startGyroscope.execute()
                .subscribe();
    }

    public void onSendRandomGyroscopeCoordinates() {
        sendRandomGyroscopeCoordinates.execute()
                .doOnSuccess(message -> getView().sendRandomGyroscopeCoordinates(message))
                .subscribe();
    }

    public void onSendGyroscopeTranslation(Translation translation) {
        sendGyroscopeTranslationAction.execute(translation)
                .subscribe();
    }

    public interface View extends Presenter.View {

        void start();

        void sendRandomGyroscopeCoordinates(String message);
    }
}
