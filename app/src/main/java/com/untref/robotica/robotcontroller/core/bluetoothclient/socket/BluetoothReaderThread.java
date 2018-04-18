package com.untref.robotica.robotcontroller.core.bluetoothclient.socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class BluetoothReaderThread extends Thread {

    private final InputStream inputStream;
    private final Handler readHandler;

    private static final String TAG = "DEVICE";
    private static final char DELIMITER = '\n';
    private String rx_buffer = "";

    public BluetoothReaderThread(InputStream inputStream, Handler handler) {
        this.inputStream = inputStream;
        this.readHandler = handler;
    }

    public void run() {
        // Entry point when thread.start() is called.
        while (!this.isInterrupted()) {

            if ((inputStream == null)) {
                Log.e(TAG, "Lost bluetooth connection!");
                break;
            }

            String s;
            try {
                s = read();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (s.length() > 0)
                rx_buffer += s;

            parseMessages();
        }
    }

    private String read() throws IOException {
        //Return data read from the socket, or a blank string.
        String s = "";

        if (inputStream.available() > 0) {

            byte[] inBuffer = new byte[1024];
            int bytesRead = inputStream.read(inBuffer);

            s = new String(inBuffer, "ASCII");
            s = s.substring(0, bytesRead);
        }

        return s;
    }

    private void parseMessages() {

        // Find the first delimiter in the buffer
        int inx = rx_buffer.indexOf(DELIMITER);

        // If there is none, exit
        if (inx == -1)
            return;

        // Get the complete message
        String s = rx_buffer.substring(0, inx);

        // Remove the message from the buffer
        rx_buffer = rx_buffer.substring(inx + 1);

        // Send to read handler
        sendToReadHandler(s);

        // Look for more complete messages
        parseMessages();
    }

    private void sendToReadHandler(String s) {
        //Pass a message to the read handler.
        Message msg = Message.obtain();
        msg.obj = s + "\r\n";
        readHandler.sendMessage(msg);
        Log.i(TAG, "[RECV] " + s);
    }
}