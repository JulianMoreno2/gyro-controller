package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;
import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeTranslation;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CubeRenderer implements GLSurfaceView.Renderer {

    private Cube cube;
    private final Observable<GyroscopeCoordinates> gyroscopeCoordinatesObservable;
    private final Observable<GyroscopeTranslation> gyroscopeTranslationObservable;
    private GyroscopeCoordinates coords;
    private GyroscopeTranslation translation;

    private final float[] modelMatrix = new float[16];

    public CubeRenderer(Observable<GyroscopeCoordinates> gyroscopeCoordinatesObservable,
                        Observable<GyroscopeTranslation> gyroscopeTranslationObservable) {

        this.gyroscopeCoordinatesObservable = gyroscopeCoordinatesObservable;
        this.gyroscopeTranslationObservable = gyroscopeTranslationObservable;
        this.coords = new GyroscopeCoordinates(0, 0, 0, 0);
        this.translation = new GyroscopeTranslation(0, 0, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        activeDistantView(gl);
        //activeInsideView(gl);
        rotate(gl, coords);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, this.translation.getX(), this.translation.getY(), this.translation.getZ());

        this.cube.draw(gl);
    }

    private void activeDistantView(GL10 gl) {
        float dist = 3;
        gl.glTranslatef(0, 0, -dist);
    }

    private void activeInsideView(GL10 gl) {
        float dist = 3;
        drawTranslatedCube(gl, 0, 0, -dist);
        drawTranslatedCube(gl, 0, 0, dist);
        drawTranslatedCube(gl, 0, -dist, 0);
        drawTranslatedCube(gl, 0, dist, 0);
        drawTranslatedCube(gl, -dist, 0, 0);
        drawTranslatedCube(gl, dist, 0, 0);
    }

    private void rotate(GL10 gl, GyroscopeCoordinates coords) {
        gl.glRotatef((float) (2.0f * Math.acos(coords.getW()) * 180.0f / Math.PI), coords.getX(), coords.getY(), coords.getZ());
    }

    private void drawTranslatedCube(GL10 gl, float translateX, float translateY, float translateZ) {
        gl.glPushMatrix();
        gl.glTranslatef(translateX, translateY, translateZ);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        cube.draw(gl);
        gl.glPopMatrix();
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

        gyroscopeCoordinatesObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coords -> this.coords = coords);

        gyroscopeTranslationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translation -> this.translation = translation);
    }
}
