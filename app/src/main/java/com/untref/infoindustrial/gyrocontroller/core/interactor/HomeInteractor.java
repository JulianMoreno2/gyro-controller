package com.untref.infoindustrial.gyrocontroller.core.interactor;

import android.content.Context;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.HomeActivity;

public class HomeInteractor {

    private BluetoothClient bluetoothClient;

    public HomeInteractor(BluetoothClient bluetoothClient) {

        this.bluetoothClient = bluetoothClient;
    }

    public boolean enableBluetooth(Context context, HomeActivity homeActivity) {
        if (!bluetoothClient.isEnabled()) {
            bluetoothClient.setPermissions(context, homeActivity);
            return bluetoothClient.enable();
        }
        return !bluetoothClient.disable();
    }

    public boolean isBluetoothEnable() {
        return bluetoothClient.isEnabled();
    }
}
