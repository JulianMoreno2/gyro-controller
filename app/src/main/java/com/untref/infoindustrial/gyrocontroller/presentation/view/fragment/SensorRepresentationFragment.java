package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.untref.infoindustrial.gyrocontroller.core.provider.ActionProvider;
import com.untref.infoindustrial.gyrocontroller.core.provider.Provider;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.SensorRepresentationPresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.Bounds;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.CubeRenderer;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


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
        //calculo maximo de pantalla
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth(); // deprecated
        Log.d(": ", "WIDTH: " + String.valueOf(width));
        int height = display.getHeight(); // deprecated
        Log.d("HEIGHT: ", String.valueOf(height));
        if (height == 0) height = 1; // To prevent divide by 0

        float minWidth = -width / 1000.0f;
        float maxWidth = width / 1000.0f;
        float minHeight = -height / 1000.0f;
        float maxHeight = height / 1000.0f;

        Bounds bounds = new Bounds(maxHeight, minHeight, maxWidth, minWidth);
        CubeRenderer renderer = new CubeRenderer(gyroscopeRotationObservable, accelerometerTranslationObservable, bounds, createVibrator());
        glSurfaceView = new GLSurfaceView(getActivity());
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);

        return glSurfaceView;
    }

    private Action createVibrator() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        return () -> v.vibrate(400);
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