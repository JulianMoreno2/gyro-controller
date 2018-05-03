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
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeTranslation;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.GyroscopeRepresentationPresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.CubeRenderer;

import butterknife.ButterKnife;
import io.reactivex.Observable;

public class GyroscopeRepresentationFragment extends Fragment implements GyroscopeRepresentationPresenter.View {

    private GLSurfaceView glSurfaceView;
    private Observable<GyroscopeCoordinates> gyroscopeCoordinatesObservable;
    private Observable<GyroscopeTranslation> gyroscopeTranslationObservable;
    private GyroscopeRepresentationPresenter gyroscopeRepresentationPresenter;

    public GyroscopeRepresentationFragment() {
        setHasOptionsMenu(true);
        gyroscopeCoordinatesObservable = Provider.provideGyroscopeCoordinatesPublishSubject();
        gyroscopeTranslationObservable = Provider.provideGyroscopeTranslationPublishSubject();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gyroscopeRepresentationPresenter = new GyroscopeRepresentationPresenter(
                ActionProvider.getListenGyroscopeCoordinatesFromBluetoothAction(),
                ActionProvider.getListenGyroscopeTranslationFromBluetoothAction());
        gyroscopeRepresentationPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        this.gyroscopeRepresentationPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        this.gyroscopeRepresentationPresenter.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CubeRenderer renderer = new CubeRenderer(gyroscopeCoordinatesObservable, gyroscopeTranslationObservable);
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