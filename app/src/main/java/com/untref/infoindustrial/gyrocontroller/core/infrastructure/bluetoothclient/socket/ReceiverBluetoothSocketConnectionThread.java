package com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import io.reactivex.subjects.PublishSubject;

/**
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs until a connection is accepted
 * (or until cancelled).
 */
public class ReceiverBluetoothSocketConnectionThread extends Thread {

    // Debugging
    private static final String TAG = "BluetoothChatService";

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // Constants that indicate the current connection state
    private static final int STATE_NONE = 0;       // we're doing nothing
    private static final int STATE_LISTEN = 1;     // now listening for incoming connections
    private static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    private static final int STATE_CONNECTED = 3;  // now connected to a remote device

    // Member fields
    private int state;
    private int newState;

    // The local server socket
    private final BluetoothServerSocket bluetoothServerSocket;
    private String mSocketType;
    private final BluetoothAdapter bluetoothAdapter;
    private final PublishSubject<BluetoothDevice> bluetoothDevicePublishSubject;

    public ReceiverBluetoothSocketConnectionThread(
            BluetoothAdapter bluetoothAdapter,
            PublishSubject<BluetoothDevice> bluetoothDevicePublishSubject) {

        this.bluetoothAdapter = bluetoothAdapter;
        this.bluetoothDevicePublishSubject = bluetoothDevicePublishSubject;

        BluetoothServerSocket tmp = null;

        // Create a new listening server socket
        try {
            tmp = this.bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
        }
        bluetoothServerSocket = tmp;
        state = STATE_LISTEN;
    }

    public void run() {
        Log.d(TAG, "Socket Type: " + mSocketType + "BEGIN mAcceptThread" + this);
        setName("AcceptThread" + mSocketType);

        BluetoothSocket socket = null;

        // Listen to the server socket if we're not connected
        while (state != STATE_CONNECTED) {
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                synchronized (ReceiverBluetoothSocketConnectionThread.this) {
                    switch (state) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            bluetoothDevicePublishSubject.onNext(socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }

        Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
    }

    public void cancel() {
        Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
        try {
            bluetoothServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
        }
    }

}
