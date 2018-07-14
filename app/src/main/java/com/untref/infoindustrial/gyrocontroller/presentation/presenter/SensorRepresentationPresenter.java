package com.untref.infoindustrial.gyrocontroller.presentation.presenter;

import com.untref.infoindustrial.gyrocontroller.core.action.ListenAccelerometerTranslationFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.action.ListenGyroscopeRotationFromBluetoothAction;
import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.Bounds;
import com.untref.infoindustrial.gyrocontroller.presentation.view.domain.HasCollisionBetweenObjects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class SensorRepresentationPresenter extends Presenter<SensorRepresentationPresenter.View> {

    private static final float DEFAULT_DEPTH = 3f;
    private final ListenGyroscopeRotationFromBluetoothAction listenGyroscopeRotationFromBluetoothAction;
    private final ListenAccelerometerTranslationFromBluetoothAction listenAccelerometerTranslationFromBluetoothAction;
    private final HasCollisionBetweenObjects hasCollisionBetweenObjects;
    private final Action vibrator;
    private final Observable<AccelerometerTranslation> accelerometerTranslationObservable;
    private final Bounds bounds;
    private final AccelerometerTranslation translation;
    private AccelerometerTranslation previousAccelerometerTranslation;
    private Disposable compositeDisposable;

    public SensorRepresentationPresenter(
            ListenGyroscopeRotationFromBluetoothAction listenGyroscopeRotationFromBluetoothAction,
            ListenAccelerometerTranslationFromBluetoothAction listenAccelerometerTranslationFromBluetoothAction,
            HasCollisionBetweenObjects hasCollisionBetweenObjects, Action vibrator,
            Observable<AccelerometerTranslation> accelerometerTranslationObservable,
            Bounds bounds) {

        this.listenGyroscopeRotationFromBluetoothAction = listenGyroscopeRotationFromBluetoothAction;
        this.listenAccelerometerTranslationFromBluetoothAction = listenAccelerometerTranslationFromBluetoothAction;
        this.hasCollisionBetweenObjects = hasCollisionBetweenObjects;
        this.vibrator = vibrator;
        this.accelerometerTranslationObservable = accelerometerTranslationObservable;
        this.bounds = bounds;

        this.translation = new AccelerometerTranslation(0, 1, DEFAULT_DEPTH);
        this.previousAccelerometerTranslation = new AccelerometerTranslation(0, 0, DEFAULT_DEPTH);
    }

    public void onResume() {
        listenGyroscopeRotationFromBluetoothAction.execute();
        listenAccelerometerTranslationFromBluetoothAction.execute();

        compositeDisposable = accelerometerTranslationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translation -> {
                    this.translation.sum(translation, this.previousAccelerometerTranslation, this.bounds);
                    this.previousAccelerometerTranslation = translation;
                    getView().moveObject(this.translation.getXAccel(), this.translation.getYAccel());
                    if (hasCollisionBetweenObjects.execute(getView().getObject(), getView().getObstacle(), getView().getObstacle2())) {
                        this.translation.reverse(translation, this.previousAccelerometerTranslation, this.bounds);
                        this.vibrator.run();
                    }
                });
    }

    public void onPause() {
        if(compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    public interface View extends Presenter.View {
        void moveObject(float x, float y);

        android.view.View getObject();

        android.view.View getObstacle();

        android.view.View getObstacle2();
    }
}
