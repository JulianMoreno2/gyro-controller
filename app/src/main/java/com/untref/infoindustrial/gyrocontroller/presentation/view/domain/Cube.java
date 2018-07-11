package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cube {

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
    private static final byte[] INDICES = new byte[]{
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

    public Cube(float size) {

        final float vertices[] = {
                -size, -size, -size,//0
                size, -size, -size, //1
                size, size, -size,  //2
                -size, size, -size, //3
                -size, -size, size, //4
                size, -size, size,  //5
                size, size, size,   //6
                -size, size, size,  //7
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(COLORS.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(COLORS);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(INDICES.length);
        mIndexBuffer.put(INDICES);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl) {
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CW);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, INDICES.length, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
    }
}