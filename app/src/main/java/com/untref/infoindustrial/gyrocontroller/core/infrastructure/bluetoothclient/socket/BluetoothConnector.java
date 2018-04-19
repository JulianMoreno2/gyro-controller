package com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnector {

    private static BluetoothConnector instance;
    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothDevice device;
    private final Handler handler;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private boolean connected = false;

    public BluetoothConnector(BluetoothAdapter bluetoothAdapter, BluetoothDevice bluetoothDevice, Handler handler) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.device = bluetoothDevice;
        this.handler = handler;
    }

    /**
     * Connect to bluetooth device through a candidate uuid.
     *
     * @return
     */
    public void connect() {

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {

                Log.d("DEVICE", "Bluetooth enabled. Connecting...");
                bluetoothAdapter.cancelDiscovery();

                try {
                    ParcelUuid[] deviceUuids = device.getUuids();
                    int counter = 0;
                    do {
                        UUID uuid = deviceUuids[counter].getUuid();
                        Log.d("DEVICE", "UUID connect?: " + uuid.toString());
                        socket = device.createRfcommSocketToServiceRecord(uuid);
                        try {
                            socket.connect();
                        } catch (IOException e) {
                            Log.d("DEVICE", "Retry bluetooth connection");
                        }
                        counter++;
                    } while (!socket.isConnected() && counter < deviceUuids.length);
                    connected = true;

                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    new BluetoothReaderThread(inputStream, handler).start();

                    Log.d("DEVICE", "Connection success");

                } catch (IOException e) {
                    connected = false;
                    Log.d("DEVICE", "Connection Timeout");
                    e.printStackTrace();
                }

            } else {
                Log.d("DEVICE", "Cannot connection to bluetooth device.");
            }
        }
    }

    public void disconnect() {

        if (isConnected()) {
            try {
                if (inputStream != null) {
                    this.inputStream.close();
                }

                if (outputStream != null) {
                    this.outputStream.close();
                }

                if (this.socket != null) {
                    this.socket.close();
                }

                connected = false;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void send(String message) {
        try {
            outputStream.write(message.getBytes());
            SystemClock.sleep(200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BluetoothConnector create(BluetoothAdapter bluetoothAdapter, BluetoothDevice device, Handler handler) {
        if (instance == null) {
            instance = new BluetoothConnector(bluetoothAdapter, device, handler);
        }
        return instance;
    }

    public static BluetoothConnector getInstance() {
        return instance;
    }
}
