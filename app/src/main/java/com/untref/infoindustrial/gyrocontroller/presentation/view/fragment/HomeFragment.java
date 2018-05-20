package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.untref.infoindustrial.gyrocontroller.R;
import com.untref.infoindustrial.gyrocontroller.core.provider.Provider;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.HomePresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.DevicesActivity;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.SensorRepresentationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements HomePresenter.View {

    @BindView(R.id.btn_client)
    Button btn_client;

    private HomePresenter homePresenter;

    public HomeFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePresenter = new HomePresenter(Provider.provideBluetoothService());
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

        enableBluetooth();

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
        btn_client.setEnabled(true);
    }

    @Override
    public void renderGyroscopeRepresentationActivity() {
        Intent intent = new Intent(getActivity(), SensorRepresentationActivity.class);
        startActivity(intent);
    }

    @Override
    public Context context() {
        return getContext();
    }
}
