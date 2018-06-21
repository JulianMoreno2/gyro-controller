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
    private Obstacle obstacle;
    private final Observable<GyroscopeRotation> gyroscopeRotationObservable;
    private final Observable<AccelerometerTranslation> accelerometerTranslationObservable;
    private GyroscopeRotation coords;
    private AccelerometerTranslation translation;
    private AccelerometerTranslation previousAccelerometerTranslation;
    private boolean isActiveGyroscope;
    private float maxHeight;
    private float minHeight;
    private float maxWidth;
    private float minWidth;

    public CubeRenderer(Observable<GyroscopeRotation> gyroscopeRotationObservable,
                        Observable<AccelerometerTranslation> accelerometerTranslationObservable,
                        float maxHeight, float minHeight, float maxWidth, float minWidth) {

        this.gyroscopeRotationObservable = gyroscopeRotationObservable;
        this.accelerometerTranslationObservable = accelerometerTranslationObservable;
        this.coords = new GyroscopeRotation(0, 0, 0, 0);
        this.translation = new AccelerometerTranslation(0, 0, 0);
        this.previousAccelerometerTranslation = new AccelerometerTranslation(0, 0, -3);
        this.isActiveGyroscope = false;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        translate(gl, translation);
        rotate(gl, coords);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        this.cube.draw(gl);
        this.obstacle.draw(gl, translation.getZAccel());
    }

    private void translate(GL10 gl, AccelerometerTranslation translation) {
        float depth = 3;
        if (isActiveGyroscope) {
            gl.glTranslatef(0, 0, -depth);
            translation.restartXAccel();
            translation.restartYAccel();
        } else {
            gl.glTranslatef(translation.getXAccel(), translation.getYAccel(), translation.getZAccel());
        }
    }

    private void rotate(GL10 gl, GyroscopeRotation coords) {
        gl.glRotatef((float) (2.0f * Math.acos(coords.getWGyro()) * 180.0f / Math.PI),
                coords.getXGyro(), coords.getYGyro(), coords.getZGyro());
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
        this.cube = new Cube(0.5f);
        this.obstacle = new Obstacle(0.2f, this.maxWidth, this.minWidth, this.maxHeight, this.minHeight);

        gl.glDisable(GL10.GL_DITHER);
        gl.glClearColor(1f, 1f, 1f, 1f);
        this.obstacle.draw(gl,-3);

        gyroscopeRotationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coords -> {
                    this.isActiveGyroscope = true;
                    this.coords = coords;
                });

        accelerometerTranslationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translation -> {
                    this.isActiveGyroscope = false;
                    this.translation.sum(translation, this.previousAccelerometerTranslation, this.maxHeight, this.minHeight, this.maxWidth, this.minWidth);
                    this.previousAccelerometerTranslation = translation;
                });
    }

}
