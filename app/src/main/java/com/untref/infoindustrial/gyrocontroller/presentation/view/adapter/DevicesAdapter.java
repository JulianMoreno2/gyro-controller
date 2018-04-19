package com.untref.infoindustrial.gyrocontroller.presentation.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.untref.infoindustrial.gyrocontroller.R;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.DevicesPresenter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {

    private List<BluetoothDevice> devices;
    private DevicesPresenter devicesPresenter;

    public DevicesAdapter(DevicesPresenter devicesPresenter) {
        this.devicesPresenter = devicesPresenter;
        devices = new ArrayList<>();
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DevicesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DevicesViewHolder holder, int position) {

        BluetoothDevice device = devices.get(position);
        holder.device = device;
        holder.textViewDeviceName.setText(device.getName());
        holder.textViewDeviceAddress.setText(device.getAddress());

        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            showStatusDevice(holder, "Device already paired", "UnPair", true);
        }

        holder.btnPair.setOnClickListener(v -> {
            Log.d("DEVICE", "Device state: " + device.getBondState());

            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                invokeBluetoothMethod(device, "removeBond");
                showStatusDevice(holder, "Unpairing device", "Pair", false);
            } else {
                invokeBluetoothMethod(device, "createBond");
                showStatusDevice(holder, "Pairing device", "UnPair", true);
            }
        });

        holder.btnConnect.setOnClickListener(v -> {
            devicesPresenter.connectToPairDevice(holder.device);
            devicesPresenter.getView().renderConcreteGyroscopeActivity();
        });
    }

    private void showStatusDevice(DevicesViewHolder holder, String msg, String btnText, boolean enableConnect) {
        Log.d("DEVICE", msg);
        holder.btnPair.setText(btnText);
        holder.btnConnect.setEnabled(enableConnect);
    }

    private void invokeBluetoothMethod(BluetoothDevice device, String action) {
        try {
            Method method = device.getClass().getMethod(action, (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setDevices(List<BluetoothDevice> devices) {
        this.devices = devices;
    }

    public static class DevicesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_device_name)
        TextView textViewDeviceName;

        @BindView(R.id.txt_device_address)
        TextView textViewDeviceAddress;

        @BindView(R.id.btn_pair)
        Button btnPair;

        @BindView(R.id.btn_connect)
        Button btnConnect;

        BluetoothDevice device;

        public DevicesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
