package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.untref.infoindustrial.gyrocontroller.core.provider.ActionProvider;
import com.untref.infoindustrial.gyrocontroller.core.provider.Provider;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.SensorRepresentationPresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.CubeRenderer;

import butterknife.ButterKnife;
import io.reactivex.Observable;

public class SensorRepresentationFragment extends Fragment implements SensorRepresentationPresenter.View {

    private GLSurfaceView glSurfaceView;
    private Observable<GyroscopeRotation> gyroscopeRotationObservable;
    private Observable<AccelerometerTranslation> accelerometerTranslationObservable;
    private SensorRepresentationPresenter sensorRepresentationPresenter;

    public SensorRepresentationFragment() {
        setHasOptionsMenu(true);
        gyroscopeRotationObservable = Provider.provideGyroscopeRotationPublishSubject();
        accelerometerTranslationObservable = Provider.provideAccelerometerTranslationPublishSubject();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorRepresentationPresenter = new SensorRepresentationPresenter(
                ActionProvider.getListenGyroscopeRotationFromBluetoothAction(),
                ActionProvider.getListenAccelerometerTranslationFromBluetoothAction());
        sensorRepresentationPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        this.sensorRepresentationPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        this.sensorRepresentationPresenter.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CubeRenderer renderer = new CubeRenderer(gyroscopeRotationObservable, accelerometerTranslationObservable);
        glSurfaceView = new GLSurfaceView(getActivity());
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);

        return glSurfaceView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public Context context() {
        return getContext();
    }
}