package com.untref.infoindustrial.gyrocontroller.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.untref.infoindustrial.gyrocontroller.R;
import com.untref.infoindustrial.gyrocontroller.core.provider.ActionProvider;
import com.untref.infoindustrial.gyrocontroller.core.provider.Provider;
import com.untref.infoindustrial.gyrocontroller.presentation.presenter.SensorRepresentationPresenter;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.Bounds;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SensorRepresentationFragment extends Fragment implements SensorRepresentationPresenter.View {

    @BindView(R.id.object)
    ImageView object;

    @BindView(R.id.obstacle)
    ImageView obstacle;

    @BindView(R.id.obstacle2)
    ImageView obstacle2;

    @BindView(R.id.absolute_translation)
    TextView absoluteTranslation;


    private SensorRepresentationPresenter sensorRepresentationPresenter;

    public SensorRepresentationFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorRepresentationPresenter = new SensorRepresentationPresenter(
                ActionProvider.getListenGyroscopeRotationFromBluetoothAction(),
                ActionProvider.getListenAccelerometerTranslationFromBluetoothAction(),
                ActionProvider.getHasCollisionBetweenObjectsAction(),
                ActionProvider.getSendVibrateMessageAction(),
                Provider.provideAccelerometerTranslationPublishSubject(),
                getBounds());
        sensorRepresentationPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.sensorRepresentationPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.sensorRepresentationPresenter.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensor_representation, container, false);
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

    @Override
    public void moveObject(float x, float y) {
        object.setX(x);
        object.setY(y);
    }

    @Override
    public View getObject() {
        return object;
    }

    @Override
    public View getObstacle() {
        return obstacle;
    }

    @Override
    public View getObstacle2() {
        return obstacle2;
    }

    @Override
    public void updateAbsoluleTranslation(float value){
        absoluteTranslation.setText(String.valueOf(value));
    }

    private Bounds getBounds() {
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth(); // deprecated
        Log.d(": ", "WIDTH: " + String.valueOf(width));
        int height = display.getHeight(); // deprecated
        Log.d("HEIGHT: ", String.valueOf(height));
        if (height == 0) height = 1; // To prevent divide by 0

        float minWidth = 0f;
        float maxWidth = width * 0.82f;//es una relacion que saque en base a un telefono motog4
        float minHeight = 0f;
        float maxHeight = height * 0.85f;//es una relacion que saque en base a un telefono motog4

        return new Bounds(maxHeight, minHeight, maxWidth, minWidth);
    }
}