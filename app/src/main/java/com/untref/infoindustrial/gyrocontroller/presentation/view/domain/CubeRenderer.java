package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import android.opengl.GLSurfaceView;

import com.untref.infoindustrial.gyrocontroller.core.sensor.accelerometer.AccelerometerTranslation;
import com.untref.infoindustrial.gyrocontroller.core.sensor.gyroscope.GyroscopeRotation;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CubeRenderer implements GLSurfaceView.Renderer {

    private Cube cube;
    private final Observable<GyroscopeRotation> gyroscopeRotationObservable;
    private final Observable<AccelerometerTranslation> gyroscopeTranslationObservable;
    private GyroscopeRotation coords;
    private AccelerometerTranslation translation;
    private AccelerometerTranslation nonTranslation;
    private boolean isTranslationActive;

    public CubeRenderer(Observable<GyroscopeRotation> gyroscopeRotationObservable,
                        Observable<AccelerometerTranslation> gyroscopeTranslationObservable) {

        this.gyroscopeRotationObservable = gyroscopeRotationObservable;
        this.gyroscopeTranslationObservable = gyroscopeTranslationObservable;
        this.coords = new GyroscopeRotation(0, 0, 0, 0);
        this.translation = new AccelerometerTranslation(0, 0, 0);
        this.nonTranslation = new AccelerometerTranslation(0, 0, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glPushMatrix();

        translate(gl, translation);
        rotate(gl, coords);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        this.cube.draw(gl);

        gl.glPopMatrix();
    }

    private void translate(GL10 gl, AccelerometerTranslation translation) {
        float depth = 3;

        gl.glTranslatef(translation.getX() - this.nonTranslation.getX(), translation.getY() - this.nonTranslation.getY(), -depth);
    }

    private void rotate(GL10 gl, GyroscopeRotation coords) {
        gl.glRotatef((float) (2.0f * Math.acos(coords.getW()) * 180.0f / Math.PI), coords.getX(), coords.getY(), coords.getZ());
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
                })
                .subscribe(coords -> {
                    this.isTranslationActive = false;
                    this.coords = coords;
                });

        gyroscopeTranslationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(gyroscopeTranslation -> {

                })
                .subscribe(translation -> {
                    this.isTranslationActive = true;
                    this.nonTranslation = new AccelerometerTranslation(this.translation.getX(), this.translation.getY(), this.translation.getZ());
                    this.translation.sum(translation);
                });
    }
}
