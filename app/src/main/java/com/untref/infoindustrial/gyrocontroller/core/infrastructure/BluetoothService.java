package com.untref.infoindustrial.gyrocontroller.core.infrastructure;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.reactivex.functions.Action;
import io.reactivex.subjects.PublishSubject;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {

    // Name for the SDP record when creating server socket
    private static final String NAME = "";

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private final CustomBluetoothAdapter bluetoothAdapter;
    private final Handler handler;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private PublishSubject<String> bluetoothMessagePublishSubject;

    private int state;
    // Constants that indicate the current connection state
    private static final int STATE_NONE = 0;       // we're doing nothing
    private static final int STATE_LISTEN = 1;     // now listening for incoming connections
    private static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    private static final int STATE_CONNECTED = 3;  // now connected to a remote device

    // Message types sent from the BluetoothService Handler
    private static final int MESSAGE_STATE_CHANGE = 1;
    private static final int MESSAGE_READ = 2;
    private static final int MESSAGE_WRITE = 3;
    private static final int MESSAGE_DEVICE_NAME = 4;

    public BluetoothService(PublishSubject<String> bluetoothMessagePublishSubject) {
        this.bluetoothMessagePublishSubject = bluetoothMessagePublishSubject;
        bluetoothAdapter = new CustomBluetoothAdapter();
        state = STATE_NONE;
        handler = createHandler();
    }

    private Handler createHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_WRITE:
                        break;
                    case MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        bluetoothMessagePublishSubject.onNext(readMessage);
                        break;
                    case MESSAGE_DEVICE_NAME:
                        break;
                }
            }
        };
    }

    public boolean isEnabled() {
        return isCompatible() && bluetoothAdapter.isEnabled();
    }

    private boolean isCompatible() {
        return bluetoothAdapter != null;
    }

    public void startDiscovery() {
        bluetoothAdapter.startDiscovery();
    }

    public Set<BluetoothDevice> getBoundedDevices() {
        return new HashSet<>(bluetoothAdapter.getBondedDevices());
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        this.state = state;
        // Give the new state to the Handler so the UI Activity can update
        handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Start the chat service. Specifically startGyroscope AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start(Action startGyroscopeRepresentationFragment) {
        // Cancel any thread attempting to make a connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        // Start the thread to listen on a BluetoothServerSocket
        if (acceptThread == null) {
            acceptThread = new AcceptThread(startGyroscopeRepresentationFragment);
            acceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device, Action startConcreteGyroscope) {
        // Cancel any thread attempting to make a connection
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }
        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device, startConcreteGyroscope);
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread that completed the connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        // Cancel the accept thread because we only want to connect to one device
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (state != STATE_CONNECTED) return;
            r = connectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_LISTEN);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState(STATE_LISTEN);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private Action startGyroscopeRepresentationFragment;

        public AcceptThread(Action startGyroscopeRepresentationFragment) {
            this.startGyroscopeRepresentationFragment = startGyroscopeRepresentationFragment;
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mmServerSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            // Listen to the server socket if we're not connected
            while (state != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (Exception e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothService.this) {
                        switch (state) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                try {
                                    this.startGyroscopeRepresentationFragment.run();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private Action startConcreteGyroscope;

        public ConnectThread(BluetoothDevice device, Action startConcreteGyroscope) {
            mmDevice = device;
            this.startConcreteGyroscope = startConcreteGyroscope;
            BluetoothSocket tmp = null;
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            setName("ConnectThread");
            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
                startConcreteGyroscope.run();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException ignored) {
                }
                // Start the service over to restart listening mode
                BluetoothService.this.start(() -> {
                });
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                connectThread = null;
            }
            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // Share the sent message back to the UI Activity
                handler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class CustomBluetoothAdapter {

        private final boolean enabled;
        private final BluetoothAdapter defaultAdapter;

        public CustomBluetoothAdapter() {
            defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            enabled = defaultAdapter != null;
        }


        public boolean isEnabled() {
            return enabled && defaultAdapter.isEnabled();
        }

        public void startDiscovery() {
            if (enabled) defaultAdapter.startDiscovery();
        }


        public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String name, UUID myUuid) throws IOException {
            return enabled ? defaultAdapter.listenUsingRfcommWithServiceRecord(name, myUuid) : null;
        }

        public Set<BluetoothDevice> getBondedDevices() {
            return enabled ? defaultAdapter.getBondedDevices() : new HashSet<>();
        }

        public void cancelDiscovery() {
            if (enabled) defaultAdapter.cancelDiscovery();
        }
    }
}