package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import android.opengl.GLSurfaceView;

import com.untref.infoindustrial.gyrocontroller.core.sensor.GyroscopeCoordinates;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Class that implements the rendering of a cube with the current rotation of the device that is provided by a
 * OrientationProvider
 *
 * @author Alexander Pacha
 */
public class CubeRenderer implements GLSurfaceView.Renderer {

    private final Cube cube;
    private final Observable<GyroscopeCoordinates> observeGyroscopeCoordinates;

    public CubeRenderer(Observable<GyroscopeCoordinates> observeGyroscopeCoordinates) {
        this.observeGyroscopeCoordinates = observeGyroscopeCoordinates;
        this.cube = new Cube();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // clear screen
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // set-up modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        activeDistantView(gl);
        //activeInsideView(gl);

        // draw our object
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

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

        // draw our object
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        cube.draw(gl);
        gl.glPopMatrix();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // set view-port
        gl.glViewport(0, 0, width, height);
        // set projection matrix
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glClearColor(0, 0, 0, 1);

        // TODO: test that susbscribe emit all coords
        observeGyroscopeCoordinates
                .subscribeOn(Schedulers.io())
                .subscribe(coords -> onCustomizedDrawFrame(gl, coords));
    }

    private void onCustomizedDrawFrame(GL10 gl, GyroscopeCoordinates coords) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        activeDistantView(gl);
        //activeInsideView(gl);

        rotate(gl, coords);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        this.cube.draw(gl);
    }
}
