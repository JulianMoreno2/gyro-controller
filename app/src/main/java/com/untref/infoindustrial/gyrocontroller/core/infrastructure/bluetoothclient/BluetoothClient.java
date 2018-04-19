package com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.socket.BluetoothConnector;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.HomeActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.subjects.PublishSubject;

public class BluetoothClient {

    private BluetoothAdapter bluetoothAdapter;
    private PublishSubject<String> publishSubject;

    public BluetoothClient(BluetoothAdapter bluetoothAdapter, PublishSubject<String> publishSubject) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.publishSubject = publishSubject;
    }

    public boolean isEnabled() {
        return isCompatible() && bluetoothAdapter.isEnabled();
    }

    private boolean isCompatible() {
        return bluetoothAdapter != null;
    }

    public boolean enable() {
        return isCompatible() && bluetoothAdapter.enable();
    }

    public boolean disable() {
        return isCompatible() && bluetoothAdapter.disable();
    }

    public void setPermissions(Context context, HomeActivity activity) {
        // Android 6.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String accessCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
            switch (ContextCompat.checkSelfPermission(context,
                    accessCoarseLocation)) {
                case PackageManager.PERMISSION_DENIED:
                    if (ContextCompat.checkSelfPermission(context,
                            accessCoarseLocation) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{accessCoarseLocation}, 1);
                    }
                    break;
                case PackageManager.PERMISSION_GRANTED:
                    break;
            }
        }
    }

    public void startDiscovery() {
        bluetoothAdapter.startDiscovery();
    }

    public Set<BluetoothDevice> getBoundedDevices() {
        return new HashSet<>(bluetoothAdapter.getBondedDevices());
    }

    public void connectToPairDevice(BluetoothDevice device) {
        Handler handler = new Handler(message -> {
            String s = (String) message.obj;
            publishSubject.onNext(s);
            return true;
        });

        BluetoothConnector.create(bluetoothAdapter, device, handler).connect();
    }

    public void send(String navigateMessage) {
        BluetoothConnector instance = BluetoothConnector.getInstance();
        if (instance.isConnected()) {
            instance.send(navigateMessage);
        } else {
            Log.d("DEVICE", "Bluetooth is disconnected");
        }
    }

    public void disconnect(String navigateMessage){
        BluetoothConnector instance = BluetoothConnector.getInstance();
        if (instance.isConnected()) {
            Log.d("DEVICE", "Send -> " + navigateMessage);
            instance.send(navigateMessage);
            instance.disconnect();
        }
    }
}
