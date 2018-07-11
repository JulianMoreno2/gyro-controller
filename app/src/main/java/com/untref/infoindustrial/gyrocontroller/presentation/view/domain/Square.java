package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {

    private static final float NONE = 0.0f;
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ByteBuffer indexBuffer;
    private static final float[] COLORS = new float[]{
            0, 0,
            0, 1,
            1, 0,
            0, 1,
            1, 1,
            0, 1,
            0, 1,
            0, 1,
            0, 0,
            1, 1,
            1, 0,
            1, 1,
            1, 1,
            1, 1,
            0, 1,
            1, 1,
    };

    private static final byte[] INDEX = new byte[]{
            0, 4, 5,
            0, 5, 1,
            1, 5, 6,
            1, 6, 2,
            2, 6, 7,
            2, 7, 3,
            3, 7, 4,
            3, 4, 0,
            4, 7, 6,
            4, 6, 5,
            3, 0, 1,
            3, 1, 2
    };

    // https://developer.android.com/training/graphics/opengl/shapes
    public Square(float size) {

        final float vertex[] = {
                -size, size, NONE,   // top left
                -size, -size, NONE,  // bottom left
                size, -size, NONE,   // bottom right
                size, size, NONE     // top right
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertex.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(COLORS.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(COLORS);
        colorBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(INDEX.length);
        indexBuffer.put(INDEX);
        indexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CW);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, INDEX.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
    }
}