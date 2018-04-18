package com.untref.robotica.robotcontroller.core.interactor;

import android.content.Context;

import com.untref.robotica.robotcontroller.core.bluetoothclient.BluetoothClient;
import com.untref.robotica.robotcontroller.presentation.view.activity.HomeActivity;

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
