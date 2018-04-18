package com.untref.robotica.robotcontroller.presentation.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;

import com.untref.robotica.robotcontroller.core.interactor.DevicesInteractor;
import com.untref.robotica.robotcontroller.presentation.presenter.receiver.BluetoothReceiver;
import com.untref.robotica.robotcontroller.presentation.presenter.receiver.PairReceiver;
import com.untref.robotica.robotcontroller.presentation.view.adapter.DevicesAdapter;

import java.util.List;

public class DevicesPresenter extends Presenter<DevicesPresenter.View> {

    private final DevicesInteractor devicesInteractor;
    private final DevicesAdapter devicesAdapter;

    private BroadcastReceiver pairReceiver;
    private BroadcastReceiver bluetoothReceiver;

    public DevicesPresenter(Context context, DevicesInteractor devicesInteractor) {
        this.devicesInteractor = devicesInteractor;

        devicesAdapter = new DevicesAdapter(this);

        registerBluetoothDiscoverReceiver(context);
        registerBluetoothPairReceiver(context);
    }

    private void registerBluetoothPairReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        pairReceiver = new PairReceiver(this);
        context.registerReceiver(pairReceiver, filter);
    }

    private void registerBluetoothDiscoverReceiver(Context context) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        bluetoothReceiver = new BluetoothReceiver(this);
        context.registerReceiver(bluetoothReceiver, filter);
    }

    public void onStartDiscovery() {
        devicesInteractor.startDiscovery();
    }

    public List<BluetoothDevice> getDevices() {
        return devicesInteractor.getDevices();
    }

    public RecyclerView.Adapter getDevicesAdapter() {
        return devicesAdapter;
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(bluetoothReceiver);
        context.unregisterReceiver(pairReceiver);
    }

    public void connectToPairDevice(BluetoothDevice device) {
        this.devicesInteractor.connectToPairDevice(device);
    }

    public List<BluetoothDevice> getBoundedDevices() {
        return devicesInteractor.getBoundedDevices();
    }

    public void addDevice(BluetoothDevice device) {
        devicesInteractor.addDevice(device);
    }

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showDevicesNotFoundMessage();

        void renderDevices(List<BluetoothDevice> devices);

        void pairDevice();

        void unPairDevice();

        void renderNavigateActivity();
    }
}
