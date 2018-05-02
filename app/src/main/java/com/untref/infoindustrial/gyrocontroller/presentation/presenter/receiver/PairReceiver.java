package com.untref.infoindustrial.gyrocontroller.presentation.presenter.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.presentation.presenter.DevicesPresenter;

public class PairReceiver extends BroadcastReceiver {

    private DevicesPresenter devicesPresenter;

    public PairReceiver(DevicesPresenter devicesPresenter) {
        this.devicesPresenter = devicesPresenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

            if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                Log.d("DEVICE", "Device paired");
                //devicesPresenter.getView().pairDevice();
            } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                Log.d("DEVICE", "Device paired");
                devicesPresenter.getView().unPairDevice();
            }
        }
        devicesPresenter.getDevicesAdapter().notifyDataSetChanged();
    }
}
