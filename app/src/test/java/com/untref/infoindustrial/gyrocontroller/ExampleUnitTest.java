package com.untref.infoindustrial.gyrocontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

import com.untref.infoindustrial.gyrocontroller.core.infrastructure.bluetoothclient.BluetoothClient;
import com.untref.infoindustrial.gyrocontroller.core.interactor.NavigateInteractor;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.NavigatePresenter;

import org.junit.Test;

import java.util.HashSet;
import java.util.UUID;

import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExampleUnitTest {

    @Test
    public void subscribeToIncommingMessage() throws Exception {

        BluetoothAdapter adapter = mock(BluetoothAdapter.class);
        when(adapter.enable()).thenReturn(true);
        when(adapter.isEnabled()).thenReturn(true);

        BluetoothDevice device = mock(BluetoothDevice.class);
        String deviceName = "Device";
        String deviceAddress = "Address";
        when(device.getName()).thenReturn(deviceName);
        when(device.getAddress()).thenReturn(deviceAddress);
        UUID uuid = UUID.randomUUID();
        ParcelUuid[] uuids = new ParcelUuid[1];
        ParcelUuid parcelUUID = new ParcelUuid(uuid);
        uuids[0] = parcelUUID;
        when(device.getUuids()).thenReturn(uuids);

        HashSet<BluetoothDevice> value = new HashSet<>();
        value.add(device);

        when(adapter.getBondedDevices()).thenReturn(value);
        when(adapter.cancelDiscovery()).thenReturn(true);
        when(adapter.startDiscovery()).thenReturn(true);

        PublishSubject<String> subject = PublishSubject.create();
        BluetoothClient client = new BluetoothClient(adapter, subject);
        NavigateInteractor interactor = new NavigateInteractor(client);
        NavigatePresenter navigatePresenter = new NavigatePresenter(interactor, subject);

        NavigatePresenter spyNavigatePresenter = spy(navigatePresenter);
        NavigatePresenter.View view = mock(NavigatePresenter.View.class);
        when(spyNavigatePresenter.getView()).thenReturn(view);

        navigatePresenter.readFromBluetooth();

        String expectedMessage = "Message";
        subject.onNext(expectedMessage);


        //verify(spyNavigatePresenter.getView()).writeIncommingMessage(expectedMessage);
    }
}