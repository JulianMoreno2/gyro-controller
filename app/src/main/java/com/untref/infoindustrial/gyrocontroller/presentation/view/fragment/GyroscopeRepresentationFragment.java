package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;
import com.untref.infoindustrial.gyrocontroller.core.provider.ActionProvider;
import com.untref.infoindustrial.gyrocontroller.core.provider.Provider;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.GyroscopeRepresentationPresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.CubeRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import io.reactivex.subjects.PublishSubject;

public class GyroscopeRepresentationFragment extends Fragment implements GyroscopeRepresentationPresenter.View {

    private GLSurfaceView glSurfaceView;
    private PublishSubject<GyroscopeCoordinates> coordinatesPublishSubject;
    private GyroscopeRepresentationPresenter gyroscopeRepresentationPresenter;

    public GyroscopeRepresentationFragment() {
        setHasOptionsMenu(true);
        coordinatesPublishSubject = Provider.provideGyroscopeCoordinatesPublishSubject();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gyroscopeRepresentationPresenter = new GyroscopeRepresentationPresenter(
                ActionProvider.getListenGyroscopeCoordinatesFromBluetoothAction()
        );
        gyroscopeRepresentationPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        this.gyroscopeRepresentationPresenter.onResume();
        //simulateRotation();
    }

    private void simulateRotation() {
        List<GyroscopeCoordinates> gyroscopeCoordinates = new ArrayList<>();
        Random rand = new Random();

        for (int x = 0; x < 2000; x++) {
            float mX = rand.nextFloat();
            float mY = rand.nextFloat();
            float mZ = rand.nextFloat();
            float mW = rand.nextFloat();

            gyroscopeCoordinates.add(new GyroscopeCoordinates(mX, mY, mZ, -mW));
        }

        for (int i = 0; i < gyroscopeCoordinates.size(); i++) {
            coordinatesPublishSubject.onNext(gyroscopeCoordinates.get(i));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        this.gyroscopeRepresentationPresenter.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CubeRenderer renderer = new CubeRenderer(coordinatesPublishSubject);
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