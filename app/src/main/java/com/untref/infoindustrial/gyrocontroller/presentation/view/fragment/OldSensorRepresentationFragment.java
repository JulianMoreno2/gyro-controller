package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
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
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.OldSensorRepresentationPresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.Bounds;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.CubeRenderer;

import butterknife.ButterKnife;
import io.reactivex.Observable;


public class OldSensorRepresentationFragment extends Fragment implements OldSensorRepresentationPresenter.View {

    private GLSurfaceView glSurfaceView;
    private Observable<GyroscopeRotation> gyroscopeRotationObservable;
    private Observable<AccelerometerTranslation> accelerometerTranslationObservable;
    private OldSensorRepresentationPresenter sensorRepresentationPresenter;

    public OldSensorRepresentationFragment() {
        setHasOptionsMenu(true);
        gyroscopeRotationObservable = Provider.provideGyroscopeRotationPublishSubject();
        accelerometerTranslationObservable = Provider.provideAccelerometerTranslationPublishSubject();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorRepresentationPresenter = new OldSensorRepresentationPresenter(
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
        int width = display.getWidth();  // deprecated
        Log.d("ANCHO: ", String.valueOf(width));
        int height = display.getHeight();  // deprecated
        Log.d("ALTO: ", String.valueOf(height));
        if (height == 0) height = 1;            // To prevent divide by 0

        float minWidth = ((float) -width) / 1000.0f;
        float maxWidth = ((float) width) / 1000.0f;
        float minHeight = ((float) -height) / 1000.0f;
        float maxHeight = ((float) height) / 1000.0f;

        Bounds bounds = new Bounds(maxHeight, minHeight, maxWidth, minWidth);
        CubeRenderer renderer = new CubeRenderer(gyroscopeRotationObservable, accelerometerTranslationObservable, bounds);
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