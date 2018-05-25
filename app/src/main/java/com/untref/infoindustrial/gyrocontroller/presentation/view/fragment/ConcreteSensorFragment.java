package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.untref.infoindustrial.gyrocontroller.R;
import com.untref.infoindustrial.gyrocontroller.core.provider.ActionProvider;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.Translation;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.ConcreteSensorPresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.ConcreteSensorActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConcreteSensorFragment extends Fragment implements ConcreteSensorPresenter.View {

    @BindView(R.id.btn_start_gyroscope)
    Button btn_start_gyroscope;
    @BindView(R.id.btn_stop_gyroscope)
    Button btn_stop_gyroscope;
    @BindView(R.id.btn_start_accelerometer)
    Button btn_start_accelermeter;
    @BindView(R.id.btn_stop_accelerometer)
    Button btn_stop_accelermeter;
    @BindView(R.id.btn_send_coord)
    Button btn_send_coord;

    @BindView(R.id.btn_left)
    Button btn_left;
    @BindView(R.id.btn_right)
    Button btn_right;
    @BindView(R.id.btn_up)
    Button btn_up;
    @BindView(R.id.btn_down)
    Button btn_down;

    private ConcreteSensorPresenter concreteSensorPresenter;

    public ConcreteSensorFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(ConcreteSensorActivity.SENSOR_SERVICE);
        concreteSensorPresenter = new ConcreteSensorPresenter(
                ActionProvider.getStartGyroscopeAction(sensorManager),
                ActionProvider.getStartAccelerometerAction(sensorManager),
                ActionProvider.getSendGyroscopeRotationToBluetoothWhenArrivesAction(),
                ActionProvider.getSendRandomGyroscopeRotation(),
                ActionProvider.getSendAccelerometerTranslationAction(),
                ActionProvider.getSendAccelerometerTranslationToBluetoothWhenArrivesAction());

        concreteSensorPresenter.setView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_concrete_sensor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btn_start_gyroscope.setOnClickListener(v -> concreteSensorPresenter.onGyroscopeStart());
        btn_stop_gyroscope.setOnClickListener(v -> concreteSensorPresenter.onGyroscopeStop());
        btn_start_accelermeter.setOnClickListener(v -> concreteSensorPresenter.onAccelerometerStart());
        btn_stop_accelermeter.setOnClickListener(v -> concreteSensorPresenter.onAccelerometerStop());
        btn_send_coord.setOnClickListener(v -> concreteSensorPresenter.onSendRandomGyroscopeRotation());

        btn_left.setOnClickListener(v -> concreteSensorPresenter.onSendGyroscopeTranslation(Translation.LEFT));
        btn_right.setOnClickListener(v -> concreteSensorPresenter.onSendGyroscopeTranslation(Translation.RIGHT));
        btn_up.setOnClickListener(v -> concreteSensorPresenter.onSendGyroscopeTranslation(Translation.UP));
        btn_down.setOnClickListener(v -> concreteSensorPresenter.onSendGyroscopeTranslation(Translation.DOWN));
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void startGyroscope() {
        btn_start_gyroscope.setEnabled(false);
        btn_stop_gyroscope.setEnabled(true);
    }

    @Override
    public void stopGyroscope() {
        btn_start_gyroscope.setEnabled(true);
        btn_stop_gyroscope.setEnabled(false);
    }

    @Override
    public void startAccelerometer() {
        btn_start_accelermeter.setEnabled(false);
        btn_stop_accelermeter.setEnabled(true);
    }

    @Override
    public void stopAccelerometer() {
        btn_start_accelermeter.setEnabled(true);
        btn_stop_accelermeter.setEnabled(false);
    }
}
