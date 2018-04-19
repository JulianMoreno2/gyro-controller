package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;

import com.untref.infoindustrial.gyrocontroller.core.interactor.DevicesInteractor;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.receiver.BluetoothReceiver;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.receiver.PairReceiver;
import com.untref.infoindustrial.gyrocontroller.presentation.view.adapter.DevicesAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<BluetoothDevice> getDevices() {
        //Set<BluetoothDevice> founded = devicesInteractor.getDevices();
        Set<BluetoothDevice> bounded =  devicesInteractor.getBoundedDevices();

        Set<BluetoothDevice> devices = new HashSet<>();
        //devices.addAll(founded);
        devices.addAll(bounded);

        return devices;
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

    public Set<BluetoothDevice> getBoundedDevices() {
        return devicesInteractor.getBoundedDevices();
    }

    public void addDevice(BluetoothDevice device) {
        devicesInteractor.addDevice(device);
    }

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showDevicesNotFoundMessage();

        void renderDevices(Set<BluetoothDevice> devices);

        void pairDevice();

        void unPairDevice();

        void renderConcreteGyroscopeActivity();
    }
}
