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
import android.widget.TextView;

import com.untref.infoindustrial.gyrocontroller.R;
import com.untref.infoindustrial.gyrocontroller.core.provider.ActionProvider;
import com.untref.infoindustrial.gyrocontroller.core.sensor.Translation;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.ConcreteGyroscopePresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.activity.ConcreteGyroscopeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConcreteGyroscopeFragment extends Fragment implements ConcreteGyroscopePresenter.View {

    @BindView(R.id.btn_start)
    Button btn_start;
    @BindView(R.id.btn_send_coord)
    Button btn_send_coord;
    @BindView(R.id.log_textview)
    TextView logTextView;

    @BindView(R.id.btn_left)
    Button btn_left;
    @BindView(R.id.btn_right)
    Button btn_right;
    @BindView(R.id.btn_up)
    Button btn_up;
    @BindView(R.id.btn_down)
    Button btn_down;

    private ConcreteGyroscopePresenter concreteGyroscopePresenter;

    public ConcreteGyroscopeFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(ConcreteGyroscopeActivity.SENSOR_SERVICE);
        concreteGyroscopePresenter = new ConcreteGyroscopePresenter(
                ActionProvider.getStartGyroscopeAction(sensorManager),
                ActionProvider.getSendGyroscopeCoordinatesToBluetoothWhenArrivesAction(),
                ActionProvider.getSendRandomGyroscopeCoordinates(),
                ActionProvider.getSendGyroscopeTranslationToBluetoothAction());

        concreteGyroscopePresenter.setView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_concrete_gyroscope, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btn_start.setOnClickListener(v -> concreteGyroscopePresenter.onStart());
        btn_send_coord.setOnClickListener(v ->
                concreteGyroscopePresenter.onSendRandomGyroscopeCoordinates());

        btn_left.setOnClickListener(v -> concreteGyroscopePresenter.onSendGyroscopeTranslation(Translation.LEFT));
        btn_right.setOnClickListener(v -> concreteGyroscopePresenter.onSendGyroscopeTranslation(Translation.RIGHT));
        btn_up.setOnClickListener(v -> concreteGyroscopePresenter.onSendGyroscopeTranslation(Translation.UP));
        btn_down.setOnClickListener(v -> concreteGyroscopePresenter.onSendGyroscopeTranslation(Translation.DOWN));
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void start() {
        logTextView.append("STARTING\n");
        btn_start.setEnabled(false);
    }

    @Override
    public void sendRandomGyroscopeCoordinates(String message) {
        logTextView.append(message + "\n");
    }
}
