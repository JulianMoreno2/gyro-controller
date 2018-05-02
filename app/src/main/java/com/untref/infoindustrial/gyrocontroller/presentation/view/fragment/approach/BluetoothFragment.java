package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment.approach;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.untref.infoindustrial.gyrocontroller.core.provider.Provider;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.ConcreteGyroscopeActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.DevicesActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.GyroscopeRepresentationActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.fragment.DevicesFragment;

import io.reactivex.subjects.PublishSubject;

public class BluetoothFragment extends Activity {

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Member object for the chat services
    private BluetoothService mChatService = null;

    private PublishSubject<String> bluetoothMessagePublishSubject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothMessagePublishSubject = Provider.provideBluetoothReaderPublishSubject();

        startDevicesActivity();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    private void startDevicesActivity() {
        Intent intent = new Intent(this, DevicesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mChatService == null) mChatService = Provider.provideBluetoothService();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                mChatService.start(() -> startGyroscopeRepresentationActivity());
            }
        }
    }

    private void startGyroscopeRepresentationActivity() {
        Intent intent = new Intent(this, GyroscopeRepresentationActivity.class);
        startActivity(intent);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DevicesFragment.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    //mChatService.connect(device);

                    startConcreteGyroscopeActivity();
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    mChatService = Provider.provideBluetoothService();
                } else {
                    // User did not enable Bluetooth or an error occured
                    finish();
                }
        }
    }

    private void startConcreteGyroscopeActivity() {
        Intent intent = new Intent(this, ConcreteGyroscopeActivity.class);
        startActivity(intent);
    }
}