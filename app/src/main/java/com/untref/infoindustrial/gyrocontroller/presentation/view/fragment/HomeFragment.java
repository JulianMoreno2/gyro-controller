package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.untref.infoindustrial.gyrocontroller.R;
import com.untref.infoindustrial.gyrocontroller.core.interactor.HomeInteractor;
import com.untref.infoindustrial.gyrocontroller.core.provider.Provider;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.HomePresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.ConcreteGyroscopeActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.DevicesActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.GyroscopeRepresentationActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

public class HomeFragment extends Fragment implements HomePresenter.View {

    @BindView(R.id.txt_status)
    TextView txt_status;
    @BindView(R.id.btn_enable)
    Button btn_enable;
    @BindView(R.id.btn_server)
    Button btn_server;
    @BindView(R.id.btn_client)
    Button btn_client;

    private HomePresenter homePresenter;

    public HomeFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePresenter = new HomePresenter(
                new HomeInteractor(Provider.provideBluetoothClient()),
                Provider.provideBluetoothService());
        homePresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (homePresenter.isBluetoothEnable()) {
            enableBluetooth();
        } else {
            disableBluetooth();
        }

        btn_enable.setOnClickListener(v ->
                homePresenter.onEnableBluetooth(getContext(), (HomeActivity) getActivity()));

        btn_server.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GyroscopeRepresentationActivity.class);
            startActivity(intent);
        });

        btn_client.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DevicesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void enableBluetooth() {

        txt_status.setText("Bluetooth is On");
        txt_status.setTextColor(Color.GREEN);

        btn_enable.setText("Disable");
        btn_enable.setEnabled(true);

        btn_server.setEnabled(true);
        btn_client.setEnabled(true);
    }

    @Override
    public void disableBluetooth() {

        txt_status.setText("Bluetooth is Off");
        txt_status.setTextColor(Color.RED);

        btn_enable.setText("Enable");
        btn_enable.setEnabled(true);

        btn_server.setEnabled(false);
        btn_client.setEnabled(false);
    }

    @Override
    public void renderGyroscopeRepresentationActivity() {
        Intent intent = new Intent(getActivity(), GyroscopeRepresentationActivity.class);
        startActivity(intent);
    }

    @Override
    public Context context() {
        return getContext();
    }
}
