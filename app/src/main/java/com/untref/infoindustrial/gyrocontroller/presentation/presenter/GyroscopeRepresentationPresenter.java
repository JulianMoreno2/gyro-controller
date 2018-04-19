package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenBluetoothDeviceConnectionAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeCoordinatesFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.socket.ReceiverBluetoothSocketConnectionThread;

import io.reactivex.disposables.Disposable;

public class GyroscopeRepresentationPresenter extends Presenter<GyroscopeRepresentationPresenter.View> {

    private final ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction;
    private final ListenBluetoothDeviceConnectionAction listenBluetoothDeviceConnection;
    private final ReceiverBluetoothSocketConnectionThread receiverBluetoothSocketConnectionThread;
    private Disposable disposable;

    public GyroscopeRepresentationPresenter(
            ListenGyroscopeCoordinatesFromBluetoothAction listenGyroscopeCoordinatesFromBluetoothAction,
            ListenBluetoothDeviceConnectionAction listenBluetoothDeviceConnection,
            ReceiverBluetoothSocketConnectionThread receiverBluetoothSocketConnectionThread) {

        this.listenGyroscopeCoordinatesFromBluetoothAction = listenGyroscopeCoordinatesFromBluetoothAction;
        this.listenBluetoothDeviceConnection = listenBluetoothDeviceConnection;
        this.receiverBluetoothSocketConnectionThread = receiverBluetoothSocketConnectionThread;
    }

    public void onResume() {
        disposable = listenBluetoothDeviceConnection.execute()
                .flatMapObservable(bluetoothDevice -> listenGyroscopeCoordinatesFromBluetoothAction.execute())
                .subscribe();

        receiverBluetoothSocketConnectionThread.run();
    }

    public void onPause() {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        receiverBluetoothSocketConnectionThread.cancel();
    }

    public interface View extends Presenter.View {

    }
}
