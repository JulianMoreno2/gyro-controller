package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CubeRenderer implements GLSurfaceView.Renderer {

    private Cube cube;
    private final Observable<GyroscopeRotation> gyroscopeRotationObservable;
    private final Observable<AccelerometerTranslation> accelerometerTranslationObservable;
    private GyroscopeRotation coords;
    private AccelerometerTranslation translation;
    private AccelerometerTranslation nonTranslation;
    private boolean isActiveGyroscope;

    public CubeRenderer(Observable<GyroscopeRotation> gyroscopeRotationObservable,
                        Observable<AccelerometerTranslation> accelerometerTranslationObservable) {

        this.gyroscopeRotationObservable = gyroscopeRotationObservable;
        this.accelerometerTranslationObservable = accelerometerTranslationObservable;
        this.coords = new GyroscopeRotation(0, 0, 0, 0);
        this.translation = new AccelerometerTranslation(0, 0, 0);
        this.nonTranslation = new AccelerometerTranslation(0, 0, 0);
        this.isActiveGyroscope = false;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        //gl.glPushMatrix();

        translate(gl, translation);
        rotate(gl, coords);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        this.cube.draw(gl);

        //gl.glPopMatrix();
    }

    private void translate(GL10 gl, AccelerometerTranslation translation) {
        float depth = 3;
        if (isActiveGyroscope) {
            gl.glTranslatef(0, 0, -depth);
        } else {
            gl.glTranslatef(translation.getX(), translation.getY(), -depth);
        }
    }

    private void rotate(GL10 gl, GyroscopeRotation coords) {
        gl.glRotatef((float) (2.0f * Math.acos(coords.getW()) * 180.0f / Math.PI),
                coords.getX(), coords.getY(), coords.getZ());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.cube = new Cube();
        gl.glDisable(GL10.GL_DITHER);
        gl.glClearColor(1f, 1f, 1f, 1f);

        gyroscopeRotationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(gyroscopeRotation -> {
                    Log.d("TRANSLATION", "true");
                    this.isActiveGyroscope = true;})
                .subscribe(coords -> {
                    this.coords = coords;
                });

        accelerometerTranslationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(accelerometerTranslation -> {
                    Log.d("TRANSLATION", "false");
                    this.isActiveGyroscope = false;})
                .subscribe(translation -> {
                    this.translation.sum(translation);
                });
    }
}
